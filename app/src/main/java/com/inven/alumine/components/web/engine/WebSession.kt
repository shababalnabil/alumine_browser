package com.inven.alumine.components.web.engine

import com.inven.alumine.components.web.delegates.SessionListener
import com.inven.alumine.filter.CleanContent
import com.inven.alumine.utils.browser.RuntimeProvider
import com.inven.alumine.utils.app.InappSettings
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.GeckoSession
import org.mozilla.geckoview.GeckoSession.ContentDelegate
import org.mozilla.geckoview.GeckoSession.NavigationDelegate
import org.mozilla.geckoview.GeckoSession.ProgressDelegate
import org.mozilla.geckoview.GeckoSessionSettings
import org.mozilla.geckoview.WebRequestError
import org.mozilla.geckoview.WebResponse

class WebSession() : GeckoSession(
    GeckoSessionSettings.Builder()
        .userAgentMode(GeckoSessionSettings.USER_AGENT_MODE_MOBILE)
        .useTrackingProtection(true)
        .displayMode(GeckoSessionSettings.DISPLAY_MODE_BROWSER)
        .fullAccessibilityTree(true)
        .viewportMode(GeckoSessionSettings.VIEWPORT_MODE_MOBILE)
        .userAgentOverride(InappSettings.current_user_agent)
        .build()
), NavigationDelegate, ProgressDelegate,ContentDelegate {
    var sessionListener: SessionListener? = null
    var geckoview: WebEngine? = null
    private var currentUrl : String = ""

    init {
        navigationDelegate = this
        progressDelegate = this
        contentDelegate = this
    }

    override fun onCanGoBack(session: GeckoSession, canGoBack: Boolean) {
        super.onCanGoBack(session, canGoBack)
        sessionListener?.onCanGoBack(canGoBack)
    }

    override fun onProgressChange(session: GeckoSession, progress: Int) {
        super.onProgressChange(session, progress)
        sessionListener?.onProgressChange(progress)
    }

    override fun setNavigationDelegate(delegate: NavigationDelegate?) {
        super.setNavigationDelegate(this)
    }

    override fun onSubframeLoadRequest(
        session: GeckoSession,
        request: NavigationDelegate.LoadRequest
    ): GeckoResult<AllowOrDeny>? {
        if (CleanContent.contains(request.uri)) {
            geckoview?.apply {
                activate(RuntimeProvider.getInstance()).apply {
                    showBlockedError()
                }
            }
            close()
        }
        return super.onLoadRequest(session, request)
    }

    override fun onPageStart(session: GeckoSession, url: String) {
        super.onPageStart(session, url)
        sessionListener?.onPageStart()
    }


    override fun onLocationChange(
        session: GeckoSession,
        url: String?,
        perms: MutableList<PermissionDelegate.ContentPermission>,
        hasUserGesture: Boolean
    ) {
        super.onLocationChange(session, url, perms, hasUserGesture)
        sessionListener?.onNavigationChange(url.toString())
        if (!url.isNullOrBlank() && url!="about:blank"){
            currentUrl = url.toString()
        }
    }

    private fun showBlockedError() {
        sessionListener?.onLoadError(null, true)
    }

    override fun onLoadError(
        session: GeckoSession,
        uri: String?,
        error: WebRequestError
    ): GeckoResult<String>? {
        sessionListener?.onLoadError(error, false)
        return super.onLoadError(session, uri, error)
    }

    override fun onPageStop(session: GeckoSession, success: Boolean) {
        super.onPageStop(session, success)
        if (success&& currentUrl.isNotBlank()) {
            sessionListener?.onSuccessfullyLoaded(currentUrl)
        }
    }

    override fun onLoadRequest(
        session: GeckoSession,
        request: NavigationDelegate.LoadRequest
    ): GeckoResult<AllowOrDeny>? {
        if (CleanContent.contains(request.uri)) {
            close()
            geckoview?.apply {
                activate(RuntimeProvider.getInstance()).apply {
                    showBlockedError()
                }
            }
            return GeckoResult.deny()
        }
        return super.onLoadRequest(session, request)
    }

    override fun onNewSession(session: GeckoSession, uri: String): GeckoResult<GeckoSession>? {
        return GeckoResult.fromValue(this)
    }

}