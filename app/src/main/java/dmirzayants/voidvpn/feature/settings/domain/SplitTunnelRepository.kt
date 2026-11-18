package dmirzayants.voidvpn.feature.settings.domain

import kotlinx.coroutines.flow.Flow

/** Приложения, которые работают напрямую, мимо VPN. Остальные идут через туннель. */
interface SplitTunnelRepository {
    val bypassPackages: Flow<Set<String>>

    suspend fun toggle(packageName: String)
}
