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
        val meow = launch {
            val schemes = schemeDao.getAll()
            if (schemes.size == 0){
                addScheme("První",false)
                addScheme("Druhý",false)
                addScheme("Třetí",true)
                addScheme("čtvrtý",false)
            }
        }
    }

    fun changeSchema(scheme: SchemeEntity){
        launch {
        schemeDao.unuseAll()
        schemeDao.insertOrUpdate(
            note = SchemeEntity(
                description = scheme.description,
                used = true,
                id = scheme.id
            )
        )
        }
    }

    fun addScheme(description: String, used: Boolean) {
        launch {
            schemeDao.insertOrUpdate(
                note = SchemeEntity(
                    description = description,
                    used = used
                )
            )
        }
    }
    fun removeFeed(note: SchemeEntity){
        launch{
            schemeDao.remove(note)
        }
    }

}