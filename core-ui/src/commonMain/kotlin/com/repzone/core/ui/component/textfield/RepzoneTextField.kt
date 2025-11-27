package com.repzone.core.ui.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.repzone.core.platform.NumberFormatter

/**
 * Input tipi için enum
 */
enum class TextFieldInputType {
    TEXT,           // Normal metin girişi
    NUMBER,         // Tam sayı girişi (1234)
    DECIMAL,        // Ondalık sayı girişi (123,45)
    CURRENCY,       // Para birimi girişi (1.234,56 ₺)
    PHONE,          // Telefon numarası
    EMAIL           // E-posta
}

/**
 * Genel amaçlı TextField komponenti
 * Search, Currency, Number ve Text girişlerini destekler
 * Platform-aware NumberFormatter kullanır
 */
@Composable
fun RepzoneTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onSearch: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    enabled: Boolean = true,
    height: Dp = 35.dp,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 22.dp,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
    placeholderColor: Color = Color.Gray,
    iconTint: Color = Color.Gray,
    cursorColor: Color = Color.Red,
    leadingIcon: ImageVector? = Icons.Default.Search,
    showClearIcon: Boolean = true,
    inputType: TextFieldInputType = TextFieldInputType.TEXT,
    prefix: String? = null,
    suffix: String? = null,
    imeAction: ImeAction = if (inputType == TextFieldInputType.TEXT) ImeAction.Search else ImeAction.Done,
    maxLength: Int? = null,
    decimalPlaces: Int = 2,
    showCurrencySymbol: Boolean = true,
    showBorder: Boolean = false,
    borderWidth: Dp = 1.5.dp,
    borderColor: Color = Color.Transparent,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Gray.copy(alpha = 0.3f)
) {
    // Platform-aware NumberFormatter
    val formatter = remember { NumberFormatter() }

    // Focus state
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Input tipine göre keyboard type belirleme
    val keyboardType = remember(inputType) {
        when (inputType) {
            TextFieldInputType.TEXT -> KeyboardType.Text
            TextFieldInputType.NUMBER -> KeyboardType.Number
            TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> KeyboardType.Decimal
            TextFieldInputType.PHONE -> KeyboardType.Phone
            TextFieldInputType.EMAIL -> KeyboardType.Email
        }
    }

    // Visual transformation (currency formatting için)
    val visualTransformation = remember(inputType, formatter) {
        when (inputType) {
            TextFieldInputType.CURRENCY -> CurrencyVisualTransformation(
                decimalSeparator = formatter.decimalSeparator,
                groupingSeparator = formatter.groupingSeparator
            )
            else -> VisualTransformation.None
        }
    }

    // Input filtreleme
    val filteredOnValueChange: (String) -> Unit = { newValue ->
        val filtered = when (inputType) {
            TextFieldInputType.NUMBER -> newValue.filter { it.isDigit() }
            TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> {
                filterDecimalInput(
                    input = newValue,
                    decimalPlaces = decimalPlaces,
                    decimalSeparator = formatter.decimalSeparator
                )
            }
            TextFieldInputType.PHONE -> newValue.filter { it.isDigit() || it == '+' || it == ' ' || it == '-' }
            else -> newValue
        }

        // Max length kontrolü
        val finalValue = if (maxLength != null && filtered.length > maxLength) {
            filtered.take(maxLength)
        } else {
            filtered
        }

        onValueChange(finalValue)
    }

    // Suffix belirleme (currency için otomatik)
    val effectiveSuffix = when {
        suffix != null -> suffix
        inputType == TextFieldInputType.CURRENCY && showCurrencySymbol -> formatter.currencySymbol
        else -> null
    }

    // Border rengi belirleme
    val currentBorderColor = when {
        !showBorder -> Color.Transparent
        borderColor != Color.Transparent -> borderColor // Manuel renk verilmişse onu kullan
        isFocused -> focusedBorderColor
        else -> unfocusedBorderColor
    }

    val shape = RoundedCornerShape(cornerRadius)

    BasicTextField(
        value = value,
        onValueChange = filteredOnValueChange,
        modifier = modifier
            .height(height)
            .background(backgroundColor, shape)
            .then(
                if (showBorder) {
                    Modifier.border(borderWidth, currentBorderColor, shape)
                } else {
                    Modifier
                }
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        textStyle = textStyle,
        singleLine = true,
        enabled = enabled,
        cursorBrush = SolidColor(cursorColor),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch?.invoke() },
            onDone = { onSearch?.invoke() }
        ),
        visualTransformation = visualTransformation,
        interactionSource = interactionSource,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon
                if (leadingIcon != null) {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = "Icon",
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                // Prefix
                if (prefix != null) {
                    Text(
                        text = prefix,
                        style = textStyle.copy(color = placeholderColor),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // TextField + Placeholder
                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = textStyle.copy(color = placeholderColor),
                        )
                    }
                    innerTextField()
                }

                // Suffix
                if (effectiveSuffix != null && value.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = effectiveSuffix,
                        style = textStyle.copy(color = placeholderColor),
                    )
                }

                // Clear Icon
                if (showClearIcon && value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            onClear?.invoke() ?: onValueChange("")
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = iconTint,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    )
}

/**
 * Decimal input filtreleme
 */
private fun filterDecimalInput(
    input: String,
    decimalPlaces: Int,
    decimalSeparator: Char
): String {
    val otherSeparator = if (decimalSeparator == ',') '.' else ','

    // Diğer separator'ı locale'e uygun olana çevir
    var normalized = input.replace(otherSeparator, decimalSeparator)

    // Sadece rakam ve decimal separator'a izin ver
    normalized = normalized.filter { it.isDigit() || it == decimalSeparator }

    // Birden fazla decimal separator varsa sadece ilkini tut
    val parts = normalized.split(decimalSeparator)
    return when {
        parts.size <= 1 -> normalized
        else -> {
            val integerPart = parts[0]
            val decimalPart = parts[1].take(decimalPlaces)
            "$integerPart$decimalSeparator$decimalPart"
        }
    }
}

/**
 * Currency için visual transformation
 * 1234567,89 -> 1.234.567,89 (TR format için)
 */
private class CurrencyVisualTransformation(
    private val decimalSeparator: Char,
    private val groupingSeparator: Char
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        if (originalText.isEmpty()) {
            return TransformedText(text, OffsetMapping.Identity)
        }

        // Parse the number
        val parts = originalText.split(decimalSeparator)
        val integerPart = parts.getOrNull(0) ?: ""
        val decimalPart = parts.getOrNull(1)

        // Format integer part with grouping
        val formattedInteger = formatWithGrouping(integerPart)

        // Build final string
        val formattedText = if (decimalPart != null) {
            "$formattedInteger$decimalSeparator$decimalPart"
        } else if (originalText.endsWith(decimalSeparator)) {
            "$formattedInteger$decimalSeparator"
        } else {
            formattedInteger
        }

        return TransformedText(
            AnnotatedString(formattedText),
            CurrencyOffsetMapping(originalText, formattedText, groupingSeparator)
        )
    }

    private fun formatWithGrouping(integerPart: String): String {
        if (integerPart.isEmpty()) return ""

        val reversed = integerPart.reversed()
        val grouped = reversed.chunked(3).joinToString(groupingSeparator.toString())
        return grouped.reversed()
    }
}

/**
 * Currency offset mapping
 */
private class CurrencyOffsetMapping(
    private val originalText: String,
    private val formattedText: String,
    private val groupingSeparator: Char
) : OffsetMapping {

    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0) return 0
        if (originalText.isEmpty()) return 0

        var transformedOffset = 0
        var originalCount = 0

        for (char in formattedText) {
            if (originalCount >= offset) break
            transformedOffset++
            if (char != groupingSeparator) {
                originalCount++
            }
        }

        return transformedOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0) return 0
        if (formattedText.isEmpty()) return 0

        var originalOffset = 0
        var transformedCount = 0

        for (char in formattedText) {
            if (transformedCount >= offset) break
            transformedCount++
            if (char != groupingSeparator) {
                originalOffset++
            }
        }

        return originalOffset.coerceAtMost(originalText.length)
    }
}

// ============================================
// Convenience Composables
// ============================================

/**
 * Currency input için hazır component
 */
@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    decimalPlaces: Int = 2,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    borderWidth: Dp = 1.5.dp,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Gray.copy(alpha = 0.3f),
    showClearIcon: Boolean = true
) {
    RepzoneTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        inputType = TextFieldInputType.CURRENCY,
        decimalPlaces = decimalPlaces,
        leadingIcon = null,
        showClearIcon = showClearIcon,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        height = height,
        showBorder = showBorder,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
    )
}

/**
 * Number input için hazır component
 */
@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    maxLength: Int? = null,
    suffix: String? = null,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Gray.copy(alpha = 0.3f),
    borderWidth: Dp = 1.5.dp,
    showClearIcon: Boolean = true
) {
    RepzoneTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        inputType = TextFieldInputType.NUMBER,
        maxLength = maxLength,
        suffix = suffix,
        leadingIcon = null,
        showClearIcon = showClearIcon,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        height = height,
        showBorder = showBorder,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth
    )
}

/**
 * Decimal input için hazır component
 */
@Composable
fun DecimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    decimalPlaces: Int = 2,
    suffix: String? = null,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Gray.copy(alpha = 0.3f),
    borderWidth: Dp = 1.5.dp,
    showClearIcon: Boolean = true
) {
    RepzoneTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        enabled = enabled,
        inputType = TextFieldInputType.DECIMAL,
        decimalPlaces = decimalPlaces,
        suffix = suffix,
        leadingIcon = null,
        showClearIcon = showClearIcon,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        height = height,
        showBorder = showBorder,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
    )
}

/**
 * Search input için hazır component
 */
@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onSearch: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    enabled: Boolean = true,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 22.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = false,
    focusedBorderColor: Color = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor: Color = Color.Gray.copy(alpha = 0.3f),
    borderWidth: Dp = 1.5.dp,
    showClearIcon: Boolean = true
) {
    RepzoneTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        placeholder = placeholder,
        onSearch = onSearch,
        onClear = onClear,
        enabled = enabled,
        inputType = TextFieldInputType.TEXT,
        leadingIcon = Icons.Default.Search,
        showClearIcon = showClearIcon,
        backgroundColor = backgroundColor,
        cornerRadius = cornerRadius,
        height = height,
        showBorder = showBorder,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth
    )
}