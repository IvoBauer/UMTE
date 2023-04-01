@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.feeds

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
    viewModel: FeedVM = getViewModel(),
) {

    val feeds = viewModel.feeds.collectAsState(emptyList())

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                items = feeds.value,
                key = { it.id },
            ) { note ->
                Card(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground,
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
                            Checkbox(
                                checked = note.solved,
                                onCheckedChange = {
                                    viewModel.handleNoteCheck(note)
                                },
                            )
                            Text(
                                text = "Active",
                                modifier = Modifier.align(Alignment.CenterVertically),
                            )
                            TextButton(onClick = {
                                viewModel.removeFeed(note)
                            }) {
                                Text("Delete Feed")
                            }
                        }
                        }
                }
            }
        }
        var inputText by remember { mutableStateOf("https://www.reddit.com/r/AskReddit/new/.rss") }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = {
                    Text(text = "Feed NAME")
                },
                modifier = Modifier.weight(1F),
            )
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                label = {
                    Text(text = "Feed URI")
                },
                modifier = Modifier.weight(1F),
            )
            Button(
                modifier = Modifier.height(IntrinsicSize.Max),
                enabled = inputText.isBlank().not(),
                onClick = {
                    viewModel.addFeed(inputText)
                    inputText = ""
                },
            ) {
                Text(text = "Add")
            }
        }
    }
}