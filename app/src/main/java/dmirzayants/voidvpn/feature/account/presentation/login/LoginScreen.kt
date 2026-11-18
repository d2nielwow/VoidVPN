package dmirzayants.voidvpn.feature.account.presentation.login

import android.content.ClipboardManager
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.QrCode2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToImport: () -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    CollectEffects(viewModel.effects) { effect ->
        when(effect) {
            LoginEffect.RequestPaste -> {
                val clipboard = context.getSystemService(ClipboardManager::class.java)
                val text =
                    clipboard.primaryClip?.getItemAt(0)?.text?.toString().orEmpty()
                viewModel.onIntent(LoginIntent.InputChanged(text))
            }

            LoginEffect.NavigateToHome -> onNavigateToHome()
            LoginEffect.NavigateToImport -> onNavigateToImport()
            LoginEffect.NavigateBack -> onNavigateBack()
        }
    }
    LoginContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun LoginContent(
    state: LoginState,
    onIntent: (LoginIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .padding(start = 16.dp, top = 12.dp)
                .size(44.dp)
                .background(VoidSurface.cardAlt, CircleShape)
                .clickable { onIntent(LoginIntent.BackClicked) },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.login_back),
                tint = VoidText.primary
            )
        }
        Column(
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 20.dp)
        ) {
            Text(
                stringResource(R.string.login_title),
                style = MaterialTheme.typography.headlineLarge,
                color = VoidText.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.login_text),
                style = MaterialTheme.typography.bodyLarge,
                color = VoidText.secondary
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 26.dp)
                .fillMaxWidth()
                .border(
                    2.dp,
                    if (state.isError) VoidStatus.error else VoidAction,
                    RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BasicTextField(
                value = state.input,
                onValueChange = { onIntent(LoginIntent.InputChanged(it)) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = VoidMono.value.copy(
                    fontSize = 18.sp,
                    letterSpacing = 1.sp,
                    color = VoidText.primary
                ),
                cursorBrush = SolidColor(VoidStatus.protected)
            )
            Text(
                stringResource(R.string.login_paste),
                modifier = Modifier
                    .background(VoidSurface.cardAlt, RoundedCornerShape(10.dp))
                    .clickable { onIntent(LoginIntent.PasteClicked) }
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = VoidText.primary
            )
        }
        Text(
            stringResource(if (state.isError) R.string.login_error else R.string.login_format),
            modifier = Modifier.padding(start = 28.dp, end = 28.dp, top = 12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = if (state.isError) VoidStatus.error else VoidText.disabled
        )
        Row(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 30.dp)
                .fillMaxWidth()
                .background(VoidSurface.base, RoundedCornerShape(20.dp))
                .border(1.dp, VoidOutline.subtle, RoundedCornerShape(20.dp))
                .clickable { onIntent(LoginIntent.ImportClicked) }
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
                Icon(
                    imageVector = Icons.Rounded.QrCode2,
                    contentDescription = null,
                    tint = VoidText.primary
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.login_import_title),
                    style = MaterialTheme.typography.titleSmall.copy(fontSize = 15.sp),
                    color = VoidText.primary
                )
                Text(
                    stringResource(R.string.login_import_text),
                    style = MaterialTheme.typography.labelMedium,
                    color = VoidText.tertiary
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                contentDescription = null,
                tint = VoidText.disabled
            )
        }
        Spacer(Modifier.weight(1f))
        Button(
            onClick = { onIntent(LoginIntent.SubmitClicked) },
            enabled = state.canSubmit,
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, bottom = 34.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VoidAction,
                contentColor = VoidText.onLight,
                disabledContainerColor = VoidOutline.default,
                disabledContentColor = VoidText.disabled
            )
        ) {
            Text(
                stringResource(R.string.login_submit),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun LoginContentPreview() {
    VoidVPNTheme {
        LoginContent(state = LoginState(input = "VOID7F3KQ9LM"), onIntent = {})
    }
}
