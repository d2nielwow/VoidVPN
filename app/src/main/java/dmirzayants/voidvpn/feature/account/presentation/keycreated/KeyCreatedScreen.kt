package dmirzayants.voidvpn.feature.account.presentation.keycreated

import android.content.ClipData
import android.content.ClipboardManager
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun KeyCreatedScreen(
    onNavigateToHome: () -> Unit,
    viewModel: KeyCreatedViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            is KeyCreatedEffect.CopyToClipboard -> {
                val clipboard = context.getSystemService(ClipboardManager::class.java)
                clipboard.setPrimaryClip(ClipData.newPlainText("Void key", effect.text))
                Toast.makeText(context, R.string.key_copied, Toast.LENGTH_SHORT).show()
            }

            KeyCreatedEffect.NavigateToHome -> onNavigateToHome()
        }
    }
    KeyCreatedContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun KeyCreatedContent(
    state: KeyCreatedState,
    onIntent: (KeyCreatedIntent) -> Unit
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
                stringResource(R.string.key_title),
                style = MaterialTheme.typography.headlineLarge,
                color = VoidText.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.key_text),
                style = MaterialTheme.typography.bodyLarge,
                color = VoidText.secondary
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 28.dp)
                .fillMaxWidth()
                .background(VoidSurface.base, RoundedCornerShape(24.dp))
                .border(1.dp, VoidOutline.subtle, RoundedCornerShape(24.dp))
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            state.keyParts.chunked(2).forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    pair.forEach { part ->
                        KeyPart(
                            part, Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onIntent(KeyCreatedIntent.CopyClicked) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = VoidText.onLight,
                        containerColor = VoidAction
                    )
                ) {
                    Text(
                        stringResource(R.string.key_copy),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Button(
                    onClick = { onIntent(KeyCreatedIntent.SaveQrClicked) },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = VoidSurface.cardAlt,
                        contentColor = VoidText.primary
                    )
                ) {
                    Text(
                        stringResource(R.string.key_save_qr),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 14.dp)
                .fillMaxWidth()
                .background(
                    VoidStatus.connecting.copy(alpha = 0.08f),
                    RoundedCornerShape(18.dp)
                )
                .border(
                    1.dp, VoidStatus.connecting.copy(alpha = 0.25f),
                    RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("!", color = VoidStatus.connecting, style = MaterialTheme.typography.labelLarge)
            Text(
                stringResource(R.string.key_warning),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE6D3A8)
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { onIntent(KeyCreatedIntent.Confirm) },
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
                stringResource(R.string.key_saved),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun KeyPart(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(VoidSurface.cardAlt, RoundedCornerShape(14.dp))
            .padding(vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            style = VoidMono.key,
            color = VoidText.primary
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun KeyCreatedScreenPreview() {
    VoidVPNTheme {
        KeyCreatedContent(
            state = KeyCreatedState(keyParts = listOf("VOID", "7F3K", "Q9LM", "2XPA")),
            onIntent = {}
        )
    }
}