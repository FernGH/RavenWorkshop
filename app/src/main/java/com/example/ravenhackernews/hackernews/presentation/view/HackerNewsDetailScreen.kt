package com.example.ravenhackernews.hackernews.presentation.view

import android.annotation.SuppressLint
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.ravenhackernews.core.definitions.Constants
import androidx.compose.ui.res.stringResource
import com.example.ravenhackernews.R

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun HackerNewsDetailScreen(
    url: String,
    navController: NavController
) {
    var isLoading by remember { mutableStateOf(true) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    Box(Modifier.fillMaxSize()) {

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    webViewRef = this
                    settings.javaScriptEnabled = true
                    settings.domStorageEnabled = true
                    settings.defaultTextEncodingName = "utf-8"
                    settings.loadWithOverviewMode = true
                    settings.useWideViewPort = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            isLoading = false
                        }

                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            super.onReceivedError(view, request, error)
                            isLoading = false
                            val ctx = view?.context ?: return
                            val description = error?.description?.toString().orEmpty()

                            val message = when {
                                description.contains("ERR_CONNECTION_TIMED_OUT", true) ->
                                    ctx.getString(R.string.error_timeout)
                                description.contains("ERR_BLOCKED_BY_ORB", true) ->
                                    ctx.getString(R.string.error_blocked_by_orb)
                                description.contains("ERR_INTERNET_DISCONNECTED", true) ->
                                    ctx.getString(R.string.error_no_internet)
                                else ->
                                    ctx.getString(R.string.error_generic)
                            }

                            view.loadDataWithBaseURL(
                                null,
                                Constants.getGenericErrorHtml(message),
                                "text/html; charset=utf-8",
                                "utf-8",
                                null
                            )
                            showErrorDialog = true
                        }
                    }

                    loadUrl(url)
                }
            },
            update = { webViewRef = it }
        )

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text(stringResource(R.string.dialog_title)) },
                text = { Text(stringResource(R.string.dialog_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showErrorDialog = false
                            navController.popBackStack()
                            isLoading = true
                        }
                    ) {
                        Text(stringResource(R.string.dialog_close))
                    }
                }
            )
        }
    }
}
