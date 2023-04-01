package cz.uhk.umte.data.remote.service

import cz.uhk.umte.data.remote.response.RocketDetailResponse
import cz.uhk.umte.data.remote.response.RocketLaunchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpaceXAPIService {

    // BaseApiUrl/launches?launch_success=true

    @GET("launches")
    suspend fun fetchAllLaunches(
        @Query("launch_success") wasLaunchSuccessful: Boolean,
    ): List<RocketLaunchResponse>

    @GET("rockets/{rocketId}")
    suspend fun fetchRocketDetail(
        @Path("rocketId") rocketId: String,
    ): RocketDetailResponse?
}