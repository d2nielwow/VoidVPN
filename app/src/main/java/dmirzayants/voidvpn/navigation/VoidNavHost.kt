package dmirzayants.voidvpn.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dmirzayants.voidvpn.feature.account.presentation.keycreated.KeyCreatedScreen
import dmirzayants.voidvpn.feature.account.presentation.login.LoginScreen
import dmirzayants.voidvpn.feature.account.presentation.start.StartScreen
import dmirzayants.voidvpn.feature.onboarding.presentation.OnboardingScreen
import dmirzayants.voidvpn.feature.permission.presentation.PermissionScreen
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidText

@Composable
fun VoidNavHost(
    startDestination: Any,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable<OnboardingRoute> {
            OnboardingScreen(
                onNavigateToPermission = {
                    navController.navigate(PermissionRoute) {
                        popUpTo(OnboardingRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<PermissionRoute> {
            PermissionScreen(
                onNavigateToHome = {
                    navController.navigate(StartRoute) {
                        popUpTo(PermissionRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<StartRoute> {
            StartScreen(
                onNavigateToKeyCreated = { navController.navigate(KeyCreatedRoute) },
                onNavigateToLogin = { navController.navigate(LoginRoute) }
            )
        }
        composable<KeyCreatedRoute> {
            KeyCreatedScreen(
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(StartRoute) { inclusive = true }
                    }
                }
            )
        }
        composable<LoginRoute> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(StartRoute) { inclusive = true }
                    }
                },
                onNavigateToImport = { },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<HomeRoute> {
            PlaceHolderScreen("Home")
        }
    }
}

@Composable
private fun PlaceHolderScreen(name: String) {
    Box(
        Modifier
            .fillMaxSize()
            .background(VoidBlack),
        contentAlignment = Alignment.Center,
    ) {
        Text(name, color = VoidText.primary, style = MaterialTheme.typography.headlineLarge)
    }
}