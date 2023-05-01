package cz.uhk.umte.data.db.entities

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SchemeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val description: String = "",
    val schemeNumber: Int,
    val used: Boolean = false,
)