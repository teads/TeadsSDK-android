package tv.teads.teadssdkdemo.v6.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = TeadsPrimaryDark,
    primaryContainer = TeadsPrimaryVariantDark,
    secondary = TeadsSecondaryDark,
    background = TeadsBackgroundDark,
    surface = TeadsSurfaceDark,
    onPrimary = TeadsOnPrimaryDark,
    onSecondary = TeadsOnSecondaryDark,
    onBackground = TeadsOnBackgroundDark,
    onSurface = TeadsOnSurfaceDark,
)

private val LightColorScheme = lightColorScheme(
    primary = TeadsPrimary,
    primaryContainer = TeadsPrimaryVariant,
    secondary = TeadsSecondary,
    background = TeadsBackground,
    surface = TeadsSurface,
    onPrimary = TeadsOnPrimary,
    onSecondary = TeadsOnSecondary,
    onBackground = TeadsOnBackground,
    onSurface = TeadsOnSurface,
)

@Composable
fun TeadsSDKDemoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = TeadsTypography,
        content = content
    )
}