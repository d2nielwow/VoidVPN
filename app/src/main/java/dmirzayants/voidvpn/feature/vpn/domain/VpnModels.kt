package dmirzayants.voidvpn.feature.vpn.domain

enum class VpnProtocol(val label: String) {
    AUTO("Авто"),
    VLESS_REALITY("VLESS-Reality"),
    HYSTERIA2("Hysteria2"),
    TROJAN("Trojan"),
    SHADOWSOCKS("Shadowsocks"),
}

data class Server(
    val id: String,
    val countryCode: String,
    val country: String,
    val city: String,
    /** null: пинг ещё измеряется (или сервер недоступен, см. [available]). */
    val pingMs: Int?,
    /** Нагрузка 0..100. */
    val load: Int,
    val available: Boolean = true,
    val favorite: Boolean = false,
)

enum class FailReason { TIMEOUT, BLOCKED, UNKNOWN }

sealed interface VpnStatus {
    data object Disconnected : VpnStatus
    data class Connecting(val protocol: VpnProtocol) : VpnStatus
    data class Connected(
        val sinceMillis: Long,
        val ip: String,
        val downMbps: Double,
        val upMbps: Double,
    ) : VpnStatus
    data class Failed(val reason: FailReason) : VpnStatus
}
