package com.repzone.presentationlegacy.manager

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class PermissionManager  constructor() {

    private lateinit var appContext: Context
    private lateinit var launcherMultiple: ActivityResultLauncher<Array<String>>
    private lateinit var launcherSingle: ActivityResultLauncher<String>

    private var pending: CancellableContinuation<Boolean>? = null

    // Compose tarafı burayı çağıracak
    fun attachLaunchers(
        context: Context,
        multiple: ActivityResultLauncher<Array<String>>,
        single: ActivityResultLauncher<String>
    ) {
        appContext = context.applicationContext
        launcherMultiple = multiple
        launcherSingle = single
    }

    fun onMultipleResult(grants: Map<String, Boolean>) {
        pending?.resume(grants.values.all { it })
        pending = null
    }

    fun onSingleResult(granted: Boolean) {
        pending?.resume(granted)
        pending = null
    }

    private fun hasPermission(perm: String): Boolean =
        ContextCompat.checkSelfPermission(appContext, perm) == PackageManager.PERMISSION_GRANTED

    private suspend fun requestMultiple(perms: Array<String>): Boolean =
        suspendCancellableCoroutine { cont ->
            check(::launcherMultiple.isInitialized) { "Permission launchers not attached" }
            check(pending == null) { "Another permission request is in progress" }
            pending = cont
            launcherMultiple.launch(perms)
            cont.invokeOnCancellation { pending = null }
        }

    private suspend fun requestOne(perm: String): Boolean =
        _root_ide_package_.kotlinx.coroutines.suspendCancellableCoroutine { cont ->
            check(::launcherSingle.isInitialized) { "Permission launchers not attached" }
            check(pending == null) { "Another permission request is in progress" }
            pending = cont
            launcherSingle.launch(perm)
            cont.invokeOnCancellation { pending = null }
        }

    // ---- public API ----
    actual suspend fun ensureBluetooth(): com.repzone.core.util.PermissionStatus {
        val needed = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            arrayOf(android.Manifest.permission.BLUETOOTH_SCAN, android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (needed.all { hasPermission(it) }) return com.repzone.core.util.PermissionStatus.Granted
        val ok = requestMultiple(needed)
        return if (ok) com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkBluetooth(): com.repzone.core.util.PermissionStatus {
        val granted = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            hasPermission(android.Manifest.permission.BLUETOOTH_SCAN) &&
                    hasPermission(android.Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return if (granted) com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(true)
    }

    actual suspend fun ensureNotifications(): com.repzone.core.util.PermissionStatus {
        if (android.os.Build.VERSION.SDK_INT < 33) return com.repzone.core.util.PermissionStatus.Granted
        if (hasPermission(android.Manifest.permission.POST_NOTIFICATIONS)) return com.repzone.core.util.PermissionStatus.Granted
        val ok = requestOne(android.Manifest.permission.POST_NOTIFICATIONS)
        return if (ok) com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkNotifications(): com.repzone.core.util.PermissionStatus {
        if (android.os.Build.VERSION.SDK_INT < 33) return com.repzone.core.util.PermissionStatus.Granted
        return if (hasPermission(android.Manifest.permission.POST_NOTIFICATIONS))
            com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(true)
    }

    actual suspend fun ensureLocation(): com.repzone.core.util.PermissionStatus {
        val perm = android.Manifest.permission.ACCESS_FINE_LOCATION
        if (hasPermission(perm)) return com.repzone.core.util.PermissionStatus.Granted
        val ok = requestMultiple(arrayOf(perm))
        return if (ok) com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkLocation(): com.repzone.core.util.PermissionStatus {
        val granted = hasPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
        return if (granted) com.repzone.core.util.PermissionStatus.Granted else com.repzone.core.util.PermissionStatus.Denied(true)
    }
}