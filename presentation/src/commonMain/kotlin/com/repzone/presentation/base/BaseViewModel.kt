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

    //region KMM iOS Export Edilebilir Fonksiyonlar
    open fun setLoading(isLoading: Boolean) {
        val currentState = _state.value
        if (currentState is HasUiFrame) {
            updateStateInternal {
                (currentState as HasUiFrame).copyWithUiFrame(
                    currentState.uiFrame.copy(
                        isLoading = isLoading,
                        error = if (isLoading) null else currentState.uiFrame.error
                    )
                ) as S
            }
        }
    }

    open fun setError(error: String?) {
        val currentState = _state.value
        if (currentState is HasUiFrame) {
            updateStateInternal {
                (currentState as HasUiFrame).copyWithUiFrame(
                    currentState.uiFrame.copy(
                        isLoading = false,
                        error = error
                    )
                ) as S
            }
        }
    }

    open fun clearError() {
        setError(null)
    }

    open fun resetUiFrame() {
        val currentState = _state.value
        if (currentState is HasUiFrame) {
            updateStateInternal {
                (currentState as HasUiFrame).copyWithUiFrame(UiFrame()) as S
            }
        }
    }

    open fun isLoading(): Boolean {
        val currentState = _state.value
        return if (currentState is HasUiFrame) currentState.uiFrame.isLoading else false
    }

    open fun hasError(): Boolean {
        val currentState = _state.value
        return if (currentState is HasUiFrame) currentState.uiFrame.error != null else false
    }

    open fun getCurrentError(): String? {
        val currentState = _state.value
        return if (currentState is HasUiFrame) currentState.uiFrame.error else null
    }
    //endregion
}
