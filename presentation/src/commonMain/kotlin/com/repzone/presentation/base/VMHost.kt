package com.repzone.presentation.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import org.koin.compose.getKoin
import org.koin.core.parameter.parametersOf


/**
 * Koin'den VM'i üretir, Compose yaşam döngüsüne bağlar:
 * - remember: tek instance
 * - LaunchedEffect: onStart()
 * - DisposableEffect: onDispose()
 */
@Composable
inline fun <reified VM : BaseViewModel<*, *>> rememberViewModel(vararg params: Any?): VM {
    val koin = getKoin()
    val vm = remember(params) {
        if (params.isEmpty()) koin.get<VM>()
        else koin.get<VM> { parametersOf(*params) }
    }

    LaunchedEffect(vm) { vm.onStart() }
    DisposableEffect(vm) { onDispose { vm.onDispose() } }
    return vm
}

/** Sarmalayıcı: VM’i al ve content’e geçir. */
@Composable
inline fun <reified VM : BaseViewModel<*, *>> ViewModelHost(
    vararg params: Any?,
    content: @Composable (VM) -> Unit
) {
    val vm = rememberViewModel<VM>(*params)
    content(vm)
}