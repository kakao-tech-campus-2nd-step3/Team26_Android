package org.ktc2.cokaen.wouldyouin.core_navigation

import android.content.Intent

data class ActivityNavigationOptions(
    val flags: Int = Intent.FLAG_ACTIVITY_NEW_TASK,
    val clearTop: Boolean = false,
    val singleTop: Boolean = false
)

data class NavigationCommand(
    val destination: NavigationDestination,
    val data: Map<String, String> = emptyMap(),
    val navOptions: Int? = null,
    val activityOptions: ActivityNavigationOptions? = null
)