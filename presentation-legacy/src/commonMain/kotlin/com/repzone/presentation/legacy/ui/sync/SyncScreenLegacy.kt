package com.repzone.presentation.legacy.ui.sync

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.platform.HandleBackPress
import com.repzone.core.ui.viewmodel.sync.SyncViewModel
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.common.onError
import com.repzone.domain.common.onSuccess
import com.repzone.sync.model.SyncJobStatus
import com.repzone.sync.util.getProgressPercentage
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.img_generic_logo_min
import repzonemobile.core.generated.resources.running
import repzonemobile.core.generated.resources.sync_complete
import repzonemobile.core.generated.resources.waiting
import repzonemobile.presentation_legacy.generated.resources.img_login_background


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SyncScreenLegacy(onSyncCompleted: () -> Unit) = ViewModelHost<SyncViewModel> { viewModel ->
    val state by viewModel.state.collectAsState()
    val allJobStatus = state.allJobsStatusList
    var statusStr: String? by remember { mutableStateOf("") }
    var allCompleted: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(allJobStatus) {
        if (allJobStatus.isNotEmpty()) {
            allCompleted = allJobStatus.all { (_, status) ->
                status is SyncJobStatus.Success || status is SyncJobStatus.Failed
            }
            if (allCompleted) {
                viewModel.onEvent(SyncViewModel.Event.Success).onSuccess {
                    onSyncCompleted()
                }.onError {

                }

            }
        }
    }

    HandleBackPress {

    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                painter = painterResource(Res.drawable.img_generic_logo_min),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .aspectRatio(1f),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(32.dp))

            CircularProgressIndicator(
                modifier = Modifier.size(64.dp),
                strokeWidth = 4.dp
            )


            Spacer(modifier = Modifier.height(24.dp))
            allJobStatus.forEach { (_, status) ->

                statusStr = when (status) {
                    is SyncJobStatus.Success -> {
                        status.resourceUi?.getMessage()
                    }
                    is SyncJobStatus.Progress -> {
                       "${status.getProgressPercentage()} ${status.resourceUi?.getMessage()}"
                    }
                    is SyncJobStatus.Failed -> {
                        status.error
                    }
                    is SyncJobStatus.Idle -> {
                        Res.string.waiting.fromResource()
                    }
                    is SyncJobStatus.Running -> {
                        Res.string.running.fromResource()
                    }
                }
            }

            if (allCompleted){
                statusStr = Res.string.sync_complete.fromResource()
            }

            Text(
                text = statusStr ?: "",
                style = TextStyle(
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    color = Color.White
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            val totalJobs = allJobStatus.size
            val completedJobs = allJobStatus.count { (_, status) ->
                status is SyncJobStatus.Success || status is SyncJobStatus.Failed
            }

            if (totalJobs > 0) {
                Text(
                    text = "$completedJobs / $totalJobs",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}