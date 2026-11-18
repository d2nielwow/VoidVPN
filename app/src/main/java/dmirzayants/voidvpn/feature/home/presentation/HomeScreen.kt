package dmirzayants.voidvpn.feature.home.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PowerSettingsNew
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
import dmirzayants.voidvpn.core.ui.VoidMoon
import dmirzayants.voidvpn.feature.vpn.domain.FailReason
import dmirzayants.voidvpn.feature.vpn.domain.Server
import dmirzayants.voidvpn.feature.vpn.domain.VpnProtocol
import dmirzayants.voidvpn.feature.vpn.domain.VpnStatus
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun HomeScreen(
    onTabSelected: (MainTab) -> Unit,
    onNavigateToServers: () -> Unit,
    onNavigateToPlan: () -> Unit,
    onNavigateToProtocol: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.onIntent(HomeIntent.PermissionResult(result.resultCode == Activity.RESULT_OK))
    }

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            is HomeEffect.RequestVpnPermission -> launcher.launch(effect.intent)
            HomeEffect.NavigateToServers -> onNavigateToServers()
            HomeEffect.NavigateToPlan -> onNavigateToPlan()
            HomeEffect.NavigateToProtocol -> onNavigateToProtocol()
        }
    }
    HomeContent(
        state = state,
        onIntent = viewModel::onIntent,
        onTabSelected = onTabSelected
    )
}

private data class HomeVisual(
    val ring: Color,
    val ringFraction: Float,
    val glow: Color?,
    val core: Color,
    val coreBorder: Color,
    val icon: Color,
    val accent: Color,
    val chipColor: Color,
    val chipDot: Color,
)

private fun visualFor(status: VpnStatus): HomeVisual = when (status) {
    VpnStatus.Disconnected -> HomeVisual(
        ring = VoidOutline.default, ringFraction = 1f, glow = null,
        core = VoidSurface.base, coreBorder = Color(0xFF232327),
        icon = VoidText.primary, accent = VoidText.primary,
        chipColor = VoidText.tertiary, chipDot = VoidText.disabled
    )

    is VpnStatus.Connecting -> HomeVisual(
        ring = VoidStatus.connecting, ringFraction = 0.265f,
        glow = VoidStatus.connecting.copy(alpha = 0.15f),
        core = VoidSurface.base, coreBorder = Color(0xFF3A2F14),
        icon = VoidStatus.connecting, accent = VoidStatus.connecting,
        chipColor = VoidStatus.connecting, chipDot = VoidStatus.connecting
    )

    is VpnStatus.Connected -> HomeVisual(
        ring = VoidStatus.protected, ringFraction = 1f,
        glow = VoidStatus.protected.copy(alpha = 0.35f),
        core = VoidStatus.protected, coreBorder = VoidStatus.protected,
        icon = Color.Black, accent = VoidStatus.protected,
        chipColor = VoidStatus.protected, chipDot = VoidStatus.protected
    )

    is VpnStatus.Failed -> HomeVisual(
        ring = VoidStatus.error, ringFraction = 1f,
        glow = VoidStatus.error.copy(alpha = 0.18f),
        core = VoidSurface.base, coreBorder = Color(0xFF4A1C22),
        icon = VoidStatus.error, accent = VoidStatus.error,
        chipColor = VoidStatus.error, chipDot = VoidStatus.error
    )
}

@Composable
fun HomeContent(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit,
    onTabSelected: (MainTab) -> Unit
) {
    val visual = visualFor(state.status)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .statusBarsPadding()
    ) {
        HomeHeader(state.status, visual)

        if (state.showBanner) {
            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 14.dp)
                    .fillMaxWidth()
                    .background(VoidStatus.connecting.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
                    .border(1.dp, VoidStatus.connecting.copy(alpha = 0.22f), RoundedCornerShape(16.dp))
                    .clickable { onIntent(HomeIntent.RenewClicked) }
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    stringResource(R.string.home_banner_text),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFE6D3A8)
                )
                Text(
                    stringResource(R.string.home_banner_renew),
                    style = MaterialTheme.typography.bodySmall,
                    color = VoidStatus.connecting
                )
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PowerButton(visual, state.status) { onIntent(HomeIntent.ToggleClicked) }
            Spacer(Modifier.height(18.dp))
            Text(
                titleFor(state.status),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                color = visual.accent,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(6.dp))
            val sub = subFor(state)
            Text(
                sub,
                style = if (state.status is VpnStatus.Connected) VoidMono.meta.copy(fontSize = 13.sp)
                else MaterialTheme.typography.bodySmall,
                color = VoidText.tertiary,
                textAlign = TextAlign.Center
            )
        }

        ServerCard(state) { onIntent(HomeIntent.ServerClicked) }

        if (state.status is VpnStatus.Failed) {
            ErrorHint(onChange = { onIntent(HomeIntent.ChangeProtocolClicked) })
        } else {
            StatsRow(state)
        }

        Spacer(Modifier.height(14.dp))
        VoidBottomBar(selected = MainTab.Home, onSelect = onTabSelected)
    }
}

@Composable
private fun HomeHeader(status: VpnStatus, visual: HomeVisual) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VoidMoon(size = 26.dp, cutColor = VoidBlack)
            Text(
                "void",
                style = MaterialTheme.typography.titleSmall.copy(fontSize = 20.sp),
                color = VoidText.primary
            )
        }
        Row(
            modifier = Modifier
                .background(VoidSurface.base, CircleShape)
                .border(1.dp, VoidOutline.subtle, CircleShape)
                .padding(horizontal = 12.dp, vertical = 7.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                Modifier
                    .size(7.dp)
                    .background(visual.chipDot, CircleShape)
            )
            Text(
                chipFor(status),
                style = MaterialTheme.typography.labelMedium,
                color = visual.chipColor
            )
        }
    }
}

@Composable
private fun PowerButton(visual: HomeVisual, status: VpnStatus, onClick: () -> Unit) {
    val transition = rememberInfiniteTransition(label = "spin")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1_400, easing = LinearEasing), RepeatMode.Restart),
        label = "angle"
    )
    val spinning = status is VpnStatus.Connecting

    Box(
        modifier = Modifier.size(250.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .rotate(if (spinning) angle - 90f else -90f)
        ) {
            val stroke = 3.dp.toPx()
            val inset = stroke / 2
            val arcSize = Size(size.width - stroke, size.height - stroke)
            drawArc(
                color = Color(0xFF1A1A1D),
                startAngle = 0f, sweepAngle = 360f, useCenter = false,
                topLeft = Offset(inset, inset), size = arcSize,
                style = Stroke(width = stroke)
            )
            drawArc(
                color = visual.ring,
                startAngle = 0f, sweepAngle = 360f * visual.ringFraction, useCenter = false,
                topLeft = Offset(inset, inset), size = arcSize,
                style = Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
        Box(
            modifier = Modifier
                .size(184.dp)
                .drawBehind {
                    visual.glow?.let { glow ->
                        drawCircle(
                            brush = Brush.radialGradient(
                                0.5f to glow,
                                1f to Color.Transparent,
                                radius = size.minDimension * 0.95f
                            ),
                            radius = size.minDimension * 0.95f
                        )
                    }
                }
                .background(visual.core, CircleShape)
                .border(2.dp, visual.coreBorder, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.PowerSettingsNew,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = visual.icon
            )
        }
    }
}

@Composable
private fun ServerCard(state: HomeState, onClick: () -> Unit) {
    val server = state.server
    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .background(VoidSurface.base, RoundedCornerShape(20.dp))
            .border(1.dp, VoidOutline.subtle, RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (server != null) VoidFlag(server.countryCode, 40.dp) else Spacer(Modifier.size(40.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                server?.country ?: stringResource(R.string.home_no_server),
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                color = VoidText.primary
            )
            if (server != null) {
                Text(
                    "${server.city.substringBefore(" ·")} · ${state.protocol.label}",
                    style = VoidMono.log.copy(fontSize = 11.sp),
                    color = VoidText.tertiary
                )
            }
        }
        server?.pingMs?.let { ping ->
            Text(
                stringResource(R.string.servers_ping_value, ping),
                style = VoidMono.meta.copy(fontSize = 13.sp),
                color = pingColor(ping)
            )
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = VoidText.disabled
        )
    }
}

@Composable
private fun StatsRow(state: HomeState) {
    val connected = state.status as? VpnStatus.Connected
    val time = if (state.status is VpnStatus.Connected || state.status is VpnStatus.Connecting)
        formatElapsed(state.elapsedSeconds) else "—"
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatCell(stringResource(R.string.home_stat_time), time, null, Modifier.weight(1f))
        StatCell(
            stringResource(R.string.home_stat_down),
            connected?.let { "%.1f".format(it.downMbps) } ?: "—",
            connected?.let { stringResource(R.string.home_unit_mbps) },
            Modifier.weight(1f)
        )
        StatCell(
            stringResource(R.string.home_stat_up),
            connected?.let { "%.1f".format(it.upMbps) } ?: "—",
            connected?.let { stringResource(R.string.home_unit_mbps) },
            Modifier.weight(1f)
        )
    }
}

@Composable
private fun StatCell(label: String, value: String, unit: String?, modifier: Modifier) {
    Column(
        modifier = modifier
            .background(VoidSurface.base, RoundedCornerShape(16.dp))
            .border(1.dp, VoidOutline.subtle, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 11.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = VoidText.tertiary)
        Spacer(Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, style = VoidMono.value, color = VoidText.primary, maxLines = 1)
            if (unit != null) {
                Text(
                    " $unit",
                    style = VoidMono.log.copy(fontSize = 10.sp),
                    color = VoidText.disabled,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
private fun ErrorHint(onChange: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            .fillMaxWidth()
            .background(VoidStatus.error.copy(alpha = 0.07f), RoundedCornerShape(18.dp))
            .border(1.dp, VoidStatus.error.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Text(
            stringResource(R.string.home_hint_text),
            modifier = Modifier.padding(horizontal = 2.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFF2B8BE)
        )
        Row(
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp)
                    .background(VoidAction, RoundedCornerShape(12.dp))
                    .clickable(onClick = onChange),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.home_hint_change),
                    style = MaterialTheme.typography.labelMedium,
                    color = VoidText.onLight
                )
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .background(VoidSurface.cardAlt, RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    stringResource(R.string.home_hint_report),
                    style = MaterialTheme.typography.labelMedium,
                    color = VoidText.primary
                )
            }
        }
    }
}

@Composable
private fun titleFor(status: VpnStatus): String = stringResource(
    when (status) {
        VpnStatus.Disconnected -> R.string.home_off_title
        is VpnStatus.Connecting -> R.string.home_connecting_title
        is VpnStatus.Connected -> R.string.home_on_title
        is VpnStatus.Failed -> R.string.home_error_title
    }
)

@Composable
private fun chipFor(status: VpnStatus): String = stringResource(
    when (status) {
        VpnStatus.Disconnected -> R.string.home_off_chip
        is VpnStatus.Connecting -> R.string.home_connecting_chip
        is VpnStatus.Connected -> R.string.home_on_chip
        is VpnStatus.Failed -> R.string.home_error_chip
    }
)

@Composable
private fun subFor(state: HomeState): String = when (val s = state.status) {
    VpnStatus.Disconnected -> stringResource(R.string.home_off_sub)
    is VpnStatus.Connecting -> stringResource(R.string.home_connecting_sub, s.protocol.label)
    is VpnStatus.Connected -> stringResource(R.string.home_on_sub, s.ip)
    is VpnStatus.Failed -> stringResource(
        when (s.reason) {
            FailReason.TIMEOUT -> R.string.home_error_timeout
            FailReason.BLOCKED -> R.string.home_error_blocked
            FailReason.UNKNOWN -> R.string.home_error_unknown
        }
    )
}

private fun formatElapsed(seconds: Long): String =
    "%02d:%02d:%02d".format(seconds / 3600, (seconds / 60) % 60, seconds % 60)

internal fun pingColor(ping: Int): Color = when {
    ping < 60 -> VoidStatus.protected
    ping < 100 -> VoidStatus.connecting
    else -> VoidStatus.error
}

private val previewServer = Server("nl", "nl", "Нидерланды", "Амстердам", 38, 65)

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun HomeConnectedPreview() {
    VoidVPNTheme {
        HomeContent(
            state = HomeState(
                status = VpnStatus.Connected(0, "185.107.56.12", 48.2, 12.6),
                server = previewServer,
                protocol = VpnProtocol.VLESS_REALITY,
                elapsedSeconds = 5047
            ),
            onIntent = {}, onTabSelected = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun HomeOffPreview() {
    VoidVPNTheme {
        HomeContent(
            state = HomeState(server = previewServer, showBanner = true),
            onIntent = {}, onTabSelected = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun HomeErrorPreview() {
    VoidVPNTheme {
        HomeContent(
            state = HomeState(
                status = VpnStatus.Failed(FailReason.TIMEOUT),
                server = previewServer
            ),
            onIntent = {}, onTabSelected = {}
        )
    }
}
