package com.remote.matrix.data.local

import android.content.Context
import com.remote.domain.models.AppData

class LocalDataRepo(context: Context) {
    companion object{
        const val LANGUAGE = "Lng"
        const val THEME = "THM"
    }
    private val key = "RMP"
    val pref = context.getSharedPreferences(key, Context.MODE_PRIVATE)

    fun getData(): AppData =
        AppData(
            themeMode = pref.getInt(THEME, AppData.AUTO_MODE),
            languageMode = pref.getInt(LANGUAGE, AppData.AUTO_MODE)
        )

    fun setData(data: AppData){
        pref.edit().apply{
            putInt(THEME, data.themeMode)
            putInt(LANGUAGE, data.languageMode)
        }.apply()
    }
}