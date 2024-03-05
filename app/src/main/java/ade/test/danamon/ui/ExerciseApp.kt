package ade.test.danamon.ui

import ade.test.danamon.ui.theme.ExerciseTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController

@Composable
fun ExerciseApp(defaultRoute: String) {
    ExerciseTheme {
        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            ExerciseActions(navController)
        }

        ExerciseNavGraph(
            navController = navController,
            navigateToWelcome = navigationActions.navigateToWelcome,
            navigateToSignIn = navigationActions.navigateToSignIn,
            navigateToSignUp = navigationActions.navigateToSignUp,
            navigateToHome = navigationActions.navigateToHome,
            navigateUp = navigationActions.navigateUp,
            startDestination = defaultRoute
        )
    }
}