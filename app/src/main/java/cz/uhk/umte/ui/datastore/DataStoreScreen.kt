package cz.uhk.umte.ui.datastore

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.first
import org.koin.androidx.compose.getViewModel

@Composable
fun DataStoreScreen(
    viewModel: DataStoreViewModel = getViewModel(),
) {

    val lastInputText = viewModel.textFlow.collectAsState(initial = "")
    var inputText by remember { mutableStateOf("") }
    var checkBox by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit, block = {
        checkBox = viewModel.isChecked()
        inputText = viewModel.textFlow.first().orEmpty()
    })

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Input text")
            }
        )

        Text(text = "Poslední uložený text:")
        Text(text = lastInputText.value.orEmpty(), color = Color.Red)

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Checkbox(
                checked = checkBox,
                onCheckedChange = { checkBox = it },
            )
            Text(
                text = "Checkbox"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.save(
                    text = inputText,
                    checked = checkBox,
                )
            },
        ) {
            Text(text = "Uložit")
        }
    }
}