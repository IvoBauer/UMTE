package cz.uhk.umte.ui.async.launches

import cz.uhk.umte.data.remote.response.RocketLaunchResponse
import cz.uhk.umte.di.repositories.SpaceXRepository
import cz.uhk.umte.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LaunchesViewModel(
    private val repo: SpaceXRepository,
) : BaseViewModel() {

    private val _successfulRocketLaunches = MutableStateFlow<List<RocketLaunchResponse>>(emptyList())
    val successRocketLaunches = _successfulRocketLaunches.asStateFlow()

    init {
        fetchSuccessfulRocketLaunches()
    }

    private fun fetchSuccessfulRocketLaunches() {
        launch {
            val launches = repo.fetchAllSuccessfulLaunches()
            _successfulRocketLaunches.emit(launches)
        }
    }
}