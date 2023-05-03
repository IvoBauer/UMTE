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
                addScheme("Red and black",1,true)
                addScheme("Green and black",2, false)
                addScheme("Grey and black",3, false)
                addScheme("Blue and black",4, false)
            }
        }
    }

    fun changeSchema(scheme: SchemeEntity){
        launch {
        schemeDao.unuseAll()
        schemeDao.insertOrUpdate(
            note = SchemeEntity(
                description = scheme.description,
                schemeNumber = scheme.schemeNumber,
                used = true,
                id = scheme.id
            )
        )
        }
    }

    fun addScheme(description: String, schemeNumber: Int, used: Boolean) {
        launch {
            schemeDao.insertOrUpdate(
                note = SchemeEntity(
                    description = description,
                    schemeNumber = schemeNumber,
                    used = used
                )
            )
        }
    }
}

