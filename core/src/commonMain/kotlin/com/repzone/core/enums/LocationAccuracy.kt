package com.repzone.core.enums

enum class LocationAccuracy {
    HIGH,       // En yüksek doğruluk (GPS)
    BALANCED,   // Dengeli (GPS + Network)
    LOW,        // Düşük doğruluk (Network only)
    PASSIVE     // Pasif (diğer uygulamalardan)
}