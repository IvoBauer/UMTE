@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.schemes

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.uhk.umte.data.db.entities.SchemeEntity
import org.koin.androidx.compose.getViewModel

@Composable
fun SchemeScreen(
    viewModel: SchemeVM = getViewModel(),
) {
    var selectedOptionIndex by remember { mutableStateOf(0) }
    //viewModel.checkSchemes()
    var schemes = viewModel.schemes.collectAsState(emptyList()).value
    var schemeNumber = 1;
    if (schemes.size > 0){
        selectedOptionIndex = schemes.indexOf(schemes.find{it.used})
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

    Column {

        var meow = mutableListOf("")

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 50.dp)
        ) {
            Text(
                text = "Choose a color scheme:",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        schemes.forEachIndexed { index, text ->
            if (!meow.contains(text.description)){
                meow.add(text.description)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedOptionIndex == index),
                        onClick = { selectedOptionIndex = index
                        print(index)}
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (selectedOptionIndex == index),
                    onClick = { selectedOptionIndex = index
                        viewModel.changeSchema(schemes[index])},
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colors.primary
                    )
                )
                Text(
                    text = text.description,
                    style = MaterialTheme.typography.body1.merge(),
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
        }
    }


}

fun getColor(schemeNumber: Int, index: Int):Color {
    var color = Color.Blue
    if (index == 0) {
        if (schemeNumber == 1) {
            color = Color.Red
        }
        if (schemeNumber == 2) {
            color = Color.Green
        }
        if (schemeNumber == 3) {
            color = Color.DarkGray
        }
        if (schemeNumber == 4) {
            color = Color.Blue
        }
    }

    if (index == 1) {
        if (schemeNumber == 1) {
            color = Color.Black
        }
        if (schemeNumber == 2) {
            color = Color.Black
        }
        if (schemeNumber == 3) {
            color = Color.Black
        }
        if (schemeNumber == 4) {
            color = Color.Black
        }
    }
    return color
}
