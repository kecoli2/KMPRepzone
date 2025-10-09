package com.repzone.core.config

/**
 * Otomatik generate
 * ELLE DUZELTMEYINIZ
 *              
 */
object BuildConfig {
    const val USE_NEW_UI: Boolean = true
    const val IS_DEBUG: Boolean = false
    const val APP_VERSION: String = "1.0"
    const val THEME_NAME: String = "red"
    
    val activeUIModule: UIModule
        get() = if (USE_NEW_UI) UIModule.NEW else UIModule.LEGACY
    
    fun isUIModuleActive(module: UIModule): Boolean {
        return activeUIModule == module
    }
}

enum class UIModule {
    NEW,
    LEGACY
}