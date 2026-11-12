package dmirzayants.voidvpn.feature.onboarding.presentation

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.LaunchedEffect
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
import dmirzayants.voidvpn.feature.onboarding.presentation.components.OnboardingRings
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun OnboardingScreen(
    onNavigateToPermission: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            OnboardingEffect.NavigateToPermission -> onNavigateToPermission()
        }
    }
    OnboardingContent(state = state, onIntent = viewModel::onIntent)
}

@Composable
fun OnboardingContent(
    state: OnboardingState,
    onIntent: (OnboardingIntent) -> Unit
) {

    val pagerState = rememberPagerState(initialPage = state.page) { onboardingSlides.size }
    val isLast = state.page == onboardingSlides.lastIndex

    // ViewModel → Pager: нажали «Далее», пейджер доезжает до нужной страницы
    LaunchedEffect(state.page) {
        pagerState.animateScrollToPage(state.page)
    }
    // Pager → ViewModel: пролистали пальцем, сообщаем состоянию
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .collect { onIntent(OnboardingIntent.PageChanged(it)) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 22.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (!isLast) {
                Text(
                    text = stringResource(R.string.onboarding_skip),
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoidText.secondary,
                    modifier = Modifier.clickable { onIntent(OnboardingIntent.Skip) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) { page ->
            val slide = onboardingSlides[page]
            Column(Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    OnboardingRings(slide)
                }
                Column(Modifier.padding(horizontal = 28.dp)) {
                    Text(
                        text = stringResource(slide.title),
                        style = MaterialTheme.typography.headlineLarge,
                        color = VoidText.primary
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = stringResource(slide.text),
                        style = MaterialTheme.typography.bodyLarge,
                        color = VoidText.secondary
                    )
                }
            }
        }
        Row(
            modifier = Modifier.padding(start = 28.dp, top = 28.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            repeat(onboardingSlides.size) { index ->
                Box(
                    Modifier
                        .height(6.dp)
                        .width(if (index == state.page) 28.dp else 6.dp)
                        .background(
                            if (index == state.page) VoidText.primary else VoidOutline.default,
                            CircleShape
                        )
                )
            }
        }
        Box(
            modifier = Modifier
                .padding(start = 22.dp, end = 22.dp, top = 24.dp, bottom = 34.dp)
                .fillMaxWidth()
                .height(56.dp)
                .background(VoidAction, RoundedCornerShape(18.dp))
                .clickable { onIntent(OnboardingIntent.Next) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(
                    if (isLast) R.string.onboarding_start else R.string.onboarding_next
                ),
                style = MaterialTheme.typography.labelLarge,
                color = VoidText.onLight
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun OnboardingContentPreview() {
    VoidVPNTheme {
        OnboardingContent(state = OnboardingState(page = 0), onIntent = {})
    }
}