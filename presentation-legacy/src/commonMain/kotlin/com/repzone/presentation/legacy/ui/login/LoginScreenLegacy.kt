package com.repzone.presentation.legacy.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import com.repzone.presentation.legacy.viewmodel.login.LoginScreenViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.hidden
import repzonemobile.core.generated.resources.loginenterpassword
import repzonemobile.core.generated.resources.loginenterusername
import repzonemobile.core.generated.resources.loginloginbutton
import repzonemobile.core.generated.resources.password
import repzonemobile.core.generated.resources.show
import repzonemobile.core.generated.resources.username
import repzonemobile.presentation_legacy.generated.resources.img_login_background

@Composable
fun LoginScreenLegacy(onLoginSuccess: () -> Unit) = ViewModelHost<LoginScreenViewModel>() { viewModel ->
    val state by viewModel.state.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val themeManager: ThemeManager = koinInject()

    // Success handling
    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            onLoginSuccess()
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
                .matchParentSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            OutlinedTextField(
                value = state.username,
                onValueChange = viewModel::updateUsername,
                label = { Text(Res.string.username.fromResource(), color = Color.White) },
                placeholder = { Text(Res.string.loginenterusername.fromResource(), color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = state.uiFrame.isInteractionEnabled,
                shape = MaterialTheme.shapes.extraLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    disabledContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ))

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::updatePassword,
                label = { Text(Res.string.password.fromResource(), color = Color.White) },
                placeholder = { Text(Res.string.loginenterpassword.fromResource(), color = Color.White.copy(alpha = 0.6f)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = MaterialTheme.shapes.extraLarge,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    disabledContainerColor = Color.White.copy(alpha = 0.1f),
                    focusedTextColor = themeManager.getCurrentColorScheme().colorPalet.white,
                    unfocusedTextColor = themeManager.getCurrentColorScheme().colorPalet.white
                ),
                enabled = state.uiFrame.isInteractionEnabled,
                visualTransformation = if (isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    TextButton(
                        onClick = { isPasswordVisible = !isPasswordVisible },
                        enabled = state.uiFrame.isInteractionEnabled
                    ) {
                        Text(
                            text = if (isPasswordVisible) Res.string.hidden.fromResource() else Res.string.show.fromResource(),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        viewModel.login(state.username, state.password)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = state.canSubmit,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = MaterialTheme.colorScheme.primary,
                )
            ) {
                if (state.uiFrame.isLoading) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    }
                    Text(modifier = Modifier.padding(start = 2.dp), text = (state.loadingMessage as StringResource).fromResource())
                } else {
                    Text(text = Res.string.loginloginbutton.fromResource())
                }
            }

            // Error display
            state.uiFrame.getError()?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    maxLines = 5,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }


}