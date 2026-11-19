package dmirzayants.voidvpn.feature.vpn.domain

import kotlinx.coroutines.flow.StateFlow

interface VpnController {
    val status: StateFlow<VpnStatus>

    suspend fun connect(server: Server, protocol: VpnProtocol)

    suspend fun disconnect()
}
