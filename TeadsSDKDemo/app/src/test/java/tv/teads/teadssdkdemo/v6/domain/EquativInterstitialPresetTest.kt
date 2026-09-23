package tv.teads.teadssdkdemo.v6.domain

import org.junit.Assert.assertEquals
import org.junit.Test
import tv.teads.teadssdkdemo.v6.ui.base.DemoViewModel
import tv.teads.teadssdkdemo.v6.ui.base.navigation.Route
import tv.teads.teadssdkdemo.v6.ui.base.navigation.RouteFactory

class EquativInterstitialPresetTest {

    @Test
    fun `Teads preset uses the mediation placement`() {
        val placement = EquativInterstitialPreset.TEADS.placement

        assertEquals(787047L, placement.siteId)
        assertEquals(2231145L, placement.pageId)
        assertEquals(158215L, placement.formatId)
    }

    @Test
    fun `Equativ interstitial uses Compose column route`() {
        val route = RouteFactory.createRoute(
            format = FormatType.INTERSTITIAL,
            provider = ProviderType.EQUATIV,
            integration = IntegrationType.COLUMN,
            displayMode = null
        )

        assertEquals(Route.InterstitialEquativColumn, route)
    }

    @Test
    fun `main screen offers Teads and Equativ test presets`() {
        val viewModel = DemoViewModel()
        viewModel.onFormatChipClick(
            viewModel.getFormatChips().indexOfFirst { it.text == "Interstitial" }
        )
        viewModel.onProviderChipClick(
            viewModel.getProviderChips().indexOfFirst { it.text == "Equativ" }
        )

        assertEquals(
            listOf("Teads Ad", "Equativ Test Ad"),
            viewModel.getPidChips().map { it.text }
        )
    }
}
