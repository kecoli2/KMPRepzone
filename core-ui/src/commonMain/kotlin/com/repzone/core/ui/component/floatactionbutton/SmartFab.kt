package com.repzone.core.ui.component.floatactionbutton

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.component.floatactionbutton.model.FabAction
import com.repzone.core.ui.component.floatactionbutton.model.FabMenuItem
import com.repzone.core.util.extensions.fromResource

@Composable
fun SmartFabScaffold(
    fabAction: FabAction?,
    onFabClick: () -> Unit = {},
    onMenuItemClick: (FabMenuItem) -> Unit = {},
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = topBar,
            bottomBar = bottomBar,
            snackbarHost = snackbarHost,
            floatingActionButton = {
                if (fabAction != null) {
                    when (fabAction) {
                        is FabAction.Single -> {
                            SingleFab(
                                fabAction = fabAction,
                                onClick = onFabClick
                            )
                        }
                        is FabAction.Multiple -> {
                            FloatingActionButton(
                                onClick = { expanded = !expanded }
                            ) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.Close else fabAction.icon,
                                    contentDescription = fabAction.contentDescription
                                )
                            }
                        }
                    }
                }
            },
            containerColor = containerColor,
            contentColor = contentColor
        ) { padding ->
            content(padding)
        }

        if (fabAction is FabAction.Multiple && expanded) {
            FabMenuOverlay(
                items = fabAction.items,
                onDismiss = { expanded = false },
                onItemClick = { item ->
                    onMenuItemClick(item)
                    expanded = false
                }
            )
        }
    }
}

/**
 * Single FAB with optional badge support
 */
@Composable
private fun SingleFab(
    fabAction: FabAction.Single,
    onClick: () -> Unit
) {
    val badgeCount = fabAction.badgeCount

    if (badgeCount != null && badgeCount > 0) {
        // FAB with Badge
        BadgedBox(
            badge = {
                Badge(
                    containerColor = fabAction.badgeColor ?: MaterialTheme.colorScheme.error,
                    contentColor = fabAction.badgeContentColor ?: MaterialTheme.colorScheme.onError
                ) {
                    Text(
                        text = if (badgeCount > 99) "99+" else badgeCount.toString()
                    )
                }
            }
        ) {
            FloatingActionButton(onClick = onClick) {
                Icon(
                    imageVector = fabAction.icon,
                    contentDescription = fabAction.contentDescription
                )
            }
        }
    } else {
        // FAB without Badge
        FloatingActionButton(onClick = onClick) {
            Icon(
                imageVector = fabAction.icon,
                contentDescription = fabAction.contentDescription
            )
        }
    }
}

@Composable
private fun FabMenuOverlay(
    items: List<FabMenuItem>,
    onDismiss: () -> Unit,
    onItemClick: (FabMenuItem) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    onDismiss()
                }
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 88.dp, end = 16.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEachIndexed { index, item ->
                key(item.label) {
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(
                            animationSpec = tween(
                                durationMillis = 150,
                                delayMillis = index * 50
                            )
                        ) + slideInVertically(
                            initialOffsetY = { 100 },
                            animationSpec = tween(
                                durationMillis = 200,
                                delayMillis = index * 50
                            )
                        )
                    ) {
                        FabMenuItemRow(
                            item = item,
                            onItemClick = { onItemClick(item) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FabMenuItemRow(
    item: FabMenuItem,
    onItemClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Label
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 2.dp,
            tonalElevation = 2.dp
        ) {
            Text(
                text = item.label.fromResource(),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // Small FAB
        SmallFloatingActionButton(
            onClick = onItemClick
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.label.fromResource()
            )
        }
    }
}