package dmirzayants.voidvpn.feature.permission.presentation

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.ui.VoidErrorSheet
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun PermissionScreen(
    onNavigateToHome: () -> Unit,
    viewModel: PermissionViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.onIntent(
            PermissionIntent.PermissionResult(result.resultCode == Activity.RESULT_OK)
        )
    }

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            is PermissionEffect.RequestVpnPermission -> launcher.launch(effect.intent)
            PermissionEffect.NavigateToHome -> onNavigateToHome()
        }
    }
    PermissionContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun PermissionContent(
    state: PermissionState,
    onIntent: (PermissionIntent) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 36.dp)
        ) {
            Text(
                stringResource(R.string.permission_step),
                style = VoidMono.meta,
                color = VoidText.tertiary
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                stringResource(R.string.permission_title),
                style = MaterialTheme.typography.headlineLarge,
                color = VoidText.primary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                stringResource(R.string.permission_text),
                style = MaterialTheme.typography.bodyLarge,
                color = VoidText.secondary
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 28.dp, end = 28.dp, top = 30.dp)
                .fillMaxWidth()
                .background(VoidSurface.raised, RoundedCornerShape(26.dp))
                .padding(start = 22.dp, end = 22.dp, top = 22.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VpnKeyMoon()
                Text(
                    stringResource(R.string.permission_card_title),
                    style = MaterialTheme.typography.labelLarge,
                    color = VoidText.primary
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                stringResource(R.string.permission_card_text),
                style = MaterialTheme.typography.bodySmall,
                color = VoidText.secondary
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.permission_card_cancel),
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoidText.tertiary,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                )
                Text(
                    stringResource(R.string.permission_card_ok),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                    modifier = Modifier
                        .background(VoidStatus.protected, CircleShape)
                        .padding(horizontal = 18.dp, vertical = 10.dp)
                )
            }
        }
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 28.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            PermissionPoint(stringResource(R.string.permission_point_1))
            PermissionPoint(stringResource(R.string.permission_point_2))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = { onIntent(PermissionIntent.ContinueClicked) },
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, bottom = 34.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VoidAction,
                contentColor = VoidText.onLight
            )
        ) {
            Text(
                stringResource(R.string.permission_continue),
                style = MaterialTheme.typography.labelLarge
            )
        }
        if (state.denied) {
            VoidErrorSheet(
                glyph = "x",
                accent = VoidStatus.error,
                iconBackground = VoidStatus.error.copy(alpha = 0.12f),
                title = stringResource(R.string.permission_denied_title),
                text = stringResource(R.string.permission_denied_text),
                primaryLabel = stringResource(R.string.permission_denied_retry),
                secondaryLabel = stringResource(R.string.permission_denied_why),
                onPrimary = {onIntent(PermissionIntent.ContinueClicked)},
                onSecondary = {},
                onDismiss = {onIntent(PermissionIntent.DismissDenied)}
            )
        }
    }
}


@Composable
private fun VpnKeyMoon() {
    Box(
        modifier = Modifier
            .size(26.dp)
            .clip(CircleShape)
            .background(VoidText.primary)
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .align(Alignment.TopEnd)
                .offset(x = 4.dp, y = (-2).dp)
                .background(VoidSurface.raised, CircleShape)
        )
    }
}

@Composable
private fun PermissionPoint(text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("●", color = VoidStatus.protected)
        Text(
            text, style = MaterialTheme.typography.bodyMedium,
            color = VoidText.secondary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PermissionContentPreview() {
    VoidVPNTheme {
        PermissionContent(state = PermissionState(), onIntent = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun PreviewPermissionErrorSheet() {
    VoidVPNTheme {
        PermissionContent(state = PermissionState(denied = true), onIntent = {})
    }
}