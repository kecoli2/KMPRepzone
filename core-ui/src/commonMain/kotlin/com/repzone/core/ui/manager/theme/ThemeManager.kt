package com.repzone.core.ui.manager.theme

import com.repzone.core.enums.ThemeMode
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Core tema yönetimi
 * Sadece renk şeması ve light/dark mode yönetir
 * Modül seçimi build time'da yapılır
 */
class ThemeManager {

    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _currentColorSchemeId = MutableStateFlow<String?>(null)
    val currentColorSchemeId: StateFlow<String?> = _currentColorSchemeId.asStateFlow()

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

        println("✅ [ThemeManager] Initialized with module: ${config.moduleId}")
        println("   Available schemes: ${config.provideColorSchemes().joinToString { it.name }}")
        println("   Default scheme: $defaultSchemeId")
    }

    /**
     * Renk şemasını değiştir
     */
    fun setColorScheme(schemeId: String) {
        val config = activePresentationConfig ?: return
        val schemes = config.provideColorSchemes()

        if (schemes.any { it.id == schemeId }) {
            _currentColorSchemeId.value = schemeId
            println("🎨 [ThemeManager] Color scheme changed to: $schemeId")
        } else {
            println("⚠️ [ThemeManager] Color scheme not found: $schemeId")
        }
    }

    /**
     * Aktif renk şemasını getir
     */
    fun getCurrentColorScheme(): ColorSchemeVariant? {
        val config = activePresentationConfig ?: return null
        val schemeId = _currentColorSchemeId.value ?: return null

        return config.provideColorSchemes().find { it.id == schemeId }
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
        println("🌓 [ThemeManager] Theme mode: $mode")
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
}