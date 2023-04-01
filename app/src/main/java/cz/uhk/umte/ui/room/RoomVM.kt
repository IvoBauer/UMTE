package cz.uhk.umte.ui.room

import cz.uhk.umte.data.db.dao.NoteDao
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.ui.base.BaseViewModel

class RoomVM(
    private val noteDao: NoteDao,
) : BaseViewModel() {

    val notes = noteDao.selectAll()

    fun addNote(text: String) {
        launch {
            noteDao.insertOrUpdate(
                note = NoteEntity(
                    text = text,
                )
            )
        }
    }

    fun handleNoteCheck(note: NoteEntity) {
        launch {
            noteDao.insertOrUpdate(
                note = note.copy(
                    solved = note.solved.not(),
                )
            )
        }
    }

    fun handleNotePriority(note: NoteEntity, newPriority: Int) {
        launch {
            noteDao.insertOrUpdate(
                note = note.copy(
                    priority = newPriority
                )
            )
        }
    }
}