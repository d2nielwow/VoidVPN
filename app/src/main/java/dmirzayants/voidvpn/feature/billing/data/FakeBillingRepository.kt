package dmirzayants.voidvpn.feature.billing.data

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dmirzayants.voidvpn.feature.billing.domain.BillingCatalog
import dmirzayants.voidvpn.feature.billing.domain.BillingRepository
import dmirzayants.voidvpn.feature.billing.domain.Subscription
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Подписка-заглушка до появления бэкенда. */
@Singleton
class FakeBillingRepository @Inject constructor() : BillingRepository {

    private val _subscription = MutableStateFlow(
        Subscription(
            planName = "Pro · Год",
            untilText = "до 14 марта 2027",
            remainingDays = 132,
            totalDays = 365,
            trafficText = "Безлимит",
            trafficUsedText = "42.6 ГБ в этом мес.",
            devicesUsed = 2,
            devicesMax = 5,
            active = true,
        )
    )
    override val subscription: Flow<Subscription> = _subscription

    override suspend fun activate(planId: String) {
        val plan = BillingCatalog.plan(planId)
        val days = when (planId) {
            "year" -> 365
            "month" -> 30
            else -> 3
        }
        _subscription.value = _subscription.value.copy(
            planName = "Pro · ${plan.name}",
            remainingDays = days,
            totalDays = days,
            untilText = "через $days дн.",
            active = true,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class BillingModule {
    @Binds
    @Singleton
    abstract fun bindBillingRepository(impl: FakeBillingRepository): BillingRepository
}
