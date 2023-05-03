package cz.uhk.umte.data.db.dao

import androidx.room.*
import cz.uhk.umte.data.db.entities.SchemeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface


SchemeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(note: SchemeEntity)

    @Query("UPDATE SchemeEntity SET used = false")
    fun unuseAll()
    @Query("Select * From SchemeEntity")
    fun selectAll(): Flow<List<SchemeEntity>>

    @Query("Select * From SchemeEntity")
    fun getAll(): List<SchemeEntity>

    @Query("SELECT * FROM SchemeEntity")
    fun getAllPersons(): List<SchemeEntity>

}