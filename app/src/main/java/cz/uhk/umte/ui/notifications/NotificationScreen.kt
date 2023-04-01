package cz.uhk.umte.ui.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import cz.uhk.umte.R

@Composable
fun NotificationScreen() {

    val context = LocalContext.current

    val permissionGranted = remember {
        mutableStateOf(context.isNotificationGranted())
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                Toast.makeText(context, "Povoleno", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Zamítnuto", Toast.LENGTH_SHORT).show()
            }
            permissionGranted.value = granted
        },
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Button(onClick = { checkAndRequestPermission(context, launcher) }) {
            Text(text = "Získat oprávnění")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Oprávnění získáno: ${permissionGranted.value}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { sendPushNotification(context) }) {
            Text(text = "Odeslat notifikaci")
        }
    }
}

fun sendPushNotification(context: Context) {
    val builder = NotificationCompat.Builder(context, "General")
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("Hello world!")
        .setContentText("Toto je tělo notifikace")
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = "General chanell"
        val description = "Kanál pro zobrazování obecných informaci"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("General", name, importance)
        channel.description = description
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    NotificationManagerCompat.from(context)
        .notify(1, builder.build())
}

fun checkAndRequestPermission(
    context: Context,
    launcher: ManagedActivityResultLauncher<String, Boolean>,
) {
    val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
    } else {
        PackageManager.PERMISSION_GRANTED
    }
    if (granted == PackageManager.PERMISSION_GRANTED) {
        // Povolení je uděleno
    } else {
        launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}

private fun Context.isNotificationGranted() =
    ActivityCompat.checkSelfPermission(
        this,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED