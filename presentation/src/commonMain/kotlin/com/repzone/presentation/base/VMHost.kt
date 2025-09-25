@file:OptIn(ExperimentalTime::class)

package com.repzone.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
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

    private data class ViewModelEntry(
        val viewModel: BaseViewModel<*, *>,
        val lastAccessTime: Long = Clock.System.now().toEpochMilliseconds(),
        val createdTime: Long = Clock.System.now().toEpochMilliseconds()
    )

    @Suppress("UNCHECKED_CAST")
    fun <VM : BaseViewModel<*, *>> get(key: String,factory: () -> VM): VM {
        val entry = store.getOrPut(key) {
            ViewModelEntry(factory())
        }

        // Access time'ı güncelle
        store[key] = entry.copy(lastAccessTime = Clock.System.now().toEpochMilliseconds())

        return entry.viewModel as VM
    }

    // Composition active/inactive tracking
    fun markCompositionActive(key: String) {
        activeCompositions.add(key)
        println("Composition ACTIVE: $key")
        println("Active compositions: ${activeCompositions.joinToString(", ")}")
    }

    fun markCompositionInactive(key: String) {
        activeCompositions.remove(key)
        println("Composition INACTIVE: $key")
        println("Active compositions: ${activeCompositions.joinToString(", ")}")
    }

    // Smart cleanup - sadece inactive ViewModel'leri temizle
    fun smartCleanup() {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val timeout = 2 * 60 * 1000L // 2 dakika (test için kısa)

        val toRemove = store.filter { (key, entry) ->
            val isOld = currentTime - entry.lastAccessTime > timeout
            val isInactive = !activeCompositions.contains(key)

            // Sadece hem eski hem de inactive olan ViewModel'leri temizle
            isOld && isInactive
        }

        if (toRemove.isNotEmpty()) {
            println("Smart cleanup removing: ${toRemove.keys}")
            toRemove.forEach { (key, entry) ->
                println("Disposing inactive ViewModel: $key")
                entry.viewModel.onDispose()
                store.remove(key)
            }
        }

        // Aktif ViewModel'leri koruyarak log
        val activeVMs = store.keys.filter { activeCompositions.contains(it) }
        if (activeVMs.isNotEmpty()) {
            println("Protected active ViewModels: ${activeVMs.joinToString(", ")}")
        }
    }

    fun clearAll() {
        store.values.forEach { it.viewModel.onDispose() }
        store.clear()
        activeCompositions.clear()
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
            // NOT disposing here - let smart cleanup handle it
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

/*    // Debug info
    if (BuildConfig.DEBUG) {
        LaunchedEffect(Unit) {
            println("ViewModelHost: $key - ${SmartViewModelStore.getDebugInfo()}")
        }
    }*/

    content(vm)
}

/** Alternative ViewModelHost without parameters */
@Composable
inline fun <reified VM : BaseViewModel<*, *>> ViewModelHost(
    noinline content: @Composable (VM) -> Unit
) {
    val key = VM::class.simpleName ?: "default"
    ViewModelHost<VM>(key = key, content = content)
}

// App initialization
fun initializeSmartViewModelStore() {
    SmartViewModelStore.startSmartCleanup()
}