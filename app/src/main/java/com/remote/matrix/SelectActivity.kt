package com.remote.matrix

    import android.content.Context
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.animation.AnimatedVisibility
    import androidx.compose.animation.fadeIn
    import androidx.compose.animation.fadeOut
    import androidx.compose.animation.slideInVertically
    import androidx.compose.animation.slideOutVertically
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.PaddingValues
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.wrapContentSize
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.DropdownMenu
    import androidx.compose.material.DropdownMenuItem
    import androidx.compose.material.Icon
    import androidx.compose.material.IconButton
    import androidx.compose.material.MaterialTheme
    import androidx.compose.material.Surface
    import androidx.compose.material.Switch
    import androidx.compose.material.Text
    import androidx.compose.material.TopAppBar
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.MoreVert
    import androidx.compose.material.icons.filled.Refresh
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.runtime.setValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import androidx.lifecycle.viewModelScope
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.remote.domain.models.AppData
    import com.remote.domain.models.NetBrief
    import com.remote.domain.useCases.brief.GetNetsBrief
    import com.remote.matrix.data.firebaseDB.NetsBrief
    import com.remote.matrix.data.local.LocalDataRepo
    import com.remote.matrix.ui.generalElements.LoadingBigBlink
    import com.remote.matrix.ui.theme.RemoteMatrixTheme
    import com.remote.matrix.ui.theme.TertiaryContainerDark
    import com.remote.matrix.ui.theme.TertiaryContainerLight
    import com.remote.matrix.ui.theme.TertiaryDark
    import com.remote.matrix.ui.theme.TertiaryLight
    import com.remote.matrix.ui.theme.changeAlpha
    import com.remote.matrix.ui.theme.onTertiaryContainerDark
    import com.remote.matrix.ui.theme.onTertiaryContainerLight
    import com.remote.matrix.ui.theme.onTertiaryDark
    import com.remote.matrix.ui.theme.onTertiaryLight
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch
    import me.saket.cascade.CascadeDropdownMenu

class SelectActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SelectActivityViewModel = viewModel(factory = SAVMFactory(applicationContext))
            val theme by viewModel.theme.collectAsState()
            val language by viewModel.language.collectAsState()


            viewModel.refresh()
            RemoteMatrixTheme(darkTheme = when(theme){
                AppData.DARK_MODE -> true
                AppData.LIGHT_MODE -> false
                else -> isSystemInDarkTheme()
            }) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    SelectActivityScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun SelectActivityScreen(viewModel: SelectActivityViewModel = viewModel()) {
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val listOfNets by viewModel.listOfNets.observeAsState()
    var isMenuOpened by remember { mutableStateOf(false) }
    var expandedTheme by remember { mutableStateOf(false) }
    var expandedLanguage by remember { mutableStateOf(false) }

//    TODO("add the buttons: language, theme")

    Column() {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(text = stringResource(id = R.string.net_select),
                 color = MaterialTheme.colors.onPrimary,
                 modifier = Modifier
                     .weight(1f)
                     .padding(10.dp, 0.dp),
                 fontSize = 20.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = { viewModel.refresh() }) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
            IconButton(onClick = { isMenuOpened = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onPrimary
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .padding(0.dp)
        ){
            var counter = 0
            for (net in listOfNets!!) {
                counter++
                item {
                    AnimatedVisibility(visible = !isRefreshing,
                                       enter = slideInVertically () + fadeIn(),
                                       exit = slideOutVertically() + fadeOut()) {
                        NetElement(net = net)
                    }
                }
                if (counter < listOfNets!!.size)
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colors.onBackground)
                        )}
            }
        }
    }
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(0.dp, 56.dp)
        .wrapContentSize(Alignment.TopEnd)) {
        CascadeDropdownMenu(expanded = isMenuOpened, onDismissRequest = { isMenuOpened = false }){
            DropdownMenuItem(
                text = {
                    Row (modifier = Modifier.fillMaxWidth(),
                         verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_settings_brightness), null
                        )
                        Text(
                            text = stringResource(id = R.string.change_theme),
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                    }

                },
                children = {
                    DropdownMenuItem(onClick = {
                        isMenuOpened = false
                        viewModel.theme.value = AppData.AUTO_MODE
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_brightness_auto), null
                        )
                        Text(
                            text = stringResource(id = R.string.theme_auto),
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                    }
                    DropdownMenuItem(onClick = {
                        isMenuOpened = false
                        viewModel.theme.value = AppData.DARK_MODE
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_dark_mode), null
                        )
                        Text(
                            text = stringResource(id = R.string.theme_dark),
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                    }
                    DropdownMenuItem(onClick = {
                        isMenuOpened = false
                        viewModel.theme.value = AppData.LIGHT_MODE
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_light_mode), null
                        )
                        Text(
                            text = stringResource(id = R.string.theme_light),
                            modifier = Modifier.padding(10.dp, 0.dp)
                        )
                    }
                })
            DropdownMenuItem(text = {
                Row (modifier = Modifier.fillMaxWidth(),
                     verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_language), null
                    )
                    Text(
                        text = stringResource(id = R.string.change_language),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }

            }, children = {
                DropdownMenuItem(onClick = {
                    isMenuOpened = false
                    viewModel.language.value = AppData.AUTO_MODE
                }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_language), null
                    )
                    Text(
                        text = stringResource(id = R.string.language_auto),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                DropdownMenuItem(onClick = {
                    isMenuOpened = false
                    viewModel.language.value = AppData.RU_MODE
                }) {
                    Text(text = "Ru", fontSize = 18.sp,
                         color = MaterialTheme.colors.onBackground)
                    Text(
                        text = stringResource(id = R.string.language_ru),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                DropdownMenuItem(onClick = {
                    isMenuOpened = false
                    viewModel.language.value = AppData.EN_MODE
                }) {
                    Text(text = "En", fontSize = 18.sp,
                         color = MaterialTheme.colors.onBackground)
                    Text(text = stringResource(id = R.string.language_en),
                         modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
            })
        }
    }

    if (isRefreshing)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.changeAlpha(Color.DarkGray, 0.5f))
            .clickable {}, contentAlignment = Alignment.Center) {
            LoadingBigBlink(size = 200.dp, color2 = MaterialTheme.colors.primary)
        }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RemoteMatrixTheme {
        SelectActivityScreen()
    }

}

@Composable
fun NetElement(net: NetBrief){
    Column(Modifier
               .fillMaxWidth()
               .clickable { }
               .background(
                   if (isSystemInDarkTheme()) TertiaryContainerDark
                   else TertiaryContainerLight
               )
               .padding(20.dp)
    ){
        val textColor = if (isSystemInDarkTheme()) onTertiaryContainerDark 
                        else onTertiaryContainerLight
        Text(
            text = stringResource(id = R.string.net_id) + net.id,
            color = textColor,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(id = R.string.net_bmn_cnt) + net.countOfBMNs,
                color = textColor,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f, true)
                )
            Column(modifier = Modifier
                .weight(1f)
                .background(
                    if (isSystemInDarkTheme()) TertiaryDark else TertiaryLight,
                    shape = RoundedCornerShape(10.dp)
                )
                .padding(10.dp)
            ) {
                Text(
                    text = if (net.connectedDevices.size > 0) stringResource(id = R.string.net_dvcs)
                    else stringResource(id = R.string.net_no_dvcs),
                    color = if (isSystemInDarkTheme()) onTertiaryDark else onTertiaryLight,
                    fontSize = 14.sp,
                )
                for (device in net.connectedDevices) Text(
                    text = "\t$device",
                    color = if (isSystemInDarkTheme()) onTertiaryDark else onTertiaryLight,
                    fontSize = 13.sp
                )
            }
        }
    }
}

class SelectActivityViewModel(context: Context): ViewModel(){
    private val _isRefreshing = MutableStateFlow(true)
    private val _listOfNets = MutableLiveData(ArrayList<NetBrief>())
    private val _appData: AppData
    init {
        _appData = LocalDataRepo(context).getData()
    }

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing
    val listOfNets: LiveData<ArrayList<NetBrief>>
        get() = _listOfNets
    var theme: MutableStateFlow<Int>
        get() = MutableStateFlow(_appData.themeMode)
        set(value) {
            if (value.value == AppData.AUTO_MODE ||
                value.value == AppData.DARK_MODE ||
                value.value == AppData.DARK_MODE){
                _appData.themeMode = value.value
            }
        }
    var language: MutableStateFlow<Int>
        get() = MutableStateFlow(_appData.languageMode)
        set(value) {
            if (value.value == AppData.AUTO_MODE ||
                value.value == AppData.RU_MODE ||
                value.value == AppData.EN_MODE){
                _appData.languageMode = value.value
            }
        }
    fun refresh(){
        _isRefreshing.value = true
        viewModelScope.launch {
            GetNetsBrief(NetsBrief()).execute {
                _isRefreshing.value = false
                _listOfNets.value = it
            }
        }
    }
}

class SAVMFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SelectActivityViewModel(context) as T
}