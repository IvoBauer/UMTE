@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.articles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.prof.rssparser.Parser
import cz.uhk.umte.data.db.entities.ArticleEntity
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.feeds.FeedVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.getViewModel
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ArticlesScreen(
    viewModel: FeedVM = getViewModel(),
) {

    // vytvoření instance CoroutineScope
    val myScope = CoroutineScope(Dispatchers.Default)

    val feeds = viewModel.feeds.collectAsState(emptyList
        ())


    var meow = mutableListOf<NoteEntity>()
    var articles by remember { mutableStateOf(emptyList<ArticleEntity>()) }
    val feeds3 = feeds.value
    feeds3.forEach { feed ->
        meow.add(feed)
        myScope.launch {
            val newArticles = getFeeds(feed)
            newArticles.forEach{
                    //newArticle -> articles.add(newArticle)
            }

        }

        LaunchedEffect(Unit) {
            val newArticles =  getFeeds(feed) // získání seznamu článků z RSS
            withContext(Dispatchers.Main) {
                articles = articles + newArticles // aktualizace stavové proměnné s novými články
            }
        }
    }


    articles.forEach{
     item -> println(item.title)
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
                items = articles,
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
                            text = note.title ?: "Title not found. :/",
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier.fillMaxWidth(),
                        )
                        Row {
                            Text(
                                //text = note.uri,
                                text = note.summary ?: "Summary not found. :/",
                                style = MaterialTheme.typography.h6,
                                color = Color.Gray
                            )
                        }
                        Row {
                            Text(
                                text = getTime(note.pubDate),
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

private suspend fun getFeeds(feed: NoteEntity): List<ArticleEntity>{
    val articles = mutableListOf<ArticleEntity>()
    val parser = Parser.Builder()
        .charset(Charset.forName("ISO-8859-7"))
        .build()

    //url of RSS feed

    val url = "https://www.androidauthority.com/feed"

    try {

        val channel = parser.getChannel(url)
        //val articlesMEOW = channel.articles;
        channel.articles.forEach { item ->
            val dateString = item.pubDate
            val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            val date = format.parse(dateString)
        articles.add(
            ArticleEntity(item.title, item.description, date)
        )
        }
        print("---------------------------------------------")
        // Do something with your data

    } catch (e: Exception) {
        e.printStackTrace()
        // Handle the exception
    }
    return articles
}

private fun getTime(date: Date):String{
    val calendar = Calendar.getInstance()
    calendar.time = date

    val hours = calendar.get(Calendar.HOUR_OF_DAY).toString()
    val minutes = calendar.get(Calendar.MINUTE).toString()
    val days = calendar.get(Calendar.DAY_OF_MONTH).toString()
    val month = calendar.get(Calendar.MONTH).toString()
    val year = calendar.get(Calendar.YEAR).toString()

    val stringDate = hours + ":" + minutes + " " + days + ". " + month + ". " + year
    return stringDate
}