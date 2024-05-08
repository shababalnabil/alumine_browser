package com.inven.alumine.components.web.engine

import android.content.Context
import android.util.AttributeSet
import android.widget.Toast
import com.inven.alumine.AlumineApp
import com.inven.alumine.components.web.delegates.BrowserListener
import com.inven.alumine.components.web.delegates.SessionListener
import com.inven.alumine.filter.CleanContent
import com.inven.alumine.utils.browser.RuntimeProvider
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoView
import org.mozilla.geckoview.WebRequestError

class WebEngine : GeckoView, SessionListener {
    var canGoBack = false
    var browserListener: BrowserListener? = null
    var currentUrl: String = ""
    private var session: WebSession? = null
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)


    fun activate(runtime: GeckoRuntime): WebSession {
        if (session != null) {
            releaseSession()
        }
        session = WebSession()
        session?.open(runtime)
        session?.sessionListener = this
        session?.geckoview = this
        setSession(session!!)
        return session!!
    }

    fun loadUrl(url: String) {
        session?.loadUri(url)
    }

    override fun onCanGoBack(oncanGoBack: Boolean) {
        this.canGoBack = oncanGoBack
    }

    fun goBack() {
        session!!.goBack()
    }

    override fun onNavigationChange(url: String) {
        if (url == "about:blank") {
            return
        }
        currentUrl = url
        browserListener?.onUpdateUrl(url)
    }

    override fun onLoadError(error: WebRequestError?, isBlocked: Boolean) {
        browserListener?.apply {
            if (isBlocked) {
                onPageBlocked()
            } else {
                onLoadError(error!!)
            }
        }
    }

    override fun onPageStart() {
        browserListener?.onPageStartedLoading()
    }

    override fun onProgressChange(progress: Int) {
        browserListener?.apply {
            onProgressUpdate(progress)
            if (progress in 1..99) {
                onShowProgressBar()
            } else {
                onHideProgressBar()
            }
        }
    }

    override fun onSuccessfullyLoaded(url: String) {
        if (!CleanContent.contains(url)) {
            browserListener?.onPageFinished()
        }
    }

    fun stopLoading(){
        session?.stop()
    }

    fun reload() {
        session?.reload(GeckoSession.LOAD_FLAGS_BYPASS_CACHE)
    }

}
