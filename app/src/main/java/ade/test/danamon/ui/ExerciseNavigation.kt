package ade.test.danamon.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val path: String? = null,
    vararg val arguments: NamedNavArgument
) {
    data object Welcome : Screen("welcome")
    data object SignIn : Screen("signin?email={email}", "signin",
        navArgument("email") {
            nullable = true
            defaultValue = null
            type = NavType.StringType
        }
    )

    data object SignUp : Screen("signup?email={email}", "signup",
        navArgument("email") {
            nullable = true
            defaultValue = null
            type = NavType.StringType
        }
    )

    data object Home : Screen("home")
}

class ExerciseActions(navController: NavController) {
    val navigateToWelcome: () -> Unit = {
        navController.navigate(Screen.Welcome.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }
    val navigateToSignIn: (String?) -> Unit = {
        var route = Screen.SignIn.path.orEmpty()
        if (!it.isNullOrEmpty()) {
            route = "$route?email=$it"
        }
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }
    val navigateToSignUp: (String?) -> Unit = {
        var route = Screen.SignUp.path.orEmpty()
        if (!it.isNullOrEmpty()) {
            route = "$route?email=$it"
        }
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        }
    }
    val navigateToHome: () -> Unit = {
        navController.navigate(Screen.Home.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }
    val navigateUp: () -> Unit = {
        navController.navigateUp()
    }
}