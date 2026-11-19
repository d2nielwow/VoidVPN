package dmirzayants.voidvpn.feature.billing.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.ui.VoidPrimaryButton
import dmirzayants.voidvpn.core.ui.VoidRadio
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.feature.billing.domain.BillingCatalog
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText

@Composable
fun PlansScreen(
    onClose: () -> Unit,
    onContinue: (planId: String) -> Unit,
) {
    var selectedId by rememberSaveable { mutableStateOf(BillingCatalog.plans.first().id) }
    val selected = BillingCatalog.plan(selectedId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 24.dp, top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(VoidSurface.cardAlt, CircleShape)
                    .clickable(onClick = onClose),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Rounded.Close, contentDescription = null, tint = VoidText.primary)
            }
            Text(
                stringResource(R.string.plans_restore),
                style = MaterialTheme.typography.bodySmall,
                color = VoidText.tertiary
            )
        }
        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 14.dp)) {
            Text(
                stringResource(R.string.plans_title),
                style = MaterialTheme.typography.headlineLarge,
                color = VoidText.primary
            )
            Column(
                modifier = Modifier.padding(top = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    R.string.plans_feature_1,
                    R.string.plans_feature_2,
                    R.string.plans_feature_3
                ).forEach { res ->
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("✓", color = VoidStatus.protected, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            stringResource(res),
                            style = MaterialTheme.typography.bodyMedium,
                            color = androidx.compose.ui.graphics.Color(0xFFC9C9CF)
                        )
                    }
                }
            }
        }
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BillingCatalog.plans.forEach { plan ->
                Box {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .voidCard(selected = plan.id == selectedId)
                            .clickable { selectedId = plan.id }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        VoidRadio(plan.id == selectedId)
                        Column(modifier = Modifier.weight(1f)) {
                            Text(plan.name, style = MaterialTheme.typography.labelLarge, color = VoidText.primary)
                            Text(plan.per, style = MaterialTheme.typography.labelMedium, color = VoidText.tertiary)
                        }
                        Text(plan.priceText, style = VoidMono.value, color = VoidText.primary)
                    }
                    plan.badge?.let { badge ->
                        Text(
                            badge,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(end = 16.dp)
                                .offset(y = (-10).dp)
                                .background(VoidStatus.protected, CircleShape)
                                .padding(horizontal = 9.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = VoidText.onLight
                        )
                    }
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 34.dp)) {
            VoidPrimaryButton(
                text = stringResource(R.string.plans_pay, selected.priceText),
                onClick = { onContinue(selected.id) }
            )
            Text(
                stringResource(R.string.plans_note),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
                color = VoidText.disabled
            )
        }
    }
}
