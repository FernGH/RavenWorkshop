package com.example.ravenhackernews.hackernews.data.local.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class HackerNewEntity(
    @PrimaryKey val id: String,
    val title: String,
    val url: String,
    val author: String,
    val createdAt: String,
    val isDeleted: Boolean = false
)
