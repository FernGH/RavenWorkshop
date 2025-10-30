package com.example.ravenhackernews.hackernews.domain.usecase

import com.example.ravenhackernews.core.exceptions.UseCaseException
import com.example.ravenhackernews.hackernews.domain.repository.HackerNewsRepository
import io.reactivex.rxjava3.core.Completable
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner.StrictStubs::class)
class DeleteHackerNewsUseCaseTest {

    private companion object {
        const val BD_FAIL = "DB failure"
    }

    private lateinit var hackerNewsRepository: HackerNewsRepository
    private lateinit var useCase: DeleteHackerNewsUseCase

    @Before
    fun setup() {
        hackerNewsRepository = mock()
        useCase = DeleteHackerNewsUseCase(hackerNewsRepository)
    }

    @Test
    fun `complete successfully when valid id is provided`() {
        val id = "123"
        val params = DeleteHackerNewsUseCase.DeleteHackerNewsUseCaseParams(id)
        Mockito.`when`(hackerNewsRepository.deleteNewById(id))
            .thenReturn(Completable.complete())
        
        val testCase = useCase.buildUseCase(params).test()

        testCase.assertComplete()
        testCase.assertNoErrors()

        Mockito.verify(hackerNewsRepository).deleteNewById(id)
    }

    @Test
    fun `emit error when params are null`() {
        val testCase = useCase.buildUseCase(null).test()

        testCase.assertError(UseCaseException.GenericException::class.java)
        testCase.assertNotComplete()

        Mockito.verifyNoInteractions(hackerNewsRepository)
    }

    @Test
    fun `emit error when repository returns error`() {
        val id = "456"
        val params = DeleteHackerNewsUseCase.DeleteHackerNewsUseCaseParams(id)
        val exception = RuntimeException(BD_FAIL)

        Mockito.`when`(hackerNewsRepository.deleteNewById(id))
            .thenReturn(Completable.error(exception))

        val testCase = useCase.buildUseCase(params).test()

        testCase.assertError(exception)
        testCase.assertNotComplete()

        Mockito.verify(hackerNewsRepository).deleteNewById(id)
    }
}
