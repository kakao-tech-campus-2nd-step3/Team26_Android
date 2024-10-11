package org.ktc2.cokaen.wouldyouin.core_navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.your.`package`.core.navigation.R

class NavigationHandler(
    private val context: Context,
    private val navController: NavController
) : NavigationUtil {

    override fun navigate(command: NavigationCommand) {
        when (command.destination) {
            is NavigationDestination.Fragment -> navigateToFragment(command)
            is NavigationDestination.Activity -> navigateToActivity(command)
        }
    }

    private fun navigateToFragment(command: NavigationCommand) {
        val deepLinkRequest = buildDeepLinkRequest(command)
        navController.navigate(deepLinkRequest, command.navOptions ?: NavOptions.Builder().build())
    }

    private fun navigateToActivity(command: NavigationCommand) {
        val intent = Intent(Intent.ACTION_VIEW, buildDeepLinkUri(command))
        applyNavOptionsToIntent(intent, command.navOptions)
        context.startActivity(intent)
    }

    private fun applyNavOptionsToIntent(intent: Intent, navOptions: NavOptions?) {
        var flags = Intent.FLAG_ACTIVITY_NEW_TASK

        navOptions?.let { options ->
            if (options.shouldLaunchSingleTop()) {
                flags = flags or Intent.FLAG_ACTIVITY_SINGLE_TOP
            }
        }

        intent.addFlags(flags)
    }

    private fun buildDeepLinkRequest(command: NavigationCommand): NavDeepLinkRequest {
        return NavDeepLinkRequest.Builder
            .fromUri(buildDeepLinkUri(command))
            .build()
    }

    private fun buildDeepLinkUri(command: NavigationCommand): Uri {
        val uriBuilder = Uri.parse(command.destination.deepLink).buildUpon()
        command.data.forEach { (key, value) ->
            uriBuilder.appendQueryParameter(key, value)
        }
        return uriBuilder.build()
    }
}