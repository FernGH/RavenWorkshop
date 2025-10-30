package com.example.ravenhackernews.hackernews.data.remote.model.response

import com.example.ravenhackernews.hackernews.data.remote.model.entity.Hit
import com.google.gson.annotations.SerializedName

data class NewsResponse(
    @SerializedName("hits")
    val hits: List<Hit>
)