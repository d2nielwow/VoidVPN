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
import androidx.navigation.NavController
import androidx.navigation.toRoute
import dmirzayants.voidvpn.core.ui.MainTab
import dmirzayants.voidvpn.feature.billing.presentation.MyPlanScreen
import dmirzayants.voidvpn.feature.billing.presentation.PaymentMethodsScreen
import dmirzayants.voidvpn.feature.billing.presentation.PaymentStatusScreen
import dmirzayants.voidvpn.feature.billing.presentation.PlansScreen
import dmirzayants.voidvpn.feature.home.presentation.HomeScreen
import dmirzayants.voidvpn.feature.onboarding.presentation.OnboardingScreen
import dmirzayants.voidvpn.feature.servers.presentation.ServersScreen
import dmirzayants.voidvpn.feature.account.presentation.importsub.ImportScreen
import dmirzayants.voidvpn.feature.settings.presentation.LogsScreen
import dmirzayants.voidvpn.feature.settings.presentation.ProtocolScreen
import dmirzayants.voidvpn.feature.settings.presentation.SplitTunnelingScreen
import dmirzayants.voidvpn.feature.settings.presentation.SettingsScreen
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
                onNavigateToImport = { navController.navigate(ImportRoute) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable<HomeRoute> {
            HomeScreen(
                onTabSelected = navController::navigateToTab,
                onNavigateToServers = { navController.navigateToTab(MainTab.Servers) },
                onNavigateToPlan = { navController.navigateToTab(MainTab.Plan) },
                onNavigateToProtocol = { navController.navigate(ProtocolRoute) }
            )
        }
        composable<ServersRoute> {
            ServersScreen(
                onTabSelected = navController::navigateToTab,
                onNavigateToHome = { navController.navigateToTab(MainTab.Home) }
            )
        }
        composable<PlanRoute> {
            MyPlanScreen(
                onTabSelected = navController::navigateToTab,
                onNavigateToPlans = { navController.navigate(PlansRoute) }
            )
        }
        composable<PlansRoute> {
            PlansScreen(
                onClose = { navController.popBackStack() },
                onContinue = { planId -> navController.navigate(PaymentMethodsRoute(planId)) }
            )
        }
        composable<PaymentMethodsRoute> { entry ->
            val route = entry.toRoute<PaymentMethodsRoute>()
            PaymentMethodsScreen(
                planId = route.planId,
                onBack = { navController.popBackStack() },
                onPay = { methodId ->
                    navController.navigate(PaymentStatusRoute(route.planId, methodId))
                }
            )
        }
        composable<PaymentStatusRoute> {
            PaymentStatusScreen(
                onConfirmed = {
                    navController.navigate(PlanRoute) {
                        popUpTo(HomeRoute)
                        launchSingleTop = true
                    }
                }
            )
        }
        composable<SettingsRoute> {
            SettingsScreen(
                onTabSelected = navController::navigateToTab,
                onNavigateToProtocol = { navController.navigate(ProtocolRoute) },
                onNavigateToSplitTunneling = { navController.navigate(SplitTunnelingRoute) },
                onNavigateToLogs = { navController.navigate(LogsRoute) }
            )
        }
        composable<ProtocolRoute> {
            ProtocolScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable<SplitTunnelingRoute> {
            SplitTunnelingScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable<LogsRoute> {
            LogsScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable<ImportRoute> {
            ImportScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(HomeRoute) {
                        popUpTo(StartRoute) { inclusive = true }
                    }
                }
            )
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

/** Переключение вкладок нижней панели: одна запись на вкладку, Home всегда внизу стека. */
fun NavController.navigateToTab(tab: MainTab) {
    val route: Any = when (tab) {
        MainTab.Home -> HomeRoute
        MainTab.Servers -> ServersRoute
        MainTab.Plan -> PlanRoute
        MainTab.Settings -> SettingsRoute
    }
    navigate(route) {
        popUpTo(HomeRoute) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
