package org.ktc2.cokaen.wouldyouin.core_navigation

sealed class NavigationDestination {
    abstract val deepLink: Any

    data class Fragment(override val deepLink: Any) : NavigationDestination()
    data class Activity(override val deepLink: Any) : NavigationDestination()
}