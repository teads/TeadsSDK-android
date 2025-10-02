package tv.teads.teadssdkdemo.v6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import tv.teads.teadssdkdemo.v6.ui.screens.DemoScreen
import tv.teads.teadssdkdemo.v6.ui.theme.TeadsSDKDemoTheme

class MainActivityV6 : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            TeadsSDKDemoTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Teads SDK Demo V6") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                titleContentColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                ) { paddingValues ->
                    DemoScreen(
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}