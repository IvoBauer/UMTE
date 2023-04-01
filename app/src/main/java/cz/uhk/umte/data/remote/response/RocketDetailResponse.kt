package cz.uhk.umte.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketDetailResponse(
    private val id: Long,
    @SerialName("rocket_name") val name: String,
    val description: String,
    @SerialName("active") val isActive: Boolean,
    @SerialName("stages") val stagesCount: Int,
    @SerialName("boosters") val boostersCount: Int,
    @SerialName("wikipedia") val wikipediaLink: String?,
)