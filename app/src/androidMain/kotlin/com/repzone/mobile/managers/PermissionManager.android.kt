package com.repzone.mobile.managers

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.repzone.core.util.PermissionStatus
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume

actual class PermissionManager(
    private val activity: Activity,
    caller: ActivityResultCaller
) {
    private var continuation: Continuation<Boolean>? = null

    private val multiLauncher: ActivityResultLauncher<Array<String>> =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            continuation?.resume(result.values.all { it } )
            continuation = null
        }

    private val singleLauncher: ActivityResultLauncher<String> =
        caller.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            continuation?.resume(granted)
            continuation = null
        }

    // --- helpers ---
    private fun hasAll(perms: Array<String>): Boolean =
        perms.all { ActivityCompat.checkSelfPermission(activity, it) == PackageManager.PERMISSION_GRANTED }

    private suspend fun requestMultiple(perms: Array<String>): Boolean =
        suspendCancellableCoroutine { cont ->
            // Aynı anda ikinci bir istek gelmesini engelle
            require(continuation == null) { "Permission request already in progress" }
            continuation = cont
            multiLauncher.launch(perms)
            cont.invokeOnCancellation { continuation = null }
        }

    private suspend fun requestOne(perm: String): Boolean =
        suspendCancellableCoroutine { cont ->
            require(continuation == null) { "Permission request already in progress" }
            continuation = cont
            singleLauncher.launch(perm)
            cont.invokeOnCancellation { continuation = null }
        }

    // ---- Bluetooth ----
    actual suspend fun ensureBluetooth(): PermissionStatus {
        val needed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            // Eski cihazlarda BLE taraması için konum izni gerekir
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (hasAll(needed)) return PermissionStatus.Granted
        val ok = requestMultiple(needed)
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkBluetooth(): PermissionStatus {
        val granted =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                hasAll(arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                ))
            } else {
                ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
            }
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    // ---- Notifications (API 33+) ----
    actual suspend fun ensureNotifications(): PermissionStatus {
        if (Build.VERSION.SDK_INT < 33) return PermissionStatus.Granted
        val p = Manifest.permission.POST_NOTIFICATIONS
        val granted = ActivityCompat.checkSelfPermission(activity, p) == PackageManager.PERMISSION_GRANTED
        if (granted) return PermissionStatus.Granted

        val ok = requestOne(p)
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkNotifications(): PermissionStatus {
        if (Build.VERSION.SDK_INT < 33) return PermissionStatus.Granted
        val p = Manifest.permission.POST_NOTIFICATIONS
        val granted = ActivityCompat.checkSelfPermission(activity, p) == PackageManager.PERMISSION_GRANTED
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    // ---- Location (opsiyonel) ----
    actual suspend fun ensureLocation(): PermissionStatus {
        val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        if (hasAll(perms)) return PermissionStatus.Granted
        val ok = requestMultiple(perms)
        return if (ok) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkLocation(): PermissionStatus {
        val granted = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }
}