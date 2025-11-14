@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.repzone.core.platform

expect object Logger {
    fun d(tag: String, message: String)
    fun d( message: String)
    fun error(ex: Exception?)
    fun error(tag: String, ex: Exception?)
}