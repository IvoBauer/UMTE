package cz.uhk.umte.data.remote.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RocketLaunchResponse(

    @SerialName("flight_number")
    val flightNumber: Int,

    @SerialName("mission_name")
    val missionName: String?,

    val rocket: RocketResponse,
) {

    @Serializable
    data class RocketResponse(
        @SerialName("rocket_id") val rocketId: String,
        @SerialName("rocket_name") val rocketName: String,
    )
}
