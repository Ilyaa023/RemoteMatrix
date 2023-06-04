package com.remote.matrix

    import android.content.Context
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.animation.AnimatedVisibility
    import androidx.compose.animation.expandIn
    import androidx.compose.animation.fadeIn
    import androidx.compose.animation.fadeOut
    import androidx.compose.animation.shrinkOut
    import androidx.compose.animation.slideIn
    import androidx.compose.animation.slideInVertically
    import androidx.compose.animation.slideOut
    import androidx.compose.animation.slideOutVertically
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.AppBarDefaults
    import androidx.compose.material.DropdownMenu
    import androidx.compose.material.DropdownMenuItem
    import androidx.compose.material.MaterialTheme
    import androidx.compose.material.Surface
    import androidx.compose.material.Switch
    import androidx.compose.material.Text
    import androidx.compose.material.TopAppBar
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.runtime.mutableStateOf
    import androidx.compose.runtime.remember
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.geometry.Offset
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.ColorFilter
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.IntOffset
    import androidx.compose.ui.unit.IntSize
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
    import com.remote.matrix.ui.generalElements.LoadingBlink
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
    val isMenuOpened = remember { mutableStateOf(false) }


//    TODO("add the buttons: language, theme")

    Column() {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onPrimary,
            contentPadding = AppBarDefaults.ContentPadding
        ) {
            Text(text = stringResource(id = R.string.net_select),
                 color = MaterialTheme.colors.onPrimary,
                 modifier = Modifier
                     .weight(1f)
                     .padding(10.dp, 0.dp),
                 fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Image(painter = painterResource(id = R.drawable.ic_refresh),
                  contentDescription = "",
                  modifier = Modifier
                      .clickable { viewModel.refresh() }
                      .padding(10.dp, 0.dp),
                  colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary))
            Image(painter = painterResource(id = R.drawable.ic_settings),
                  contentDescription = "",
                  modifier = Modifier
                      .clickable { isMenuOpened.value = true }
                      .padding(10.dp, 0.dp),
                  colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onPrimary))
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

    if (isRefreshing)
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = Color.changeAlpha(Color.DarkGray, 0.5f))
            .clickable {}, contentAlignment = Alignment.Center) {
            LoadingBlink(size = 200.dp, count = 20, color2 = MaterialTheme.colors.primary)
        }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopEnd){
        DropdownMenu(expanded = isMenuOpened.value, onDismissRequest = { isMenuOpened.value = false }) {
            DropdownMenuItem(onClick = {}) {
                Image(painter = painterResource(id = R.drawable.ic_settings_brightness), null)
                Text("theme")
                Switch(checked = true, onCheckedChange = {})
            }
        }
    }

//    AnimatedVisibility(
//        visible = isMenuOpened.value,
//        enter = fadeIn(),
//        exit = fadeOut()
//    ) {
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .background(color = Color.changeAlpha(Color.DarkGray, 0.3f))
//            .clickable { isMenuOpened.value = false }, contentAlignment = Alignment.TopEnd) {
//            AnimatedVisibility(
//                modifier = Modifier.padding(10.dp, 56.dp),
//                visible = isMenuOpened.value,
//                enter = slideIn { IntOffset(100, -100) },
//                exit = slideOut { IntOffset(100, -100) }
//            ) {
//                Box(
//                    Modifier.background(
//                        MaterialTheme.colors.background,
//                        RoundedCornerShape(20.dp)
//                    )
//                ) {
//                    Box(modifier = Modifier
//                        .background(MaterialTheme.colors.background)
//                        .width(20.dp)
//                        .height(20.dp), contentAlignment = Alignment.TopEnd) {}
//                    Column(modifier = Modifier.padding(15.dp)) {
//                        Text(
//                            text = stringResource(R.string.settings),
//                            color = MaterialTheme.colors.onBackground,
//                            fontSize = 13.sp
//                        )
//
//                    }
//                }
//            }
//        }
//    }
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
    val theme: StateFlow<Int>
        get() = MutableStateFlow(_appData.themeMode)
    val language: StateFlow<Int>
        get() = MutableStateFlow(_appData.languageMode)

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