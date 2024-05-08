package com.inven.alumine.suggestion.client

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SuggestService {
    @GET("autocomplete")
    fun getSuggestion(@Query("q") query : String, @Query("type") type : String) : Call<List<Any>>
}