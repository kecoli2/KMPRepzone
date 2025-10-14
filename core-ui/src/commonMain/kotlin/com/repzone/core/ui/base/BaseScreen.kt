package com.repzone.core.ui.base

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.repzone.core.model.UiFrame
import androidx.compose.material3.*
import androidx.compose.ui.Modifier

@Composable
fun BaseScreen(
    frame: UiFrame,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    when {
        frame.isLoading -> LinearProgressIndicator(modifier)
        frame.getError() != null -> Text(
            frame.getError()!!,
            color = MaterialTheme.colorScheme.error,
            modifier = modifier
        )
        else -> content()
    }
}