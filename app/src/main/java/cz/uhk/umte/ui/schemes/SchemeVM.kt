package cz.uhk.umte.ui.schemes

import androidx.compose.ui.graphics.Color
import cz.uhk.umte.data.db.dao.SchemeDao
import cz.uhk.umte.data.db.entities.SchemeEntity
import cz.uhk.umte.ui.base.BaseViewModel

class SchemeVM(
    private val schemeDao: SchemeDao,
) : BaseViewModel() {

    val schemes = schemeDao.selectAll()
    fun checkSchemes(){
        launch {
            val schemes = schemeDao.getAll()
            if (schemes.size == 0){
                addScheme("První")
                addScheme("Druhý")
                addScheme("Třetí")
                addScheme("čtvrtý")
            }
        }
    }

    fun addScheme(description: String) {
        launch {
            schemeDao.insertOrUpdate(
                note = SchemeEntity(
                    description = description
                )
            )
        }
    }
    fun removeFeed(note: SchemeEntity){
        launch{
            schemeDao.remove(note)
        }
    }

    fun handleNoteCheck(note: SchemeEntity) {
        launch {
            schemeDao.insertOrUpdate(
                note = note.copy(
                    used = note.used.not(),
                )
            )
        }
    }

    fun handleNotePriority(note: SchemeEntity, newPriority: Int) {
        launch {
            schemeDao.insertOrUpdate(
                note = note.copy(
                )
            )
        }
    }

}