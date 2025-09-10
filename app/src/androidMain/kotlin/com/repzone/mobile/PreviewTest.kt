package com.repzone.mobile

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import com.repzone.core.generated.resources.Res
import com.repzone.core.generated.resources.hello
import io.ktor.client.HttpClient
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.dsl.module

// --- Preview’e özel minimal modüller ---
private fun previewModules(ctx: android.content.Context) = module {
    // Eğer bazı sınıflar Context isterse düz Context ver, Application isteme
    single<android.content.Context> { ctx }

    // Ağ/DB yerine fake’ler:
    single<HttpClient> { HttpClient() } // No engine, no network
    // single<Repository> { FakeRepository() } vs.
}

@Composable
private fun Content() {
    // Normalde get()/koinInject() ile alıp kullanırsınız
    val client: HttpClient = koinInject()
    Text(text = stringResource(Res.string.hello))
}

/*@Preview(showBackground = true)
@Composable
fun AppAndroidPreview() {
    Text(text = stringResource(Res.string.hello))
    //Text(text = (Res::class.qualifiedName ?: "no Res"))
}*/
