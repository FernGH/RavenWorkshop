package com.example.ravenhackernews.hackernews.data.mapper

import com.example.ravenhackernews.core.extesions.value
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.data.remote.model.entity.Hit
import javax.inject.Inject

class HackerNewToEntityMapper @Inject constructor()  {
    fun transform(value: Hit): HackerNewEntity {
        return with(value) {
            HackerNewEntity(
                id = objectID.value(),
                title = title.value(story_title.value("(sin título)")),
                url = story_url.value(url.value()),
                author = author.value("desconocido"),
                createdAt = created_at.value(),
                isDeleted = false
            )
        }
    }
}
