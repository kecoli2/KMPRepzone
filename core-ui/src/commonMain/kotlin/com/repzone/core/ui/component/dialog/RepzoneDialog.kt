package com.repzone.core.ui.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.repzone.core.model.StringResource
import com.repzone.core.util.extensions.fromResource

@Composable
fun RepzoneDialog(
    isOpen: Boolean,
    title: String,
    message: String,
    showYesButton: Boolean = true,
    onYes: () -> Unit,
    showNoButton: Boolean = true,
    onNo: () -> Unit = {},
    onOther: () -> Unit = {},
    yesText: String = StringResource.YES.fromResource(),
    noText: String = StringResource.NO.fromResource(),
    otherText: String = StringResource.OTHER.fromResource(),
    showOther: Boolean = false,
    onDismissRequest: () -> Unit = onNo
) {
    if (!isOpen) return

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        confirmButton = {
            if(showYesButton){
                Button(
                    onClick = {
                        onYes()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(yesText)
                }
            }
        },
        dismissButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (showOther) {
                    OutlinedButton(
                        onClick = {
                            onOther()
                        }
                    ) {
                        Text(otherText)
                    }
                }
                if(showNoButton){
                    TextButton(
                        onClick = {
                            onNo()
                        }
                    ) {
                        Text(noText)
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp
    )
}
