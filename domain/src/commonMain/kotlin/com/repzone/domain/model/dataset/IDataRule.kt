package com.repzone.domain.model.dataset

interface IDataRule {
    fun validate(value: String?): ValidationResult
}