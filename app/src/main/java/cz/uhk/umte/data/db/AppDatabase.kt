package cz.uhk.umte.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import cz.uhk.umte.data.db.dao.NoteDao
import cz.uhk.umte.data.db.entities.NoteEntity

@Database(
    version = AppDatabase.Version,
    entities = [
        NoteEntity::class,
    ],
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        const val Version = 1
        const val Name = "UmteDatabase"
    }
}