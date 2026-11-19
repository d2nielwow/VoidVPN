package dmirzayants.voidvpn.feature.billing.domain

import kotlinx.coroutines.flow.Flow

data class PlanOption(
    val id: String,
    val name: String,
    val per: String,
    val priceRub: Int,
    val badge: String? = null,
) {
    val priceText: String get() = "${"%,d".format(priceRub).replace(',', ' ')} ₽"
}

data class PayMethod(
    val id: String,
    val icon: String,
    val name: String,
    val hint: String,
    val cta: String,
)

data class Subscription(
    val planName: String,
    val untilText: String,
    val remainingDays: Int,
    val totalDays: Int,
    val trafficText: String,
    val trafficUsedText: String,
    val devicesUsed: Int,
    val devicesMax: Int,
    val active: Boolean,
)

object BillingCatalog {
    val plans = listOf(
        PlanOption("year", "Год", "249 ₽ в месяц", 2990, badge = "−50%"),
        PlanOption("month", "Месяц", "Помесячно", 499),
        PlanOption("trial", "Пробный", "3 дня, 1 устройство", 0),
    )

    val methods = listOf(
        PayMethod("sbp", "СБП", "СБП или карта РФ", "ЮKassa · мгновенно", "Оплатить через СБП"),
        PayMethod("crypto", "₮", "Криптовалюта", "USDT (TRC-20), TON, BTC", "Оплатить криптовалютой"),
        PayMethod("tg", "TG", "Telegram-бот", "@voidvpn_bot", "Открыть Telegram-бота"),
        PayMethod("card", "VISA", "Зарубежная карта", "Visa, Mastercard", "Оплатить картой"),
    )

    fun plan(id: String): PlanOption = plans.firstOrNull { it.id == id } ?: plans.first()
    fun method(id: String): PayMethod = methods.firstOrNull { it.id == id } ?: methods.first()
}

interface BillingRepository {
    val subscription: Flow<Subscription>

    suspend fun activate(planId: String)
}
