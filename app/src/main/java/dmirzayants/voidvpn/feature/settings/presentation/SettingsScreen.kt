package dmirzayants.voidvpn.feature.settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.ui.MainTab
import dmirzayants.voidvpn.core.ui.VoidBottomBar
import dmirzayants.voidvpn.core.ui.VoidSwitch
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun SettingsScreen(
    onTabSelected: (MainTab) -> Unit,
    onNavigateToProtocol: () -> Unit,
    onNavigateToSplitTunneling: () -> Unit,
    onNavigateToLogs: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            SettingsEffect.NavigateToProtocol -> onNavigateToProtocol()
            SettingsEffect.NavigateToSplitTunneling -> onNavigateToSplitTunneling()
            SettingsEffect.NavigateToLogs -> onNavigateToLogs()
        }
    }
    SettingsContent(state, viewModel::onIntent, onTabSelected)
}

@Composable
fun SettingsContent(
    state: SettingsState,
    onIntent: (SettingsIntent) -> Unit,
    onTabSelected: (MainTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .statusBarsPadding()
    ) {
        Text(
            stringResource(R.string.settings_title),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = VoidText.primary
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Group(R.string.settings_group_connection) {
                SettingRow(
                    stringResource(R.string.settings_protocol),
                    value = state.protocol.label,
                    onClick = { onIntent(SettingsIntent.ProtocolClicked) }
                )
                SettingRow(
                    stringResource(R.string.settings_split),
                    value = stringResource(R.string.settings_split_value, state.splitCount),
                    onClick = { onIntent(SettingsIntent.SplitTunnelingClicked) }
                )
                SettingRow(
                    stringResource(R.string.settings_killswitch),
                    sub = stringResource(R.string.settings_killswitch_sub),
                    checked = state.settings.killSwitch,
                    onChecked = { onIntent(SettingsIntent.KillSwitchChanged(it)) }
                )
                SettingRow(
                    stringResource(R.string.settings_autoconnect),
                    sub = stringResource(R.string.settings_autoconnect_sub),
                    checked = state.settings.autoConnect,
                    onChecked = { onIntent(SettingsIntent.AutoConnectChanged(it)) }
                )
                SettingRow(
                    stringResource(R.string.settings_boot),
                    checked = state.settings.launchOnBoot,
                    onChecked = { onIntent(SettingsIntent.LaunchOnBootChanged(it)) }
                )
            }
            Group(R.string.settings_group_network) {
                SettingRow(
                    stringResource(R.string.settings_dns),
                    value = stringResource(R.string.settings_dns_value)
                )
            }
            Group(R.string.settings_group_app) {
                SettingRow(
                    stringResource(R.string.settings_language),
                    value = stringResource(R.string.settings_language_value)
                )
                SettingRow(
                    stringResource(R.string.settings_theme),
                    value = stringResource(R.string.settings_theme_value)
                )
            }
            Group(R.string.settings_group_help) {
                SettingRow(stringResource(R.string.settings_support))
                SettingRow(
                    stringResource(R.string.settings_logs),
                    onClick = { onIntent(SettingsIntent.LogsClicked) }
                )
            }
            Box(Modifier.height(16.dp))
        }
        VoidBottomBar(selected = MainTab.Settings, onSelect = onTabSelected)
    }
}

@Composable
private fun Group(titleRes: Int, content: @Composable () -> Unit) {
    Text(
        stringResource(titleRes),
        modifier = Modifier.padding(start = 6.dp, top = 16.dp, bottom = 8.dp),
        style = MaterialTheme.typography.labelMedium,
        color = VoidText.disabled
    )
    Column(modifier = Modifier
        .fillMaxWidth()
        .voidCard()) {
        content()
    }
}

@Composable
private fun SettingRow(
    label: String,
    sub: String? = null,
    value: String? = null,
    checked: Boolean? = null,
    onChecked: (Boolean) -> Unit = {},
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.bodyMedium, color = VoidText.primary)
            if (sub != null) {
                Text(sub, style = MaterialTheme.typography.labelSmall, color = VoidText.tertiary)
            }
        }
        if (value != null) {
            Text(value, style = MaterialTheme.typography.bodySmall, color = VoidText.tertiary)
        }
        if (checked != null) {
            VoidSwitch(checked = checked, onCheckedChange = onChecked)
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = VoidOutline.handle
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun SettingsPreview() {
    VoidVPNTheme { SettingsContent(SettingsState(), {}, {}) }
}
