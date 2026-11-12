package dmirzayants.voidvpn.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

// Void — только тёмная тема на чистом чёрном (OLED). Dynamic color отключён намеренно:
// бренд монохромный, цвет означает только состояние.
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
