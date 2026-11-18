package dmirzayants.voidvpn.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import dmirzayants.voidvpn.ui.theme.VoidSurface

/** Круглый флаг страны (drawable flag_<код>) на тёмной подложке. */
@Composable
fun VoidFlag(countryCode: String, size: Dp, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val resId = remember(countryCode) {
        context.resources.getIdentifier(
            "flag_${countryCode.lowercase()}", "drawable", context.packageName
        )
    }
    Box(
        modifier = modifier
            .size(size)
            .background(VoidSurface.cardAlt, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                modifier = Modifier
                    .size(size * 0.68f)
                    .clip(CircleShape)
            )
        }
    }
}
