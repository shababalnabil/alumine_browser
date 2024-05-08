package com.inven.alumine.utils.browser

import com.inven.alumine.utils.app.InappSettings
import org.mozilla.geckoview.AllowOrDeny
import org.mozilla.geckoview.GeckoResult
import org.mozilla.geckoview.WebExtension
import org.mozilla.geckoview.WebExtensionController

class ExtensionManager {
    private var webExtensionController : WebExtensionController? = null

    fun connect(extensionController: WebExtensionController) : ExtensionManager {
        webExtensionController = extensionController
        extensionController.promptDelegate = promptDelegate
        return this
    }

    private val promptDelegate = object : WebExtensionController.PromptDelegate {
        override fun onInstallPrompt(extension: WebExtension): GeckoResult<AllowOrDeny> {
            return if (extension.location.startsWith("resource://android/assets/extensions/")) {
                GeckoResult.allow()
            } else {
                GeckoResult.deny()
            }
        }

        override fun onOptionalPrompt(
            extension: WebExtension,
            permissions: Array<out String>,
            origins: Array<out String>
        ): GeckoResult<AllowOrDeny>? {
            return if (extension.location.startsWith("resource://android/assets/extensions/")) {
                GeckoResult.allow()
            } else {
                GeckoResult.deny()
            }
        }
    }

    fun installBuiltIn( addon: String, id: String) : ExtensionManager{
        if (!InappSettings.isExtensionInstalled(id)) {
            webExtensionController?.apply {
                install("resource://android/assets/extensions/$addon",
                    WebExtensionController.INSTALLATION_METHOD_FROM_FILE
                ).accept {
                    it?.let {
                        setAllowedInPrivateBrowsing(it, true)
                    }
                }
                InappSettings.updateExtension(id, true)
            }
        }
        return this
    }

}