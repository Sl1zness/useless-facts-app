package com.uselessfacts.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UselessFactsApi {
    @GET("/api/v2/facts/random")
    suspend fun getUselessFact(@Query("language") lang: String): Response<UselessFact>

    @GET("/api/v2/facts/today")
    suspend fun getFactOfTheDay(@Query("language") lang: String): Response<UselessFact>
}