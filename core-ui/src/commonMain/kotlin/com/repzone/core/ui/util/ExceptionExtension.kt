package com.repzone.core.ui.util

import androidx.compose.runtime.Composable
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode

@Composable
fun DomainException.toMessage(): String {
    return when (this) {
        is DomainException.NotFoundException -> {
            // stringResource(Res.string.error_not_found, entityName, entityId.toString())
            "TODO: Use stringResource when resources are set up"
        }

        is DomainException.ValidationException -> {
            when (errorCode) {
                ErrorCode.REQUIRED_FIELD -> {
                    val fieldName = params["fieldName"] as? String ?: field ?: "Field"
                    // stringResource(Res.string.error_validation_required_field, fieldName)
                    "$fieldName is required"
                }
                ErrorCode.INVALID_EMAIL -> {
                    // stringResource(Res.string.error_validation_invalid_email)
                    "Invalid email format"
                }
                ErrorCode.INVALID_PHONE -> {
                    // stringResource(Res.string.error_validation_invalid_phone)
                    "Invalid phone number"
                }
                else -> {
                    // stringResource(Res.string.error_validation)
                    "Validation error"
                }
            }
        }

        is DomainException.DatabaseException -> {
            // stringResource(Res.string.error_database)
            "Database error occurred"
        }

        is DomainException.BusinessRuleException -> {
            when (errorCode) {
                ErrorCode.INSUFFICIENT_STOCK -> {
                    // stringResource(Res.string.error_business_insufficient_stock)
                    "Insufficient stock"
                }
                ErrorCode.DUPLICATE_ENTRY -> {
                    // stringResource(Res.string.error_business_duplicate_entry)
                    "This entry already exists"
                }
                else -> {
                    // stringResource(Res.string.error_business_rule)
                    "Business rule violation"
                }
            }
        }

        is DomainException.NetworkException -> {
            when (errorCode) {
                ErrorCode.NO_INTERNET -> {
                    // stringResource(Res.string.error_network_no_internet)
                    "No internet connection"
                }
                ErrorCode.TIMEOUT -> {
                    // stringResource(Res.string.error_network_timeout)
                    "Request timed out"
                }
                else -> {
                    // stringResource(Res.string.error_network)
                    "Network error occurred"
                }
            }
        }

        is DomainException.ConflictException -> {
            // stringResource(Res.string.error_conflict, entityName, field, value.toString())
            "$entityName with $field=$value already exists"
        }

        is DomainException.UnauthorizedException -> {
            // stringResource(Res.string.error_unauthorized)
            "Unauthorized operation"
        }

        is DomainException.UnknownException -> {
            // stringResource(Res.string.error_unknown)
            "An unknown error occurred"
        }
    }
}

@Composable
fun <T> com.repzone.domain.common.Result<T>.getErrorMessage(): String? {
    return when (this) {
        is com.repzone.domain.common.Result.Error -> exception.toMessage()
        is com.repzone.domain.common.Result.Success -> null
    }
}

fun DomainException.getErrorCode(): String = errorCode.code

fun DomainException.getParams(): Map<String, Any> = params