package dmirzayants.voidvpn.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.ui.VoidBackButton
import dmirzayants.voidvpn.core.ui.VoidRadio
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.feature.vpn.domain.ProtocolRepository
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidText
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProtocolViewModel @Inject constructor(
    private val repository: ProtocolRepository
) : ViewModel() {

    val selected: StateFlow<VpnProtocol> = repository.protocol
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VpnProtocol.AUTO)

    fun select(protocol: VpnProtocol) {
        viewModelScope.launch { repository.setProtocol(protocol) }
    }
}

@Composable
fun ProtocolScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProtocolViewModel = hiltViewModel()
) {
    val selected by viewModel.selected.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        VoidBackButton(onClick = onNavigateBack, modifier = Modifier.padding(start = 16.dp, top = 12.dp))
        Text(
            stringResource(R.string.protocol_title),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
            style = MaterialTheme.typography.headlineLarge,
            color = VoidText.primary
        )
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VpnProtocol.entries.forEach { protocol ->
                ProtocolRow(
                    protocol = protocol,
                    selected = protocol == selected,
                    onClick = { viewModel.select(protocol) }
                )
            }
        }
    }
}

@Composable
private fun ProtocolRow(protocol: VpnProtocol, selected: Boolean, onClick: () -> Unit) {
    val (desc, tag) = when (protocol) {
        VpnProtocol.AUTO -> R.string.protocol_auto_desc to R.string.protocol_auto_tag
        VpnProtocol.VLESS_REALITY -> R.string.protocol_vless_desc to R.string.protocol_vless_tag
        VpnProtocol.HYSTERIA2 -> R.string.protocol_hysteria_desc to null
        VpnProtocol.TROJAN -> R.string.protocol_trojan_desc to null
        VpnProtocol.SHADOWSOCKS -> R.string.protocol_ss_desc to null
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .voidCard(selected = selected)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        VoidRadio(selected)
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                protocol.label,
                style = if (protocol == VpnProtocol.AUTO) MaterialTheme.typography.labelLarge
                else VoidMono.value,
                color = VoidText.primary
            )
            Text(
                stringResource(desc),
                style = MaterialTheme.typography.labelMedium,
                color = VoidText.tertiary
            )
            if (tag != null) {
                Text(
                    stringResource(tag),
                    modifier = Modifier.padding(top = 4.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = if (protocol == VpnProtocol.AUTO) VoidStatus.protected else VoidText.primary
                )
            }
        }
    }
}
