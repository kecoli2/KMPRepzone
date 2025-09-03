package com.repzone.presentation.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

open class BaseViewModel<S : Any, E : Any>(initialState: S) {
    //region Field
    private val job = SupervisorJob()
    protected val scope = CoroutineScope(job + Dispatchers.Default)

    protected val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state

    /** Tek seferlik UI etkileri (snackbar, nav vb.) için opsiyonel */
    open val effect: Flow<E>? = null
    //endregion

    //region Properties
    //endregion

    //region Constructor
    //endregion

    //region Public Method
    /** Ekran görünür olduğunda */
    open fun onStart() {}

    /** Ekran görünürlükten çıkınca  */
    open fun onStop() {}

    /** Ekrandan ayrılırken: tüm işler iptal → referanslar serbest (GC) */
    open fun onDispose() {
        job.cancel()
    }
    //endregion

    //region Protected Method
    protected inline fun updateState(block: (S) -> S) {
        _state.value = block(_state.value)
    }
    //endregion

    //region Private Method
    //endregion
}