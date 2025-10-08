package com.repzone.presentationlegacy.ui.login
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.repzone.presentationlegacy.base.ViewModelHost
import com.repzone.presentationlegacy.viewmodel.login.LoginScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreen() = ViewModelHost<LoginScreenViewModel>() { viewModel ->
    val state by viewModel.state.collectAsState()
    var isPasswordVisible by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Success handling
    LaunchedEffect(state.isLoginSuccessful) {
        if (state.isLoginSuccessful) {
            // Navigate to main screen
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        OutlinedTextField(
            value = state.username, // ✅ ViewModel'den geliyor
            onValueChange = viewModel::updateUsername, // ✅ ViewModel'i güncelliyor
            label = { Text("Kullanıcı Adı") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = state.uiFrame.isInteractionEnabled
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.password, // ✅ ViewModel'den geliyor
            onValueChange = viewModel::updatePassword, // ✅ ViewModel'i güncelliyor
            label = { Text("Şifre") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
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
                        text = if (isPasswordVisible) "Gizle" else "Göster",
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
            enabled = state.canSubmit
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
                    Text(state.loadingMessage)
                }
            } else {
                Text("Giriş Yap")
            }
        }

        // Error display
        state.uiFrame.error?.let { error ->
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}