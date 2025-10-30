package com.example.ravenhackernews.hackernews.data.local.datasource

import com.example.ravenhackernews.hackernews.data.local.HackerNewsDao
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.mocks.HackerNewsMocks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.Mockito
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class HackerNewsDataLocalSourceTest {

    private val dao: HackerNewsDao = mock()
    private val localSource = HackerNewsDataLocalSource(dao)
    private val mocks = HackerNewsMocks()

    @Test
    fun `returns all entities including deleted`() {
        val entities = mocks.hackerEntityList()
        whenever(dao.getAllIncludingDeleted()).thenReturn(Single.just(entities))

        val localSourceTest = localSource.getHackerNews().test()

        localSourceTest.assertComplete()
        localSourceTest.assertNoErrors()
        assertEquals(entities, localSourceTest.values().first())

        Mockito.verify(dao).getAllIncludingDeleted()
    }

    @Test
    fun `delegates updateAll to dao`() {
        val list = listOf(
            HackerNewEntity("1", "Android News", "author", "url", "2025", false)
        )
        whenever(dao.updateAll(list)).thenReturn(Completable.complete())

        val localSourceTest = localSource.updateAll(list).test()

        localSourceTest.assertComplete()
        localSourceTest.assertNoErrors()
        Mockito.verify(dao).updateAll(list)
    }

    @Test
    fun `delegates softDelete to dao`() {
        val id = "1"
        whenever(dao.softDelete(id)).thenReturn(Completable.complete())

        val localSourceTest = localSource.softDelete(id).test()

        localSourceTest.assertComplete()
        localSourceTest.assertNoErrors()
        Mockito.verify(dao).softDelete(id)
    }

    @Test
    fun `propagates error from dao`() {
        val exception = RuntimeException("DB error")
        whenever(dao.getAllIncludingDeleted()).thenReturn(Single.error(exception))

        val localSourceTest = localSource.getHackerNews().test()

        localSourceTest.assertError(exception)
        Mockito.verify(dao).getAllIncludingDeleted()
    }
}
