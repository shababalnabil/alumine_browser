package com.inven.alumine.suggestion

import com.inven.alumine.suggestion.client.SuggestService
import com.inven.alumine.suggestion.client.SuggestionClient
import com.inven.alumine.suggestion.listener.SuggestionResponseListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SuggestionProvider {
    private val suggestionClient = SuggestionClient()
    private val suggestService: SuggestService = suggestionClient.getInstance().create(SuggestService::class.java)
    private var suggestionListener : SuggestionResponseListener? = null

    fun setSuggestionResponseListener(listener: SuggestionResponseListener){
        suggestionListener = listener
    }

    fun clear(){
        suggestionClient.retrofitClient.dispatcher().cancelAll()
    }

    fun requestSuggestions(query: String) {
        suggestService.getSuggestion(query, "list").enqueue(object : Callback<List<Any>> {
            override fun onResponse(call: Call<List<Any>>, response: Response<List<Any>>) {
                val suggestionJsonArray = response.body()?.get(1).toString()
                if (suggestionJsonArray=="null"||suggestionJsonArray.isEmpty()){return}
                suggestionListener?.onGetSuggestions(suggestionJsonArray.replace("([\\[\\]])".toRegex(), ""))
            }

            override fun onFailure(call: Call<List<Any>>, t: Throwable) {

            }
        })

    }

}