@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.feeds

import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.dialog.switch
import cz.uhk.umte.ui.schemes.SchemeVM
import cz.uhk.umte.ui.schemes.getColor
import cz.uhk.umte.ui.theme.UMTETheme
import org.koin.androidx.compose.getViewModel


@Composable
fun FeedScreen(
    viewModel: FeedVM = getViewModel(),
    viewModel2: SchemeVM = getViewModel(),
) {
    val feeds = viewModel.feeds.collectAsState(emptyList()).value
    //viewModel2.checkSchemes()
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
                items = feeds,
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
        var feedName by remember { mutableStateOf("CBS NEWS") }
        var feedUri by remember { mutableStateOf("https://www.cbsnews.com/latest/rss/us") }
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
    //AlertDialogScreen()
}

fun readValue(string: List<NoteEntity>){
    print(string)
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
            color = Color.Cyan
        }
    }

    if (index == 1) {
        if (schemeNumber == 1) {
            color = Color.Yellow
        }
        if (schemeNumber == 2) {
            color = Color.Black
        }
        if (schemeNumber == 3) {
            color = Color.LightGray
        }
        if (schemeNumber == 4) {
            color = Color.Magenta
        }
    }
    return color
}