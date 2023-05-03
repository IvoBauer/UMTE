@file:OptIn(ExperimentalFoundationApi::class)

package cz.uhk.umte.ui.articles

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import com.prof.rssparser.Parser
import cz.uhk.umte.data.db.entities.ArticleEntity
import cz.uhk.umte.data.db.entities.NoteEntity
//import cz.uhk.umte.ui.async.rocket.openWebPage
import cz.uhk.umte.ui.feeds.FeedVM
import cz.uhk.umte.ui.schemes.SchemeVM
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
    viewModel2: SchemeVM = getViewModel()
) {
    var schemes = viewModel2.schemes.collectAsState(emptyList()).value
    var schemeNumber = 1;
    if (schemes.size > 0){
        schemeNumber = (schemes.find{it.used})?.schemeNumber ?: 1
    }
    var filter by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = Brush.linearGradient(
                colors = listOf(getColor(schemeNumber,0), getColor(schemeNumber,1)),
                start = Offset(0f, 0f),
                end = Offset(LocalConfiguration.current.screenWidthDp.dp.value, LocalConfiguration.current.screenHeightDp.dp.value)
            ))
    )

    val feeds = viewModel.feeds.collectAsState(emptyList()).value
    var isLoading by remember { mutableStateOf(true) }
    var articles by remember { mutableStateOf(emptyList<ArticleEntity>()) }
    var filteredArticles by remember { mutableStateOf<List<ArticleEntity>>(articles) }

    feeds.forEach { feed ->

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
    filteredArticles = articles.filter { it.summary!!.contains(filter, ignoreCase = true) || it.title!!.contains(filter, ignoreCase = true) }
    if (feeds.isEmpty()){
        isLoading = false
    }
    if (filteredArticles.isEmpty()){
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
        val sortedArticles = filteredArticles.sortedByDescending { it.pubDate }
        Column {
            Box(contentAlignment = Alignment.Center,
                modifier = Modifier.padding(top = 16.dp)
                    .padding(bottom = 8.dp)
                    .background(Color.Transparent)
                    .width(LocalConfiguration.current.screenWidthDp.dp)
                    ) {
                OutlinedTextField(
                    value = filter,
                    onValueChange = {filterText ->
                        filter = filterText
                     },
                    label = { Text(text = "Filter") },
                    modifier = Modifier.width((LocalConfiguration.current.screenWidthDp*0.8).dp).background(Color.Transparent).padding(bottom = 8.dp).background(color = Color.Transparent, shape = RoundedCornerShape(16.dp))
                )
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(
                    items = sortedArticles,
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
                                val context = LocalContext.current

                                Button(onClick = {
                                    context.openWebPage(note.uri!!)

                                }) {
                                    Text("Read article", style = MaterialTheme.typography.h6)
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

    try {
        val channel = parser.getChannel(feed.uri)
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
            if (item.description == null){
                item.description == "Not found."
            }
        articles.add(
            ArticleEntity(item.title, item.description, date, item.link)
        )
        }
    } catch (e: Exception) {
        e.printStackTrace()
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

fun Context.openWebPage(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}