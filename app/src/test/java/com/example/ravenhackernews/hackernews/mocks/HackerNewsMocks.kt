package com.example.ravenhackernews.hackernews.mocks

import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.data.remote.model.entity.Hit
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto

class HackerNewsMocks {

    internal fun hackerDtoList() = listOf(
        HackerNewDto(
            id = "1",
            title = "Android News",
            author = "fulanito",
            url = "https://test.com",
            createdAt = "2025"
        ),
        HackerNewDto(
            id = "2",
            title = "Kotlin News",
            author = "jetbrains",
            url = "https://kotlinlang.org",
            createdAt = "2025")
    )

    internal fun hackerEntityList() = listOf(
        HackerNewEntity(
            "1",
            "Android News",
            "author",
            "url",
            "2025",
            false
        ),
        HackerNewEntity(
            "2",
            "Kotlin News",
            "jetbrains",
            "https://kotlinlang.org",
            "2025",
            true)
    )




    internal fun hackerHitList() = listOf(
        Hit(
            objectID = "123",
            title = "Android News",
            story_title = "Android News",
            story_url = "https://test.com",
            author = "fulanito",
            url = "https://test.com",
            created_at = "2025"
        ),
        Hit(
            objectID = "234",
            title = "Kotlin News",
            story_title = "Kotlin News",
            story_url = "https://kotlinlang.org",
            author = "jetbrains",
            url = "https://kotlinlang.org",
            created_at = "2025")
    )

}
