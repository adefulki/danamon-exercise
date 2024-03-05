package ade.test.danamon.ui

import ade.test.danamon.data.pref.UserPref
import ade.test.danamon.ui.home.HomeScreen
import ade.test.danamon.ui.signin.SignInScreen
import ade.test.danamon.ui.signup.SignUpScreen
import ade.test.danamon.ui.welcome.WelcomeScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun ExerciseNavGraph(
    modifier: Modifier = Modifier,
    navigateToWelcome: () -> Unit,
    navigateToSignIn: (String?) -> Unit,
    navigateToSignUp: (String?) -> Unit,
    navigateToHome: () -> Unit,
    navigateUp: () -> Unit,
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Welcome.route
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = Screen.Welcome.route) {
            WelcomeScreen(
                onContinue = { email: String, isExist: Boolean ->
                    if (isExist) navigateToSignIn(email)
                    else navigateToSignUp(email)
                },
                onSignUp = {
                    navigateToSignUp(null)
                },
                onAlreadyLogin = {
                    navigateToHome()
                }
            )
        }
        composable(route = Screen.SignUp.route, arguments = Screen.SignUp.arguments.toList()) {
            val startingEmail = it.arguments?.getString("email")
            SignUpScreen(
                email = startingEmail,
                onSignUpSubmitted = { email ->
                    navigateToSignIn(email)
                },
                onSignIn = {
                    navigateToSignIn(null)
                }, onNavUp = {
                    navigateUp()
                })
        }
        composable(route = Screen.SignIn.route, arguments = Screen.SignIn.arguments.toList()) {
            val startingEmail = it.arguments?.getString("email")
            SignInScreen(
                email = startingEmail,
                onSignInSubmitted = {
                    navigateToHome()
                },
                onSignUp = {
                    navigateToSignUp(null)
                },
                onNavUp = {
                    navigateUp()
                })
        }
        composable(route = Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    UserPref(context).remove()
                    navigateToWelcome()
                }
            )
        }
    }
}