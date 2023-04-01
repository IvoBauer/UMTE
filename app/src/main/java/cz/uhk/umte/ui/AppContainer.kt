package cz.uhk.umte.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.uhk.umte.ui.async.launches.RocketLaunchesScreen
import cz.uhk.umte.ui.async.rocket.RocketDetailScreen
import cz.uhk.umte.ui.datastore.DataStoreScreen
import cz.uhk.umte.ui.home.HomeScreen
import cz.uhk.umte.ui.intents.IntentsScreen
import cz.uhk.umte.ui.notifications.NotificationScreen
import cz.uhk.umte.ui.room.RoomScreen

@Composable
fun AppContainer(
    controller: NavHostController,
) {

    NavHost(
        navController = controller,
        startDestination = DestinationHome,
    ) {
        // Graph navigation

        composable(
            route = DestinationHome,
        ) {
            HomeScreen(
                parentController = controller,
            )
        }

        composable(
            route = DestinationLaunches,
        ) {
            RocketLaunchesScreen(
                onNavigateDetail = { rocketId ->
                    controller.navigateRocketDetail(rocketId)
                },
            )
        }

        composable(
            route = DestinationRocketDetail,
            arguments = listOf(navArgument(ArgRocketId) { type = NavType.StringType })
        ) { navBackStackEntry ->
            RocketDetailScreen(
                rocketId = navBackStackEntry.arguments?.getString(ArgRocketId).orEmpty(),
            )
        }

        composable(
            route = DestinationRoom
        ) {
            RoomScreen()
        }

        composable(
            route = DestinationDataStore
        ) {
            DataStoreScreen()
        }

        composable(
            route = DestinationIntents
        ) {
            IntentsScreen()
        }

        composable(
            route = DestinationNotifications
        ) {
            NotificationScreen()
        }
    }
}

private const val ArgRocketId = "argRocketId"

private const val DestinationHome = "home"
private const val DestinationLaunches = "launches"
private const val DestinationRocketDetail = "rocket/{$ArgRocketId}"
private const val DestinationRoom = "room"
private const val DestinationDataStore = "dataStore"
private const val DestinationIntents = "intents"
private const val DestinationNotifications = "notifications"

fun NavHostController.navigateRocketDetail(rocketId: String) =
    navigate(DestinationRocketDetail.replaceArg(ArgRocketId, rocketId))

fun NavHostController.navigateRocketLaunches() =
    navigate(DestinationLaunches)

fun NavHostController.navigateRoomScreen() =
    navigate(DestinationRoom)

fun NavHostController.navigateDataStoreScreen() =
    navigate(DestinationDataStore)

fun NavHostController.navigateIntents() =
    navigate(DestinationIntents)

fun NavHostController.navigateNotifications() =
    navigate(DestinationNotifications)

private fun String.replaceArg(argName: String, value: String) =
    replace("{$argName}", value)