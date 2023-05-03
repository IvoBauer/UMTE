package cz.uhk.umte.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import cz.uhk.umte.ui.articles.ArticlesScreen
import cz.uhk.umte.ui.home.HomeScreen
import cz.uhk.umte.ui.feeds.FeedScreen
import cz.uhk.umte.ui.schemes.SchemeScreen

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
            route = DestinationRoom
        ) {
            FeedScreen()
        }

        composable(
            route = DestinationRoom3
        ) {
            SchemeScreen()
        }

        composable(
            route = DestinationRoom2
        ) {
            ArticlesScreen()
        }
    }
}

private const val ArgRocketId = "argRocketId"

private const val DestinationHome = "home"
private const val DestinationLaunches = "launches"
private const val DestinationRocketDetail = "rocket/{$ArgRocketId}"
private const val DestinationRoom = "room"
private const val DestinationRoom2 = "room2"
private const val DestinationRoom3 = "room3"
private const val DestinationDataStore = "dataStore"
private const val DestinationIntents = "intents"
private const val DestinationNotifications = "notifications"

fun NavHostController.navigateRocketDetail(rocketId: String) =
    navigate(DestinationRocketDetail.replaceArg(ArgRocketId, rocketId))

fun NavHostController.navigateRocketLaunches() =
    navigate(DestinationLaunches)

fun NavHostController.navigateRoomScreen() =
    navigate(DestinationRoom)

fun NavHostController.navigateRoomScreen2() =
    navigate(DestinationRoom2)

fun NavHostController.navigateRoomScreen3() =
    navigate(DestinationRoom3)

fun NavHostController.navigateDataStoreScreen() =
    navigate(DestinationDataStore)

fun NavHostController.navigateIntents() =
    navigate(DestinationIntents)

fun NavHostController.navigateNotifications() =
    navigate(DestinationNotifications)

private fun String.replaceArg(argName: String, value: String) =
    replace("{$argName}", value)