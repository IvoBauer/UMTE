@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.room

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun RoomScreen(
    viewModel: RoomVM = getViewModel(),
) {

    val notes = viewModel.notes.collectAsState(emptyList())

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                items = notes.value,
                key = { it.id },
            ) { note ->
                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground,
                    modifier = Modifier.animateItemPlacement(),
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.End,
                    ) {
                        Text(
                            text = note.text,
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row {
                            TextButton(onClick = {
                                viewModel.handleNotePriority(note, note.priority + 1)
                            }) {
                                Text("+")
                            }

                            TextButton(onClick = {
                                viewModel.handleNotePriority(note, note.priority - 1)
                            }) {
                                Text("-")
                            }
                        }
                        Checkbox(
                            checked = note.solved,
                            onCheckedChange = {
                                viewModel.handleNoteCheck(note)
                            },
                        )
                    }
                }
            }
        }
        var inputText by remember { mutableStateOf("") }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = {
                    Text(text = "Note")
                },
                modifier = Modifier.weight(1F),
            )
            Button(
                modifier = Modifier.height(IntrinsicSize.Max),
                enabled = inputText.isBlank().not(),
                onClick = {
                    viewModel.addNote(inputText)
                    inputText = ""
                },
            ) {
                Text(text = "Add")
            }
        }
    }
}