package com.example.ravenhackernews.hackernews.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.ravenhackernews.hackernews.presentation.view.HackerNewsDetailScreen
import com.example.ravenhackernews.hackernews.presentation.view.HackerNewsRoute
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun RavenNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "news"
    ) {
        composable("news") {
            HackerNewsRoute(onOpen = { url ->
                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                navController.navigate("detail/$encodedUrl")
            })
        }

        composable("detail/{url}") { backStackEntry ->
            val url = backStackEntry.arguments?.getString("url") ?: ""
            HackerNewsDetailScreen(
                url = url,
                navController = navController
            )
        }
    }
}
