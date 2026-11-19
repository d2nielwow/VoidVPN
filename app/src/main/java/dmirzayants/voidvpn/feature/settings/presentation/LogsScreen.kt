package dmirzayants.voidvpn.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.ui.VoidBackButton
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidText

private data class LogLine(val time: String, val level: String, val color: Color, val message: String)

private val sampleLogs = listOf(
    LogLine("12:04:11", "INFO", VoidText.tertiary, "sing-box started"),
    LogLine("12:04:11", "INFO", VoidText.tertiary, "outbound hysteria2 nl-ams-03"),
    LogLine("12:04:13", "WARN", VoidStatus.connecting, "handshake timeout, retry 1/3"),
    LogLine("12:04:16", "ERROR", VoidStatus.error, "connection reset by peer"),
    LogLine("12:04:16", "INFO", VoidText.tertiary, "fallback → vless-reality"),
)

@Composable
fun LogsScreen(onNavigateBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        VoidBackButton(onClick = onNavigateBack, modifier = Modifier.padding(start = 16.dp, top = 12.dp))
        Text(
            stringResource(R.string.logs_title),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = VoidText.primary
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 18.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            sampleLogs.forEach { line ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(line.time, style = VoidMono.log, color = VoidText.disabled)
                    Text(line.level, style = VoidMono.log, color = line.color)
                    Text(line.message, style = VoidMono.log, color = VoidText.secondary)
                }
            }
        }
    }
}
