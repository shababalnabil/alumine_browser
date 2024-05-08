package com.inven.alumine.suggestion.client

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SuggestionClient {
    private var instance : Retrofit? = null
    val retrofitClient by lazy { OkHttpClient() }

    fun getInstance() : Retrofit{
        if (instance==null){
            instance = Retrofit.Builder()
                .client(retrofitClient)
                .baseUrl("https://ac.ecosia.org/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
        }

        return instance!!

    }

}