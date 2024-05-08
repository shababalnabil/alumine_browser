package com.inven.alumine

import android.app.Application
import android.content.Context
import com.inven.alumine.filter.CleanContent
import java.lang.ref.WeakReference

class AlumineApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ContextProvider.mContext = WeakReference(applicationContext)
        CleanContent.initBlockedUrls()
    }

    object ContextProvider{
        var mContext : WeakReference<Context>? = null
    }

}