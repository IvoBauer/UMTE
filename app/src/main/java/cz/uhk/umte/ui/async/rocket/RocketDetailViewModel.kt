package cz.uhk.umte.ui.async.rocket

import cz.uhk.umte.data.remote.response.RocketDetailResponse
import cz.uhk.umte.di.repositories.SpaceXRepository
import cz.uhk.umte.ui.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RocketDetailViewModel(
    private val rocketId: String,
    private val repo: SpaceXRepository,
) : BaseViewModel() {

    private val _rocketDetail = MutableStateFlow<RocketDetailResponse?>(null)
    val rocketDetail = _rocketDetail.asStateFlow()

    init {
        fetchRocketDetail()
    }

    private fun fetchRocketDetail() {
        launch {
            repo.fetchRocketDetail(rocketId)
                .let { detail ->
                    _rocketDetail.emit(detail)
                }
        }
    }
}