@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.feeds

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cz.uhk.umte.data.db.entities.NoteEntity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.getViewModel

@Composable
fun FeedScreen(
    viewModel: FeedVM = getViewModel(),
) {
    val feeds = viewModel.feeds.collectAsState(emptyList())
    val feeds3 = feeds.value

// projití seznamu feeds a zpracování jednotlivých položek
    var meow = mutableListOf<NoteEntity>()
    feeds3.forEach { feed ->
        // zpracování jednotlivé položky feed
        meow.add(feed)
    }
    println("--------------------------------------------------------------------------------------------------------------------")
    println("--------------------------------------------------------------------------------------------------------------------")
    println(meow.size)
    println(meow.size)

    Column {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp),
        ) {
            items(
                //items = feeds.value,
                items = meow,
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
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row {
                            Text(
                                //text = note.uri,
                                text = note.uri,
                                style = MaterialTheme.typography.h6,
                                color = Color.Gray
                            )
                        }

                        Row (horizontalArrangement = Arrangement.SpaceBetween){
                            Checkbox(
                                checked = note.solved,
                                onCheckedChange = {
                                    viewModel.handleNoteCheck(note)
                                },
                            )
                            Text(
                                text = "Active",
                                modifier = Modifier.align(Alignment.CenterVertically),
                                style = MaterialTheme.typography.h6
                            )
                            Spacer(modifier = Modifier.width(width = 60.dp))

                            Button(onClick = {
                                viewModel.removeFeed(note)
                            }) {
                                Text("Delete Feed", style = MaterialTheme.typography.h6)
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Smazat feed", modifier = Modifier.align(Alignment.CenterVertically))
                            }
                        }
                        }
                }
            }
        }
        var feedName by remember { mutableStateOf("Reddit NEWS") }
        var feedUri by remember { mutableStateOf("https://servis.idnes.cz/rss.aspx?c=zpravodaj") }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = feedName,
                onValueChange = { feedName = it },
                label = {
                    Text(text = "Feed NAME")
                },
                modifier = Modifier.weight(1F),
            )
        }
        Row (horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {


            OutlinedTextField(
                value = feedUri,
                onValueChange = { feedUri = it },
                label = {
                    Text(text = "Feed URI")
                },
                modifier = Modifier.weight(1F),
            )
            Button(
                modifier = Modifier.height(IntrinsicSize.Max),
                enabled = feedUri.isBlank().not(),
                onClick = {
                    viewModel.addFeed(feedName,feedUri)
                    feedName = ""
                    feedUri = ""
                },
            ) {
                Text(text = "Add")
            }
        }
    }
}

fun readValue(string: List<NoteEntity>){
    print(string)
}