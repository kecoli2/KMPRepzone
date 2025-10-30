package com.repzone.presentation.legacy.ui.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.ui.rememberPermissionManager
import com.repzone.presentation.legacy.navigation.LegacyScreen
import com.repzone.presentation.legacy.navigation.LocalNavController
import com.repzone.core.ui.viewmodel.splash.SplashScreenViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import repzonemobile.presentation_legacy.generated.resources.img_generic_logo_min
import repzonemobile.presentation_legacy.generated.resources.img_login_background

@Composable
fun SplashScreenLegacy(onNavigateToLogin: () -> Unit, onNavigateToMain: () -> Unit) = ViewModelHost<SplashScreenViewModel>{ viewModel ->
    val state by viewModel.state.collectAsState()
    val navController = LocalNavController.current
    val pm = rememberPermissionManager()
    var showTempDeniedDialog by remember { mutableStateOf(false) }
    var showPermDeniedDialog by remember { mutableStateOf(false) }

    LaunchedEffect("request-permissions-on-splash") {
        // ViewModel tarafındaki kuyruğu tetikleyecek izin kontrolü:
        viewModel.checkPermissionsAndProceed(pm)
    }

    LaunchedEffect(Unit){
        viewModel.events.collect{ event ->
            when (event){
                is SplashScreenViewModel.Event.ControllSucces -> {
                    onNavigateToMain()
                }
                is SplashScreenViewModel.Event.NavigateToLogin -> {
                    onNavigateToLogin()
                }

                is SplashScreenViewModel.Event.PermissionDeniedPermanent -> {
                    showPermDeniedDialog  = true
                }
                is SplashScreenViewModel.Event.PermissionDeniedTemporary -> {
                    showTempDeniedDialog = true
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(repzonemobile.presentation_legacy.generated.resources.Res.drawable.img_login_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(repzonemobile.presentation_legacy.generated.resources.Res.drawable.img_generic_logo_min),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .aspectRatio(1f)
                    .padding(24.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.weight(1f))
            DotsLoadingIndicator()
            Spacer(modifier = Modifier.weight(1f))
        }

        // GEÇİCİ RED (Tekrar Dene)
        if (showTempDeniedDialog) {
            AlertDialog(
                onDismissRequest = { showTempDeniedDialog = false },
                title = { Text("İzin Gerekli") },
                text  = { Text("Bazı izinler verilmedi. Devam edebilmek için tekrar denemek ister misiniz?") },
                confirmButton = {
                    TextButton(onClick = {
                        showTempDeniedDialog = false
                        // Tekrar sırayla dene:
                        // pm zaten burada (rememberPermissionManager)
                        // VM fonksiyonunu tekrar çağırıyoruz:
                        viewModel.scope.launch { viewModel.checkPermissionsAndProceed(pm) }
                    }) { Text("Tekrar Dene") }
                },
                dismissButton = {
                    TextButton(onClick = { showTempDeniedDialog = false }) { Text("Vazgeç") }
                }
            )
        }

        // KALICI RED (Ayarlar’a Git)
        if (showPermDeniedDialog) {
            AlertDialog(
                onDismissRequest = { showPermDeniedDialog = false },
                title = { Text("İzinler Engellendi") },
                text  = { Text("İzinler kalıcı olarak reddedilmiş görünüyor. Lütfen Ayarlar’dan izinleri açın.") },
                confirmButton = {
                    TextButton(onClick = {
                        showPermDeniedDialog = false
                        pm.openAppSettings() // PermissionManager içine platform-spesifik ayarlar açma
                    }) { Text("Ayarlar’a Git") }
                },
                dismissButton = {
                    TextButton(onClick = { showPermDeniedDialog = false }) { Text("Kapat") }
                }
            )
        }
    }
}

@Composable
fun AnimatedLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()

    // Dönen animasyon
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    // Pulse animasyonu
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Alpha animasyonu
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(60.dp)
            .graphicsLayer {
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Dış halka
            drawCircle(
                color = Color.White,
                radius = canvasWidth / 2,
                style = Stroke(width = 4.dp.toPx())
            )

            // İç nokta
            drawCircle(
                color = Color.White,
                radius = canvasWidth / 6,
                center = Offset(canvasWidth / 2, canvasHeight / 4)
            )
        }
    }
}

@Composable
fun DotsLoadingIndicator() {
    val dotCount = 3
    val infiniteTransition = rememberInfiniteTransition()

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(dotCount) { index ->
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.5f,
                targetValue = 1.5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 600,
                        delayMillis = index * 200,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .size(12.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .background(Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun MaterialLoadingIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = Color.White,
        strokeWidth = 4.dp
    )
}

@Composable
fun WaveLoadingIndicator() {
    val infiniteTransition = rememberInfiniteTransition()

    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(5) { index ->
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -20f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 500,
                        delayMillis = index * 100,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                )
            )

            Box(
                modifier = Modifier
                    .size(8.dp, 30.dp)
                    .offset(y = offsetY.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Composable
fun BouncingBallIndicator() {
    val infiniteTransition = rememberInfiniteTransition()

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -40f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    val squash by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500,
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .size(40.dp)
            .offset(y = offsetY.dp)
            .graphicsLayer {
                scaleX = squash
                scaleY = 2f - squash
            }
            .background(Color.White, CircleShape)
    )
}