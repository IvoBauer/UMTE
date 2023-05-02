package cz.uhk.umte.ui.feeds

import android.provider.ContactsContract.CommonDataKinds.Note
import android.util.Log
import androidx.compose.material.*
import androidx.compose.runtime.*
import com.prof.rssparser.Parser
import cz.uhk.umte.data.db.dao.NoteDao
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.nio.charset.Charset

class FeedVM(
    private val feedDao: NoteDao,
) : BaseViewModel() {

    val feeds = feedDao.selectAll()

    fun addFeed(feedName: String, feedUri: String) {
        launch {
            val parser = Parser.Builder()
                .charset(Charset.forName("UTF-8"))
                .build()
            val channel = parser.getChannel(feedUri)
            if (channel.articles.isNotEmpty()){
                feedDao.insertOrUpdate(
                    note = NoteEntity(
                        text = feedName,
                        uri =  feedUri
                    )
                )
            }
        }
    }
    fun removeFeed(note: NoteEntity){
        launch{
            feedDao.remove(note)
        }
    }

    fun handleNoteCheck(note: NoteEntity) {
        launch {
            feedDao.insertOrUpdate(
                note = note.copy(
                    solved = note.solved.not(),
                )
            )
        }
    }
}