package com.example.ravenhackernews.hackernews.domain.dto

data class HackerNewDto(
    val id: String,
    val title: String,
    val url: String,
    val author: String,
    val createdAt: String,
    val isDeleted: Boolean = false
)
