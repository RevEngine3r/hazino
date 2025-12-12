package r.finance.hazino.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class CustomWalletItemColors(
    val walletItemBackgroundGray: Color,
    val walletItemBorderGray: Color,
    val walletItemBackgroundGreen: Color,
    val walletItemBorderGreen: Color,
    val walletItemBackgroundRed: Color,
    val walletItemBorderRed: Color,
)

val customWalletItemColorScheme: CustomWalletItemColors
    @Composable get() = if (isSystemInDarkTheme()) {
        // Dark
        CustomWalletItemColors(
            walletItemBackgroundGray = Color(0xFF616161),
            walletItemBorderGray = Color(0xFF9E9E9E),
            walletItemBackgroundGreen = Color(0xFF388E3C),
            walletItemBorderGreen = Color(0xFF4CAF50),
            walletItemBackgroundRed = Color(0xFFD32F2F),
            walletItemBorderRed = Color(0xFFF44336)

        )
    } else {
        // Light
        CustomWalletItemColors(
            walletItemBackgroundGray = Color(0xFF9E9E9E),
            walletItemBorderGray = Color(0xFF616161),
            walletItemBackgroundGreen = Color(0xFF4CAF50),
            walletItemBorderGreen = Color(0xFF388E3C),
            walletItemBackgroundRed = Color(0xFFF44336),
            walletItemBorderRed = Color(0xFFD32F2F)
        )
    }