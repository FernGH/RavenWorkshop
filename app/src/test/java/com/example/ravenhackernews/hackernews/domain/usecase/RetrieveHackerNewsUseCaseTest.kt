package com.example.ravenhackernews.hackernews.domain.usecase

import com.example.ravenhackernews.core.exceptions.UseCaseException
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import com.example.ravenhackernews.hackernews.mocks.HackerNewsMocks
import io.reactivex.rxjava3.core.Single
import org.junit.Before

import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import org.mockito.junit.MockitoJUnitRunner
import kotlin.test.Test

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class RetrieveHackerNewsUseCaseTest {

    private lateinit var hackerNewsRepository: HackerNewsRepository
    private lateinit var useCase: RetrieveHackerNewsUseCase

    private val mocks = HackerNewsMocks()

    @Before
    fun setup() {
        hackerNewsRepository = mock()
        useCase = RetrieveHackerNewsUseCase(hackerNewsRepository)
    }

    @Test
    fun `return list of HackerNewDto when query is valid`() {
        val query = "android"
        val params = RetrieveHackerNewsUseCase.RetrieveHackerNewsUseCaseParams(query)
        val expectedList = mocks.hackerDtoList()

        `when`(hackerNewsRepository.fetchHackerNews(query))
            .thenReturn(Single.just(expectedList))


        val testCase = useCase.buildUseCase(params).test()

        testCase.assertValue(expectedList)
        testCase.assertComplete()
        testCase.assertNoErrors()

        Mockito.verify(hackerNewsRepository).fetchHackerNews(query)
    }

    @Test
    fun `emit error when params are null`() {

        val testObserver = useCase.buildUseCase(null).test()

        testObserver.assertError(UseCaseException.GenericException::class.java)
        testObserver.assertNotComplete()

        Mockito.verifyNoInteractions(hackerNewsRepository)
    }

    @Test
    fun `emit error when repository returns error`() {
        val query = "error"
        val params = RetrieveHackerNewsUseCase.RetrieveHackerNewsUseCaseParams(query)
        val exception = RuntimeException("Network error")

        `when`(hackerNewsRepository.fetchHackerNews(query))
            .thenReturn(Single.error(exception))

        val testCase = useCase.buildUseCase(params).test()

        testCase.assertError(exception)
        testCase.assertNotComplete()

        Mockito.verify(hackerNewsRepository).fetchHackerNews(query)
    }
}
