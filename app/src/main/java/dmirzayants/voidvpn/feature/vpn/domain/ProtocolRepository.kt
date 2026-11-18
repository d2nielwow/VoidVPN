package dmirzayants.voidvpn.feature.vpn.domain

import kotlinx.coroutines.flow.Flow

interface ProtocolRepository {
    val protocol: Flow<VpnProtocol>

    suspend fun setProtocol(protocol: VpnProtocol)
}
