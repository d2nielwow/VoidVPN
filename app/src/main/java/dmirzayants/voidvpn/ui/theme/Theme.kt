package dmirzayants.voidvpn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val VoidColorScheme = darkColorScheme(
    primary = VoidAction,
    onPrimary = VoidText.onLight,
    background = VoidBlack,
    onBackground = VoidText.primary,
    surface = VoidBlack,
    onSurface = VoidText.primary,
    onSurfaceVariant = VoidText.secondary,
    error = VoidStatus.error,
)

@Composable
fun VoidVPNTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = VoidColorScheme,
        typography = VoidTypography,
        content = content,
    )
}
