package cz.uhk.umte.ui.feeds

import android.provider.ContactsContract.CommonDataKinds.Note
import cz.uhk.umte.data.db.dao.NoteDao
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.base.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class FeedVM(
    private val feedDao: NoteDao,
) : BaseViewModel() {

    val feeds = feedDao.selectAll()

    fun addFeed(feedName: String, feedUri: String) {
        launch {
            feedDao.insertOrUpdate(
                note = NoteEntity(
                    text = feedName,
                    uri =  feedUri
                )
            )
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

    fun handleNotePriority(note: NoteEntity, newPriority: Int) {
        launch {
            feedDao.insertOrUpdate(
                note = note.copy(
                    priority = newPriority
                )
            )
        }
    }
}