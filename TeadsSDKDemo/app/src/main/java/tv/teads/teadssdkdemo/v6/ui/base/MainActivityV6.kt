package tv.teads.teadssdkdemo.v6.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.ui.base.navigation.NavigationHandler
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route
import tv.teads.teadssdkdemo.v6.ui.base.theme.TeadsSDKDemoTheme
import tv.teads.teadssdkdemo.v6.ui.compose.FeedColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.FeedLazyColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaAdmobColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaAppLovinColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaFeedColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaLazyColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeAdmobColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeAppLovinColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeLazyColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeSmartColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaSmartColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.RecommendationsColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.RecommendationsLazyColumnScreen

class MainActivityV6 : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TeadsSDKDemoTheme {
                var currentRoute by remember { mutableStateOf<Route>(Route.Demo) }

                // Handle device back button
                val backDispatcher = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
                val lifecycleOwner = LocalLifecycleOwner.current

                DisposableEffect(currentRoute) {
                    val callback = object : OnBackPressedCallback(currentRoute != Route.Demo) {
                        override fun handleOnBackPressed() {
                            currentRoute = Route.Demo
                        }
                    }
                    backDispatcher?.addCallback(lifecycleOwner, callback)
                    onDispose { callback.remove() }
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = when (currentRoute) {
                                        Route.MediaColumn -> "Media Column"
                                        Route.MediaFeedColumn -> "Media Feed Column"
                                        Route.MediaLazyColumn -> "Media LazyColumn"
                                        Route.MediaNativeColumn -> "Media Native Column"
                                        Route.MediaNativeLazyColumn -> "Media Native LazyColumn"
                                        Route.MediaAdMobColumn -> "Media AdMob Column"
                                        Route.MediaNativeAdMobColumn -> "Media Native AdMob Column"
                                        Route.MediaAppLovinColumn -> "Media AppLovin Column"
                                        Route.MediaNativeAppLovinColumn -> "Media Native AppLovin Column"
                                        Route.MediaSmartColumn -> "Media Smart Column"
                                        Route.MediaNativeSmartColumn -> "Media Native Smart Column"
                                        Route.FeedColumn -> "Feed Column"
                                        Route.FeedLazyColumn -> "Feed LazyColumn"
                                        Route.RecommendationsColumn -> "Recommendations Column"
                                        Route.RecommendationsLazyColumn -> "Recommendations LazyColumn"
                                        else -> "Teads SDK Demo"
                                    },
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 24.sp
                                    ),
                                    textAlign = if (currentRoute == Route.Demo) TextAlign.Center else TextAlign.Left,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            navigationIcon = {
                                when (currentRoute) {
                                    Route.MediaColumn, Route.MediaFeedColumn, Route.MediaLazyColumn,
                                    Route.MediaNativeColumn, Route.MediaNativeLazyColumn,
                                    Route.MediaAdMobColumn, Route.MediaNativeAdMobColumn,
                                    Route.MediaAppLovinColumn, Route.MediaNativeAppLovinColumn,
                                    Route.MediaSmartColumn, Route.MediaNativeSmartColumn,
                                    Route.FeedColumn, Route.FeedLazyColumn,
                                    Route.RecommendationsColumn, Route.RecommendationsLazyColumn -> {
                                        IconButton(onClick = { currentRoute = Route.Demo }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                                contentDescription = "Back to Demo"
                                            )
                                        }
                                    }
                                    else -> {}
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    when (currentRoute) {
                        Route.Demo -> {
                            val viewModel: DemoViewModel = viewModel()

                            // Set up navigation callback
                            viewModel.setOnNavigateCallback { navRoute ->
                                when (navRoute) {
                                    Route.MediaColumn, Route.MediaFeedColumn, Route.MediaLazyColumn,
                                    Route.MediaNativeColumn, Route.MediaNativeLazyColumn,
                                    Route.MediaAdMobColumn, Route.MediaNativeAdMobColumn,
                                    Route.MediaAppLovinColumn, Route.MediaNativeAppLovinColumn,
                                    Route.MediaSmartColumn, Route.MediaNativeSmartColumn,
                                    Route.FeedColumn, Route.FeedLazyColumn,
                                    Route.RecommendationsColumn, Route.RecommendationsLazyColumn -> {
                                        currentRoute = navRoute
                                    }
                                    else -> {
                                        NavigationHandler.navigateToRoute(
                                            fromActivity = this@MainActivityV6,
                                            route = navRoute
                                        )
                                    }
                                }
                            }

                            DemoScreen(
                                modifier = Modifier.padding(paddingValues),
                                viewModel = viewModel
                            )
                        }
                        Route.MediaColumn -> {
                            MediaColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaFeedColumn -> {
                            MediaFeedColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaLazyColumn -> {
                            MediaLazyColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaNativeColumn -> {
                            MediaNativeColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaNativeLazyColumn -> {
                            MediaNativeLazyColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.FeedColumn -> {
                            FeedColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.FeedLazyColumn -> {
                            FeedLazyColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.RecommendationsColumn -> {
                            RecommendationsColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.RecommendationsLazyColumn -> {
                            RecommendationsLazyColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaAdMobColumn -> {
                            MediaAdmobColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaNativeAdMobColumn -> {
                            MediaNativeAdmobColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaAppLovinColumn -> {
                            MediaAppLovinColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaNativeAppLovinColumn -> {
                            MediaNativeAppLovinColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaSmartColumn -> {
                            MediaSmartColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        Route.MediaNativeSmartColumn -> {
                            MediaNativeSmartColumnScreen(
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                        else -> {}
                    }
                }
            }
        }
    }
}