package com.remote.matrix

    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.background
    import androidx.compose.foundation.clickable
    import androidx.compose.foundation.isSystemInDarkTheme
    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.material.MaterialTheme
    import androidx.compose.material.Surface
    import androidx.compose.material.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.runtime.livedata.observeAsState
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import androidx.compose.ui.unit.sp
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.MutableLiveData
    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.viewModelScope
    import androidx.lifecycle.viewmodel.compose.viewModel
    import com.remote.domain.models.NetBrief
    import com.remote.domain.useCases.brief.GetNetsBrief
    import com.remote.matrix.data.firebaseDB.NetsBrief
    import com.remote.matrix.ui.generalElements.LoadingCube
    import com.remote.matrix.ui.theme.RemoteMatrixTheme
    import com.remote.matrix.ui.theme.TertiaryContainerDark
    import com.remote.matrix.ui.theme.TertiaryContainerLight
    import com.remote.matrix.ui.theme.onTertiaryContainerDark
    import com.remote.matrix.ui.theme.onTertiaryContainerLight
    import kotlinx.coroutines.flow.MutableStateFlow
    import kotlinx.coroutines.flow.StateFlow
    import kotlinx.coroutines.launch

class SelectActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: SelectActivityViewModel = viewModel()
            viewModel.refresh()
            RemoteMatrixTheme {
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
    TODO("add the buttons: refresh, language, theme")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ){
        var counter = 0
        for (net in listOfNets!!) {
            counter++
            item {
                NetElement(net = net)
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
    if (isRefreshing)
        Box(modifier = Modifier
            .fillMaxSize()
            .clickable {}, contentAlignment = Alignment.Center) {
            LoadingCube(size = 200.dp, count = 20)
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
    Column(
        Modifier
            .fillMaxWidth()
            .background(
                if (isSystemInDarkTheme()) TertiaryContainerDark
                else TertiaryContainerLight
            )
    ){
        val textColor = if (isSystemInDarkTheme()) onTertiaryContainerDark 
                        else onTertiaryContainerLight
        Text(
            text = net.id, 
            color = textColor,
            fontSize = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
//        Text(text = )
        TODO("complete the card of net")
    }
}

class SelectActivityViewModel: ViewModel(){
    private val _isRefreshing = MutableStateFlow(true)
    private val _listOfNets = MutableLiveData(ArrayList<NetBrief>())

    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing
    val listOfNets: LiveData<ArrayList<NetBrief>>
        get() = _listOfNets

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