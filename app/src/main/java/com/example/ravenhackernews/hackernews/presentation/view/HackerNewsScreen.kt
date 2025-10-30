package com.example.ravenhackernews.hackernews.presentation.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ravenhackernews.R
import com.example.ravenhackernews.core.definitions.Constants.DEFAULT_QUERY
import com.example.ravenhackernews.hackernews.domain.dto.HackerNewDto
import com.example.ravenhackernews.hackernews.presentation.states.HackerNewsUiState
import com.example.ravenhackernews.hackernews.presentation.viewmodels.HackerNewsViewModel


@Composable
fun HackerNewsRoute(onOpen: (String) -> Unit) {
    val viewModel: HackerNewsViewModel = hiltViewModel()
    HackerNewsScreen(
        onOpen = onOpen,
        viewModel = viewModel
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HackerNewsScreen(
    onOpen: (String) -> Unit,
    viewModel: HackerNewsViewModel,
) {
    val state by viewModel.uiState.collectAsState()
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_bar_title)) })
        },
    ) { padding ->
        val pullState = rememberPullToRefreshState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            PullToRefreshBox(
                state = pullState,
                isRefreshing = state is HackerNewsUiState.Loading,
                onRefresh = { viewModel.fetchNews(DEFAULT_QUERY) },
            ) {
                when (val ui = state) {
                    is HackerNewsUiState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is HackerNewsUiState.Error -> {
                        errorMessage = ui.message
                        showErrorDialog = true
                    }

                    is HackerNewsUiState.Success -> {
                        NewsList(
                            news = ui.items,
                            onOpen = onOpen,
                            onDelete = { id -> viewModel.delete(id) },
                        )
                    }

                    HackerNewsUiState.SuccessDeletion -> viewModel.fetchNews(DEFAULT_QUERY)
                }

                if (showErrorDialog && errorMessage != null) {
                    AlertDialog(
                        onDismissRequest = { },
                        title = { Text(stringResource(R.string.dialog_title)) },
                        text = { Text(errorMessage ?: stringResource(R.string.dialog_message)) },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showErrorDialog = false
                                    errorMessage = null
                                    viewModel.fetchNews(DEFAULT_QUERY)
                                }
                            ) {
                                Text(stringResource(R.string.dialog_retry))
                            }
                        }
                    )

                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewsList(
    news: List<HackerNewDto>,
    onOpen: (String) -> Unit,
    onDelete: (String) -> Unit,
) {

    var showErrorDialog by remember { mutableStateOf(false) }
    var errorTitle by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(news, key = { it.id }) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { value ->
                    if (value == SwipeToDismissBoxValue.EndToStart) {
                        onDelete(item.id)
                        true
                    } else false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                enableDismissFromEndToStart = true,
                backgroundContent = {
                    val color = when (dismissState.targetValue) {
                        SwipeToDismissBoxValue.EndToStart -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_content_description),
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.delete_label),
                                color = MaterialTheme.colorScheme.onError,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                },
                content = {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable {
                                if (item.url.isBlank()) {
                                   showErrorDialog = true
                                } else {
                                    onOpen(item.url)
                                }
                            },
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        ListItem(
                            headlineContent = { Text(item.title) },
                            supportingContent = {
                                Text("${item.author} • ${item.createdAt}")
                            }
                        )
                    }
                }
            )
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text(stringResource(R.string.dialog_generic_title)) },
            text = { Text(stringResource(R.string.dialog_impossible_message)) },
            confirmButton = {
                TextButton(
                    onClick = { showErrorDialog = false }
                ) {
                    Text(stringResource(R.string.dialog_close))
                }
            }
        )
    }
}
