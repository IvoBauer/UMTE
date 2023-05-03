package cz.uhk.umte.ui.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import cz.uhk.umte.R
import cz.uhk.umte.ui.*
import cz.uhk.umte.ui.schemes.SchemeVM
import cz.uhk.umte.ui.schemes.getColor
import org.koin.androidx.compose.getViewModel

@Composable
fun HomeScreen(
    parentController: NavHostController,
    viewModel2: SchemeVM = getViewModel()
) {

    val context = LocalContext.current
    viewModel2.checkSchemes()
    var schemes = viewModel2.schemes.collectAsState(emptyList()).value
    var schemeNumber = 1;
    if (schemes.size > 0){
        schemeNumber = (schemes.find{it.used})?.schemeNumber ?: 1
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(
                colors = listOf(getColor(schemeNumber,0), getColor(schemeNumber,1)),
                start = Offset(0f, 0f),
                end = Offset(LocalConfiguration.current.screenWidthDp.dp.value, LocalConfiguration.current.screenHeightDp.dp.value)
            ))
    )


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "RSS Reader",
            style = MaterialTheme.typography.h2,
            color = Color.Gray
        )
        Button(
            onClick = {
                parentController.navigateRoomScreen()
            }
        ) {
            Text(text = "Subscribed feeds")
        }

        Button(
            onClick = {
                parentController.navigateRoomScreen2()
            }
        ) {
            Text(text = "Read articles")
        }

        Button(
            onClick = {
                parentController.navigateRoomScreen3()
            }
        ) {
            Text(text = "Scheme settings")
        }
    }
}