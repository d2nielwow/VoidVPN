package dmirzayants.voidvpn.feature.account.presentation.start

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.ui.VoidMoon
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun StartScreen(
    onNavigateToKeyCreated: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: StartViewModel = hiltViewModel()
) {
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            StartEffect.NavigateToKeyCreated -> onNavigateToKeyCreated()
            StartEffect.NavigateToLogin -> onNavigateToLogin()
        }
    }
    StartContent(onIntent = viewModel::onIntent)
}

@Composable
fun StartContent(
    onIntent: (StartIntent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            VoidMoon(
                size = 72.dp,
                cutColor = VoidBlack
            )
            Spacer(Modifier.height(30.dp))
            Text(
                stringResource(R.string.start_title_1),
                style = MaterialTheme.typography.displaySmall,
                color = VoidText.primary
            )
            Text(
                stringResource(R.string.start_title_2),
                style = MaterialTheme.typography.displaySmall,
                color = VoidText.disabled
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, bottom = 34.dp)
        ) {
            Button(
                onClick = { onIntent(StartIntent.CreateAccount) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VoidAction,
                    contentColor = VoidText.onLight
                )
            ) {
                Text(
                    stringResource(R.string.start_create),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { onIntent(StartIntent.HaveKey) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, VoidOutline.default),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VoidSurface.cardAlt,
                    contentColor = VoidText.primary
                )
            ) {
                Text(
                    stringResource(R.string.start_have_key),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(Modifier.height(18.dp))
            Text(
                stringResource(R.string.start_terms),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = VoidText.disabled
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun StartScreenPreview() {
    VoidVPNTheme {
        StartContent(onIntent = {})
    }
}
