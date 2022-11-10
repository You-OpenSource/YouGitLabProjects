package com.github.youopensource.yougitlab.data.repository

import com.github.youopensource.yougitlab.data.*
import com.github.youopensource.yougitlab.services.TelemetryService
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.concurrent.thread

object RemoteYouRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://you.com/api/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val apiService: APIService = retrofit.create(APIService::class.java)

    fun getCodeSuggestions(request: SolutionRequest): Observable<CodeSuggestionApiResult> {
        if (request.codeLine == null) {
            return Observable.empty()
        }
        return Observable.create {
            val apiRequest: Call<CodeSuggestionApiResult?> =
                apiService.getApiResult(request.codeLine, 15, "codesnippets", 1)
            val telemetry = TelemetryService.instance.action("intellij_user_search")
                .property("search.param", request.codeLine)
            try {
                telemetry.started()
                val body = apiRequest.execute().body()
                if (body != null) {
                    it.onNext(body)
                }
                telemetry.success()
            } catch (e: Exception) {
                telemetry.error(e)
            }
            telemetry.finished()
            telemetry.send()
            sendSearchEvent(request.codeLine)
            it.onComplete()
        }
    }

    private fun sendSearchEvent(codeLine: String) {
        thread {
            apiService.recordAnalyticsEvent(
                AnalyticsEvent(
                    "intellij_user_search",
                    EventData(
                        codeLine
                    ),
                    DeviceProperties(
                        0, 0, 0, 0, true
                    )
                ),
            ).execute()
            println("Analytics executed")
        }
    }

    fun sendButtonClickedEvent(solution: Solution) {
        thread {
            TelemetryService.instance.action("intellij_user_click")
                .property("solution.number", solution.number.toString())
                .property("solution.codeSnippet", solution.codeSnippet)
                .send()
            apiService.recordAnalyticsEvent(
                AnalyticsEvent(
                    "intellij_user_click",
                    EventData(
                        solution.number.toString()
                    ),
                    DeviceProperties(
                        0, 0, 0, 0, true
                    )
                ),
            ).execute()
            println("Analytics executed")
        }
    }
}
