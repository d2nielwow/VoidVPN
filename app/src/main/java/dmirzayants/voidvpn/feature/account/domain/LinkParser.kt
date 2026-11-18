package dmirzayants.voidvpn.feature.account.domain

import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import java.net.URI

/** Результат разбора ссылки из буфера или поля ввода. */
data class ParsedLink(
    val raw: String,
    /** null для ссылки-подписки https://…/sub: протоколы приходят внутри неё. */
    val protocol: VpnProtocol?,
    val host: String?,
    val isSubscription: Boolean,
)

object LinkParser {

    private val schemes = mapOf(
        "vless" to VpnProtocol.VLESS_REALITY,
        "hy2" to VpnProtocol.HYSTERIA2,
        "hysteria2" to VpnProtocol.HYSTERIA2,
        "trojan" to VpnProtocol.TROJAN,
        "ss" to VpnProtocol.SHADOWSOCKS,
    )

    /** Поддерживаются vless://, hy2://, trojan://, ss://, а также https://…/sub. Остальное даёт null. */
    fun parse(text: String): ParsedLink? {
        val raw = text.trim()
        if (raw.isEmpty() || raw.any { it.isWhitespace() }) return null
        val scheme = raw.substringBefore("://", missingDelimiterValue = "").lowercase()
        if (scheme.isEmpty()) return null

        if (scheme == "https" || scheme == "http") {
            val uri = runCatching { URI(raw) }.getOrNull() ?: return null
            val host = uri.host ?: return null
            if (!uri.path.orEmpty().contains("sub", ignoreCase = true)) return null
            return ParsedLink(raw, protocol = null, host = host, isSubscription = true)
        }

        val protocol = schemes[scheme] ?: return null
        val host = runCatching { URI(raw).host }.getOrNull()
            ?: raw.substringAfter("@", "").substringBefore(":").substringBefore("/")
                .takeIf { it.isNotBlank() }
        return ParsedLink(raw, protocol = protocol, host = host, isSubscription = false)
    }
}
