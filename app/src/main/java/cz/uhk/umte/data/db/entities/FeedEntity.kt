package cz.uhk.umte.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val text: String = "",
    val uri: String = "",
    val used: Boolean = false,
)