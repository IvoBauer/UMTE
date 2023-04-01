package cz.uhk.umte.ui.home

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import cz.uhk.umte.R
import cz.uhk.umte.ui.*
import cz.uhk.umte.ui.dialog.DialogActivity
import cz.uhk.umte.ui.form.FormActivity
import cz.uhk.umte.ui.lazylist.LazyListActivity

@Composable
fun HomeScreen(
    parentController: NavHostController,
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = {
                context.startActivity(
                    Intent(context, FormActivity::class.java)
                )
            },
            content = {
                Text(text = stringResource(id = R.string.home_btn_activity_form))
            },
        )
        Button(
            onClick = {
                context.startActivity(
                    Intent(context, LazyListActivity::class.java)
                )
            }
        ) {
            Text(text = stringResource(id = R.string.home_btn_activity_list))
        }
        Button(
            onClick = {
                context.startActivity(
                    Intent(context, DialogActivity::class.java)
                )
            }
        ) {
            Text(text = stringResource(id = R.string.home_btn_dialog))
        }
        Button(
            onClick = {
                parentController.navigateRocketLaunches()
            }
        ) {
            Text(text = stringResource(id = R.string.home_btn_launches))
        }
        Button(
            onClick = {
                parentController.navigateRoomScreen()
            }
        ) {
            Text(text = "Room screen")
        }
        Button(
            onClick = {
                parentController.navigateDataStoreScreen()
            }
        ) {
            Text(text = "DataStore screen")
        }
        Button(
            onClick = {
                parentController.navigateIntents()
            }
        ) {
            Text(text = "Intens screen")
        }
        Button(
            onClick = {
                parentController.navigateNotifications()
            }
        ) {
            Text(text = "Notifications screen")
        }
    }
}