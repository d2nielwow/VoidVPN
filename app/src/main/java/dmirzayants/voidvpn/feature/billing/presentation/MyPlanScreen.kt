package dmirzayants.voidvpn.feature.billing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.core.ui.MainTab
import dmirzayants.voidvpn.core.ui.VoidBottomBar
import dmirzayants.voidvpn.core.ui.VoidPrimaryButton
import dmirzayants.voidvpn.core.ui.VoidSecondaryButton
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.feature.account.domain.AccountRepository
import dmirzayants.voidvpn.feature.billing.domain.BillingRepository
import dmirzayants.voidvpn.feature.billing.domain.Subscription
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyPlanState(
    val subscription: Subscription? = null,
    val accessKey: String = "",
    val keyVisible: Boolean = false,
) : UiState {
    val keyText: String
        get() {
            val parts = accessKey.chunked(4)
            if (parts.size < 4) return accessKey
            return if (keyVisible) parts.joinToString("-")
            else "${parts[0]}-${parts[1]}-••••-••••"
        }
}

sealed interface MyPlanIntent : UiIntent {
    data object ToggleKey : MyPlanIntent
    data object PlansClicked : MyPlanIntent
}

sealed interface MyPlanEffect : UiEffect {
    data object NavigateToPlans : MyPlanEffect
}

@HiltViewModel
class MyPlanViewModel @Inject constructor(
    billing: BillingRepository,
    account: AccountRepository,
) : MviViewModel<MyPlanState, MyPlanIntent, MyPlanEffect>(MyPlanState()) {

    init {
        viewModelScope.launch {
            combine(billing.subscription, account.accessKey) { sub, key ->
                sub to key.orEmpty()
            }.collect { (sub, key) -> setState { copy(subscription = sub, accessKey = key) } }
        }
    }

    override fun onIntent(intent: MyPlanIntent) {
        when (intent) {
            MyPlanIntent.ToggleKey -> setState { copy(keyVisible = !keyVisible) }
            MyPlanIntent.PlansClicked -> sendEffect(MyPlanEffect.NavigateToPlans)
        }
    }
}

@Composable
fun MyPlanScreen(
    onTabSelected: (MainTab) -> Unit,
    onNavigateToPlans: () -> Unit,
    viewModel: MyPlanViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            MyPlanEffect.NavigateToPlans -> onNavigateToPlans()
        }
    }
    MyPlanContent(state, viewModel::onIntent, onTabSelected)
}

@Composable
fun MyPlanContent(
    state: MyPlanState,
    onIntent: (MyPlanIntent) -> Unit,
    onTabSelected: (MainTab) -> Unit
) {
    val sub = state.subscription
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .statusBarsPadding()
    ) {
        Text(
            stringResource(R.string.plan_my_title),
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = VoidText.primary
        )
        if (sub != null) {
            // Светлая карточка тарифа
            Column(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 16.dp)
                    .fillMaxWidth()
                    .background(VoidAction, RoundedCornerShape(26.dp))
                    .padding(22.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.plan_active),
                        modifier = Modifier
                            .background(Color.Black, CircleShape)
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                        color = VoidStatus.protected
                    )
                    Text("PRO", style = VoidMono.meta, color = VoidText.disabled)
                }
                Spacer(Modifier.height(18.dp))
                Text(
                    sub.planName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = VoidText.onLight
                )
                Text(
                    sub.untilText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = VoidText.disabled
                )
                Spacer(Modifier.height(18.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.Black.copy(alpha = 0.1f), CircleShape)
                ) {
                    Box(
                        Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(
                                (sub.remainingDays.toFloat() / sub.totalDays).coerceIn(0f, 1f)
                            )
                            .background(Color.Black, CircleShape)
                    )
                }
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(R.string.plan_left_days, sub.remainingDays),
                    style = MaterialTheme.typography.labelMedium,
                    color = VoidText.onLight
                )
            }

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .voidCard(18.dp)
                        .padding(14.dp)
                ) {
                    Text(stringResource(R.string.plan_traffic), style = MaterialTheme.typography.labelMedium, color = VoidText.tertiary)
                    Text(
                        sub.trafficText,
                        modifier = Modifier.padding(top = 6.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = VoidText.primary
                    )
                    Text(
                        sub.trafficUsedText,
                        modifier = Modifier.padding(top = 4.dp),
                        style = VoidMono.log,
                        color = VoidText.disabled
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .voidCard(18.dp)
                        .padding(14.dp)
                ) {
                    Text(stringResource(R.string.plan_devices), style = MaterialTheme.typography.labelMedium, color = VoidText.tertiary)
                    Text(
                        stringResource(R.string.plan_devices_value, sub.devicesUsed, sub.devicesMax),
                        modifier = Modifier.padding(top = 6.dp),
                        style = MaterialTheme.typography.titleSmall,
                        color = VoidText.primary
                    )
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(sub.devicesMax) { i ->
                            Box(
                                Modifier
                                    .weight(1f)
                                    .height(4.dp)
                                    .background(
                                        if (i < sub.devicesUsed) VoidAction else VoidOutline.default,
                                        CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
                    .fillMaxWidth()
                    .voidCard(18.dp)
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.plan_key_label), style = MaterialTheme.typography.labelMedium, color = VoidText.tertiary)
                    Text(
                        state.keyText,
                        modifier = Modifier.padding(top = 4.dp),
                        style = VoidMono.value.copy(letterSpacing = 1.sp),
                        color = VoidText.primary
                    )
                }
                Text(
                    stringResource(if (state.keyVisible) R.string.plan_key_hide else R.string.plan_key_show),
                    modifier = Modifier
                        .background(VoidSurface.cardAlt, RoundedCornerShape(10.dp))
                        .clickable { onIntent(MyPlanIntent.ToggleKey) }
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.labelMedium.copy(fontSize = 13.sp),
                    color = VoidText.primary
                )
            }

            Row(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                VoidPrimaryButton(
                    stringResource(R.string.plan_renew),
                    onClick = { onIntent(MyPlanIntent.PlansClicked) },
                    modifier = Modifier.weight(1f),
                    height = 52.dp
                )
                VoidSecondaryButton(
                    stringResource(R.string.plan_change),
                    onClick = { onIntent(MyPlanIntent.PlansClicked) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.weight(1f))
        VoidBottomBar(selected = MainTab.Plan, onSelect = onTabSelected)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000, heightDp = 780)
@Composable
private fun MyPlanPreview() {
    VoidVPNTheme {
        MyPlanContent(
            MyPlanState(
                subscription = Subscription(
                    "Pro · Год", "до 14 марта 2027", 132, 365,
                    "Безлимит", "42.6 ГБ в этом мес.", 2, 5, true
                ),
                accessKey = "VOID7F3KQ9LM2XPA"
            ), {}, {}
        )
    }
}
