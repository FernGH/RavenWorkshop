package com.example.ravenhackernews.hackernews.presentation.viewmodels

import com.example.ravenhackernews.hackernews.domain.usecase.DeleteHackerNewsUseCase
import com.example.ravenhackernews.hackernews.domain.usecase.RetrieveHackerNewsUseCase
import com.example.ravenhackernews.hackernews.mocks.HackerNewsMocks
import com.example.ravenhackernews.hackernews.presentation.states.HackerNewsUiState
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner.StrictStubs::class)
class HackerNewsViewModelTest {

    private val retrieveUseCase: RetrieveHackerNewsUseCase = mock()
    private val deleteUseCase: DeleteHackerNewsUseCase = mock()

    private lateinit var viewModel: HackerNewsViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val mocks = HackerNewsMocks()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = HackerNewsViewModel(retrieveUseCase, deleteUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchNews emits Success state when retrieveUseCase succeeds`() = runTest {
        val list = mocks.hackerDtoList()

        whenever(retrieveUseCase.execute(any()))
            .thenReturn(Single.just(list))

        viewModel.fetchNews("android")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is HackerNewsUiState.Success)
        assertEquals(list, (state as HackerNewsUiState.Success).items)
    }

    @Test
    fun `fetchNews emits Error state when retrieveUseCase fails`() = runTest {
        val exception = RuntimeException("Network error")
        whenever(retrieveUseCase.execute(any()))
            .thenReturn(Single.error(exception))

        viewModel.fetchNews("android")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is HackerNewsUiState.Error)
        assertEquals("Network error", (state as HackerNewsUiState.Error).message)
    }

    @Test
    fun `delete emits SuccessDeletion when deleteUseCase completes`() = runTest {
        whenever(deleteUseCase.execute(any()))
            .thenReturn(Completable.complete())

        viewModel.delete("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is HackerNewsUiState.SuccessDeletion)
    }

    @Test
    fun `delete emits Error when deleteUseCase fails`() = runTest {
        val exception = RuntimeException("Delete failed")
        whenever(deleteUseCase.execute(any()))
            .thenReturn(Completable.error(exception))

        viewModel.delete("1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assert(state is HackerNewsUiState.Error)
        assertEquals("Delete failed", (state as HackerNewsUiState.Error).message)
    }
}
