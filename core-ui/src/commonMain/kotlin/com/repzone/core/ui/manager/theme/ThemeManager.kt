package com.repzone.core.ui.manager.theme

import com.repzone.core.enums.ThemeMode
import com.repzone.core.enums.ThemeType
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Core tema yönetimi
 * Sadece renk şeması ve light/dark mode yönetir
 * Ayrıca dimensons larıda burada yönetilir
 * Modül seçimi build time'da yapılır
 */
class ThemeManager {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _currentColorSchemeId = MutableStateFlow<ThemeType?>(null)
    val currentColorSchemeId: StateFlow<ThemeType?> = _currentColorSchemeId.asStateFlow()

    // YENİ: Responsive state
    private val _responsiveState = MutableStateFlow(ResponsiveState())
    val responsiveState: StateFlow<ResponsiveState> = _responsiveState.asStateFlow()

    // YENİ: Dil yönetimi
    private val _currentLanguage = MutableStateFlow("tr") // Default Türkçe
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    // Aktif presentation config (build time'da set edilir)
    private var activePresentationConfig: IPresentationConfig? = null

    /**
     * Build time'da aktif presentation modülünü set et
     * App.kt'de bir kez çağrılır
     */
    fun initialize(config: IPresentationConfig) {
        activePresentationConfig = config

        // Default renk şemasını set et
        val defaultSchemeId = config.getDefaultColorSchemeId()
        _currentColorSchemeId.value = defaultSchemeId
    }

    /**
     * Renk şemasını değiştir
     */
    fun setColorScheme(themeType: ThemeType) {
        val config = activePresentationConfig ?: return
        val schemes = config.provideColorSchemes()

        if (schemes.any { it.id == themeType }) {
            _currentColorSchemeId.value = themeType
        }
    }

    /**
     * Aktif renk şemasını getir
     */
    fun getCurrentColorScheme(): ColorSchemeVariant {
        val config = activePresentationConfig!!
        val schemeId = _currentColorSchemeId.value

        return config.provideColorSchemes().find { it.id == schemeId }!!
    }

    /**
     * Mevcut tüm renk şemalarını getir
     */
    fun getAvailableColorSchemes(): List<ColorSchemeVariant> {
        return activePresentationConfig?.provideColorSchemes() ?: emptyList()
    }

    /**
     * Tema modunu değiştir (Light/Dark/System)
     */
    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
    }

    /**
     * Light ve Dark arasında toggle
     */
    fun toggleTheme() {
        _themeMode.value = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }
    }

    /**
     * Aktif modül bilgisini getir
     */
    fun getActiveModuleInfo(): String {
        return activePresentationConfig?.moduleName ?: "Not initialized"
    }

    /**
     * YENİ: Responsive state'i güncelle
     * AppTheme composable tarafından otomatik çağrılır
     */
    fun updateResponsiveState(windowSizeClass: WindowSizeClass, isLandscape: Boolean) {
        val dimensions = when (windowSizeClass.widthSizeClass) {
            WindowWidthSizeClass.Compact -> DimensionDefaults.compact
            WindowWidthSizeClass.Medium -> DimensionDefaults.medium
            WindowWidthSizeClass.Expanded -> DimensionDefaults.expanded
        }

        _responsiveState.value = ResponsiveState(
            windowSizeClass = windowSizeClass,
            isLandscape = isLandscape,
            dimensions = dimensions
        )
    }

    /**
     * YENİ: Dil değiştir
     * @param languageCode: "tr", "en", "de", vb.
     */
    fun setLanguage(languageCode: String) {
        _currentLanguage.value = languageCode
    }

    /**
     * YENİ: Mevcut dili getir
     */
    fun getCurrentLanguage(): String {
        return _currentLanguage.value
    }
}