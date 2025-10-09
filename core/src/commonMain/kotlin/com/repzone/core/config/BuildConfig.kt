package com.repzone.core.config

/**
 * Otomatik generate
 * ELLE DUZELTMEYINIZ
 *              
 */
object BuildConfig {
    private val uiModule: UIModule = UIModule.LEGACY
    const val endpointV1: String = "https://repzoneprodapi.azurewebsites.net"
    const val endpointV2: String = "https://repzone-mobile-api.azurewebsites.net"
    const val IS_DEBUG: Boolean = false
    const val APP_VERSION: String = "1.0"
    const val THEME_NAME: String = "red"
    
    val activeUIModule: UIModule
        get() = uiModule
    
    fun isUIModuleActive(module: UIModule): Boolean {
        return uiModule == module
    }
}

enum class UIModule {
    NEW,
    LEGACY
}