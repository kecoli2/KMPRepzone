package com.repzone.core.ui.manager.permissions

import com.repzone.core.util.PermissionStatus
import kotlinx.cinterop.*
import platform.CoreBluetooth.*
import platform.UserNotifications.*
import platform.CoreLocation.*
import platform.darwin.*
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.Foundation.NSOperatingSystemVersion
import platform.Foundation.NSProcessInfo
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual class PermissionManager {

    // -------- Bluetooth --------
    private fun isAtLeastIOS13(): Boolean =
        NSProcessInfo.processInfo.isOperatingSystemAtLeastVersion(
            cValue<NSOperatingSystemVersion> {
                majorVersion = 13
                minorVersion = 0
                patchVersion = 0
            }
        )

    private fun currentBluetoothStatus(): PermissionStatus {
        val auth = if (isAtLeastIOS13()) {
            // iOS 13+ → CBManager.authorization var
            CBManager.authorization
        } else {
            // iOS 12 ve altı → "Always allowed" kabul edelim (eski model)
            CBManagerAuthorizationAllowedAlways
        }

        return when (auth) {
            CBManagerAuthorizationAllowedAlways -> PermissionStatus.Granted
            CBManagerAuthorizationDenied -> PermissionStatus.Denied(canAskAgain = false)
            CBManagerAuthorizationRestricted -> PermissionStatus.Restricted
            else -> PermissionStatus.NotDetermined
        }
    }

    actual suspend fun ensureBluetooth(): PermissionStatus {
        // iOS'ta doğrudan "request bluetooth permission" yok.
        // İlk gerçek BLE işlemi tetiklendiğinde sistem prompt'u çıkar.
        val status = currentBluetoothStatus()
        if (status !is PermissionStatus.NotDetermined) return status

        return suspendCancellableCoroutine { cont ->
            // CBCentralManager oluşturup state değişimini bekle.
            // (Bazı durumlarda prompt, ilk scan/connect denemesinde gelir.)
            val delegate = object : NSObject(), CBCentralManagerDelegateProtocol {
                override fun centralManagerDidUpdateState(central: CBCentralManager) {
                    // State değiştikçe authorization'ı kontrol et
                    val s = currentBluetoothStatus()
                    // NotDetermined ise küçük bir "ping" taraması deneyebiliriz
                    if (s is PermissionStatus.NotDetermined && central.state == CBManagerStatePoweredOn) {
                        // Kısa bir tarama başlat/durdur -> çoğu cihazda prompt'u tetikler
                        central.scanForPeripheralsWithServices(null, null)
                        central.stopScan()
                    } else {
                        cont.resume(s)
                    }
                }
            }
            // Main queue üzerinde manager oluştur
            val manager = CBCentralManager(delegate = delegate, queue = dispatch_get_main_queue())
            // İptalde güçlü referansları bırakmaya gerek yok; coroutine tamamlandığında GC temizler.
        }
    }

    actual suspend fun checkBluetooth(): PermissionStatus = currentBluetoothStatus()

    // -------- Bildirimler --------
    private suspend fun getNotificationSettings(): UNNotificationSettings =
        suspendCancellableCoroutine { cont ->
            UNUserNotificationCenter.currentNotificationCenter()
                .getNotificationSettingsWithCompletionHandler { settings ->
                    cont.resume(settings!!)
                }
        }

    actual suspend fun ensureNotifications(): PermissionStatus {
        val settings = getNotificationSettings()
        val authorized = settings.authorizationStatus == UNAuthorizationStatusAuthorized ||
                settings.authorizationStatus == UNAuthorizationStatusProvisional
        if (authorized) return PermissionStatus.Granted

        val granted = suspendCancellableCoroutine<Boolean> { cont ->
            UNUserNotificationCenter.currentNotificationCenter()
                .requestAuthorizationWithOptions(
                    options = UNAuthorizationOptionAlert or UNAuthorizationOptionSound or UNAuthorizationOptionBadge
                ) { ok, _ ->
                    cont.resume(ok)
                }
        }
        return if (granted) PermissionStatus.Granted else PermissionStatus.Denied(canAskAgain = true)
    }

    actual suspend fun checkNotifications(): PermissionStatus {
        val s = getNotificationSettings()
        return when (s.authorizationStatus) {
            UNAuthorizationStatusAuthorized, UNAuthorizationStatusProvisional -> PermissionStatus.Granted
            UNAuthorizationStatusDenied -> PermissionStatus.Denied(canAskAgain = false)
            else -> PermissionStatus.NotDetermined
        }
    }

    // -------- Konum (opsiyonel) --------
    private fun currentLocationStatus(): PermissionStatus {
        val s = CLLocationManager.authorizationStatus()
        return when (s) {
            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> PermissionStatus.Granted
            kCLAuthorizationStatusDenied -> PermissionStatus.Denied(canAskAgain = false)
            kCLAuthorizationStatusRestricted -> PermissionStatus.Restricted
            else -> PermissionStatus.NotDetermined
        }
    }

    actual suspend fun ensureLocation(): PermissionStatus {
        val now = currentLocationStatus()
        if (now is PermissionStatus.Granted) return now

        return suspendCancellableCoroutine { cont ->
            val manager = CLLocationManager()
            val delegate = object : NSObject(), CLLocationManagerDelegateProtocol {
                override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
                    val st = currentLocationStatus()
                    if (st !is PermissionStatus.NotDetermined) {
                        cont.resume(st)
                    }
                }
            }
            manager.delegate = delegate
            manager.requestWhenInUseAuthorization()
        }
    }

    actual suspend fun checkLocation(): PermissionStatus = currentLocationStatus()
    actual fun openAppSettings() {
        val url = NSURL(string = "app-settings:")
        if (url != null && UIApplication.sharedApplication.canOpenURL(url)) {
            UIApplication.sharedApplication.openURL(url)
        }
    }
}