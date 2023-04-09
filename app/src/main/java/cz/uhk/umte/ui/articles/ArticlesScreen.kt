@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.articles

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ArticlesScreen(
    viewModel: FeedVM = getViewModel(),
) {
    Row {
        Text(
            text = "VERZE 1.02",
            style = MaterialTheme.typography.h6,
            color = Color.Gray
        )
    }

    val feeds = viewModel.feeds.collectAsState(emptyList())
    var isLoading by remember { mutableStateOf(true) }
    var articles by remember { mutableStateOf(emptyList<ArticleEntity>()) }
    val feeds3 = feeds.value

    feeds3.forEach { feed ->

        if (feed.solved){

        LaunchedEffect(Unit) {
            val newArticles =  getFeeds(feed) // získání seznamu článků z RSS
            withContext(Dispatchers.Main) {
                articles = articles + newArticles // aktualizace stavové proměnné s novými články
            }
            isLoading = false
        }
        }
    }
    if (feeds3.isEmpty()){
        isLoading = false
    }
    if (articles.isEmpty()){
        Row {
            Text(
                text = "ERROR ŽÁDNÝ ČLÁNEK!!!",
                style = MaterialTheme.typography.h6,
                color = Color.Gray
            )
        }
    }

    if (isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        val sortedArticles = articles.sortedByDescending { it.pubDate }
        println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
        sortedArticles.forEach(){
            item ->
            val article = item.pubDate.toString() + " " + item.title
            println(article)
        }
        println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
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
                                    text = getTime(note.pubDate) ?: "Date not found.",
                                    style = MaterialTheme.typography.h6,
                                    color = Color.Gray
                                )
                            }

                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
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
}

private suspend fun getFeeds(feed: NoteEntity): List<ArticleEntity>{
    val articles = mutableListOf<ArticleEntity>()
    val parser = Parser.Builder()
        .charset(Charset.forName("UTF-8"))
        .build()

    //url of RSS feed

    //val url = "https://domaci.hn.cz/?m=rss"
    //val url = "https://servis.idnes.cz/rss.aspx?c=zpravodaj"
    val url = feed.uri

    try {
        val channel = parser.getChannel(url)
        //val articlesMEOW = channel.articles;
        channel.articles.forEach { item ->

            val dateString = item.pubDate
            var date : Date? = null
            try {
                val formatXXXX = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                val dateTEST = formatXXXX.parse(dateString)
                date = dateTEST
            } catch (e: Exception){
                e.printStackTrace()
            }
            try {
                if (dateString != null && date != null) {
                    var format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
                    if (dateString?.takeLast(5).equals("+0200")) {
                        val locale = Locale("cs", "CZ")
                        format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", locale)
                    }
                    date = format.parse(dateString)
                }
            } catch (e: Exception){
                e.printStackTrace()
            }
        articles.add(
            ArticleEntity(item.title, item.description, date)
        )
        }
    } catch (e: Exception) {

        e.printStackTrace()
        // Handle the exception
    }


    return articles
}

private fun getTime(date: Date?):String?{
    var stringDate: String? = null
    if (date != null){
        val calendar = Calendar.getInstance()
        calendar.time = date

        val stringHours = calendar.get(Calendar.HOUR_OF_DAY).toString()
        val minutes = calendar.get(Calendar.MINUTE)
        val stringDays = calendar.get(Calendar.DAY_OF_MONTH).toString()
        val stringMonth = calendar.get(Calendar.MONTH).toString()
        val stringYear = calendar.get(Calendar.YEAR).toString()
        var stringMinutes = minutes.toString()
        if (minutes < 10) {
            stringMinutes = "0" + stringMinutes
        }
        stringDate = stringHours + ":" + stringMinutes + " " + stringDays + ". " + stringMonth + ". " + stringYear
    }
    return stringDate
}