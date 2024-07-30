package com.example.tec.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.tec.R

/**
 * Used for NavigationBar representation.
 */

enum class Screen {
    Home, Groups, About, Splash
}

/**
 * This data class holds information for every single item in [NavigationList]
 */
data class NavigationItemContent(
    val screen: Screen,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
)

object NavigationList {
    val navigationItemContentList = listOf(
        NavigationItemContent(
            screen = Screen.Home,
            icon = R.drawable.baseline_home_filled,
            label = R.string.home
        ),
        NavigationItemContent(
            screen = Screen.Groups,
            icon = R.drawable.baseline_groups,
            label = R.string.groups
        ),
        NavigationItemContent(
            screen = Screen.About,
            icon = R.drawable.baseline_info_outline,
            label = R.string.contact
        )
    )
}