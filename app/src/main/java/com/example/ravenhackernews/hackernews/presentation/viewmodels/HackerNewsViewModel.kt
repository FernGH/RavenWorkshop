package com.example.ravenhackernews.hackernews.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ravenhackernews.hackernews.domain.usecase.DeleteHackerNewsUseCase
import com.example.ravenhackernews.hackernews.domain.usecase.RetrieveHackerNewsUseCase
import com.example.ravenhackernews.hackernews.presentation.states.HackerNewsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx3.await

@HiltViewModel
class HackerNewsViewModel
    @Inject
    constructor(
        private val retrieveHackerNewsUseCase: RetrieveHackerNewsUseCase,
        private val deleteHackerNewsUseCase: DeleteHackerNewsUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow<HackerNewsUiState>(HackerNewsUiState.Loading)
        val uiState: StateFlow<HackerNewsUiState> = _uiState

        init {
            fetchNews("android")
        }

        fun fetchNews(query: String) {
            viewModelScope.launch {
                _uiState.value = HackerNewsUiState.Loading
                try {
                    val result =
                        retrieveHackerNewsUseCase
                            .execute(
                                RetrieveHackerNewsUseCase.RetrieveHackerNewsUseCaseParams(query),
                            ).await()

                    _uiState.value = HackerNewsUiState.Success(result)
                } catch (e: Throwable) {
                    _uiState.value = HackerNewsUiState.Error(e.message ?: "Unknown error")
                }
            }
        }

        fun delete(idNew: String) {
            viewModelScope.launch {
                _uiState.value = HackerNewsUiState.Loading
                try {
                    deleteHackerNewsUseCase.execute(DeleteHackerNewsUseCase.DeleteHackerNewsUseCaseParams(idNew)).await()
                    _uiState.value = HackerNewsUiState.SuccessDeletion
                } catch (e: Throwable) {
                    _uiState.value = HackerNewsUiState.Error(e.message ?: "Delete failed")
                }
            }
        }
    }
