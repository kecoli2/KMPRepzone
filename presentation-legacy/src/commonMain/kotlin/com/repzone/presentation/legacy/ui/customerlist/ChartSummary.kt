package com.repzone.presentation.legacy.ui.customerlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.toMoney
import com.repzone.domain.model.RepresentSummary
import org.jetbrains.compose.resources.stringResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.summaryline1
import repzonemobile.core.generated.resources.summaryline2
import repzonemobile.core.generated.resources.summaryline3
import repzonemobile.core.generated.resources.summaryline4
import repzonemobile.core.generated.resources.summaryline5
import repzonemobile.core.generated.resources.summaryperformanceheader
import repzonemobile.core.generated.resources.visitsummarytitle
import kotlin.math.abs

data class SliderItem(
    val name: String,
)

@Composable
fun RepresentSummary(representSummary: RepresentSummary, themeManager: ThemeManager) {
    var selectedIndex by remember { mutableStateOf(0) }
    var dragOffset by remember { mutableStateOf(0f) }

    val sliders = listOf(
        SliderItem(Res.string.visitsummarytitle.fromResource()),
        SliderItem(Res.string.summaryperformanceheader.fromResource())
    )

    Column(modifier = Modifier .fillMaxSize()) {
        // Indicator
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "${sliders[selectedIndex].name} ${selectedIndex + 1} / ${sliders.size}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
        AnimatedContent(
            targetState = selectedIndex,
            transitionSpec = {
                if (targetState > initialState) {
                    // Sağa kaydırma
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    // Sola kaydırma
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            label = "slider_animation",
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                if (abs(dragOffset) > 100) {
                                    if (dragOffset > 0 && selectedIndex > 0) {
                                        selectedIndex--
                                    } else if (dragOffset < 0 && selectedIndex < sliders.size - 1) {
                                        selectedIndex++
                                    }
                                }
                                dragOffset = 0f
                            },
                            onHorizontalDrag = { _, dragAmount ->
                                dragOffset += dragAmount
                            }
                        )
                    },
            ) {
                when (index) {
                    0 -> GraphicLayout(sliders[index], representSummary, themeManager)
                    1 -> TableLayout(sliders[index])
                }
            }
        }
    }
}

@Composable
fun GraphicLayout(slider: SliderItem, representSummary: RepresentSummary, themeManager: ThemeManager) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        contentAlignment = Alignment.Center
    ){
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(8.dp)
                .height(180.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(150.dp)
            ) {
                val visited = representSummary.visitDoneTotal
                val total = representSummary.visitTotal
                val progress = if(visited == 0 && total == 0) 0 else visited / total

                // Pie Chart
                Canvas(modifier = Modifier.size(150.dp)) {
                    // Arka plan circle (kalan kısım)
                    drawArc(
                        color = Color.White.copy(alpha = 0.3f),
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = true
                    )

                    // Progress arc (yapılan kısım)
                    drawArc(
                        color = Color.White,
                        startAngle = -90f,
                        sweepAngle = 360f * progress,
                        useCenter = true
                    )

                    // İç circle (donut efekti için)
                    drawCircle(
                        color = themeManager.getCurrentColorScheme().colorPalet.primary60,
                        radius = size.minDimension / 3.5f
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = representSummary.visitDoneTotal.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "/ ${representSummary.visitTotal}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                }
            }
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SummaryItemCompact(stringResource(Res.string.summaryline1, representSummary.visitTotal))
                SummaryItemCompact(stringResource(Res.string.summaryline2, representSummary.visitDoneTotal))
                SummaryItemCompact(stringResource(Res.string.summaryline3, representSummary.orderCount))
                SummaryItemCompact(stringResource(Res.string.summaryline4, representSummary.orderValue.toMoney()))
                SummaryItemCompact(stringResource(Res.string.summaryline5, representSummary.formCount))
            }
        }
    }
}

@Composable
fun SummaryItemCompact(title: String) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun TableLayout(slider: SliderItem) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .padding(24.dp)
    ) {
        Text(
            text = slider.name,
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(24.dp))

        repeat(5) { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .background(Color.White.copy(alpha = 0.2f))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Satır ${row + 1}", color = Color.White)
                Text("Değer ${(row + 1) * 100}", color = Color.White)
            }
        }
    }
}

