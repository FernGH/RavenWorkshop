package com.example.ravenhackernews.hackernews.data.service

import com.example.ravenhackernews.hackernews.data.remote.model.response.NewsResponse
import io.reactivex.rxjava3.core.Single

import retrofit2.http.GET
import retrofit2.http.Query

interface HackerNewsService {
    @GET("api/v1/search_by_date")
    fun searchByDate(@Query("query") query: String? = "android"): Single<NewsResponse>
}
