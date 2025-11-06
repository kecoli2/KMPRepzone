package com.repzone.domain.model.dataset

data class ValidationResult(
    var isValid: Boolean = false,
    var error: String? = null
) {
    val hasError: Boolean
        get() = !error.isNullOrEmpty()
}