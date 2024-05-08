package com.inven.alumine.utils.app

import android.util.Patterns
import java.util.regex.Pattern

object SearchManager {

    fun getSearchOrLoad(text: String): String {
        return if (isUrl(text.trim())) {
            getLoadableUrl(text.trim())
        } else {
            getSearchableUrl(text)
        }
    }

    fun isUrl(text: String): Boolean {
        val matcher = Patterns.WEB_URL.matcher(text)
        if (matcher.find()) {
            val possibleUrl = matcher.group(1)?.toString()
            if (!possibleUrl.isNullOrBlank() && text.startsWith(possibleUrl) && possibleUrl.contains(
                    "/search?q=",
                    true
                )
            ) {
                return true
            }
        }
        return matcher.matches()
    }

    private fun getLoadableUrl(url: String): String {
        val matcher = Pattern.compile("(.*://)(.*)").matcher(url)
        return if (matcher.matches()) {
            url
        } else {
            "http://$url"
        }
    }

    private fun getSearchableUrl(query: String): String {
        return InappSettings.current_search_url + query
    }

}