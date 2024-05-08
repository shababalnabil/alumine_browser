package com.inven.alumine.filter

import android.content.Context
import com.google.gson.Gson
import com.inven.alumine.AlumineApp
import java.io.IOException

object CleanContent {
    private var blockedUrlsArray: Array<String>? = null

    fun initBlockedUrls() {
        if (blockedUrlsArray == null) {
            blockedUrlsArray = getBlockedUrlsFromJson(
                loadJSONFromAsset(
                    AlumineApp.ContextProvider.mContext?.get()!!,
                    "filter/clean_content.json"
                ) ?: ""
            )
        }
    }

    fun contains(keyword: String): Boolean {
        val containsBlockedUrl = blockedUrlsArray!!.any { blockedUrl ->
            keyword.contains(blockedUrl,true)
        }
        return containsBlockedUrl
    }


    private fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }

    private fun getBlockedUrlsFromJson(jsonString: String): Array<String>? {
        val gson = Gson()
        val model = gson.fromJson(jsonString, BlockedUrlsModel::class.java)
        return model.blockedUrls
    }

}