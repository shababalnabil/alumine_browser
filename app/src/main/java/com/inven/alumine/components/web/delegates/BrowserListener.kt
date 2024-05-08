package com.inven.alumine.components.web.delegates

import org.mozilla.geckoview.WebRequestError

interface BrowserListener {
    fun onShowProgressBar()
    fun onHideProgressBar()
    fun onProgressUpdate(progress : Int)
    fun onUpdateUrl(url : String)
    fun onPageBlocked()
    fun onLoadError(error: WebRequestError)
    fun onPageStartedLoading()
    fun onPageFinished()
}