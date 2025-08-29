package com.repzone.mobile.compose.permissions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.repzone.mobile.managers.PermissionManager

@Composable
actual fun rememberPermissionManager(): PermissionManager {
    val pm = remember { PermissionManager() }

    val launcherMultiple = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result -> pm.onMultipleResult(result) }

    val launcherSingle = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted -> pm.onSingleResult(granted) }

    val ctx: Context = LocalContext.current.applicationContext

    // launcher + context’i güvenli şekilde bağla
    LaunchedEffect(launcherMultiple, launcherSingle, ctx) {
        pm.attachLaunchers(ctx, launcherMultiple, launcherSingle)
    }

    return pm
}