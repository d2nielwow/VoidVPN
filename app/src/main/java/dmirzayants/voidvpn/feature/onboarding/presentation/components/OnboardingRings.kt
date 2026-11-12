package dmirzayants.voidvpn.feature.onboarding.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dmirzayants.voidvpn.feature.onboarding.presentation.OnboardingSlide
import dmirzayants.voidvpn.feature.onboarding.presentation.onboardingSlides
import dmirzayants.voidvpn.ui.theme.VoidMono
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@Composable
fun OnboardingRings(slide: OnboardingSlide, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(240.dp),
        contentAlignment = Alignment.Center,
    ) {
        slide.glow?.let { glow ->
            Box(
                Modifier
                    .fillMaxSize()
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                0.35f to glow,
                                1f to Color.Transparent,
                                radius = size.minDimension / 2
                            )
                        )
                    }
            )
        }
        Box(
            Modifier
                .fillMaxSize()
                .border(1.dp, slide.outerRing, CircleShape)
        )
        Box(
            Modifier
                .fillMaxSize()
                .padding(40.dp)
                .border(1.5.dp, slide.innerRing, CircleShape)
        )
        Box(
            modifier = Modifier
                .size(88.dp)
                .background(slide.coreBackground, CircleShape)
                .border(2.dp, slide.coreBorder, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = slide.mark,
                style = VoidMono.value.copy(fontSize = 13.sp),
                color = slide.coreInk
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun OnboardingRingsPreview() {
    VoidVPNTheme {
        OnboardingRings(onboardingSlides[0])
    }
}