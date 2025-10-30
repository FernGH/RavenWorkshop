package com.example.ravenhackernews.hackernews.data.repository

import com.example.ravenhackernews.core.exceptions.UseCaseException
import com.example.ravenhackernews.core.extesions.value
import com.example.ravenhackernews.hackernews.data.local.model.entity.HackerNewEntity
import com.example.ravenhackernews.hackernews.data.mapper.HackerNewToEntityMapper
import com.example.ravenhackernews.hackernews.data.remote.model.entity.Hit
import com.example.ravenhackernews.hackernews.data.remote.model.response.NewsResponse
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsLocalSource
import com.example.ravenhackernews.hackernews.domain.datasource.HackerNewsSource
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import com.example.ravenhackernews.hackernews.domain.mapper.HackerEntityToDtoMapper
import com.example.ravenhackernews.hackernews.mocks.HackerNewsMocks
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class HackerNewsDataRepositoryTest {
    private companion object {
        const val QUERY_ANDROID = "android"
        const val ERROR_NETWORK = "Network error"
        const val TEST_ID = "123"
        const val LOCAL_TITLE = "Local Android News"
        const val REMOTE_TITLE = "Kotlin News"
        const val NEWS_TITLE = "Android News"
        const val LOCAL_AUTHOR = "author"
        const val LOCAL_URL = "url"
        const val CREATED_AT = "2025"
    }

    private lateinit var remoteSource: HackerNewsSource
    private lateinit var localSource: HackerNewsLocalSource
    private lateinit var mapperToEntity: HackerNewToEntityMapper
    private lateinit var mapperToDto: HackerEntityToDtoMapper
    private lateinit var repository: HackerNewsDataRepository

    private val mocks = HackerNewsMocks()

    @Before
    fun setup() {
        remoteSource = mock()
        localSource = mock()
        mapperToEntity = mock()
        mapperToDto = mock()
        repository = HackerNewsDataRepository(
            remoteSource,
            localSource,
            mapperToEntity,
            mapperToDto
        )

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
    fun `return merged and filtered list when remote fetch succeeds`() {
        val response = NewsResponse(mocks.hackerHitList())
        val localEntities = listOf(
            HackerNewEntity("1", NEWS_TITLE, LOCAL_URL, LOCAL_AUTHOR, CREATED_AT, true)
        )

        whenever(remoteSource.getHackerNews(QUERY_ANDROID)).thenReturn(Single.just(response))
        whenever(localSource.getHackerNews()).thenReturn(Single.just(localEntities))
        whenever(localSource.updateAll(any())).thenReturn(Completable.complete())

        whenever(mapperToEntity.transform(any())).thenAnswer { inv ->
            val hit = inv.arguments[0] as Hit
            HackerNewEntity(
                hit.objectID.value(),
                hit.title.value(),
                hit.url.value(),
                hit.author.value(),
                hit.created_at.value(),
                false
            )
        }
        whenever(mapperToDto.transform(any())).thenAnswer { inv ->
            val entity = inv.arguments[0] as HackerNewEntity
            HackerNewDto(
                entity.id,
                entity.title,
                entity.url,
                entity.author,
                entity.createdAt
            )
        }

        val testObserver = repository.fetchHackerNews(QUERY_ANDROID).test()

        testObserver.assertNoErrors()
        testObserver.assertComplete()
        val result = testObserver.values().first()

        assertEquals(2, result.size)
        assertTrue(result.any { it.title == REMOTE_TITLE })
        assertTrue(result.any { it.title == NEWS_TITLE })

        Mockito.verify(localSource).updateAll(any())
    }

    @Test
    fun `return local data when remote fails but local not empty`() {
        val exception = RuntimeException(ERROR_NETWORK)
        val localEntities = listOf(
            HackerNewEntity("1", LOCAL_TITLE, LOCAL_URL, LOCAL_AUTHOR, CREATED_AT, false)
        )

        whenever(remoteSource.getHackerNews(QUERY_ANDROID)).thenReturn(Single.error(exception))
        whenever(localSource.getHackerNews()).thenReturn(Single.just(localEntities))
        whenever(mapperToDto.transform(any())).thenAnswer { inv ->
            val entity = inv.arguments[0] as HackerNewEntity
            HackerNewDto(
                entity.id,
                entity.title,
                entity.url,
                entity.author,
                entity.createdAt
            )
        }

        val repoTest = repository.fetchHackerNews(QUERY_ANDROID).test()

        repoTest.assertNoErrors()
        repoTest.assertComplete()
        val result = repoTest.values().first()

        assertEquals(1, result.size)
        assertEquals(LOCAL_TITLE, result.first().title)
    }

    @Test
    fun `propagate error when remote and local are both empty`() {
        val exception = RuntimeException(ERROR_NETWORK)

        whenever(remoteSource.getHackerNews(QUERY_ANDROID)).thenReturn(Single.error(exception))
        whenever(localSource.getHackerNews()).thenReturn(Single.just(emptyList()))

        val repoTest = repository.fetchHackerNews(QUERY_ANDROID).test()

        repoTest.assertError(UseCaseException.GenericException::class.java)
        repoTest.assertNotComplete()
    }

    @Test
    fun `call local soft delete when deleting by id`() {
        whenever(localSource.softDelete(TEST_ID)).thenReturn(Completable.complete())

        val repoTest = repository.deleteNewById(TEST_ID).test()

        repoTest.assertComplete()
        repoTest.assertNoErrors()

        Mockito.verify(localSource).softDelete(TEST_ID)
    }
}
