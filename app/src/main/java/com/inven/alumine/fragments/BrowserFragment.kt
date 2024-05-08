package com.inven.alumine.fragments

import android.os.Bundle
import android.view.View
import com.inven.alumine.utils.browser.RuntimeProvider
import com.inven.alumine.components.web.delegates.BrowserListener
import org.mozilla.geckoview.WebRequestError

class BrowserFragment : BaseBrowserFragment(), BrowserListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webEngine.apply {
            activate(RuntimeProvider.getInstance())
            browserListener = this@BrowserFragment

            toolbar.apply {
                stopButton.setOnClickListener {
                    stopLoading()
                }

                reloadButton.setOnClickListener {
                    reload()
                }
            }

        }

    }

    override fun onBrowsingStart(initUri : String?) {
        super.onBrowsingStart(initUri)
        if (!initUri.isNullOrBlank()){
            webEngine.loadUrl(initUri.toString())
            updateUrl(initUri.toString())
        }
    }

    override fun onShowProgressBar() {
        showProgressBar()
    }

    override fun onHideProgressBar() {
        hideProgressBar()
    }

    override fun onProgressUpdate(progress: Int) {
        updateProgress(progress)
    }

    override fun onUpdateUrl(url: String) {
        updateUrl(url)
    }

    override fun onPageBlocked() {
        showErrorPage(true)
    }

    override fun onLoadError(error: WebRequestError) {
        showErrorPage(false)
    }

    override fun onPageStartedLoading() {

    }

    override fun onPageFinished() {
        hideErrorPage()
    }

}