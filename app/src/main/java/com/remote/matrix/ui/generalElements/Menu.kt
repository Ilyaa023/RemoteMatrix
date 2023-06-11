package com.remote.matrix.ui.generalElements

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.remote.domain.models.AppData
import com.remote.matrix.R
import me.saket.cascade.CascadeDropdownMenu

@Composable
fun SettingsPopupMenu(
        isMenuOpened: MutableState<Boolean>,
        viewModel: ActivityWithMenuViewModel,
        activity: Activity
){
    CascadeDropdownMenu(
        expanded = isMenuOpened.value,
        onDismissRequest = { isMenuOpened.value = false }
    ){
        DropdownMenuItem(
            modifier = Modifier.background(MaterialTheme.colors.background),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_settings_brightness),
                    null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.change_theme),
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
            },
            childrenHeader = {
                DropdownMenuHeader(
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    Text(
                        text = stringResource(id = R.string.change_theme),
                        modifier = Modifier
                            .padding(10.dp, 0.dp)
                            .fillMaxWidth()
                    )
                }
            },
            children = {
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setTheme(AppData.AUTO_MODE, activity)
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_brightness_auto),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                    Text(
                        text = stringResource(id = R.string.theme_auto),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setTheme(AppData.DARK_MODE, activity)
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dark_mode),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                    Text(
                        text = stringResource(id = R.string.theme_dark),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setTheme(AppData.LIGHT_MODE, activity)
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_light_mode),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                    Text(
                        text = stringResource(id = R.string.theme_light),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
            })
        DropdownMenuItem(
            modifier = Modifier.background(MaterialTheme.colors.background),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_language),
                    null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                )
            },
            text = {
                Text(
                    text = stringResource(id = R.string.change_language),
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
            },
            childrenHeader = {
                DropdownMenuHeader(
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    Text(
                        text = stringResource(id = R.string.change_language),
                        modifier = Modifier
                            .padding(10.dp, 0.dp)
                            .fillMaxWidth()
                    )
                }
            },
            children = {
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setLanguage(AppData.AUTO_MODE, activity)
                    }) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_language),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
                    )
                    Text(
                        text = stringResource(id = R.string.language_auto),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setLanguage(AppData.RU_MODE, activity)
                    }) {
                    Text(text = "Ru", fontSize = 18.sp,
                         color = MaterialTheme.colors.onBackground)
                    Text(
                        text = stringResource(id = R.string.language_ru),
                        modifier = Modifier.padding(10.dp, 0.dp)
                    )
                }
                androidx.compose.material.DropdownMenuItem(
                    modifier = Modifier.background(MaterialTheme.colors.background),
                    onClick = {
                        isMenuOpened.value = false
                        viewModel.setLanguage(AppData.EN_MODE, activity)
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

abstract class ActivityWithMenuViewModel(): ViewModel() {
    open fun setTheme(theme: Int, activity: Activity) {}
    open fun setLanguage(language: Int, activity: Activity) {}
}