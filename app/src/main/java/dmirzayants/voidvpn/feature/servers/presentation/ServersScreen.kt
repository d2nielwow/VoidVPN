package dmirzayants.voidvpn.feature.servers.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.ui.MainTab
import dmirzayants.voidvpn.core.ui.VoidBottomBar
import dmirzayants.voidvpn.core.ui.VoidFlag
import dmirzayants.voidvpn.feature.home.presentation.pingColor
import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidTextSoft
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun ServersScreen(
    onTabSelected: (MainTab) -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: ServersViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            ServersEffect.NavigateToHome -> onNavigateToHome()
        }
    }
    ServersContent(state, viewModel::onIntent, onTabSelected)
}

@Composable
fun ServersContent(
    state: ServersState,
    onIntent: (ServersIntent) -> Unit,
    onTabSelected: (MainTab) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                stringResource(R.string.servers_title),
                style = MaterialTheme.typography.headlineMedium,
                color = VoidText.primary
            )
            Text(
                stringResource(R.string.servers_ping),
                modifier = Modifier
                    .background(VoidSurface.cardAlt, CircleShape)
                    .clickable { onIntent(ServersIntent.RefreshPing) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = VoidText.tertiary
            )
        }

        Row(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 14.dp)
                .fillMaxWidth()
                .background(VoidSurface.cardAlt, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                Icons.Rounded.Search, contentDescription = null,
                modifier = Modifier.size(18.dp), tint = VoidText.disabled
            )
            Box(modifier = Modifier.weight(1f)) {
                if (state.query.isEmpty()) {
                    Text(
                        stringResource(R.string.servers_search),
                        style = MaterialTheme.typography.bodyMedium,
                        color = VoidText.disabled
                    )
                }
                BasicTextField(
                    value = state.query,
                    onValueChange = { onIntent(ServersIntent.QueryChanged(it)) },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodyMedium.copy(color = VoidText.primary),
                    cursorBrush = SolidColor(VoidText.primary),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                stringResource(R.string.servers_filter_all, state.totalCount),
                selected = state.filter == ServersFilter.ALL
            ) { onIntent(ServersIntent.FilterChanged(ServersFilter.ALL)) }
            FilterChip(
                stringResource(R.string.servers_filter_favorites, state.favoritesCount),
                selected = state.filter == ServersFilter.FAVORITES
            ) { onIntent(ServersIntent.FilterChanged(ServersFilter.FAVORITES)) }
        }

        AutoRow(state) { onIntent(ServersIntent.AutoClicked) }

        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 12.dp, end = 12.dp, top = 6.dp
            )
        ) {
            items(state.servers, key = { it.id }) { server ->
                ServerRow(
                    server = server,
                    selected = !state.isAuto && state.selectedId == server.id,
                    onClick = { onIntent(ServersIntent.ServerClicked(server.id)) },
                    onFavorite = { onIntent(ServersIntent.FavoriteClicked(server.id)) }
                )
            }
        }
        VoidBottomBar(selected = MainTab.Servers, onSelect = onTabSelected)
    }
}

@Composable
private fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text,
        modifier = Modifier
            .background(if (selected) VoidAction else VoidSurface.cardAlt, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            fontSize = 13.sp
        ),
        color = if (selected) VoidText.onLight else VoidTextSoft
    )
}

@Composable
private fun AutoRow(state: ServersState, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 14.dp)
            .fillMaxWidth()
            .background(VoidSurface.base, RoundedCornerShape(18.dp))
            .border(
                if (state.isAuto) 1.5.dp else 1.dp,
                if (state.isAuto) VoidAction else VoidOutline.subtle,
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .background(VoidAction, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("A", style = MaterialTheme.typography.labelLarge, color = VoidText.onLight)
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                stringResource(R.string.servers_auto),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                color = VoidText.primary
            )
            Text(
                stringResource(R.string.servers_auto_hint, state.bestServerName),
                style = MaterialTheme.typography.labelSmall,
                color = VoidText.tertiary
            )
        }
        RadioDot(selected = state.isAuto)
    }
}

@Composable
private fun RadioDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .border(
                if (selected) 6.dp else 2.dp,
                if (selected) VoidAction else VoidOutline.strong,
                CircleShape
            )
    )
}

@Composable
private fun ServerRow(
    server: Server,
    selected: Boolean,
    onClick: () -> Unit,
    onFavorite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (server.available) 1f else 0.4f)
            .clickable(enabled = server.available, onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        VoidFlag(server.countryCode, 38.dp)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                server.country,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                color = VoidText.primary
            )
            Text(
                server.city,
                style = MaterialTheme.typography.labelSmall,
                color = VoidText.tertiary
            )
        }
        Column(
            modifier = Modifier.width(64.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            val ping = server.pingMs
            Text(
                text = when {
                    !server.available -> "—"
                    ping == null -> stringResource(R.string.servers_ping_measuring)
                    else -> stringResource(R.string.servers_ping_value, ping)
                },
                style = VoidMono.meta,
                color = if (ping != null && server.available) pingColor(ping) else VoidText.disabled
            )
            Box(
                Modifier
                    .width(40.dp)
                    .height(3.dp)
                    .background(VoidOutline.subtle, CircleShape)
            ) {
                Box(
                    Modifier
                        .fillMaxWidth(server.load / 100f)
                        .height(3.dp)
                        .background(VoidText.disabled, CircleShape)
                )
            }
        }
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            modifier = Modifier
                .size(22.dp)
                .clickable(onClick = onFavorite),
            tint = if (server.favorite) VoidText.primary else VoidOutline.default
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun ServersPreview() {
    VoidVPNTheme {
        ServersContent(
            state = ServersState(
                servers = listOf(
                    Server("fi", "fi", "Финляндия", "Хельсинки", 29, 40, favorite = true),
                    Server("nl", "nl", "Нидерланды", "Амстердам", 38, 65),
                    Server("kz", "kz", "Казахстан", "Алматы · недоступен", null, 0, available = false),
                ),
                totalCount = 24, favoritesCount = 2, bestServerName = "Финляндия"
            ),
            onIntent = {}, onTabSelected = {}
        )
    }
}
