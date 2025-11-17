package com.repzone.presentation.legacy.viewmodel.gpstest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.domain.manager.gps.IGpsTrackingManager
import com.repzone.domain.model.SyncStatus
import com.repzone.domain.model.gps.GpsConfig
import com.repzone.domain.model.gps.GpsLocation
import com.repzone.domain.model.gps.ServiceState
import com.repzone.domain.model.gps.TrackingStatistics
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GpsTrackingViewModel(
    private val gpsTrackingManager: IGpsTrackingManager
) : ViewModel() {

    // UI State
    private val _uiState = MutableStateFlow(GpsTrackingUiState())
    val uiState: StateFlow<GpsTrackingUiState> = _uiState.asStateFlow()

    init {
        observeTrackingData()
    }

    /**
     * GPS tracking'i başlat
     */
    fun startTracking(config: GpsConfig? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            // Initialize ve start
            gpsTrackingManager.initialize()

            gpsTrackingManager.start()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isTracking = true,
                            error = null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isTracking = false,
                            error = error.message ?: "GPS başlatılamadı"
                        )
                    }
                }
        }
    }

    /**
     * GPS tracking'i durdur
     */
    fun stopTracking() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            gpsTrackingManager.stop()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isTracking = false,
                            lastLocation = null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message
                        )
                    }
                }
        }
    }

    /**
     * GPS tracking'i duraklat
     */
    fun pauseTracking() {
        viewModelScope.launch {
            gpsTrackingManager.pause()
                .onSuccess {
                    _uiState.update { it.copy(isPaused = true) }
                }
                .onError { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    /**
     * GPS tracking'i devam ettir
     */
    fun resumeTracking() {
        viewModelScope.launch {
            gpsTrackingManager.resume()
                .onSuccess {
                    _uiState.update { it.copy(isPaused = false) }
                }
                .onError { error ->
                    _uiState.update { it.copy(error = error.message) }
                }
        }
    }

    /**
     * Anında GPS al (force update)
     */
    fun forceGpsUpdate() {
        viewModelScope.launch {
            _uiState.update { it.copy(isForcingGps = true) }

            gpsTrackingManager.forceGpsUpdate()
                .onSuccess { location ->
                    _uiState.update {
                        it.copy(
                            isForcingGps = false,
                            lastLocation = location,
                            error = null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isForcingGps = false,
                            error = "GPS alınamadı: ${error.message}"
                        )
                    }
                }
        }
    }

    /**
     * Manuel sync tetikle
     */
    fun syncNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true) }

            gpsTrackingManager.forceSyncNow()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            error = null
                        )
                    }
                }
                .onError { error ->
                    _uiState.update {
                        it.copy(
                            isSyncing = false,
                            error = "Senkronizasyon hatası: ${error.message}"
                        )
                    }
                }
        }
    }

    /**
     * Config güncelle (çalışırken)
     */
    fun updateConfig(config: GpsConfig) {
        /*viewModelScope.launch {
            gpsTrackingManager.updateConfig(config)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            currentConfig = config,
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(error = "Config güncellenemedi: ${error.message}")
                    }
                }
        }*/
    }

    /**
     * Tracking verilerini gözlemle
     */
    private fun observeTrackingData() {
        // Last location
        viewModelScope.launch {
            gpsTrackingManager.observeLastLocation()
                .collect { location ->
                    _uiState.update { it.copy(lastLocation = location) }
                }
        }

        // Service state
        viewModelScope.launch {
            gpsTrackingManager.observeServiceState()
                .collect { state ->
                    _uiState.update {
                        it.copy(
                            serviceState = state,
                            isTracking = state is ServiceState.Running,
                            isPaused = state is ServiceState.Paused
                        )
                    }
                }
        }

        // Sync status
        viewModelScope.launch {
            gpsTrackingManager.observeSyncStatus()
                .collect { syncStatus ->
                    _uiState.update { it.copy(syncStatus = syncStatus) }
                }
        }

        // Statistics
        viewModelScope.launch {
            // Her 10 saniyede bir istatistikleri güncelle
            kotlinx.coroutines.delay(3_000)
            while (true) {
                try {
                    val stats = gpsTrackingManager.getStatistics()
                    _uiState.update { it.copy(statistics = stats) }
                } catch (e: Exception) {
                    // Ignore
                }
                kotlinx.coroutines.delay(3_000)
            }
        }
    }

    /**
     * Error'u temizle
     */
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        // ViewModel temizlendiğinde tracking'i durdurmayalım
        // Kullanıcı isterse devam etsin
    }
}

/**
 * UI State
 */
data class GpsTrackingUiState(
    val isLoading: Boolean = false,
    val isTracking: Boolean = false,
    val isPaused: Boolean = false,
    val isForcingGps: Boolean = false,
    val isSyncing: Boolean = false,
    val lastLocation: GpsLocation? = null,
    val currentConfig: GpsConfig? = null,
    val serviceState: ServiceState = ServiceState.Idle,
    val syncStatus: SyncStatus = SyncStatus.Idle,
    val statistics: TrackingStatistics? = null,
    val error: String? = null
) {
    /**
     * Tracking başlatılabilir mi?
     */
    val canStartTracking: Boolean
        get() = !isLoading && !isTracking

    /**
     * Tracking durdurulabilir mi?
     */
    val canStopTracking: Boolean
        get() = !isLoading && isTracking

    /**
     * Force GPS yapılabilir mi?
     */
    val canForceGps: Boolean
        get() = !isLoading && !isForcingGps

    /**
     * Manuel sync yapılabilir mi?
     */
    val canSync: Boolean
        get() = !isSyncing && statistics?.unsyncedCount ?: 0 > 0
}