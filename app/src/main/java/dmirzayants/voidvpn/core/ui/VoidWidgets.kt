package dmirzayants.voidvpn.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText

@Composable
fun VoidBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(44.dp)
            .background(VoidSurface.cardAlt, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.login_back),
            tint = VoidText.primary
        )
    }
}

@Composable
fun VoidPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: androidx.compose.ui.unit.Dp = 56.dp,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoidAction,
            contentColor = VoidText.onLight,
            disabledContainerColor = VoidOutline.default,
            disabledContentColor = VoidText.disabled
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun VoidSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    height: androidx.compose.ui.unit.Dp = 52.dp,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = VoidSurface.cardAlt,
            contentColor = VoidText.primary
        )
    ) {
        Text(text, style = MaterialTheme.typography.labelLarge.copy(fontSize = androidx.compose.ui.unit.TextUnit.Unspecified))
    }
}

@Composable
fun VoidRadio(selected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(22.dp)
            .border(
                if (selected) 6.dp else 2.dp,
                if (selected) VoidAction else VoidOutline.strong,
                CircleShape
            )
    )
}

@Composable
fun VoidSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    val offset by animateDpAsState(if (checked) 21.dp else 3.dp, label = "thumb")
    val track by animateColorAsState(if (checked) VoidAction else Color232327, label = "track")
    val thumb by animateColorAsState(if (checked) androidx.compose.ui.graphics.Color.Black else VoidText.disabled, label = "thumbColor")
    Box(
        modifier = modifier
            .size(width = 44.dp, height = 26.dp)
            .background(track, CircleShape)
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            Modifier
                .offset(x = offset, y = 3.dp)
                .size(20.dp)
                .background(thumb, CircleShape)
        )
    }
}

private val Color232327 = androidx.compose.ui.graphics.Color(0xFF232327)

fun Modifier.voidCard(radius: androidx.compose.ui.unit.Dp = 20.dp, selected: Boolean = false): Modifier =
    this
        .background(VoidSurface.base, RoundedCornerShape(radius))
        .border(
            if (selected) 1.5.dp else 1.dp,
            if (selected) VoidAction else VoidOutline.subtle,
            RoundedCornerShape(radius)
        )

fun Modifier.fullWidth(): Modifier = this.fillMaxWidth()
