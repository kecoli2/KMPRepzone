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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import com.repzone.core.platform.NumberFormatter

enum class TextFieldInputType {
    TEXT,
    NUMBER,
    DECIMAL,
    CURRENCY,
    PHONE,
    EMAIL
}

enum class BorderType {
    FULL,
    BOTTOM_ONLY
}
enum class TextAlignment {
    START,
    CENTER,
    END
}

/**
 * Genel amaçlı TextField komponenti
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
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: Dp = 22.dp,
    textStyle: TextStyle? = null,
    textColor: Color = Color.Unspecified,
    placeholderColor: Color = Color.Unspecified,
    iconTint: Color = Color.Unspecified,
    cursorColor: Color = Color.Unspecified,
    leadingIcon: ImageVector? = Icons.Default.Search,
    showClearIcon: Boolean = true,
    inputType: TextFieldInputType = TextFieldInputType.TEXT,
    prefix: String? = null,
    suffix: String? = null,
    imeAction: ImeAction = if (inputType == TextFieldInputType.TEXT) ImeAction.Search else ImeAction.Done,
    maxLength: Int? = null,
    decimalPlaces: Int = 2,
    showCurrencySymbol: Boolean = true,
    // Border parametreleri
    showBorder: Boolean = false,
    borderType: BorderType = BorderType.FULL,
    borderWidth: Dp = 1.5.dp,
    borderColor: Color = Color.Transparent,
    focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified,
    // Text hizalama (placeholder ve yazılan text için)
    textAlignment: TextAlignment = TextAlignment.START,
    // Focus'ta seçim
    selectAllOnFocus: Boolean = false,
    // Maksimum değer (NUMBER, DECIMAL, CURRENCY için)
    maxValue: BigDecimal? = null
) {
    val formatter = remember { NumberFormatter() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Tema uyumlu renkler - Color.Unspecified ise tema rengini kullan
    val effectiveBackgroundColor = if (backgroundColor == Color.Unspecified) {
        MaterialTheme.colorScheme.surfaceContainerHighest
    } else {
        backgroundColor
    }

    val effectiveTextColor = if (textColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurface
    } else {
        textColor
    }

    val effectivePlaceholderColor = if (placeholderColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        placeholderColor
    }

    val effectiveIconTint = if (iconTint == Color.Unspecified) {
        MaterialTheme.colorScheme.onSurfaceVariant
    } else {
        iconTint
    }

    val effectiveCursorColor = if (cursorColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        cursorColor
    }

    val effectiveFocusedBorderColor = if (focusedBorderColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primary
    } else {
        focusedBorderColor
    }

    val effectiveUnfocusedBorderColor = if (unfocusedBorderColor == Color.Unspecified) {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    } else {
        unfocusedBorderColor
    }

    // TextAlign belirleme
    val textAlign = when (textAlignment) {
        TextAlignment.START -> TextAlign.Start
        TextAlignment.CENTER -> TextAlign.Center
        TextAlignment.END -> TextAlign.End
    }

    // TextStyle - ya verilen ya da tema bazlı
    val baseTextStyle = textStyle ?: MaterialTheme.typography.bodyMedium
    val alignedTextStyle = baseTextStyle.copy(
        color = effectiveTextColor,
        textAlign = textAlign
    )

    val keyboardType = remember(inputType) {
        when (inputType) {
            TextFieldInputType.TEXT -> KeyboardType.Text
            TextFieldInputType.NUMBER -> KeyboardType.Number
            TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> KeyboardType.Decimal
            TextFieldInputType.PHONE -> KeyboardType.Phone
            TextFieldInputType.EMAIL -> KeyboardType.Email
        }
    }

    val visualTransformation = remember(inputType, formatter) {
        when (inputType) {
            TextFieldInputType.CURRENCY -> CurrencyVisualTransformation(
                decimalSeparator = formatter.decimalSeparator,
                groupingSeparator = formatter.groupingSeparator
            )
            else -> VisualTransformation.None
        }
    }

    val filteredOnValueChange: (String) -> Unit = { newValue ->
        val filtered = when (inputType) {
            TextFieldInputType.NUMBER -> newValue.filter { it.isDigit() }
            TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> {
                filterDecimalInput(newValue, decimalPlaces, formatter.decimalSeparator)
            }
            TextFieldInputType.PHONE -> newValue.filter { it.isDigit() || it == '+' || it == ' ' || it == '-' }
            else -> newValue
        }

        // maxLength uygula
        val afterMaxLength = if (maxLength != null && filtered.length > maxLength) {
            filtered.take(maxLength)
        } else {
            filtered
        }

        // maxValue uygula (NUMBER, DECIMAL, CURRENCY için)
        val finalValue = if (maxValue != null && afterMaxLength.isNotEmpty() &&
            inputType in listOf(TextFieldInputType.NUMBER, TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY)) {
            applyMaxValue(afterMaxLength, maxValue, formatter.decimalSeparator)
        } else {
            afterMaxLength
        }

        onValueChange(finalValue)
    }

    val effectiveSuffix = when {
        suffix != null -> suffix
        inputType == TextFieldInputType.CURRENCY && showCurrencySymbol -> formatter.currencySymbol
        else -> null
    }

    val currentBorderColor = when {
        !showBorder -> Color.Transparent
        borderColor != Color.Transparent -> borderColor
        isFocused -> effectiveFocusedBorderColor
        else -> effectiveUnfocusedBorderColor
    }

    val shape = RoundedCornerShape(cornerRadius)

    // Border modifier
    val borderModifier = when {
        !showBorder -> Modifier
        borderType == BorderType.BOTTOM_ONLY -> Modifier.drawBehind {
            val strokeWidth = borderWidth.toPx()
            drawLine(
                color = currentBorderColor,
                start = Offset(0f, size.height - strokeWidth / 2),
                end = Offset(size.width, size.height - strokeWidth / 2),
                strokeWidth = strokeWidth
            )
        }
        else -> Modifier.border(borderWidth, currentBorderColor, shape)
    }

    if (selectAllOnFocus) {
        // TextFieldValue state'i
        var textFieldValue by remember {
            mutableStateOf(TextFieldValue(text = value, selection = TextRange(value.length)))
        }

        // Önceki focus durumunu takip et
        var wasFocused by remember { mutableStateOf(false) }

        // Dışarıdan value değişirse güncelle (ama selection'ı koru)
        LaunchedEffect(value) {
            if (textFieldValue.text != value) {
                textFieldValue = textFieldValue.copy(text = value)
            }
        }

        // Sadece focus KAZANILDIĞINDA tümünü seç (false -> true geçişi)
        LaunchedEffect(isFocused) {
            if (isFocused && !wasFocused && textFieldValue.text.isNotEmpty()) {
                textFieldValue = textFieldValue.copy(
                    selection = TextRange(0, textFieldValue.text.length)
                )
            }
            wasFocused = isFocused
        }

        BasicTextField(
            value = textFieldValue,
            onValueChange = { newValue ->
                // Önce filtrele
                val filteredText = when (inputType) {
                    TextFieldInputType.NUMBER -> newValue.text.filter { it.isDigit() }
                    TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> {
                        filterDecimalInput(newValue.text, decimalPlaces, formatter.decimalSeparator)
                    }
                    TextFieldInputType.PHONE -> newValue.text.filter { it.isDigit() || it == '+' || it == ' ' || it == '-' }
                    else -> newValue.text
                }

                // maxLength uygula
                val afterMaxLength = if (maxLength != null && filteredText.length > maxLength) {
                    filteredText.take(maxLength)
                } else {
                    filteredText
                }

                // maxValue uygula (NUMBER, DECIMAL, CURRENCY için)
                val finalText = if (maxValue != null && afterMaxLength.isNotEmpty() &&
                    inputType in listOf(TextFieldInputType.NUMBER, TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY)) {
                    applyMaxValue(afterMaxLength, maxValue, formatter.decimalSeparator)
                } else {
                    afterMaxLength
                }

                // Selection'ı ayarla
                val newSelection = TextRange(
                    minOf(newValue.selection.start, finalText.length),
                    minOf(newValue.selection.end, finalText.length)
                )

                textFieldValue = TextFieldValue(text = finalText, selection = newSelection)
                onValueChange(finalText)
            },
            modifier = modifier
                .height(height)
                .background(effectiveBackgroundColor, shape)
                .then(borderModifier)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textStyle = alignedTextStyle,
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(effectiveCursorColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch?.invoke() },
                onDone = { onSearch?.invoke() }
            ),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                DecorationContent(
                    value = value,
                    innerTextField = innerTextField,
                    leadingIcon = leadingIcon,
                    iconTint = effectiveIconTint,
                    prefix = prefix,
                    placeholderColor = effectivePlaceholderColor,
                    placeholder = placeholder,
                    textAlignment = textAlignment,
                    textStyle = alignedTextStyle,
                    effectiveSuffix = effectiveSuffix,
                    showClearIcon = showClearIcon,
                    onClear = onClear,
                    onValueChange = onValueChange
                )
            }
        )
    } else {
        BasicTextField(
            value = value,
            onValueChange = filteredOnValueChange,
            modifier = modifier
                .height(height)
                .background(effectiveBackgroundColor, shape)
                .then(borderModifier)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textStyle = alignedTextStyle,
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(effectiveCursorColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch?.invoke() },
                onDone = { onSearch?.invoke() }
            ),
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            decorationBox = { innerTextField ->
                DecorationContent(
                    value = value,
                    innerTextField = innerTextField,
                    leadingIcon = leadingIcon,
                    iconTint = effectiveIconTint,
                    prefix = prefix,
                    placeholderColor = effectivePlaceholderColor,
                    placeholder = placeholder,
                    textAlignment = textAlignment,
                    textStyle = alignedTextStyle,
                    effectiveSuffix = effectiveSuffix,
                    showClearIcon = showClearIcon,
                    onClear = onClear,
                    onValueChange = onValueChange
                )
            }
        )
    }
}

@Composable
private fun DecorationContent(
    value: String,
    innerTextField: @Composable () -> Unit,
    leadingIcon: ImageVector?,
    iconTint: Color,
    prefix: String?,
    placeholderColor: Color,
    placeholder: String,
    textAlignment: TextAlignment,
    textStyle: TextStyle,
    effectiveSuffix: String?,
    showClearIcon: Boolean,
    onClear: (() -> Unit)?,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingIcon != null) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = "Icon",
                tint = iconTint,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (prefix != null) {
            Text(text = prefix, style = textStyle.copy(color = placeholderColor))
            Spacer(modifier = Modifier.width(4.dp))
        }

        Box(
            modifier = Modifier.weight(1f)
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = placeholderColor),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            // propagateMinConstraints = true ile innerTextField genişliği zorlanır
            Box(
                modifier = Modifier.fillMaxWidth(),
                propagateMinConstraints = true
            ) {
                innerTextField()
            }
        }

        if (effectiveSuffix != null && value.isNotEmpty()) {
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = effectiveSuffix, style = textStyle.copy(color = placeholderColor))
        }

        if (showClearIcon && value.isNotEmpty()) {
            IconButton(
                onClick = { onClear?.invoke() ?: onValueChange("") },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear",
                    tint = iconTint,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun filterDecimalInput(input: String, decimalPlaces: Int, decimalSeparator: Char): String {
    val filtered = input.filter { it.isDigit() || it == decimalSeparator || it == '.' || it == ',' }
    val normalized = filtered.replace('.', decimalSeparator).replace(',', decimalSeparator)
    val parts = normalized.split(decimalSeparator)
    return when {
        parts.size == 1 -> parts[0]
        parts.size >= 2 -> {
            val integerPart = parts[0]
            val decimalPart = parts[1].take(decimalPlaces)
            if (decimalPart.isEmpty()) "$integerPart$decimalSeparator"
            else "$integerPart$decimalSeparator$decimalPart"
        }
        else -> normalized
    }
}

/**
 * MaxValue kontrolü - girilen değer maxValue'dan büyükse maxValue döner
 */
private fun applyMaxValue(input: String, maxValue: BigDecimal, decimalSeparator: Char): String {
    if (input.isEmpty() || input == decimalSeparator.toString()) return input

    try {
        // Decimal separator'ı nokta ile değiştir (BigDecimal parse için)
        val normalizedInput = input.replace(decimalSeparator, '.')

        // Sondaki nokta varsa kaldır (geçici olarak)
        val parseableInput = normalizedInput.trimEnd('.')

        if (parseableInput.isEmpty()) return input

        val inputValue = BigDecimal.parseString(parseableInput)

        return if (inputValue > maxValue) {
            // MaxValue'yu input formatına çevir
            val maxValueStr = maxValue.toPlainString()
            if (decimalSeparator != '.') {
                maxValueStr.replace('.', decimalSeparator)
            } else {
                maxValueStr
            }
        } else {
            input
        }
    } catch (e: Exception) {
        // Parse hatası olursa input'u olduğu gibi döndür
        return input
    }
}

private class CurrencyVisualTransformation(
    private val decimalSeparator: Char,
    private val groupingSeparator: Char
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        if (originalText.isEmpty()) return TransformedText(text, OffsetMapping.Identity)

        val parts = originalText.split(decimalSeparator)
        val integerPart = parts.getOrNull(0) ?: ""
        val decimalPart = parts.getOrNull(1)
        val formattedInteger = formatWithGrouping(integerPart)

        val formattedText = when {
            decimalPart != null -> "$formattedInteger$decimalSeparator$decimalPart"
            originalText.endsWith(decimalSeparator) -> "$formattedInteger$decimalSeparator"
            else -> formattedInteger
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

private class CurrencyOffsetMapping(
    private val originalText: String,
    private val formattedText: String,
    private val groupingSeparator: Char
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        if (offset <= 0 || originalText.isEmpty()) return 0
        var transformedOffset = 0
        var originalCount = 0
        for (char in formattedText) {
            if (originalCount >= offset) break
            transformedOffset++
            if (char != groupingSeparator) originalCount++
        }
        return transformedOffset
    }

    override fun transformedToOriginal(offset: Int): Int {
        if (offset <= 0 || formattedText.isEmpty()) return 0
        var originalOffset = 0
        var transformedCount = 0
        for (char in formattedText) {
            if (transformedCount >= offset) break
            transformedCount++
            if (char != groupingSeparator) originalOffset++
        }
        return originalOffset.coerceAtMost(originalText.length)
    }
}

// ============================================
// Convenience Composables
// ============================================

@Composable
fun CurrencyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    decimalPlaces: Int = 2,
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    borderType: BorderType = BorderType.FULL,
    borderWidth: Dp = 1.5.dp,
    focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified,
    showClearIcon: Boolean = true,
    textAlignment: TextAlignment = TextAlignment.START,
    selectAllOnFocus: Boolean = false,
    maxValue: BigDecimal? = null
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
        borderType = borderType,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
        textAlignment = textAlignment,
        selectAllOnFocus = selectAllOnFocus,
        maxValue = maxValue
    )
}

@Composable
fun NumberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    maxLength: Int? = null,
    suffix: String? = null,
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    borderType: BorderType = BorderType.FULL,
    focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.5.dp,
    showClearIcon: Boolean = true,
    textAlignment: TextAlignment = TextAlignment.START,
    selectAllOnFocus: Boolean = false,
    maxValue: BigDecimal? = null
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
        borderType = borderType,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
        textAlignment = textAlignment,
        selectAllOnFocus = selectAllOnFocus,
        maxValue = maxValue
    )
}

@Composable
fun DecimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "0",
    enabled: Boolean = true,
    decimalPlaces: Int = 2,
    suffix: String? = null,
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: Dp = 8.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = true,
    borderType: BorderType = BorderType.FULL,
    focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified,
    borderWidth: Dp = 0.7.dp,
    showClearIcon: Boolean = true,
    textAlignment: TextAlignment = TextAlignment.START,
    selectAllOnFocus: Boolean = false,
    maxValue: BigDecimal? = null
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
        borderType = borderType,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
        textAlignment = textAlignment,
        selectAllOnFocus = selectAllOnFocus,
        maxValue = maxValue
    )
}

@Composable
fun SearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "",
    onSearch: (() -> Unit)? = null,
    onClear: (() -> Unit)? = null,
    enabled: Boolean = true,
    backgroundColor: Color = Color.Unspecified,
    cornerRadius: Dp = 22.dp,
    height: Dp = 35.dp,
    showBorder: Boolean = false,
    borderType: BorderType = BorderType.FULL,
    focusedBorderColor: Color = Color.Unspecified,
    unfocusedBorderColor: Color = Color.Unspecified,
    borderWidth: Dp = 1.5.dp,
    showClearIcon: Boolean = true,
    textAlignment: TextAlignment = TextAlignment.START,
    selectAllOnFocus: Boolean = false
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
        borderType = borderType,
        focusedBorderColor = focusedBorderColor,
        unfocusedBorderColor = unfocusedBorderColor,
        borderWidth = borderWidth,
        textAlignment = textAlignment,
        selectAllOnFocus = selectAllOnFocus
    )
}