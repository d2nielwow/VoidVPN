package dmirzayants.voidvpn.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dmirzayants.voidvpn.R

@OptIn(ExperimentalTextApi::class)
private fun variableFont(resId: Int, weight: FontWeight) = Font(
    resId = resId,
    weight = weight,
    variationSettings = FontVariation.Settings(FontVariation.weight(weight.weight)),
)

val Geologica = FontFamily(
    variableFont(R.font.geologica_variable, FontWeight.Normal),
    variableFont(R.font.geologica_variable, FontWeight.Medium),
    variableFont(R.font.geologica_variable, FontWeight.SemiBold),
    variableFont(R.font.geologica_variable, FontWeight.Bold),
)

val JetBrainsMono = FontFamily(
    variableFont(R.font.jetbrains_mono_variable, FontWeight.Normal),
    variableFont(R.font.jetbrains_mono_variable, FontWeight.Medium),
    variableFont(R.font.jetbrains_mono_variable, FontWeight.SemiBold),
)

private fun g(size: Int, lh: Int, w: FontWeight, ls: Double = 0.0) = TextStyle(
    fontFamily = Geologica,
    fontWeight = w,
    fontSize = size.sp,
    lineHeight = lh.sp,
    letterSpacing = ls.em,
)

val VoidTypography = Typography(
    displaySmall = g(40, 44, FontWeight.Bold, -0.035),
    headlineLarge = g(30, 34, FontWeight.Bold, -0.027),
    headlineMedium = g(26, 30, FontWeight.Bold, -0.023),
    titleLarge = g(24, 28, FontWeight.Bold, -0.02),
    titleMedium = g(22, 28, FontWeight.Bold, -0.018),
    titleSmall = g(18, 24, FontWeight.SemiBold),
    labelLarge = g(16, 20, FontWeight.SemiBold),
    bodyLarge = g(15, 23, FontWeight.Normal),
    bodyMedium = g(14, 21, FontWeight.Medium),
    bodySmall = g(13, 20, FontWeight.Normal),
    labelMedium = g(12, 16, FontWeight.SemiBold),
    labelSmall = g(11, 14, FontWeight.Medium, 0.01),
)

private fun mono(size: Int, lh: Int, w: FontWeight, ls: Double = 0.0) = TextStyle(
    fontFamily = JetBrainsMono,
    fontWeight = w,
    fontSize = size.sp,
    lineHeight = lh.sp,
    letterSpacing = ls.em,
)

object VoidMono {
    val key = mono(20, 24, FontWeight.SemiBold, 0.1)
    val value = mono(15, 20, FontWeight.SemiBold)
    val meta = mono(12, 16, FontWeight.Medium)
    val log = mono(11, 18, FontWeight.Normal)
}
