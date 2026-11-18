package dmirzayants.voidvpn.navigation

import kotlinx.serialization.Serializable

@Serializable object OnboardingRoute
@Serializable object PermissionRoute
@Serializable object HomeRoute
@Serializable object StartRoute
@Serializable object KeyCreatedRoute
@Serializable object LoginRoute
@Serializable object ServersRoute
@Serializable object PlanRoute
@Serializable object SettingsRoute
@Serializable object ProtocolRoute
@Serializable object SplitTunnelingRoute
@Serializable object LogsRoute
@Serializable object PlansRoute
@Serializable data class PaymentMethodsRoute(val planId: String)
@Serializable data class PaymentStatusRoute(val planId: String, val methodId: String)
