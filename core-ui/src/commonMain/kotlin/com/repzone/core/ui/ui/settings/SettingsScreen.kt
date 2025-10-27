package com.repzone.core.ui.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.SettingsBrightness
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.core.enums.ThemeMode
import com.repzone.core.ui.manager.theme.AppTheme
import com.repzone.core.ui.manager.theme.WindowWidthSizeClass
import com.repzone.core.ui.manager.theme.common.ColorSchemeVariant


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBackClick: () -> Unit = {}) {
    val themeManager = AppTheme.manager
    val currentLanguage by themeManager.currentLanguage.collectAsState()
    val themeMode by themeManager.themeMode.collectAsState()
    val responsiveState by themeManager.responsiveState.collectAsState()
    val colorSchemes = themeManager.getAvailableColorSchemes()
    val currentColorScheme = themeManager.getCurrentColorScheme()

    // Dialog states
    var showLanguageDialog by remember { mutableStateOf(false) }
    var showThemeModeDialog by remember { mutableStateOf(false) }
    var showColorSchemeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Ayarlar",
                            fontSize = AppTheme.dimens.textLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Geri",
                            modifier = Modifier.size(AppTheme.dimens.iconSizeMedium),
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = themeManager.getCurrentColorScheme().colorPalet.secondary20,
                    titleContentColor = themeManager.getCurrentColorScheme().colorPalet.white
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(AppTheme.dimens.paddingMedium),
            verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingLarge)
        ) {
            // Görünüm Bölümü
            item {
                SettingSection(
                    title = "Görünüm",
                    icon = Icons.Default.Palette
                ) {
                    // Tema Modu
                    SettingItem(
                        title = "Tema Modu",
                        subtitle = when (themeMode) {
                            ThemeMode.LIGHT -> "Açık tema"
                            ThemeMode.DARK -> "Koyu tema"
                            ThemeMode.SYSTEM -> "Sistem ayarını takip et"
                        },
                        icon = when (themeMode) {
                            ThemeMode.LIGHT -> Icons.Default.LightMode
                            ThemeMode.DARK -> Icons.Default.DarkMode
                            ThemeMode.SYSTEM -> Icons.Default.SettingsBrightness
                        },
                        onClick = { showThemeModeDialog = true }
                    )

                    HorizontalDivider(
                        Modifier.padding(
                            start = AppTheme.dimens.paddingExtraLarge + AppTheme.dimens.paddingMedium
                        ),
                        DividerDefaults.Thickness, DividerDefaults.color
                    )

                    // Renk Şeması
                    SettingItem(
                        title = "Renk Şeması",
                        subtitle = currentColorScheme.name,
                        icon = Icons.Default.ColorLens,
                        trailing = {
                            Box(
                                modifier = Modifier
                                    .size(AppTheme.dimens.iconSizeMedium)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = CircleShape
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = CircleShape
                                    )
                            )
                        },
                        onClick = { showColorSchemeDialog = true }
                    )
                }
            }

            // Dil ve Bölge Bölümü
            item {
                SettingSection(
                    title = "Dil ve Bölge",
                    icon = Icons.Default.Language
                ) {
                    SettingItem(
                        title = "Uygulama Dili",
                        subtitle = getLanguageName(currentLanguage),
                        icon = Icons.Default.Translate,
                        trailing = {
                            Text(
                                text = getLanguageFlag(currentLanguage),
                                fontSize = AppTheme.dimens.textLarge
                            )
                        },
                        onClick = { showLanguageDialog = true }
                    )
                }
            }

            // Hakkında Bölümü
            item {
                SettingSection(
                    title = "Hakkında",
                    icon = Icons.Default.Info
                ) {
                    InfoRow(
                        label = "Ekran Boyutu",
                        value = when (responsiveState.windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.Compact -> "Compact"
                            WindowWidthSizeClass.Medium -> "Medium"
                            WindowWidthSizeClass.Expanded -> "Expanded"
                        },
                        icon = Icons.Default.Smartphone
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = AppTheme.dimens.paddingExtraLarge + AppTheme.dimens.paddingMedium
                        ),
                        thickness = DividerDefaults.Thickness, color = DividerDefaults.color
                    )

                    InfoRow(
                        label = "Yönelim",
                        value = if (responsiveState.isLandscape) "Yatay" else "Dikey",
                        icon = if (responsiveState.isLandscape)
                            Icons.Default.ScreenRotation
                        else
                            Icons.Default.PhoneAndroid
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = AppTheme.dimens.paddingExtraLarge + AppTheme.dimens.paddingMedium
                        ),
                        thickness = DividerDefaults.Thickness, color = DividerDefaults.color
                    )

                    InfoRow(
                        label = "Font Boyutu",
                        value = "${responsiveState.dimensions.textMedium}",
                        icon = Icons.Default.TextFields
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(
                            start = AppTheme.dimens.paddingExtraLarge + AppTheme.dimens.paddingMedium
                        ),
                        thickness = DividerDefaults.Thickness, color = DividerDefaults.color
                    )

                    InfoRow(
                        label = "Versiyon",
                        value = "1.0.0",
                        icon = Icons.Default.Star
                    )
                }
            }

            // Alt boşluk
            item {
                Spacer(modifier = Modifier.height(AppTheme.dimens.paddingLarge))
            }
        }
    }

    // Dialogs
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = currentLanguage,
            onLanguageSelected = { themeManager.setLanguage(it) },
            onDismiss = { showLanguageDialog = false }
        )
    }

    if (showThemeModeDialog) {
        ThemeModeDialog(
            currentMode = themeMode,
            onModeSelected = { themeManager.setThemeMode(it) },
            onDismiss = { showThemeModeDialog = false }
        )
    }

    if (showColorSchemeDialog) {
        ColorSchemeDialog(
            colorSchemes = colorSchemes,
            currentScheme = currentColorScheme,
            onSchemeSelected = { themeManager.setColorScheme(it.id) },
            onDismiss = { showColorSchemeDialog = false }
        )
    }
}

@Composable
fun SettingSection(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall)
    ) {
        // Section Header
        Row(
            modifier = Modifier.padding(
                start = AppTheme.dimens.paddingSmall,
                bottom = AppTheme.dimens.paddingSmall
            ),
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.iconSizeSmall),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = title,
                fontSize = AppTheme.dimens.textMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Section Content Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(AppTheme.dimens.cornerRadius),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(vertical = AppTheme.dimens.paddingSmall)
            ) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    trailing: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) {
                    Modifier.clickable(onClick = onClick)
                } else Modifier
            )
            .padding(
                horizontal = AppTheme.dimens.paddingMedium,
                vertical = AppTheme.dimens.paddingMedium
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            // Icon Container
            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.iconSizeLarge)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(AppTheme.dimens.paddingSmall)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Column {
                Text(
                    text = title,
                    fontSize = AppTheme.dimens.textMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = AppTheme.dimens.textSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (trailing != null) {
            trailing()
        } else if (onClick != null) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.iconSizeMedium),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoRow(
    label: String,
    value: String,
    icon: ImageVector
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = AppTheme.dimens.paddingMedium,
                vertical = AppTheme.dimens.paddingMedium
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(AppTheme.dimens.iconSizeLarge)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(AppTheme.dimens.paddingSmall)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Text(
                text = label,
                fontSize = AppTheme.dimens.textMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = value,
            fontSize = AppTheme.dimens.textSmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// Dil Seçim Dialog'u
@Composable
fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    data class LanguageOption(
        val code: String,
        val name: String,
        val flag: String
    )

    val languages = listOf(
        LanguageOption("tr", "Türkçe", "🇹🇷"),
        LanguageOption("en", "English", "🇬🇧"),
        LanguageOption("de", "Deutsch", "🇩🇪"),
        LanguageOption("fr", "Français", "🇫🇷"),
        LanguageOption("es", "Español", "🇪🇸"),
        LanguageOption("ar", "العربية", "🇸🇦")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Language,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.iconSizeLarge),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                "Dil Seçin",
                fontSize = AppTheme.dimens.textLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall)
            ) {
                items(languages) { language ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLanguageSelected(language.code)
                                onDismiss()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (language.code == currentLanguage)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.paddingSmall)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppTheme.dimens.paddingMedium),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = language.flag,
                                    fontSize = AppTheme.dimens.textLarge
                                )
                                Text(
                                    text = language.name,
                                    fontSize = AppTheme.dimens.textMedium,
                                    fontWeight = if (language.code == currentLanguage)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            }

                            if (language.code == currentLanguage) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", fontSize = AppTheme.dimens.textMedium)
            }
        },
        shape = RoundedCornerShape(AppTheme.dimens.cornerRadius)
    )
}

// Tema Modu Dialog'u
@Composable
fun ThemeModeDialog(
    currentMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit,
    onDismiss: () -> Unit
) {
    data class ThemeModeOption(
        val mode: ThemeMode,
        val name: String,
        val icon: ImageVector
    )

    val modes = listOf(
        ThemeModeOption(ThemeMode.LIGHT, "Açık Tema", Icons.Default.LightMode),
        ThemeModeOption(ThemeMode.DARK, "Koyu Tema", Icons.Default.DarkMode),
        ThemeModeOption(ThemeMode.SYSTEM, "Sistem Ayarı", Icons.Default.SettingsBrightness)
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Palette,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.iconSizeLarge),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                "Tema Modu",
                fontSize = AppTheme.dimens.textLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall)
            ) {
                modes.forEach { option ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onModeSelected(option.mode)
                                onDismiss()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (option.mode == currentMode)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.paddingSmall)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppTheme.dimens.paddingMedium),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
                                )
                                Text(
                                    text = option.name,
                                    fontSize = AppTheme.dimens.textMedium,
                                    fontWeight = if (option.mode == currentMode)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            }

                            if (option.mode == currentMode) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", fontSize = AppTheme.dimens.textMedium)
            }
        },
        shape = RoundedCornerShape(AppTheme.dimens.cornerRadius)
    )
}

// Renk Şeması Dialog'u
@Composable
fun ColorSchemeDialog(
    colorSchemes: List<ColorSchemeVariant>,
    currentScheme: ColorSchemeVariant,
    onSchemeSelected: (ColorSchemeVariant) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.ColorLens,
                contentDescription = null,
                modifier = Modifier.size(AppTheme.dimens.iconSizeLarge),
                tint = MaterialTheme.colorScheme.primary
            )
        },
        title = {
            Text(
                "Renk Şeması",
                fontSize = AppTheme.dimens.textLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingSmall)
            ) {
                items(colorSchemes) { scheme ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSchemeSelected(scheme)
                                onDismiss()
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (scheme.id == currentScheme.id)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(AppTheme.dimens.paddingSmall)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(AppTheme.dimens.paddingMedium),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.paddingMedium),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Renk önizlemesi
                                Box(
                                    modifier = Modifier
                                        .size(AppTheme.dimens.iconSizeLarge)
                                        .background(
                                            // Scheme'in primary rengini göster
                                            color = Color(0xFFFF6B35), // Örnek renk
                                            shape = CircleShape
                                        )
                                        .border(
                                            width = 2.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = CircleShape
                                        )
                                )

                                Text(
                                    text = scheme.name,
                                    fontSize = AppTheme.dimens.textMedium,
                                    fontWeight = if (scheme.id == currentScheme.id)
                                        FontWeight.Bold
                                    else
                                        FontWeight.Normal
                                )
                            }

                            if (scheme.id == currentScheme.id) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(AppTheme.dimens.iconSizeMedium)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Kapat", fontSize = AppTheme.dimens.textMedium)
            }
        },
        shape = RoundedCornerShape(AppTheme.dimens.cornerRadius)
    )
}

// Helper fonksiyonlar
fun getLanguageName(code: String): String {
    return when (code) {
        "tr" -> "Türkçe"
        "en" -> "English"
        "de" -> "Deutsch"
        "fr" -> "Français"
        "es" -> "Español"
        "ar" -> "العربية"
        else -> code
    }
}

fun getLanguageFlag(code: String): String {
    return when (code) {
        "tr" -> "🇹🇷"
        "en" -> "🇬🇧"
        "de" -> "🇩🇪"
        "fr" -> "🇫🇷"
        "es" -> "🇪🇸"
        "ar" -> "🇸🇦"
        else -> "🌐"
    }
}