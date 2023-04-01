package cz.uhk.umte.ui.async.rocket

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cz.uhk.umte.R
import cz.uhk.umte.data.remote.response.RocketDetailResponse
import cz.uhk.umte.ui.base.State
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RocketDetailScreen(
    rocketId: String,
    viewModel: RocketDetailViewModel = getViewModel {
        parametersOf(rocketId)
    },
) {

    val state = viewModel.state.collectAsState()
    val detail = viewModel.rocketDetail.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        when (val result = state.value) {
            State.None, State.Loading -> {
                CircularProgressIndicator()
            }
            is State.Failure -> {
                Button(onClick = { result.repeat() }) {
                    Text(text= stringResource(R.string.retry))
                }
            }
            is State.Success -> {
                detail.value?.let { detail ->
                    RocketDetailView(detail)
                } ?: run {
                    Text(text = "No data available")
                }
            }
        }
    }
}

@Composable
fun RocketDetailView(detail: RocketDetailResponse) {
    Column(
        modifier = Modifier.padding(16.dp),
    ) {
        Text(text = detail.name, style = MaterialTheme.typography.h5)
        Text(text = detail.description)
        detail.wikipediaLink?.let { url ->

            val context = LocalContext.current

            Button(onClick = { context.openWebPage(url) }) {
                Text(text = "Wikipedia")
            }
        }
    }
}

private fun Context.openWebPage(url: String) {
    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
}