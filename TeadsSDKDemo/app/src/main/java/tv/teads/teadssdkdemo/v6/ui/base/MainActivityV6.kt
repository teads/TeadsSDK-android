package tv.teads.teadssdkdemo.v6.ui.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.viewmodel.compose.viewModel
import tv.teads.teadssdkdemo.v6.ui.base.navigation.NavigationHandler
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route
import tv.teads.teadssdkdemo.v6.ui.compose.MediaColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaLazyColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeColumnScreen
import tv.teads.teadssdkdemo.v6.ui.compose.MediaNativeLazyColumnScreen
import tv.teads.teadssdkdemo.v6.ui.base.theme.TeadsSDKDemoTheme

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
                                    when (currentRoute) {
                                        Route.MediaColumn -> "Media Column"
                                        Route.MediaLazyColumn -> "Media LazyColumn"
                                        Route.MediaNativeColumn -> "Media Native Column"
                                        Route.MediaNativeLazyColumn -> "Media Native LazyColumn"
                                        Route.Demo -> "Teads SDK Demo V6"
                                        else -> "Teads SDK Demo V6"
                                    }
                                ) 
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            navigationIcon = {
                                when (currentRoute) {
                                    Route.MediaColumn, Route.MediaLazyColumn, 
                                    Route.MediaNativeColumn, Route.MediaNativeLazyColumn -> {
                                        IconButton(onClick = { currentRoute = Route.Demo }) {
                                            Icon(
                                                imageVector = Icons.Filled.ArrowBack,
                                                contentDescription = "Back to Demo"
                                            )
                                        }
                                    }
                                    else -> null
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
                                    Route.MediaColumn, Route.MediaLazyColumn,
                                    Route.MediaNativeColumn, Route.MediaNativeLazyColumn -> {
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

                        else -> {}
                    }
                }
            }
        }
    }
}