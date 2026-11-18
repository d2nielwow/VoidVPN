package dmirzayants.voidvpn.feature.account.presentation.importsub

import android.content.ClipboardManager
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.ui.VoidBackButton
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidTextSoft
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun ImportScreen(
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: ImportViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val clipboard = context.getSystemService(ClipboardManager::class.java)
        val text = clipboard.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
        viewModel.onIntent(ImportIntent.ClipboardRead(text))
    }
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            ImportEffect.NavigateBack -> onNavigateBack()
            ImportEffect.NavigateToHome -> onNavigateToHome()
        }
    }
    ImportContent(state, viewModel::onIntent)
}

@Composable
fun ImportContent(
    state: ImportState,
    onIntent: (ImportIntent) -> Unit
) {
    val detected = state.detected
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        VoidBackButton(
            onClick = { onIntent(ImportIntent.BackClicked) },
            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
        )
        Text(
            stringResource(R.string.import_title),
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 20.dp),
            style = MaterialTheme.typography.headlineLarge,
            color = VoidText.primary
        )

        if (detected != null) {
            val mint = VoidStatus.protected
            Column(
                modifier = Modifier
                    .padding(start = 22.dp, end = 22.dp, top = 22.dp)
                    .fillMaxWidth()
                    .background(mint.copy(alpha = 0.06f), RoundedCornerShape(22.dp))
                    .border(1.5.dp, mint.copy(alpha = 0.45f), RoundedCornerShape(22.dp))
                    .padding(18.dp)
            ) {
                if (state.clipboardDetected) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            Modifier
                                .size(8.dp)
                                .background(mint, CircleShape)
                        )
                        Text(
                            stringResource(R.string.import_found),
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                            color = mint
                        )
                    }
                }
                Text(
                    detected.raw.take(96) + if (detected.raw.length > 96) "…" else "",
                    modifier = Modifier.padding(top = 10.dp),
                    style = VoidMono.meta.copy(fontSize = 13.sp, lineHeight = 20.sp),
                    color = VoidTextSoft
                )
                FlowRow(
                    modifier = Modifier.padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val label = detected.protocol?.label
                        ?: stringResource(R.string.import_subscription_label)
                    Chip(label)
                    detected.host?.let { Chip(it) }
                }
                Box(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(mint, RoundedCornerShape(14.dp))
                        .clickable { onIntent(ImportIntent.ImportClicked) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        stringResource(R.string.import_action),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                        color = VoidText.onLight
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(start = 22.dp, end = 22.dp, top = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .voidCard()
                    .clickable { onIntent(ImportIntent.ScanClicked) }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(VoidSurface.cardAlt, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Rounded.QrCodeScanner, contentDescription = null, tint = VoidText.primary)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(R.string.import_scan_title),
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                        color = VoidText.primary
                    )
                    Text(
                        stringResource(R.string.import_scan_text),
                        style = MaterialTheme.typography.labelMedium,
                        color = VoidText.tertiary
                    )
                }
                Icon(
                    Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                    contentDescription = null,
                    tint = VoidOutline.handle
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .voidCard()
                    .padding(16.dp)
            ) {
                Text(
                    stringResource(R.string.import_paste_title),
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                    color = VoidText.primary
                )
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxWidth()
                        .background(VoidSurface.cardAlt, RoundedCornerShape(12.dp))
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    if (state.input.isEmpty()) {
                        Text(
                            stringResource(R.string.import_paste_hint),
                            style = VoidMono.meta.copy(fontSize = 13.sp),
                            color = VoidText.disabled
                        )
                    }
                    BasicTextField(
                        value = state.input,
                        onValueChange = { onIntent(ImportIntent.InputChanged(it)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        textStyle = VoidMono.meta.copy(fontSize = 13.sp, color = VoidText.primary),
                        cursorBrush = SolidColor(VoidStatus.protected)
                    )
                }
                if (state.error) {
                    Text(
                        stringResource(R.string.import_invalid),
                        modifier = Modifier.padding(top = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = VoidStatus.error
                    )
                }
            }
        }
        Text(
            stringResource(R.string.import_formats),
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 16.dp),
            style = VoidMono.meta.copy(lineHeight = 19.sp),
            color = VoidText.disabled
        )
        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun Chip(text: String) {
    Text(
        text,
        modifier = Modifier
            .background(VoidSurface.cardAlt, RoundedCornerShape(8.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp),
        style = VoidMono.log.copy(fontSize = 11.sp),
        color = VoidText.primary
    )
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun ImportPreview() {
    VoidVPNTheme {
        ImportContent(
            ImportState(clipboard = "vless://8f2c-e91a@nl-ams.void.net:443?security=reality"),
            onIntent = {}
        )
    }
}
