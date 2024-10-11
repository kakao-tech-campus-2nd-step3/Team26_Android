package org.ktc2.cokaen.wouldyouin.core_navigation

sealed class NavigationDestination {
    abstract val deepLink: String?

    data class Fragment(override val deepLink: String) : NavigationDestination()
    data class Activity(override val deepLink: String) : NavigationDestination()
}