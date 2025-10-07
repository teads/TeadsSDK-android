package tv.teads.teadssdkdemo.v6.ui.compose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route

/**
 * Reusable composable for animated screen transitions with slide animations
 * @param currentRoute The current route to display
 * @param content The content to display for each route
 */
@Composable
fun AnimatedScreenTransition(
    currentRoute: Route,
    content: @Composable (Route) -> Unit
) {
    AnimatedContent(
        targetState = currentRoute,
        transitionSpec = {
            // Determine slide direction based on route hierarchy
            val isForward = when {
                targetState == Route.Demo -> false // Going back to demo
                initialState == Route.Demo -> true  // Going forward from demo
                else -> false // Default to backward
            }
            
            if (isForward) {
                // Slide in from right, slide out to left
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { fullWidth -> fullWidth }
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            } else {
                // Slide in from left, slide out to right
                slideInHorizontally(
                    animationSpec = tween(300),
                    initialOffsetX = { fullWidth -> -fullWidth }
                ) togetherWith slideOutHorizontally(
                    animationSpec = tween(300),
                    targetOffsetX = { fullWidth -> fullWidth }
                )
            }
        },
        label = "screen_transition"
    ) { route ->
        content(route)
    }
}
