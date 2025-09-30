package com.repzone.presentation.base

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


open class BaseViewModel<S : Any, E : Any>(initialState: S) {
    //region Fields
    private val job = SupervisorJob()
    protected val scope = CoroutineScope(job + Dispatchers.Default)

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    /** Tek seferlik UI etkileri (snackbar, nav vb.) için opsiyonel */
    open val effect: Flow<E>? = null
    //endregion

    //region Public Methods
    /** Ekran görünür olduğunda */
    open fun onStart() {}

    /** Ekran görünürlükten çıkınca */
    open fun onStop() {}

    /** Ekrandan ayrılırken: tüm işler iptal → referanslar serbest (GC) */
    open fun onDispose() {
        job.cancel()
    }
    //endregion

    //region Protected Methods
    protected inline fun updateState(block: (S) -> S) {
        _state.value = block(_state.value)
    }

    // Public accessor for extensions (internal to avoid external access)
    internal inline fun updateStateInternal(block: (S) -> S) {
        println("DEBUG BaseViewModel: updateStateInternal called")
        val oldState = _state.value
        val newState = block(oldState)
        println("DEBUG BaseViewModel: old state: $oldState")
        println("DEBUG BaseViewModel: new state: $newState")
        _state.value = newState
    }
    //endregion
}

//region Extension Functions

/**
 * HasUiFrame implement eden state'ler için loading durumunu ayarlar
 */
fun <S> BaseViewModel<S, *>.setLoading(isLoading: Boolean)
        where S : Any, S : HasUiFrame {
    updateStateInternal { currentState ->
        currentState.copyWithUiFrame(
            currentState.uiFrame.copy(
                isLoading = isLoading,
                error = if (isLoading) null else currentState.uiFrame.error
            )
        ) as S
    }
}

/**
 * HasUiFrame implement eden state'ler için error durumunu ayarlar
 */
fun <S> BaseViewModel<S, *>.setError(error: String?)
        where S : Any, S : HasUiFrame {
    updateStateInternal { currentState ->
        currentState.copyWithUiFrame(
            currentState.uiFrame.copy(
                isLoading = false,
                error = error
            )
        ) as S
    }
}

/**
 * Error'ı temizler
 */
fun <S> BaseViewModel<S, *>.clearError()
        where S : Any, S : HasUiFrame {
    setError(null)
}

/**
 * UiFrame'i varsayılan duruma sıfırlar
 */
fun <S> BaseViewModel<S, *>.resetUiFrame()
        where S : Any, S : HasUiFrame {
    updateStateInternal { currentState ->
        currentState.copyWithUiFrame(UiFrame()) as S
    }
}

/**
 * Loading durumunu kontrol eder
 */
fun <S> BaseViewModel<S, *>.isLoading(): Boolean
        where S : Any, S : HasUiFrame {
    return state.value.uiFrame.isLoading
}

/**
 * Error varsa true döner
 */
fun <S> BaseViewModel<S, *>.hasError(): Boolean
        where S : Any, S : HasUiFrame {
    return state.value.uiFrame.error != null
}

/**
 * Mevcut error mesajını döner
 */
fun <S> BaseViewModel<S, *>.getCurrentError(): String?
        where S : Any, S : HasUiFrame {
    return state.value.uiFrame.error
}

//endregion

//region UiFrame Extension Functions

/**
 * UiFrame için yardımcı extension'lar
 */
fun UiFrame.withLoading(isLoading: Boolean = true): UiFrame =
    copy(isLoading = isLoading, error = if (isLoading) null else error)

fun UiFrame.withError(error: String?): UiFrame =
    copy(isLoading = false, error = error)

fun UiFrame.reset(): UiFrame = UiFrame()

val UiFrame.isSuccess: Boolean get() = !isLoading && error == null

//endregion