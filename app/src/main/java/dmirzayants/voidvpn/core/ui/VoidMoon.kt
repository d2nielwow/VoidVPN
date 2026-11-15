package dmirzayants.voidvpn.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import dmirzayants.voidvpn.ui.theme.VoidText

@Composable
fun VoidMoon(
    size: Dp,
    cutColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(VoidText.primary)
    ) {
        Box(
            modifier = Modifier
                .size(size * 0.86f)
                .align(Alignment.TopEnd)
                .offset(x = size * 0.15f, y = -size * 0.07f)
                .background(cutColor, CircleShape)
        )
    }
}
