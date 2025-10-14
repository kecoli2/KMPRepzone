package com.repzone.presentation.legacy.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.presentation.legacy.viewmodel.splash.SplashScreenViewModel
import org.jetbrains.compose.resources.painterResource
import repzonemobile.presentation_legacy.generated.resources.img_login_background

@Composable
fun SplashScreenLegacy() = ViewModelHost<SplashScreenViewModel>(){ viewModel ->
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(repzonemobile.presentation_legacy.generated.resources.Res.drawable.img_login_background),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
        }



    }

}