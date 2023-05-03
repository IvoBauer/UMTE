package cz.uhk.umte.data.db.dao

import androidx.room.*
import cz.uhk.umte.data.db.entities.FeedEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface


FeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(note: FeedEntity)

    @Query("Select * From FeedEntity")
    fun selectAll(): Flow<List<FeedEntity>>

    @Query("Select * From FeedEntity")
    fun getAll(): List<FeedEntity>

    @Delete
    fun remove(note: FeedEntity)

    @Query("SELECT * FROM FeedEntity")
    fun getAllPersons(): List<FeedEntity>

}