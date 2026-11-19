package dmirzayants.voidvpn.feature.settings.domain

import kotlinx.coroutines.flow.Flow

interface SplitTunnelRepository {
    val bypassPackages: Flow<Set<String>>

    suspend fun toggle(packageName: String)
}
