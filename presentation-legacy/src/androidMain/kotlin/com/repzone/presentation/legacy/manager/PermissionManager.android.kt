package com.repzone.presentation.legacy.manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat
import com.repzone.core.util.PermissionStatus
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
    actual suspend fun ensureBluetooth(): PermissionStatus {
        val needed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (needed.all { hasPermission(it) }) return PermissionStatus.Granted
        val ok = requestMultiple(needed)
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkBluetooth(): PermissionStatus {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            hasPermission(Manifest.permission.BLUETOOTH_SCAN) &&
                    hasPermission(Manifest.permission.BLUETOOTH_CONNECT)
        } else {
            hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(true)
    }

    actual suspend fun ensureNotifications(): PermissionStatus {
        if (Build.VERSION.SDK_INT < 33) return PermissionStatus.Granted
        if (hasPermission(Manifest.permission.POST_NOTIFICATIONS)) return PermissionStatus.Granted
        val ok = requestOne(Manifest.permission.POST_NOTIFICATIONS)
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkNotifications(): PermissionStatus {
        if (Build.VERSION.SDK_INT < 33) return PermissionStatus.Granted
        return if (hasPermission(Manifest.permission.POST_NOTIFICATIONS))
            PermissionStatus.Granted else PermissionStatus.Denied(true)
    }

    actual suspend fun ensureLocation(): PermissionStatus {
        val perm = Manifest.permission.ACCESS_FINE_LOCATION
        if (hasPermission(perm)) return PermissionStatus.Granted
        val ok = requestMultiple(arrayOf(perm))
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkLocation(): PermissionStatus {
        val granted = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(true)
    }
}