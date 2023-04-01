package cz.uhk.umte.ui.intents

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat

@Composable
fun IntentsScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            launchEmailApp(context)
        }) {
            Text("E-mail")
        }
        Button(onClick = {
            launchNavigationApp(context)
        }) {
            Text("Navigace")
        }
    }
}

private fun launchEmailApp(context: Context) {
    val email = "filip.obornik@uhk.cz"
    val subject = "Předmět e-mailu UMTE"
    val body = "Lorem ipsum..."

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/html"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    ContextCompat.startActivity(
        context,
        Intent.createChooser(intent, "Vyber aplikaci na e-mail"),
        null,
    )
}

private fun launchNavigationApp(context: Context) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:?q=50.20417,15.89265"))
    ContextCompat.startActivity(
        context,
        intent,
        null,
    )
}