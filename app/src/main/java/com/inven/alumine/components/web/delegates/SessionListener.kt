package com.inven.alumine.components.web.delegates

import org.mozilla.geckoview.WebRequestError

interface SessionListener {
    fun onCanGoBack(canGoBack : Boolean)
    fun onProgressChange(progress : Int)
    fun onNavigationChange(url : String)
    fun onLoadError(error : WebRequestError?, isBlocked : Boolean)
    fun onPageStart()
    fun onSuccessfullyLoaded(url: String)
}