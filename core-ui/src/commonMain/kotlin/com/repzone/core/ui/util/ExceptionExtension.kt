package com.repzone.core.ui.util

import androidx.compose.runtime.Composable
import com.repzone.core.model.StringResource
import com.repzone.core.model.UiText
import com.repzone.domain.common.DomainException
import com.repzone.domain.common.ErrorCode

@Composable
fun DomainException.toMessage(): UiText {
    return when (this) {
        is DomainException.NotFoundException -> {
            UiText.dynamic("TODO: Use stringResource when resources are set up")
        }

        is DomainException.ValidationException -> {
            when (errorCode) {
                ErrorCode.REQUIRED_FIELD -> {
                    val fieldName = params["fieldName"] as? String ?: field ?: "Field"
                    UiText.dynamic("$fieldName is required")

                }
                ErrorCode.INVALID_EMAIL -> {
                    UiText.resource(StringResource.INVALID_EMAIL)
                }
                ErrorCode.INVALID_PHONE -> {
                    UiText.resource(StringResource.PHONENUMBERINVALIDWARNINGMSG)
                }
                else -> {
                    UiText.dynamic("Validation error")
                }
            }
        }

        is DomainException.DatabaseException -> {
            UiText.dynamic("Database error occurred")
        }

        is DomainException.BusinessRuleException -> {
            when (errorCode) {
                else -> {
                    UiText.dynamic("Business rule violation")
                }
            }
        }

        is DomainException.NetworkException -> {
            when (errorCode) {
                ErrorCode.NO_INTERNET -> {
                    UiText.resource(StringResource.CHECK_INTERNET)
                }
                ErrorCode.TIMEOUT -> {
                    UiText.dynamic("Request timed out")
                }
                else -> {
                    UiText.dynamic("Network error occurred")
                }
            }
        }

        is DomainException.ConflictException -> {
            UiText.dynamic("$entityName with $field=$value already exists")

        }

        is DomainException.UnauthorizedException -> {
            UiText.resource(StringResource.LOGINREJECTBYSERVER)
        }

        is DomainException.UnknownException -> {
            UiText.dynamic("An unknown error occurred")
        }
    }
}

@Composable
fun <T> com.repzone.domain.common.Result<T>.getErrorMessage(): UiText? {
    return when (this) {
        is com.repzone.domain.common.Result.Error -> exception.toMessage()
        is com.repzone.domain.common.Result.Success -> null
    }
}

fun DomainException.getErrorCode(): String = errorCode.code

fun DomainException.getParams(): Map<String, Any> = params