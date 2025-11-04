package com.repzone.domain.repository

interface ICustomerNoteRepository {
    suspend fun getCustomerNoteCount(selectedCustomerId: Int): Int
}