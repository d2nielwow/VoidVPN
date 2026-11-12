package dmirzayants.voidvpn.feature.onboarding.presentation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText

data class OnboardingSlide(
    @StringRes val title: Int,
    @StringRes val text: Int,
    val mark: String,
    val outerRing: Color,
    val innerRing: Color,
    val coreBackground: Color,
    val coreBorder: Color,
    val coreInk: Color,
    val glow: Color?
)

val onboardingSlides = listOf(
    OnboardingSlide(
        title = R.string.onboarding_1_title,
        text = R.string.onboarding_1_text,
        mark = "ON",
        outerRing = VoidOutline.subtle,
        innerRing = VoidOutline.default,
        coreBackground = VoidStatus.protected,
        coreBorder = VoidStatus.protected,
        coreInk = VoidText.onLight,
        glow = VoidStatus.protected.copy(alpha = 0.35f)
    ),
    OnboardingSlide(
        title = R.string.onboarding_2_title,
        text = R.string.onboarding_2_text,
        mark = "0 B",
        outerRing = VoidOutline.subtle,
        innerRing = VoidOutline.default,
        coreBackground = VoidBlack,
        coreBorder = VoidText.primary,
        coreInk = VoidText.primary,
        glow = null
    ),
    OnboardingSlide(
        title = R.string.onboarding_3_title,
        text = R.string.onboarding_3_text,
        mark = "DPI",
        outerRing = VoidOutline.subtle,
        innerRing = VoidStatus.protected.copy(alpha = 0.4f),
        coreBackground = VoidSurface.base,
        coreBorder = VoidStatus.protected,
        coreInk = VoidStatus.protected,
        glow = VoidStatus.protected.copy(alpha = 0.2f)
    )
)