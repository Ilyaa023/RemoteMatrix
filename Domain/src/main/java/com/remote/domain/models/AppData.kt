package com.remote.domain.models

class AppData(
        var themeMode: Int = AUTO_MODE,
        var languageMode: Int = AUTO_MODE
) {
    companion object{
        const val AUTO_MODE = 0

        const val DARK_MODE = 1
        const val LIGHT_MODE = 2

        const val RU_MODE = 1
        const val EN_MODE = 2
    }
}