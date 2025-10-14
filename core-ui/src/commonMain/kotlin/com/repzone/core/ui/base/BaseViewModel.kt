package com.repzone.core.ui.base

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource


open class BaseViewModel<S : Any, E : Any>(initialState: S) {
    //region Fields
    private val job = SupervisorJob()
    protected val scope = CoroutineScope(job + Dispatchers.Default)

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    // Event channel için
    private val _events = Channel<E>(Channel.BUFFERED)
    val events: Flow<E> = _events.receiveAsFlow()
    //endregion

    //region Public Methods
    /** Ekran görünür olduğunda */
    open fun onStart() {}

    /** Ekran görünürlükten çıkınca */
    open fun onStop() {}

    /** Ekrandan ayrılırken: tüm işler iptal → referanslar serbest (GC) */
    open fun onDispose() {
        job.cancel()
        _events.close()
    }
    //endregion

    //region Protected Methods
    protected inline fun updateState(block: (S) -> S) {
        _state.value = block(_state.value)
    }

    internal inline fun updateStateInternal(block: (S) -> S) {
        println("DEBUG BaseViewModel: updateStateInternal called")
        val oldState = _state.value
        val newState = block(oldState)
        println("DEBUG BaseViewModel: old state: $oldState")
        println("DEBUG BaseViewModel: new state: $newState")
        _state.value = newState
    }

    /**
     * Event gönderir (navigation, toast, dialog vb. tek seferlik aksiyonlar için)
     */
    protected fun sendEvent(event: E) {
        scope.launch {
            _events.send(event)
        }
    }

    /**
     * Event gönderir (suspend fonksiyon versiyonu)
     */
    protected suspend fun emitEvent(event: E) {
        _events.send(event)
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
                error = if (isLoading) null else currentState.uiFrame.error,
                errorStringRes = if (isLoading) null else currentState.uiFrame.errorStringRes
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

fun <S> BaseViewModel<S, *>.setErrorStringResource(error: StringResource?)
        where S : Any, S : HasUiFrame {
    updateStateInternal { currentState ->
        currentState.copyWithUiFrame(
            currentState.uiFrame.copy(
                isLoading = false,
                errorStringRes = error
            )
        ) as S
    }
}

fun <S> BaseViewModel<S, *>.setErrorStringResource(error: StringResource?, formatArgs: List<Any?> = emptyList())
        where S : Any, S : HasUiFrame {
    updateStateInternal { currentState ->
        currentState.copyWithUiFrame(
            currentState.uiFrame.copy(
                isLoading = false,
                errorStringRes = error,
                formatArgs = formatArgs
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
    setErrorStringResource(null, emptyList())
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
    return state.value.uiFrame.error != null || state.value.uiFrame.errorStringRes != null
}

/**
 * Mevcut error mesajını döner
 */
fun <S> BaseViewModel<S, *>.getCurrentError(): Any?
        where S : Any, S : HasUiFrame {
    if(state.value.uiFrame.error != null){
        return state.value.uiFrame.error
    }else{
        return state.value.uiFrame.errorStringRes
    }
}

//endregion

//region UiFrame Extension Functions

/**
 * UiFrame için yardımcı extension'lar
 */
fun UiFrame.withLoading(isLoading: Boolean = true): UiFrame =
    copy(isLoading = isLoading, error = if (isLoading) null else error, errorStringRes = if (isLoading) null else errorStringRes)

fun UiFrame.withError(error: String?): UiFrame =
    copy(isLoading = false, error = error)

fun UiFrame.reset(): UiFrame = UiFrame()

val UiFrame.isSuccess: Boolean get() = !isLoading && error == null && errorStringRes == null

//endregion