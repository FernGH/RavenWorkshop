package com.example.ravenhackernews.hackernews.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface HackerNewsDao {

    @Query("SELECT * FROM news WHERE isDeleted = 0 ORDER BY createdAt DESC")
    fun getFiltered(): Single<List<HackerNewEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(items: List<HackerNewEntity>): Completable

    @Query("UPDATE news SET isDeleted = 1 WHERE id = :id")
    fun softDelete(id: String): Completable

    @Query("SELECT * FROM news")
    fun getAllIncludingDeleted(): Single<List<HackerNewEntity>>
}
