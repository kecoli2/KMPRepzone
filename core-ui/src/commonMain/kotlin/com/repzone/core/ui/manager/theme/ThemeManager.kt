package com.repzone.core.ui.manager.theme

import com.repzone.core.enums.ThemeMode
import com.repzone.core.enums.ThemeType
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.config.IPresentationConfig
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Core tema yönetimi
 * Sadece renk şeması ve light/dark mode yönetir
 * Ayrıca dimensons larıda burada yönetilir
 * Modül seçimi build time'da yapılır
 */
class ThemeManager(private val iUserSession: IUserSession? = null) {

    //region Fields
    private val managerScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _themeMode = MutableStateFlow(ThemeMode.SYSTEM)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _currentColorSchemeId = MutableStateFlow<ThemeType?>(null)
    val currentColorSchemeId: StateFlow<ThemeType?> = _currentColorSchemeId.asStateFlow()

    private val _responsiveState = MutableStateFlow(ResponsiveState())
    val responsiveState: StateFlow<ResponsiveState> = _responsiveState.asStateFlow()

    private val _currentLanguage = MutableStateFlow("tr") // Default Türkçe
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()
    private var activePresentationConfig: IPresentationConfig? = null

    private val _languageChangeEvent = MutableSharedFlow<String>(replay = 0)
    val languageChangeEvent: SharedFlow<String> = _languageChangeEvent.asSharedFlow()

    //endregion Fields

    //region Public Method
    fun onDestroy() {
        managerScope.cancel()
    }
    fun loadSavedSettings(){
        iUserSession?.let { sessions ->

            sessions.getThemeMode()?.let { savedMode ->
                _themeMode.value = savedMode
            }

            sessions.getColorScheme()?.let { savedScheme ->
                _currentColorSchemeId.value = savedScheme
            }

            sessions.getLanguage()?.let { savedLanguage ->
                _currentLanguage.value = savedLanguage
            }
        }
    }

    fun initialize(config: IPresentationConfig) {
        activePresentationConfig = config
        loadSavedSettings()

        if (_currentColorSchemeId.value == null) {
            val defaultSchemeId = config.getDefaultColorSchemeId()
            _currentColorSchemeId.value = defaultSchemeId
        }
    }

    fun setColorScheme(themeType: ThemeType) {
        val config = activePresentationConfig ?: return
        val schemes = config.provideColorSchemes()

        if (schemes.any { it.id == themeType }) {
            _currentColorSchemeId.value = themeType
        }

        iUserSession?.let { session ->
            CoroutineScope(Dispatchers.IO).launch {
                session.saveColorScheme(themeType)
            }
        }
    }

    fun getCurrentColorScheme(): ColorSchemeVariant {
        val config = activePresentationConfig!!
        val schemeId = _currentColorSchemeId.value

        return config.provideColorSchemes().find { it.id == schemeId }!!
    }

    fun getAvailableColorSchemes(): List<ColorSchemeVariant> {
        return activePresentationConfig?.provideColorSchemes() ?: emptyList()
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode

        iUserSession?.let { session ->
            CoroutineScope(Dispatchers.IO).launch {
                session.saveThemeMode(mode)
            }
        }
    }

    fun toggleTheme() {
        val newMode = when (_themeMode.value) {
            ThemeMode.LIGHT -> ThemeMode.DARK
            ThemeMode.DARK -> ThemeMode.LIGHT
            ThemeMode.SYSTEM -> ThemeMode.DARK
        }

        setThemeMode(newMode)
    }

    fun getActiveModuleInfo(): String {
        return activePresentationConfig?.moduleName ?: "Not initialized"
    }

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

    fun setLanguage(languageCode: String) {
        _currentLanguage.value = languageCode
        managerScope.launch {
            iUserSession?.saveLanguage(languageCode)
            _languageChangeEvent.emit(languageCode)
        }
    }

    fun getCurrentLanguage(): String {
        return _currentLanguage.value
    }
    //endregion Public Method
}