package com.github.youopensource.yougitlab.data.repository

import com.github.youopensource.yougitlab.data.AnalyticsEvent
import com.github.youopensource.yougitlab.data.CodeSuggestionApiResult
import retrofit2.Call
import retrofit2.http.*

interface APIService {
    @GET("generic?service=codesnippets&version=1&fields=snippet_code%2Curl")
    @Headers("user-agent: youide")
    fun getApiResult(
        @Query("query") queryId: String?,
        @Query("size") sizeId: Int,
        @Query("source") sourceId: String?,
        @Query("page") pageId: Int
    ): Call<CodeSuggestionApiResult?>

    @POST("recordEvent")
    fun recordAnalyticsEvent(
        @Body body: AnalyticsEvent,
        @Header("user-agent") userAgent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36",
        @Header("content-type") contentType: String = "text/plain; charset=utf-8"
    ): Call<Map<String, String>>
}
