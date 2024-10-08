package org.ktc2.cokaen.wouldyouin.core_navigation

class NavigationCommandBuilder {
    private var destination: NavigationDestination? = null
    private val data = mutableMapOf<String, String>()
    private var flags: Int = -1

    fun to(destination: NavigationDestination) = apply { this.destination = destination }
    fun withData(key: String, value: String) = apply { data[key] = value }
    fun withFlags(flags: Int) = apply { this.flags = flags }

    fun build(): NavigationCommand {
        requireNotNull(destination) { "Destination must be set" }
        return NavigationCommand(destination!!, data, flags)
    }
}