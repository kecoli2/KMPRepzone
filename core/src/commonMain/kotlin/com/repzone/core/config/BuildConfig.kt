package com.repzone.core.config

import com.repzone.core.enums.*
object BuildConfig {
    private val uiModule: UIModule = UIModule.LEGACY
    const val apiEndpoint: String = "https://repzoneprodapi.azurewebsites.net"                
    const val IS_DEBUG: Boolean = false
    const val APP_VERSION: String = "1.0"
    val THEME_NAME: ThemeType = ThemeType.DEFAULT
    
    val activeUIModule: UIModule
        get() = uiModule
    
    fun isUIModuleActive(module: UIModule): Boolean {
        return uiModule == module
    }
}                       