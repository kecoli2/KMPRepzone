package org.repzone.mobile

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform