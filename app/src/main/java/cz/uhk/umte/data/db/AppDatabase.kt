package cz.uhk.umte.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.uhk.umte.data.db.dao.NoteDao
import cz.uhk.umte.data.db.dao.SchemeDao
import cz.uhk.umte.data.db.entities.NoteEntity
import cz.uhk.umte.data.db.entities.SchemeEntity

@Database(
    version = 2,
    entities = [
        NoteEntity::class,
        SchemeEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun feedDao(): NoteDao
    abstract fun schemeDao(): SchemeDao

    companion object {
        const val Version = 1
        const val Name = "UmteDatabase2"
    }
}