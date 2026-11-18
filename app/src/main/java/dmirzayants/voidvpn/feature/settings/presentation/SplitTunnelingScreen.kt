package dmirzayants.voidvpn.feature.settings.presentation

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dmirzayants.voidvpn.R
import dmirzayants.voidvpn.core.mvi.CollectEffects
import dmirzayants.voidvpn.core.mvi.MviViewModel
import dmirzayants.voidvpn.core.mvi.UiEffect
import dmirzayants.voidvpn.core.mvi.UiIntent
import dmirzayants.voidvpn.core.mvi.UiState
import dmirzayants.voidvpn.core.ui.VoidBackButton
import dmirzayants.voidvpn.core.ui.VoidSwitch
import dmirzayants.voidvpn.feature.settings.domain.SplitTunnelRepository
import dmirzayants.voidvpn.ui.theme.VoidBlack
import dmirzayants.voidvpn.ui.theme.VoidStatus
import dmirzayants.voidvpn.ui.theme.VoidSurface
import dmirzayants.voidvpn.ui.theme.VoidText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class InstalledApp(val packageName: String, val label: String)

data class SplitState(
    val apps: List<InstalledApp> = emptyList(),
    val bypass: Set<String> = emptySet(),
) : UiState

sealed interface SplitIntent : UiIntent {
    data class Toggle(val packageName: String) : SplitIntent
    data object Back : SplitIntent
}

sealed interface SplitEffect : UiEffect {
    data object NavigateBack : SplitEffect
}

@HiltViewModel
class SplitTunnelingViewModel @Inject constructor(
    private val repository: SplitTunnelRepository,
    @ApplicationContext private val context: Context,
) : MviViewModel<SplitState, SplitIntent, SplitEffect>(SplitState()) {

    init {
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) { loadLaunchableApps() }
            setState { copy(apps = apps) }
        }
        viewModelScope.launch {
            repository.bypassPackages.collect { set -> setState { copy(bypass = set) } }
        }
    }

    override fun onIntent(intent: SplitIntent) {
        when (intent) {
            is SplitIntent.Toggle -> viewModelScope.launch { repository.toggle(intent.packageName) }
            SplitIntent.Back -> sendEffect(SplitEffect.NavigateBack)
        }
    }

    private fun loadLaunchableApps(): List<InstalledApp> {
        val pm = context.packageManager
        val launcher = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(launcher, 0)
            .map { InstalledApp(it.activityInfo.packageName, it.loadLabel(pm).toString()) }
            .filter { it.packageName != context.packageName }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }
}

@Composable
fun SplitTunnelingScreen(
    onNavigateBack: () -> Unit,
    viewModel: SplitTunnelingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectEffects(viewModel.effects) { effect ->
        when (effect) {
            SplitEffect.NavigateBack -> onNavigateBack()
        }
    }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(VoidBlack)
            .systemBarsPadding()
    ) {
        VoidBackButton(
            onClick = { viewModel.onIntent(SplitIntent.Back) },
            modifier = Modifier.padding(start = 16.dp, top = 12.dp)
        )
        Text(
            stringResource(R.string.split_title),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 16.dp),
            style = MaterialTheme.typography.headlineMedium,
            color = VoidText.primary
        )
        Text(
            stringResource(R.string.split_text),
            modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = VoidText.secondary
        )
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 24.dp)
        ) {
            items(state.apps, key = { it.packageName }) { app ->
                val direct = app.packageName in state.bypass
                val icon = remember(app.packageName) {
                    runCatching {
                        context.packageManager.getApplicationIcon(app.packageName)
                            .toBitmap(96, 96).asImageBitmap()
                    }.getOrNull()
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.onIntent(SplitIntent.Toggle(app.packageName)) }
                        .padding(horizontal = 8.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (icon != null) {
                        Image(
                            bitmap = icon,
                            contentDescription = null,
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                    } else {
                        Column(Modifier.size(38.dp).background(VoidSurface.cardAlt, RoundedCornerShape(10.dp))) {}
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(app.label, style = MaterialTheme.typography.bodyMedium, color = VoidText.primary)
                        Text(
                            stringResource(if (direct) R.string.split_direct else R.string.split_vpn),
                            style = MaterialTheme.typography.labelMedium,
                            color = if (direct) VoidText.tertiary else VoidStatus.protected
                        )
                    }
                    VoidSwitch(
                        checked = direct,
                        onCheckedChange = { viewModel.onIntent(SplitIntent.Toggle(app.packageName)) }
                    )
                }
            }
        }
    }
}
