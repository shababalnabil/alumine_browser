package com.inven.alumine.utils.browser

import android.content.Context
import com.inven.alumine.AlumineApp
import org.mozilla.geckoview.ContentBlocking
import org.mozilla.geckoview.ContentBlocking.SafeBrowsingProvider
import org.mozilla.geckoview.GeckoRuntime
import org.mozilla.geckoview.GeckoRuntimeSettings

object RuntimeProvider {
    private var instance: GeckoRuntime? = null
    private val extensionManager = ExtensionManager()

    fun getInstance(): GeckoRuntime {
        val context = AlumineApp.ContextProvider.mContext?.get()!!
        if (instance == null) {
            synchronized(RuntimeProvider::class) {
                if (instance == null) {
                    instance = createRuntime(context)
                }
            }
        }
        return instance!!
    }

    private fun runtimeSettings(): GeckoRuntimeSettings {
        return GeckoRuntimeSettings.Builder()
            .allowInsecureConnections(GeckoRuntimeSettings.ALLOW_ALL)
            .aboutConfigEnabled(false)
            .automaticFontSizeAdjustment(true)
            .extensionsWebAPIEnabled(true)
            .forceUserScalableEnabled(true)
            .javaScriptEnabled(true)
            .contentBlocking(contentBlockingSettings())
            .webManifest(true)
            .build()
    }

    private fun contentBlockingSettings(): ContentBlocking.Settings {
        return ContentBlocking.Settings.Builder()
            .cookieBehavior(ContentBlocking.CookieBehavior.ACCEPT_NON_TRACKERS)
            .safeBrowsing(ContentBlocking.SafeBrowsing.UNWANTED)
            .antiTracking(ContentBlocking.AntiTracking.FINGERPRINTING)
            .enhancedTrackingProtectionLevel(ContentBlocking.EtpLevel.STRICT)
            .strictSocialTrackingProtection(true)
            .build()
    }

    private fun createRuntime(context: Context): GeckoRuntime {
        val runtime = GeckoRuntime.create(context.applicationContext, runtimeSettings())
        extensionManager.connect(runtime.webExtensionController)
            .installBuiltIn("ubo_android.xpi", "ubo@")
            .installBuiltIn("tracking_protection.xpi", "trp@")
            .installBuiltIn("ua_fixer.xpi", "uaf@")
            .installBuiltIn("deye.xpi", "eyd@")
        return runtime
    }


}