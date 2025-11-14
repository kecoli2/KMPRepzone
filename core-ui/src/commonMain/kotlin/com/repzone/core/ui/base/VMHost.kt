@file:OptIn(ExperimentalTime::class)

package com.repzone.core.ui.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.repzone.core.platform.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

// Smart ViewModelStore - Aktif Composition'ları koruyor
object SmartViewModelStore {
    private val store = mutableMapOf<String, ViewModelEntry>()
    private val activeCompositions = mutableSetOf<String>() // Aktif ekranları track et
    private val navigationDepth = mutableMapOf<String, Int>() // Her ViewModel'in backstack derinliği
    private val entryIdToViewModelKey = mutableMapOf<String, String>() // BackStackEntry ID -> ViewModel Key mapping

    private data class ViewModelEntry(
        val viewModel: BaseViewModel<*, *>,
        val lastAccessTime: Long = Clock.System.now().toEpochMilliseconds(),
        val createdTime: Long = Clock.System.now().toEpochMilliseconds()
    )

    @Suppress("UNCHECKED_CAST")
    fun <VM : BaseViewModel<*, *>> get(key: String, factory: () -> VM): VM {
        val entry = store.getOrPut(key) {
            ViewModelEntry(factory())
        }

        // Access time'ı güncelle
        store[key] = entry.copy(lastAccessTime = Clock.System.now().toEpochMilliseconds())

        return entry.viewModel as VM
    }

    // Composition active/inactive tracking
    fun markCompositionActive(key: String, depth: Int = 0) {
        activeCompositions.add(key)
        navigationDepth[key] = depth
    }

    fun markCompositionInactive(key: String) {
        activeCompositions.remove(key)
    }

    fun isCompositionActive(key: String): Boolean {
        return activeCompositions.contains(key)
    }

    // Navigation depth güncelleme
    fun updateNavigationDepth(key: String, depth: Int) {
        navigationDepth[key] = depth
    }

    // BackStack entry tracking
    fun registerBackStackEntry(entryId: String, viewModelKey: String) {
        entryIdToViewModelKey[entryId] = viewModelKey
    }

    fun disposeByEntryId(entryId: String) {
        val viewModelKey = entryIdToViewModelKey[entryId]
        if (viewModelKey != null) {
            disposeViewModel(viewModelKey)
            entryIdToViewModelKey.remove(entryId)
        }
    }

    // Smart cleanup - sadece acil durum temizliği için
    // Normal durumlarda NavBackStackEntry lifecycle otomatik dispose ediyor
    fun smartCleanup() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val emergencyTimeout = 2 * 60 * 60 * 1000L // 2 saat - acil durum için

        val toRemove = store.filter { (key, entry) ->
            val isInactive = !activeCompositions.contains(key)
            val isVeryOld = currentTime - entry.lastAccessTime > emergencyTimeout

            // Sadece çok eski ve inactive olanları temizle
            isInactive && isVeryOld
        }

        if (toRemove.isNotEmpty()) {
            toRemove.forEach { (key, entry) ->
                Logger.d("ViewHost","Emergency cleanup: disposing ViewModel $key (unused for 2+ hours)")
                entry.viewModel.onDispose()
                store.remove(key)
                navigationDepth.remove(key)
            }
        }
    }

    fun clearAll() {
        store.values.forEach { it.viewModel.onDispose() }
        store.clear()
        activeCompositions.clear()
        navigationDepth.clear()
    }

    // Manuel dispose - specific ViewModel'i hemen dispose et
    fun disposeViewModel(key: String) {
        store[key]?.let { entry ->
            entry.viewModel.onDispose()
            store.remove(key)
            activeCompositions.remove(key)
            navigationDepth.remove(key)
            Logger.d("ViewHost","ViewModel manually disposed: $key")
        }
    }

    // Remove ViewModel from store
    fun remove(key: String) {
        store[key]?.let { entry ->
            entry.viewModel.onDispose()
        }
        store.remove(key)
        activeCompositions.remove(key)
        navigationDepth.remove(key)
    }

    // Manuel dispose - tüm inactive ViewModel'leri dispose et
    fun disposeInactiveViewModels() {
        val inactiveKeys = store.keys.filter { !activeCompositions.contains(it) }
        inactiveKeys.forEach { key ->
            store[key]?.let { entry ->
                entry.viewModel.onDispose()
                store.remove(key)
                navigationDepth.remove(key)
                Logger.d("ViewHost","Inactive ViewModel disposed: $key")
            }
        }
    }

    // Debug info
    fun getDebugInfo(): String {
        return "Store: ${store.size}, Active: ${activeCompositions.size}"
    }

    // Auto cleanup job
    private var cleanupJob: Job? = null

    fun startSmartCleanup() {
        cleanupJob?.cancel()
        cleanupJob = CoroutineScope(Dispatchers.Default).launch {
            while (true) {
                delay(30_000) // 30 saniye'de bir kontrol et
                smartCleanup()
            }
        }
    }

    fun stopSmartCleanup() {
        cleanupJob?.cancel()
    }
}

// Composition-aware ViewModel provider
@Composable
fun <VM : BaseViewModel<*, *>> rememberCompositionAwareViewModel(key: String, factory: () -> VM): VM {
    val vm = remember(key) {
        SmartViewModelStore.get(key, factory)
    }

    // Composition lifecycle tracking
    LaunchedEffect(key) {
        SmartViewModelStore.markCompositionActive(key)
        vm.onStart()
    }

    DisposableEffect(key) {
        onDispose {
            SmartViewModelStore.markCompositionInactive(key)
            vm.onStop()
        }
    }

    return vm
}

/** Safe ViewModelHost - Ana ViewModelHost */
@Composable
inline fun <reified VM : BaseViewModel<*, *>> ViewModelHost(
    key: String = VM::class.simpleName ?: "default",
    vararg params: Any?,
    noinline content: @Composable (VM) -> Unit
) {
    val koin = getKoin()
    val vm = rememberCompositionAwareViewModel(key) {
        if (params.isEmpty()) koin.get<VM>()
        else koin.get<VM> { parametersOf(*params) }
    }

    content(vm)
}

@Composable
inline fun <reified VM : BaseViewModel<*, *>> ViewModelHost( noinline content: @Composable (VM) -> Unit) {
    val key = VM::class.simpleName ?: "default"
    ViewModelHost<VM>(key = key, content = content)
}

// App initialization
fun initializeSmartViewModelStore() {
    SmartViewModelStore.startSmartCleanup()
}