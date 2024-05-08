package com.inven.alumine.utils.app

import android.content.Context
import com.inven.alumine.AlumineApp

object InappSettings {
    var current_user_agent = "Mozilla/5.0 (Android 14; Mobile; rv:125.0) Gecko/125.0 Firefox/125.0"
    var current_search_url = "https://www.google.com/search?q="

    fun isExtensionInstalled(id: String): Boolean {
        return AlumineApp.ContextProvider.mContext?.get()!!
            .getSharedPreferences("extensionManager", Context.MODE_PRIVATE)
            .getBoolean(id, false)
    }

    fun updateExtension(id: String, isInstalled: Boolean) {
        AlumineApp.ContextProvider.mContext?.get()!!
            .getSharedPreferences("extensionManager", Context.MODE_PRIVATE).edit()
            .putBoolean(id, isInstalled).apply()
    }

    fun isFirstInstall() : Boolean{
        return AlumineApp.ContextProvider.mContext?.get()!!
            .getSharedPreferences("init_m", Context.MODE_PRIVATE)
            .getBoolean("fstins", false)
    }

    fun saveInstallInstance(){
        AlumineApp.ContextProvider.mContext?.get()!!
            .getSharedPreferences("init_m", Context.MODE_PRIVATE).edit()
            .putBoolean("fstins", true).apply()
    }

}
