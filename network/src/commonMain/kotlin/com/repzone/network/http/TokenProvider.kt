package com.repzone.network.http

fun interface TokenProvider {
    fun token(): String?
}