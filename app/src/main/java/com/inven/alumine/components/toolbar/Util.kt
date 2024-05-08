package com.inven.alumine.components.toolbar

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import com.inven.alumine.AlumineApp

object Util {

    fun getPix(dip : Float) : Int{
        val r: Resources = AlumineApp.ContextProvider.mContext?.get()!!.resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.displayMetrics)
        return px.toInt()
    }

    fun getStatusBarHeight(): Int {
        val resourceId = AlumineApp.ContextProvider.mContext?.get()!!.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            return AlumineApp.ContextProvider.mContext?.get()!!.resources.getDimensionPixelSize(resourceId)
        }
        return 0
    }

}