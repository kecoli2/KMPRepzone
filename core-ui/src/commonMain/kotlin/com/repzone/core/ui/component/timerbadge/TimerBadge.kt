@file:OptIn(ExperimentalTime::class)

package com.repzone.core.ui.component.timerbadge

import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontWeight
import com.repzone.core.util.TimeUtils.toTimerFormat
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import kotlinx.coroutines.delay
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun TimerBadge(startTime: Instant) {
    var elapsedTime by remember { mutableStateOf(now().toInstant() - startTime) }

    LaunchedEffect(startTime) {
        while (true) {
            delay(1000)
            elapsedTime = now().toInstant() - startTime
        }
    }

    Badge(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = elapsedTime.toTimerFormat(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}