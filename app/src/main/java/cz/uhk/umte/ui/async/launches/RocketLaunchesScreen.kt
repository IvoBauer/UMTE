package cz.uhk.umte.ui.async.launches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cz.uhk.umte.R
import cz.uhk.umte.data.remote.response.RocketLaunchResponse
import cz.uhk.umte.ui.base.State
import org.koin.androidx.compose.getViewModel
import java.io.IOException

@Composable
fun RocketLaunchesScreen(
    viewModel: LaunchesViewModel = getViewModel(),
    onNavigateDetail: (String) -> Unit,
) {

    val launches = viewModel.successRocketLaunches.collectAsState()
    val state = viewModel.state.collectAsState()

    RocketLaunchesViews(
        launches = launches.value,
        state = state.value,
        onNavigateDetail = onNavigateDetail,
    )
}

@Preview
@Composable
fun RocketLaunchesViews(
    launches: List<RocketLaunchResponse> = emptyList(),
    state: State = State.None,
    onNavigateDetail: (String) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        when (state) {
            State.None, State.Loading -> {
                CircularProgressIndicator()
            }
            is State.Failure -> {
                Button(onClick = { state.repeat() }) {
                    Text(text = stringResource(R.string.retry))
                }
            }
            is State.Success -> {
                // List
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(all = 8.dp),
                ) {
                    items(launches) { launch ->
                        Card {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        onNavigateDetail(launch.rocket.rocketId)
                                    }
                                    .background(MaterialTheme.colors.error)
                                    .padding(16.dp)
                                    .background(MaterialTheme.colors.surface)
                                    .padding(16.dp)
                            ) {
                                Text(
                                    text = launch.missionName ?: "-",
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier.weight(1F),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}