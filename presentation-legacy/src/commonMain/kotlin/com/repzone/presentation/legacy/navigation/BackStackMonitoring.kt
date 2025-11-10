package com.repzone.presentation.legacy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.repzone.core.ui.base.SmartViewModelStore

/**
 * NavController için backstack monitor
 * Ekranlar backstack'ten çıkarıldığında tespit eder ve dispose eder
 */
@Composable
fun NavController.MonitorBackStack() {
    val currentBackStackEntry by currentBackStackEntryAsState()
    val previousBackStackIds = remember { mutableSetOf<String>() }

    LaunchedEffect(currentBackStackEntry) {
        // Mevcut backstack'teki tüm entry ID'leri al
        val currentIds = currentBackStack.value.map { it.id }.toSet()

        // Önceki backstack'te olup şimdi olmayan entry'leri bul
        val removedIds = previousBackStackIds - currentIds

        // Removed entry'ler için ViewModel'leri dispose et
        removedIds.forEach { removedId ->
            // ID'den ViewModel key'i bul ve dispose et
            SmartViewModelStore.disposeByEntryId(removedId)
            println("BackStack entry removed: $removedId")
        }

        // Backstack'i güncelle
        previousBackStackIds.clear()
        previousBackStackIds.addAll(currentIds)
    }
}

/**
 * Her ekran için entry ID ve ViewModel key'i eşleştir
 */
@Composable
fun RegisterBackStackEntry(entryId: String, viewModelKey: String) {
    DisposableEffect(entryId) {
        SmartViewModelStore.registerBackStackEntry(entryId, viewModelKey)
        onDispose {
            // Entry composition'dan çıktı ama backstack'te olabilir
            // Gerçek dispose MonitorBackStack tarafından yapılacak
        }
    }
}