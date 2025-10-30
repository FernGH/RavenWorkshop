package com.example.ravenhackernews.hackernews.domain.mapper

import com.example.ravenhackernews.core.extesions.value
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import javax.inject.Inject

class HackerEntityToDtoMapper @Inject constructor()  {
    fun transform(entity: HackerNewEntity): HackerNewDto {
        return with(entity) {
            HackerNewDto(
                id = id.value(),
                title = title.value(),
                url = url.value(),
                author = author.value(),
                createdAt = createdAt.value(),
                isDeleted = isDeleted.value(),
            )
        }
    }

}
