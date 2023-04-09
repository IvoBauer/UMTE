package cz.uhk.umte.ui.lazylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.prof.rssparser.Parser
import cz.uhk.umte.ui.theme.UMTETheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.nio.charset.Charset

class LazyListActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ListOfPeople()
        }
    }
}

@Preview
@Composable
fun ListOfPeople() {
    UMTETheme {

        val people = remember {
            generatePeople(count = 200)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 8.dp),
            content = {
                items(people) { human ->
                    Card {
                        Row(
                            modifier = Modifier.padding(all = 16.dp),
                        ) {
                            Text(
                                text = human.name,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier.weight(1F),
                            )
                            Icon(
                                imageVector = human.status.icon(),
                                contentDescription = null,
                            )
                        }
                    }
                }
            },
        )
    }
}

private val names = arrayOf("John", "Tomáš", "Petr", "Filip")
private val surnames = arrayOf("Novák", "Kozel", "Slabý", "Malý", "Pražák")

private suspend fun doSomethingUsefulOne(): Int {
    delay(1000L) // pretend we are doing something useful here
    return 13
}


fun main() = runBlocking<Unit> {
    getFeeds()
    }       
private suspend fun getFeeds(){
    val parser = Parser.Builder()
        .charset(Charset.forName("ISO-8859-7"))
        .build()

    //url of RSS feed

    val url = "https://www.androidauthority.com/feed"

    try {

        val channel = parser.getChannel(url)
        val articlesMEOW = channel.articles
        print(articlesMEOW.size)
        print("---------------------------------------------")
        // Do something with your data

    } catch (e: Exception) {
        e.printStackTrace()
        // Handle the exception
    }
}
private fun generatePeople(count: Int = 100) = mutableListOf<Human>()
    .apply {
        // for (i, i < count, i+=2)
        for (i in 0..count) {
            add(
                Human(
                    //name = names.random() + " " + surnames.random(),
                    name = "${names.random()} ${surnames.random()}",
                    status = Status.values().random(),
                )
            )
        }
        main()
    }

data class Human(
    val name: String,
    val status: Status,
)

enum class Status {

    Okay, Favorite, Suspicious;

    fun icon() = when (this) {
        Okay -> Icons.Default.ThumbUp
        Favorite -> Icons.Default.Favorite
        Suspicious -> Icons.Default.Warning
    }
}