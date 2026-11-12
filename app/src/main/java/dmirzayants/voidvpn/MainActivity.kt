package dmirzayants.voidvpn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dmirzayants.voidvpn.navigation.VoidNavHost
import dmirzayants.voidvpn.ui.theme.VoidVPNTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splash = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splash.setKeepOnScreenCondition { startViewModel.startDestination.value == null }

        setContent {
            VoidVPNTheme {
                val destination by startViewModel.startDestination.collectAsStateWithLifecycle()
                destination?.let { VoidNavHost(startDestination = it) }
            }
        }
    }
}
