package com.example.ravenhackernews.hackernews.presentation.states

import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto

sealed class HackerNewsUiState {
    object Loading : HackerNewsUiState()

    object SuccessDeletion : HackerNewsUiState()

    data class Success(
        val items: List<HackerNewDto>,
    ) : HackerNewsUiState()

    data class Error(
        val message: String,
    ) : HackerNewsUiState()
}
