package cz.uhk.umte.ui.feeds

import com.prof.rssparser.Parser
import cz.uhk.umte.data.db.dao.FeedDao
import cz.uhk.umte.data.db.entities.FeedEntity
import cz.uhk.umte.ui.base.BaseViewModel
import java.nio.charset.Charset

class FeedVM(
    private val feedDao: FeedDao,
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
                    note = FeedEntity(
                        text = feedName,
                        uri =  feedUri
                    )
                )
            }
        }
    }
    fun removeFeed(note: FeedEntity){
        launch{
            feedDao.remove(note)
        }
    }

    fun handleNoteCheck(note: FeedEntity) {
        launch {
            feedDao.insertOrUpdate(
                note = note.copy(
                    used = note.used.not(),
                )
            )
        }
    }
}