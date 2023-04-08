@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.articles

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
import com.prof.rssparser.Parser
import cz.uhk.umte.data.db.entities.ArticleEntity
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.feeds.FeedVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.nio.charset.Charset

@Composable
fun ArticlesScreen(
    viewModel: FeedVM = getViewModel(),
) {

    // vytvoření instance CoroutineScope
    val myScope = CoroutineScope(Dispatchers.Default)

    val feeds = viewModel.feeds.collectAsState(emptyList
        ())

    var meow = mutableListOf<NoteEntity>()
    val feeds3 = feeds.value
    feeds3.forEach { feed ->
        meow.add(feed)
        myScope.launch {

            getFeeds(feed)
            val article = ArticleEntity("asdad")
        }
    }
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
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row {
                            Text(
                                //text = note.uri,
                                text = "https://servis.idnes.cz/rss.aspx?c=zpravodaj",
                                style = MaterialTheme.typography.h6,
                                color = Color.Gray
                            )
                        }

                        Row (horizontalArrangement = Arrangement.SpaceBetween){
                            Spacer(modifier = Modifier.width(width = 60.dp))

                            Button(onClick = {
                                //viewModel.removeFeed(note)
                            }) {
                                Text("Read article", style = MaterialTheme.typography.h6)
                                //Icon(imageVector = Icons.Default.Delete, contentDescription = "Read article", modifier = Modifier.align(Alignment.CenterVertically))
                            }
                        }
                        }
                }
            }
        }
    }
}

private suspend fun getFeeds(feed: NoteEntity){
    val parser = Parser.Builder()
        .charset(Charset.forName("ISO-8859-7"))
        .build()

    //url of RSS feed

    val url = "https://www.androidauthority.com/feed"

    try {

        val channel = parser.getChannel(url)
        val articlesMEOW = channel.articles;
        print(articlesMEOW.size)
        print("---------------------------------------------")
        // Do something with your data

    } catch (e: Exception) {
        e.printStackTrace()
        // Handle the exception
    }
}