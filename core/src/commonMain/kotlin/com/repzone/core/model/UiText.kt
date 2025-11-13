package com.repzone.core.model

sealed class UiText {
    data class DynamicString(val value: String) : UiText()

    data class StringResourceId( val key: StringResource, val args: List<Any> = emptyList()) : UiText() {
        constructor(key: StringResource, vararg args: Any) : this(key, args.toList())
    }

    companion object {
        fun dynamic(value: String): UiText = DynamicString(value)
        fun resource(key: StringResource, vararg args: Any): UiText =
            StringResourceId(key, args.toList())
    }
}