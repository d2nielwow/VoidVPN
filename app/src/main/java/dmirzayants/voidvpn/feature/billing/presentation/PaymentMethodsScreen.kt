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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.ui.VoidBackButton
import dmirzayants.voidvpn.core.ui.VoidPrimaryButton
import dmirzayants.voidvpn.core.ui.VoidRadio
import dmirzayants.voidvpn.core.ui.voidCard
import dmirzayants.voidvpn.feature.billing.domain.BillingCatalog
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import dmirzayants.voidvpn.ui.theme.VoidTextSoft

/** 5.2 Способ оплаты. */
@Composable
fun PaymentMethodsScreen(
    planId: String,
    onBack: () -> Unit,
    onPay: (methodId: String) -> Unit,
) {
    val plan = BillingCatalog.plan(planId)
    var methodId by rememberSaveable { mutableStateOf(BillingCatalog.methods.first().id) }
    val method = BillingCatalog.method(methodId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        VoidBackButton(onClick = onBack, modifier = Modifier.padding(start = 16.dp, top = 12.dp))
        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp)) {
            Text(
                stringResource(R.string.pay_title),
                style = MaterialTheme.typography.headlineLarge,
                color = VoidText.primary
            )
            Row(
                modifier = Modifier
                    .padding(top = 14.dp)
                    .fillMaxWidth()
                    .voidCard(16.dp)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Pro · ${plan.name}", style = MaterialTheme.typography.bodyMedium, color = VoidTextSoft)
                Text(plan.priceText, style = VoidMono.value, color = VoidText.primary)
            }
        }
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BillingCatalog.methods.forEach { m ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .voidCard(selected = m.id == methodId)
                        .clickable { methodId = m.id }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(VoidSurface.cardAlt, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(m.icon, style = VoidMono.meta.copy(fontSize = 13.sp), color = VoidText.primary)
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            m.name,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 15.sp),
                            color = VoidText.primary
                        )
                        Text(m.hint, style = MaterialTheme.typography.labelMedium, color = VoidText.tertiary)
                    }
                    VoidRadio(m.id == methodId)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Column(modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 34.dp)) {
            VoidPrimaryButton(text = method.cta, onClick = { onPay(method.id) })
            Text(
                stringResource(R.string.pay_note),
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
