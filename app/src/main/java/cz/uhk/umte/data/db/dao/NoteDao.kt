package cz.uhk.umte.data.db.dao

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.*
import cz.uhk.umte.data.db.entities.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(note: NoteEntity)

    @Query("Select * From NoteEntity Order By priority Desc")
    fun selectAll(): Flow<List<NoteEntity>>

    @Delete
    fun remove(note: NoteEntity)
}