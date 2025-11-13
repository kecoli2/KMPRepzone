package com.repzone.core.platform

actual object Logger {
    actual fun d(tag: String, message: String) {
        println("[$tag] $message")
    }

    actual fun error(ex: Exception?) {
        println("[ERROR] ${ex?.message}")
    }

    actual fun d(message: String) {
        println("[Repzone] $message")
    }
}