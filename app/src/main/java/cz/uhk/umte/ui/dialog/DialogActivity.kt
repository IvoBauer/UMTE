package cz.uhk.umte.ui.dialog

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.uhk.umte.ui.theme.UMTETheme
import kotlinx.coroutines.flow.MutableStateFlow

class DialogActivity : ComponentActivity(), SensorEventListener {

    private val sensorManager by lazy {
        getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val accelerometer by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) as Sensor
    }

    private val sensorValues = MutableStateFlow(SensorValues())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val sensorState = sensorValues.collectAsState()

            UMTETheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.background),
                ) {
                    AlertDialogScreen()
                    Text(text = "X: ${sensorState.value.accX}")
                    Text(text = "Y: ${sensorState.value.accY}")
                    Text(text = "Z: ${sensorState.value.accZ}")
                }
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (event?.sensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                sensorValues.tryEmit(
                    sensorValues.value.copy(
                        accX = event.values[0],
                        accY = event.values[1],
                        accZ = event.values[2],
                    )
                )
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        // ignored
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(
            this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL,
        )
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    data class SensorValues(
        val accX: Float = 0F,
        val accY: Float = 0F,
        val accZ: Float = 0F,
    )
}

@Preview
@Composable
fun AlertDialogScreen() {
    UMTETheme {
        val dialogShown = remember {
            mutableStateOf(false)
        }
        Button(
            onClick = { dialogShown.switch() },
        ) {
            Text(text = "${if (dialogShown.value) "Hide" else "Show"} dialog")
        }

        if (dialogShown.value) {
            AlertDialog(
                onDismissRequest = { dialogShown.switch() },
                buttons = {
                    TextButton(onClick = { dialogShown.switch() }) {
                        Text("Ok")
                    }
                },
                title = { Text("Dialog title") },
                text = { Text("Toto je tělo dialogu") },
            )
        }
    }
}

fun MutableState<Boolean>.switch() {
    value = value.not()
}