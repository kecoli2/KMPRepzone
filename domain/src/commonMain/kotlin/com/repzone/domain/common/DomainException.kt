package com.repzone.domain.common

sealed class DomainException(val errorCode: ErrorCode, val params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : Exception(errorCode.code, cause) {
    class NotFoundException(val entityName: String, val entityId: Any, cause: Throwable? = null) : DomainException(
        errorCode = ErrorCode.NOT_FOUND,
        params = mapOf("entityName" to entityName, "entityId" to entityId),
        cause = cause
    )
    class ValidationException(val field: String? = null, errorCode: ErrorCode = ErrorCode.VALIDATION_ERROR, params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(
        errorCode = errorCode,
        params = if (field != null) params + ("field" to field) else params,
        cause = cause
    )

    class DatabaseException(errorCode: ErrorCode = ErrorCode.DATABASE_ERROR, params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(errorCode, params, cause)

    class BusinessRuleException(errorCode: ErrorCode, params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(errorCode, params, cause)

    class UnauthorizedException(errorCode: ErrorCode = ErrorCode.UNAUTHORIZED, params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(errorCode, params, cause)

    class ConflictException(val entityName: String, val field: String, val value: Any, cause: Throwable? = null) : DomainException(
        errorCode = ErrorCode.CONFLICT,
        params = mapOf("entityName" to entityName, "field" to field, "value" to value),
        cause = cause
    )
    class NetworkException(errorCode: ErrorCode = ErrorCode.NETWORK_ERROR, params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(errorCode, params, cause)
    class UnknownException(params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : DomainException(ErrorCode.UNKNOWN_ERROR, params, cause)
}
enum class ErrorCode(val code: String) {
    // General errors
    NOT_FOUND("error_not_found"),
    VALIDATION_ERROR("error_validation"),
    DATABASE_ERROR("error_database"),
    UNAUTHORIZED("error_unauthorized"),
    CONFLICT("error_conflict"),
    NETWORK_ERROR("error_network"),
    UNKNOWN_ERROR("error_unknown"),

    // Validation specific
    REQUIRED_FIELD("error_validation_required_field"),
    INVALID_EMAIL("error_validation_invalid_email"),
    INVALID_PHONE("error_validation_invalid_phone"),
    INVALID_FORMAT("error_validation_invalid_format"),
    VALUE_TOO_SHORT("error_validation_too_short"),
    VALUE_TOO_LONG("error_validation_too_long"),
    VALUE_OUT_OF_RANGE("error_validation_out_of_range"),

    // Business rules
    INSUFFICIENT_STOCK("error_business_insufficient_stock"),
    DUPLICATE_ENTRY("error_business_duplicate_entry"),
    OPERATION_NOT_ALLOWED("error_business_operation_not_allowed"),
    INVALID_STATE("error_business_invalid_state"),

    // Network specific
    NO_INTERNET("error_network_no_internet"),
    TIMEOUT("error_network_timeout"),
    SERVER_ERROR("error_network_server"),
}


fun notFoundException(entityName: String, id: Any): DomainException.NotFoundException =
    DomainException.NotFoundException(entityName, id)

fun validationException(errorCode: ErrorCode = ErrorCode.VALIDATION_ERROR, field: String? = null, params: Map<String, Any> = emptyMap()): DomainException.ValidationException = DomainException.ValidationException(field, errorCode, params)

fun requiredFieldException(fieldName: String): DomainException.ValidationException = DomainException.ValidationException(
        field = fieldName,
        errorCode = ErrorCode.REQUIRED_FIELD,
        params = mapOf("fieldName" to fieldName)
    )

fun databaseException(errorCode: ErrorCode = ErrorCode.DATABASE_ERROR, cause: Throwable? = null): DomainException.DatabaseException =
    DomainException.DatabaseException(errorCode, emptyMap(), cause)

fun businessRuleException(errorCode: ErrorCode, params: Map<String, Any> = emptyMap()): DomainException.BusinessRuleException =
    DomainException.BusinessRuleException(errorCode, params)

fun networkException(errorCode: ErrorCode = ErrorCode.NETWORK_ERROR, cause: Throwable? = null): DomainException.NetworkException =
    DomainException.NetworkException(errorCode, emptyMap(), cause)