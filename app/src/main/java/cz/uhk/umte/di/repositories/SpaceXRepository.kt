package cz.uhk.umte.di.repositories

import cz.uhk.umte.data.remote.service.SpaceXAPIService

class SpaceXRepository(
    private val api: SpaceXAPIService,
) {

    suspend fun fetchAllSuccessfulLaunches() =
        api.fetchAllLaunches(wasLaunchSuccessful = true)

    suspend fun fetchRocketDetail(rocketId: String) =
        api.fetchRocketDetail(rocketId)
}