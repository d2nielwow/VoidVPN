package dmirzayants.voidvpn.feature.vpn.domain

import kotlinx.coroutines.flow.StateFlow

/**
 * Управление туннелем. Сейчас реализован фейком ([dmirzayants.voidvpn.feature.vpn.data.FakeVpnController]),
 * реальная реализация поверх sing-box (VpnService + libbox) подставляется через Hilt-модуль.
 */
interface VpnController {
    val status: StateFlow<VpnStatus>

    suspend fun connect(server: Server, protocol: VpnProtocol)

    suspend fun disconnect()
}
