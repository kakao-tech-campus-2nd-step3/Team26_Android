package org.ktc2.cokaen.wouldyouin.core_navigation

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions

fun NavController.deepLinkNavigateTo(
    context: Context,
    deepLinkDestination: DeepLinkDestination,
    popUpTo: Boolean = false
) {
    val builder = NavOptions.Builder()

    if (popUpTo) {
        builder.setPopUpTo(graph.startDestinationId, true)
    }

    navigate(
        buildDeepLink(context, deepLinkDestination),
        builder.build()
    )
}

private fun buildDeepLink(context: Context, destination: DeepLinkDestination): NavDeepLinkRequest =
    NavDeepLinkRequest.Builder
        .fromUri(Uri.parse(destination.getDeepLink(context)))
        .build()