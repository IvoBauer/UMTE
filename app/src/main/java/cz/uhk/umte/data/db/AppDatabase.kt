package cz.uhk.umte.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.uhk.umte.data.db.dao.FeedDao
import cz.uhk.umte.data.db.dao.SchemeDao
import cz.uhk.umte.data.db.entities.FeedEntity
import cz.uhk.umte.data.db.entities.SchemeEntity

@Database(
    version = 1,
    entities = [
        FeedEntity::class,
        SchemeEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun feedDao(): FeedDao
    abstract fun schemeDao(): SchemeDao

    companion object {
        const val Version = 1
        const val Name = "RDDatabaseMain"
    }
}