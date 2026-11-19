package dmirzayants.voidvpn.feature.billing.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.core.ui.VoidPrimaryButton
import dmirzayants.voidvpn.core.ui.VoidSecondaryButton
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.feature.billing.domain.BillingCatalog
import dmirzayants.voidvpn.feature.billing.domain.BillingRepository
import dmirzayants.voidvpn.navigation.PaymentStatusRoute
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentStatusState(
    val secondsLeft: Int = 180,
    val step: Int = 1,
    val invoice: String = "#A-48213",
    val summary: String = "",
) : UiState {
    val done: Boolean get() = step >= 2
}

sealed interface PaymentStatusIntent : UiIntent {
    data object ReopenClicked : PaymentStatusIntent
    data object SupportClicked : PaymentStatusIntent
}

sealed interface PaymentStatusEffect : UiEffect {
    data object PaymentConfirmed : PaymentStatusEffect
}

@HiltViewModel
class PaymentStatusViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val billing: BillingRepository,
) : MviViewModel<PaymentStatusState, PaymentStatusIntent, PaymentStatusEffect>(PaymentStatusState()) {

    private val route = savedStateHandle.toRoute<PaymentStatusRoute>()

    init {
        val plan = BillingCatalog.plan(route.planId)
        val method = BillingCatalog.method(route.methodId)
        setState { copy(summary = "${plan.priceText} · ${method.icon}") }
        viewModelScope.launch {
            repeat(6) {
                delay(1_000)
                setState { copy(secondsLeft = (secondsLeft - 1).coerceAtLeast(0)) }
            }
            billing.activate(route.planId)
            setState { copy(step = 2) }
            delay(1_200)
            sendEffect(PaymentStatusEffect.PaymentConfirmed)
        }
    }

    override fun onIntent(intent: PaymentStatusIntent) {
    }
}

@Composable
fun PaymentStatusScreen(
    onConfirmed: () -> Unit,
    viewModel: PaymentStatusViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            PaymentStatusEffect.PaymentConfirmed -> onConfirmed()
        }
    }
    PaymentStatusContent(state, viewModel::onIntent)
}

@Composable
fun PaymentStatusContent(
    state: PaymentStatusState,
    onIntent: (PaymentStatusIntent) -> Unit
) {
    val accent = if (state.done) VoidStatus.protected else VoidStatus.connecting
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val stroke = 4.dp.toPx()
                    val arc = Size(size.width - stroke, size.height - stroke)
                    drawArc(
                        Color(0xFF1A1A1D), -90f, 360f, false,
                        topLeft = Offset(stroke / 2, stroke / 2), size = arc,
                        style = Stroke(stroke)
                    )
                    val fraction = if (state.done) 1f else state.secondsLeft / 180f
                    drawArc(
                        accent, -90f, 360f * fraction, false,
                        topLeft = Offset(stroke / 2, stroke / 2), size = arc,
                        style = Stroke(stroke, cap = StrokeCap.Round)
                    )
                }
                Text(
                    if (state.done) "✓" else "%02d:%02d".format(state.secondsLeft / 60, state.secondsLeft % 60),
                    style = VoidMono.key.copy(fontSize = 22.sp),
                    color = accent
                )
            }
            Text(
                stringResource(if (state.done) R.string.status_done else R.string.status_title),
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = VoidText.primary
            )
            Text(
                stringResource(R.string.status_text),
                modifier = Modifier.padding(top = 8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = VoidText.secondary
            )
        }
        Column(
            modifier = Modifier
                .padding(start = 20.dp, end = 20.dp, top = 26.dp)
                .fillMaxWidth()
                .voidCard()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            val labels = listOf(R.string.status_step_1, R.string.status_step_2, R.string.status_step_3)
            labels.forEachIndexed { index, label ->
                val completed = index < state.step || (state.done && index == 2)
                val current = index == state.step && !state.done
                Row(
                    modifier = Modifier.padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(if (completed) VoidText.primary else Color.Transparent, CircleShape)
                            .then(
                                if (completed) Modifier else Modifier.border(
                                    2.dp,
                                    if (current) VoidStatus.connecting else VoidOutline.default,
                                    CircleShape
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (completed) Text("✓", style = MaterialTheme.typography.labelMedium, color = Color.Black)
                    }
                    Text(
                        stringResource(label),
                        style = MaterialTheme.typography.bodyMedium,
                        color = when {
                            completed -> VoidText.primary
                            current -> VoidStatus.connecting
                            else -> VoidText.disabled
                        }
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, end = 24.dp, top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Счёт ${state.invoice}", style = VoidMono.meta, color = VoidText.disabled)
            Text(state.summary, style = VoidMono.meta, color = VoidText.disabled)
        }
        Box(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 34.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            VoidPrimaryButton(
                stringResource(R.string.status_reopen),
                onClick = { onIntent(PaymentStatusIntent.ReopenClicked) }
            )
            VoidSecondaryButton(
                stringResource(R.string.status_support),
                onClick = { onIntent(PaymentStatusIntent.SupportClicked) },
                height = 48.dp
            )
        }
    }
}
