package com.example.ravenhackernews.hackernews.data.remote.datasource


import com.example.ravenhackernews.hackernews.data.remote.model.entity.Hit
import com.example.ravenhackernews.hackernews.data.remote.model.response.NewsResponse
import com.example.ravenhackernews.hackernews.data.service.HackerNewsService
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsSource
import com.example.ravenhackernews.hackernews.mocks.HackerNewsMocks
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.mockito.Mockito
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class HackerNewsDataRemoteSourceTest {

    private lateinit var hackerNewsService: HackerNewsService
    private lateinit var remoteSource: HackerNewsSource

    private val mocks = HackerNewsMocks()

    @Before
    fun setup() {
        hackerNewsService = mock()
        remoteSource = HackerNewsDataRemoteSource(hackerNewsService)

        val trampoline = Schedulers.trampoline()
        RxJavaPlugins.setIoSchedulerHandler { trampoline }
        RxJavaPlugins.setComputationSchedulerHandler { trampoline }
        RxJavaPlugins.setNewThreadSchedulerHandler { trampoline }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { trampoline }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `return news response when service succeeds`() {
        val query = "android"
        val hits = mocks.hackerHitList()
        val expectedResponse = NewsResponse(hits)

        whenever(hackerNewsService.searchByDate(query)).thenReturn(Single.just(expectedResponse))

        val remoteTest = remoteSource.getHackerNews(query).test()

        remoteTest.assertComplete()
        remoteTest.assertNoErrors()
        val result = remoteTest.values().first()
        assertEquals(expectedResponse, result)

        Mockito.verify(hackerNewsService).searchByDate(query)
    }

    @Test
    fun `propagate error when service fails`() {
        val query = "android"
        val exception = RuntimeException("Network error")
        whenever(hackerNewsService.searchByDate(query)).thenReturn(Single.error(exception))

        val remoteTest = remoteSource.getHackerNews(query).test()

        remoteTest.assertError(exception)
        remoteTest.assertNotComplete()

        Mockito.verify(hackerNewsService).searchByDate(query)
    }

    @Test
    fun `handle null query by passing null to service`() {
        whenever(hackerNewsService.searchByDate(null)).thenReturn(Single.just(NewsResponse(emptyList())))

        val remoteTest = remoteSource.getHackerNews(null).test()

        remoteTest.assertNoErrors()
        remoteTest.assertComplete()
        assertEquals(0, remoteTest.values().first().hits.size)

        Mockito.verify(hackerNewsService).searchByDate(null)
    }
}
