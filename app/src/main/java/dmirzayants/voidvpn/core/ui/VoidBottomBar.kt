package dmirzayants.voidvpn.core.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Dns
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.ui.theme.VoidAction
import dmirzayants.voidvpn.ui.theme.VoidOutline
import dmirzayants.voidvpn.ui.theme.VoidText

enum class MainTab(@StringRes val label: Int, val icon: ImageVector) {
    Home(R.string.tab_home, Icons.Rounded.Home),
    Servers(R.string.tab_servers, Icons.Rounded.Dns),
    Plan(R.string.tab_plan, Icons.Rounded.WorkspacePremium),
    Settings(R.string.tab_settings, Icons.Rounded.Settings),
}

@Composable
fun VoidBottomBar(
    selected: MainTab,
    onSelect: (MainTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.background(VoidSurfaceBar)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(VoidOutline.subtle)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(top = 10.dp, bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.Top
        ) {
            MainTab.entries.forEach { tab ->
                val isSelected = tab == selected
                Column(
                    modifier = Modifier
                        .width(72.dp)
                        .clickable { onSelect(tab) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 56.dp, height = 30.dp)
                            .background(
                                if (isSelected) VoidAction else androidx.compose.ui.graphics.Color.Transparent,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tab.icon,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = if (isSelected) VoidText.onLight else VoidText.tertiary
                        )
                    }
                    Text(
                        stringResource(tab.label),
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSelected) VoidText.primary else VoidText.tertiary
                    )
                }
            }
        }
        Spacer(Modifier.height(0.dp))
    }
}

private val VoidSurfaceBar = androidx.compose.ui.graphics.Color(0xFF0A0A0B)
