package com.repzone.presentation.legacy.ui.customerlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListScreenUiState
import org.jetbrains.compose.resources.stringResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.loading


/**
 * Ana loading state handler - tüm state'leri yönetir
 */
@Composable
fun CustomerListLoadingHandler(
    customerListState: CustomerListScreenUiState.CustomerListState,
    themeManager: ThemeManager,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (customerListState) {
            is CustomerListScreenUiState.CustomerListState.Loading -> {
                LoadingScreen(themeManager)
            }
            is CustomerListScreenUiState.CustomerListState.Empty -> {
                EmptyStateScreen(themeManager)
            }
            is CustomerListScreenUiState.CustomerListState.Error -> {
                ErrorStateScreen(
                    errorMessage = customerListState.message,
                    themeManager = themeManager,
                    onRetry = onRetry
                )
            }
            is CustomerListScreenUiState.CustomerListState.Success -> {
                content()
            }

            else -> {}
        }
    }
}

/**
 * Modern loading ekranı - animasyonlu
 */
@Composable
fun LoadingScreen(themeManager: ThemeManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        PulsingCircularProgress(themeManager)
        Spacer(modifier = Modifier.height(24.dp))
        LoadingText()
    }
}

/**
 * Pulse efektli circular progress indicator
 */
@Composable
private fun PulsingCircularProgress(themeManager: ThemeManager) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.7f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size((80 * scale).dp)
                .alpha(alpha)
                .clip(CircleShape)
                .background(themeManager.getCurrentColorScheme().colorPalet.primary50.copy(alpha = 0.2f))
        )

        CircularProgressIndicator(
            modifier = Modifier.size(60.dp),
            color = themeManager.getCurrentColorScheme().colorPalet.primary50,
            strokeWidth = 4.dp
        )
    }
}

/**
 * Animasyonlu loading text
 */
@Composable
private fun LoadingText() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading_text")

    val dots by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = 3,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "dots"
    )

    Text(
        text = "${stringResource(Res.string.loading)}${".".repeat(dots + 1)}",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        fontWeight = FontWeight.Medium
    )
}

/**
 * Empty state ekranı - müşteri bulunamadı
 */
@Composable
fun EmptyStateScreen(themeManager: ThemeManager) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // İkon
        Icon(
            imageVector = Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Başlık
        Text(
            text = "Müşteri Bulunamadı",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Açıklama
        Text(
            text = "Bu tarih aralığında veya filtrelerinize uygun\nmüşteri bulunmamaktadır.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Error state ekranı - retry butonu ile
 */
@Composable
fun ErrorStateScreen(
    errorMessage: String,
    themeManager: ThemeManager,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Error icon
        Icon(
            imageVector = Icons.Default.ErrorOutline,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Başlık
        Text(
            text = "Bir Hata Oluştu",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Error mesajı
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Retry butonu
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = themeManager.getCurrentColorScheme().colorPalet.primary50
            ),
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
        ) {
            Text(
                text = "Tekrar Dene",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


/**
 * Overlay loading - sync sırasında kullanmak için
 */
@Composable
fun SyncOverlayLoading(
    isVisible: Boolean,
    message: String = "Senkronizasyon yapılıyor...",
    themeManager: ThemeManager
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(32.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = themeManager.getCurrentColorScheme().colorPalet.primary50,
                        strokeWidth = 4.dp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

/**
 * Mini loading indicator - pull to refresh için
 */
@Composable
fun MiniLoadingIndicator(
    isLoading: Boolean,
    themeManager: ThemeManager,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isLoading,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = themeManager.getCurrentColorScheme().colorPalet.primary50,
                strokeWidth = 2.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Güncelleniyor...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}
