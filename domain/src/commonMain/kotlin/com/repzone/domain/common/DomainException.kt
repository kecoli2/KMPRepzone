package com.repzone.domain.common

import com.repzone.core.model.StringResource
import com.repzone.core.util.extensions.fromResource

sealed class DomainException(val errorCode: ErrorCode, val params: Map<String, Any> = emptyMap(), cause: Throwable? = null) : Exception(errorCode.code.toString(), cause) {
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
enum class ErrorCode(val code: StringResource) {
    // General errors
    NOT_FOUND(StringResource.ERROR_NOT_FOUND),
    VALIDATION_ERROR(StringResource.ERROR_VALIDATION),
    DATABASE_ERROR(StringResource.ERROR_DATABASE),
    UNAUTHORIZED(StringResource.ERROR_UNAUTHORIZED),
    CONFLICT(StringResource.ERROR_CONFLICT),
    NETWORK_ERROR(StringResource.ERROR_NETWORK),
    UNKNOWN_ERROR(StringResource.ERROR_UNKNOWN),

    // Validation specific
    REQUIRED_FIELD(StringResource.ERROR_VALIDATION_REQUIRED_FIELD),
    INVALID_EMAIL(StringResource.ERROR_VALIDATION_INVALID_EMAIL),
    INVALID_PHONE(StringResource.ERROR_VALIDATION_INVALID_PHONE),

    // Network specific
    NO_INTERNET(StringResource.ERROR_NETWORK_NO_INTERNET),
    TIMEOUT(StringResource.ERROR_NETWORK_TIMEOUT),
    INVALID_GPS_LOCATION(StringResource.ERROR_INVALID_GPS_LOCATION),
    GPS_INTERVAL_TOO_SHORT(StringResource.ERROR_LOCATION_GPS_INTERVAL_MINIMUM),
    SYNC_INTERVAL_TOO_SHORT(StringResource.ERROR_SYNC_INTERVAL_MINIMUM),
    GPS_MIN_DISTANCE_NAGATIVE(StringResource.ERROR_LOCATION_GPS_MIN_DISTANCE_NEGATIVE),
    GPS_ACCURACY_THRESHOLD_NEGATIVE(StringResource.ERROR_LOCATION_GPS_ACCURACY_NEGATIVE),
    GPS_PERMISSION_NOT_GRANTED(StringResource.ERROR_LOCATION_PERMISSION_DENIED),
    ERROR_START_HOUR_RANGE(StringResource.ERROR_START_HOUR_RANGE),
    ERROR_START_MINUTE_RANGE(StringResource.ERROR_START_MINUTE_RANGE),
    ERROR_END_HOUR_RANGE(StringResource.ERROR_END_HOUR_RANGE),
    ERROR_END_MINUTE_RANGE(StringResource.ERROR_END_MINUTE_RANGE),
    ERROR_ACTIVE_DAYS_REQUIRED(StringResource.ERROR_ACTIVE_DAYS_REQUIRED),
    ERROR_OUT_OF_WORKING_HOURS(StringResource.ERROR_OUT_OF_WORKING_HOURS),
    ALREADY_RUNNING(StringResource.ERROR_ALREADY_RUNNING_GPS),
    ERROR_GPS_TIMEOUT(StringResource.ERROR_GPS_TIMEOUT),
    ERROR_GPS_ISNOT_ACTIVE(StringResource.ERROR_GPS_NOT_ACTIVE)
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

fun businessRuleException(errorCode: ErrorCode, params: Map<String, Any> = emptyMap(), cause: Throwable? = null): DomainException.BusinessRuleException =
    DomainException.BusinessRuleException(errorCode, params, cause)

fun networkException(errorCode: ErrorCode = ErrorCode.NETWORK_ERROR, cause: Throwable? = null): DomainException.NetworkException =
    DomainException.NetworkException(errorCode, emptyMap(), cause)