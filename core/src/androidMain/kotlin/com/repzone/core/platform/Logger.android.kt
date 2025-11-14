package com.repzone.core.platform

actual object Logger {
    actual fun d(tag: String, message: String) {
        android.util.Log.d(tag, message)
    }

    actual fun error(ex: Exception?) {
        android.util.Log.e("ERROR", ex?.message ?: "Unknown error")
    }

    actual fun d(message: String) {
        android.util.Log.d("Repzone", message)
    }

    actual fun error(tag: String, ex: Exception?) {
        android.util.Log.e(tag, ex?.message ?: "Unknown error")
    }
}