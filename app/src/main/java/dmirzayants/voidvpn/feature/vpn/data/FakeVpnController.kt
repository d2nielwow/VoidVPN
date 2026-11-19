package dmirzayants.voidvpn.feature.vpn.data

import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.feature.vpn.domain.VpnController
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import dmirzayants.voidvpn.feature.vpn.domain.VpnStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FakeVpnController @Inject constructor() : VpnController {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var job: Job? = null

    private val _status = MutableStateFlow<VpnStatus>(VpnStatus.Disconnected)
    override val status: StateFlow<VpnStatus> = _status.asStateFlow()

    override suspend fun connect(server: Server, protocol: VpnProtocol) {
        job?.cancel()
        job = scope.launch {
            _status.value = VpnStatus.Connecting(protocol)
            delay(2_000)
            val since = System.currentTimeMillis()
            while (true) {
                _status.value = VpnStatus.Connected(
                    sinceMillis = since,
                    ip = "185.107.56.12",
                    downMbps = 30 + Random.nextDouble(25.0),
                    upMbps = 8 + Random.nextDouble(8.0),
                )
                delay(1_000)
            }
        }
    }

    override suspend fun disconnect() {
        job?.cancel()
        _status.value = VpnStatus.Disconnected
    }
}
