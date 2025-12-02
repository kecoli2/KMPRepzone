package com.repzone.core.ui.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.draw.clip
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
    keyboardActions: KeyboardActions? = null,
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
    maxValue: BigDecimal? = null,
    // Minimum değer (NUMBER, DECIMAL, CURRENCY için) - step butonları için
    minValue: BigDecimal? = null,
    // Focus requester
    focusRequester: FocusRequester? = null,
    // Step butonları parametreleri
    showStepButtons: Boolean = false,
    stepValue: BigDecimal = BigDecimal.ONE,
    stepButtonSize: Dp = 32.dp,
    stepButtonBackgroundColor: Color = Color.Unspecified,
    stepButtonIconColor: Color = Color.Unspecified,
    // İçeriğe göre boyutlanma (NUMBER, DECIMAL için step butonlarıyla kullanılır)
    wrapContentWidth: Boolean = false,
    minTextFieldWidth: Dp = 45.dp,
    maxTextFieldWidth: Dp = 120.dp
) {
    val formatter = remember { NumberFormatter() }
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

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

    val effectiveStepButtonBgColor = if (stepButtonBackgroundColor == Color.Unspecified) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        stepButtonBackgroundColor
    }

    val effectiveStepButtonIconColor = if (stepButtonIconColor == Color.Unspecified) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        stepButtonIconColor
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

    val effectiveKeyboardActions = keyboardActions ?: KeyboardActions(
        onSearch = { onSearch?.invoke() },
        onDone = { onSearch?.invoke() }
    )

    // Step butonları için artırma/azaltma fonksiyonları
    val onIncrease: () -> Unit = {
        val currentValue = parseValueToBigDecimal(value, formatter.decimalSeparator)
        val newValue = currentValue + stepValue
        val finalValue = if (maxValue != null && newValue > maxValue) maxValue else newValue
        onValueChange(formatBigDecimalToString(finalValue, inputType, decimalPlaces, formatter.decimalSeparator))
    }

    val onDecrease: () -> Unit = {
        val currentValue = parseValueToBigDecimal(value, formatter.decimalSeparator)
        val newValue = currentValue - stepValue
        val effectiveMinValue = minValue ?: BigDecimal.ZERO
        val finalValue = if (newValue < effectiveMinValue) effectiveMinValue else newValue
        onValueChange(formatBigDecimalToString(finalValue, inputType, decimalPlaces, formatter.decimalSeparator))
    }

    // Step butonları gösterilecek mi kontrol et (sadece NUMBER, DECIMAL, CURRENCY için)
    val shouldShowStepButtons = showStepButtons && inputType in listOf(
        TextFieldInputType.NUMBER,
        TextFieldInputType.DECIMAL,
        TextFieldInputType.CURRENCY
    )

    // Step butonları varsa wrapper Row kullan
    if (shouldShowStepButtons) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Eksi butonu (solda) - Yuvarlak
            CircleStepButton(
                icon = Icons.Default.Remove,
                onClick = onDecrease,
                enabled = enabled,
                size = stepButtonSize,
                backgroundColor = effectiveStepButtonBgColor,
                iconColor = effectiveStepButtonIconColor
            )

            // TextField (ortada)
            CoreTextField(
                value = value,
                onValueChange = filteredOnValueChange,
                modifier = if (wrapContentWidth) {
                    Modifier
                        .width(IntrinsicSize.Min)
                        .widthIn(min = minTextFieldWidth, max = maxTextFieldWidth)
                } else {
                    Modifier.weight(1f)
                },
                focusRequester = focusRequester,
                height = height,
                effectiveBackgroundColor = effectiveBackgroundColor,
                shape = shape,
                borderModifier = borderModifier,
                alignedTextStyle = alignedTextStyle,
                enabled = enabled,
                effectiveCursorColor = effectiveCursorColor,
                keyboardType = keyboardType,
                imeAction = imeAction,
                effectiveKeyboardActions = effectiveKeyboardActions,
                visualTransformation = visualTransformation,
                interactionSource = interactionSource,
                leadingIcon = leadingIcon,
                effectiveIconTint = effectiveIconTint,
                prefix = prefix,
                effectivePlaceholderColor = effectivePlaceholderColor,
                placeholder = placeholder,
                textAlignment = textAlignment,
                effectiveSuffix = effectiveSuffix,
                showClearIcon = false, // Step butonları varken clear icon gösterme
                onClear = onClear,
                selectAllOnFocus = selectAllOnFocus,
                isFocused = isFocused,
                inputType = inputType,
                decimalPlaces = decimalPlaces,
                maxLength = maxLength,
                maxValue = maxValue,
                formatter = formatter,
                wrapContentWidth = wrapContentWidth
            )

            // Artı butonu (sağda) - Yuvarlak
            CircleStepButton(
                icon = Icons.Default.Add,
                onClick = onIncrease,
                enabled = enabled,
                size = stepButtonSize,
                backgroundColor = effectiveStepButtonBgColor,
                iconColor = effectiveStepButtonIconColor
            )
        }
    } else {
        // Step butonları yok - normal TextField
        CoreTextField(
            value = value,
            onValueChange = filteredOnValueChange,
            modifier = modifier,
            focusRequester = focusRequester,
            height = height,
            effectiveBackgroundColor = effectiveBackgroundColor,
            shape = shape,
            borderModifier = borderModifier,
            alignedTextStyle = alignedTextStyle,
            enabled = enabled,
            effectiveCursorColor = effectiveCursorColor,
            keyboardType = keyboardType,
            imeAction = imeAction,
            effectiveKeyboardActions = effectiveKeyboardActions,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            leadingIcon = leadingIcon,
            effectiveIconTint = effectiveIconTint,
            prefix = prefix,
            effectivePlaceholderColor = effectivePlaceholderColor,
            placeholder = placeholder,
            textAlignment = textAlignment,
            effectiveSuffix = effectiveSuffix,
            showClearIcon = showClearIcon,
            onClear = onClear,
            selectAllOnFocus = selectAllOnFocus,
            isFocused = isFocused,
            inputType = inputType,
            decimalPlaces = decimalPlaces,
            maxLength = maxLength,
            maxValue = maxValue,
            formatter = formatter,
            wrapContentWidth = wrapContentWidth
        )
    }
}

/**
 * Yuvarlak step butonu
 */
@Composable
private fun CircleStepButton(
    icon: ImageVector,
    onClick: () -> Unit,
    enabled: Boolean,
    size: Dp,
    backgroundColor: Color,
    iconColor: Color
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .background(if (enabled) backgroundColor else backgroundColor.copy(alpha = 0.5f))
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = if (icon == Icons.Default.Add) "Artır" else "Azalt",
            tint = if (enabled) iconColor else iconColor.copy(alpha = 0.5f),
            modifier = Modifier.size(size * 0.55f)
        )
    }
}

@Composable
private fun CoreTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    focusRequester: FocusRequester?,
    height: Dp,
    effectiveBackgroundColor: Color,
    shape: RoundedCornerShape,
    borderModifier: Modifier,
    alignedTextStyle: TextStyle,
    enabled: Boolean,
    effectiveCursorColor: Color,
    keyboardType: KeyboardType,
    imeAction: ImeAction,
    effectiveKeyboardActions: KeyboardActions,
    visualTransformation: VisualTransformation,
    interactionSource: MutableInteractionSource,
    leadingIcon: ImageVector?,
    effectiveIconTint: Color,
    prefix: String?,
    effectivePlaceholderColor: Color,
    placeholder: String,
    textAlignment: TextAlignment,
    effectiveSuffix: String?,
    showClearIcon: Boolean,
    onClear: (() -> Unit)?,
    selectAllOnFocus: Boolean,
    isFocused: Boolean,
    inputType: TextFieldInputType,
    decimalPlaces: Int,
    maxLength: Int?,
    maxValue: BigDecimal?,
    formatter: NumberFormatter,
    wrapContentWidth: Boolean = false
) {
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
                .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
                .height(height)
                .background(effectiveBackgroundColor, shape)
                .then(borderModifier)
                .padding(
                    horizontal = if (wrapContentWidth) 6.dp else 12.dp,
                    vertical = if (wrapContentWidth) 4.dp else 8.dp
                ),
            textStyle = alignedTextStyle,
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(effectiveCursorColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = effectiveKeyboardActions,
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
                    onValueChange = onValueChange,
                    wrapContentWidth = wrapContentWidth
                )
            }
        )
    } else {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier
                .then(focusRequester?.let { Modifier.focusRequester(it) } ?: Modifier)
                .height(height)
                .background(effectiveBackgroundColor, shape)
                .then(borderModifier)
                .padding(
                    horizontal = if (wrapContentWidth) 6.dp else 12.dp,
                    vertical = if (wrapContentWidth) 4.dp else 8.dp
                ),
            textStyle = alignedTextStyle,
            singleLine = true,
            enabled = enabled,
            cursorBrush = SolidColor(effectiveCursorColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
            keyboardActions = effectiveKeyboardActions,
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
                    onValueChange = onValueChange,
                    wrapContentWidth = wrapContentWidth
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
    onValueChange: (String) -> Unit,
    wrapContentWidth: Boolean = false
) {
    Row(
        modifier = if (wrapContentWidth) Modifier else Modifier.fillMaxWidth(),
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
            modifier = if (wrapContentWidth) Modifier else Modifier.weight(1f),
            contentAlignment = if (wrapContentWidth) Alignment.Center else Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    text = placeholder,
                    style = textStyle.copy(color = placeholderColor),
                    modifier = if (wrapContentWidth) Modifier else Modifier.fillMaxWidth()
                )
            }
            Box(
                modifier = if (wrapContentWidth) Modifier else Modifier.fillMaxWidth(),
                propagateMinConstraints = !wrapContentWidth
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

// Value'yu BigDecimal'e parse et
private fun parseValueToBigDecimal(value: String, decimalSeparator: Char): BigDecimal {
    if (value.isEmpty()) return BigDecimal.ZERO
    return try {
        val normalized = value.replace(decimalSeparator, '.')
        BigDecimal.parseString(normalized)
    } catch (e: Exception) {
        BigDecimal.ZERO
    }
}

// BigDecimal'i String'e formatla
private fun formatBigDecimalToString(
    value: BigDecimal,
    inputType: TextFieldInputType,
    decimalPlaces: Int,
    decimalSeparator: Char
): String {
    return when (inputType) {
        TextFieldInputType.NUMBER -> value.toBigInteger().toString()
        TextFieldInputType.DECIMAL, TextFieldInputType.CURRENCY -> {
            val str = value.toPlainString()
            if (decimalSeparator != '.') {
                str.replace('.', decimalSeparator)
            } else {
                str
            }
        }
        else -> value.toPlainString()
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
    maxValue: BigDecimal? = null,
    minValue: BigDecimal? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    // Step butonları parametreleri
    showStepButtons: Boolean = false,
    stepValue: BigDecimal = BigDecimal.ONE,
    stepButtonSize: Dp = 32.dp,
    stepButtonBackgroundColor: Color = Color.Unspecified,
    stepButtonIconColor: Color = Color.Unspecified
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
        maxValue = maxValue,
        minValue = minValue,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        showStepButtons = showStepButtons,
        stepValue = stepValue,
        stepButtonSize = stepButtonSize,
        stepButtonBackgroundColor = stepButtonBackgroundColor,
        stepButtonIconColor = stepButtonIconColor
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
    maxValue: BigDecimal? = null,
    minValue: BigDecimal? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    focusRequester: FocusRequester? = null,
    showStepButtons: Boolean = false,
    stepValue: BigDecimal = BigDecimal.ONE,
    stepButtonSize: Dp = 32.dp,
    stepButtonBackgroundColor: Color = Color.Unspecified,
    stepButtonIconColor: Color = Color.Unspecified,
    wrapContentWidth: Boolean = false,
    minTextFieldWidth: Dp = 45.dp,
    maxTextFieldWidth: Dp = 120.dp
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
        maxValue = maxValue,
        minValue = minValue,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        focusRequester = focusRequester,
        showStepButtons = showStepButtons,
        stepValue = stepValue,
        stepButtonSize = stepButtonSize,
        stepButtonBackgroundColor = stepButtonBackgroundColor,
        stepButtonIconColor = stepButtonIconColor,
        wrapContentWidth = wrapContentWidth,
        minTextFieldWidth = minTextFieldWidth,
        maxTextFieldWidth = maxTextFieldWidth
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
    maxValue: BigDecimal? = null,
    minValue: BigDecimal? = null,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions? = null,
    // Step butonları parametreleri
    showStepButtons: Boolean = false,
    stepValue: BigDecimal = BigDecimal.ONE,
    stepButtonSize: Dp = 32.dp,
    stepButtonBackgroundColor: Color = Color.Unspecified,
    stepButtonIconColor: Color = Color.Unspecified,
    // İçeriğe göre boyutlanma
    wrapContentWidth: Boolean = false,
    minTextFieldWidth: Dp = 45.dp,
    maxTextFieldWidth: Dp = 120.dp
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
        maxValue = maxValue,
        minValue = minValue,
        imeAction = imeAction,
        keyboardActions = keyboardActions,
        showStepButtons = showStepButtons,
        stepValue = stepValue,
        stepButtonSize = stepButtonSize,
        stepButtonBackgroundColor = stepButtonBackgroundColor,
        stepButtonIconColor = stepButtonIconColor,
        wrapContentWidth = wrapContentWidth,
        minTextFieldWidth = minTextFieldWidth,
        maxTextFieldWidth = maxTextFieldWidth
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