@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.schemes

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.uhk.umte.data.db.entities.SchemeEntity
import org.koin.androidx.compose.getViewModel

@Composable
fun SchemeScreen(
    viewModel: SchemeVM = getViewModel(),
) {
    val radioOptions = listOf("Option 1", "Option 2", "Option 3")
    var selectedOptionIndex by remember { mutableStateOf(0) }
    viewModel.checkSchemes()
    var schemes = viewModel.schemes.collectAsState(emptyList()).value
    if (schemes.size > 0){
        selectedOptionIndex = schemes.indexOf(schemes.find{it.used})
    }

    Column {
        schemes.forEachIndexed { index, text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedOptionIndex == index),
                        onClick = { selectedOptionIndex = index
                        print(index)}
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
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
