package org.ktc2.cokaen.wouldyouin.core_navigation

import com.your.`package`.core.navigation.R

sealed class DeepLinkDestination(val addressRes: Int) {

    object Serialization {
        object Gson : DeepLinkDestination(R.string.serialization_gson_deeplink_url)
    }

    object Home : DeepLinkDestination(R.string.home_deeplink_url)
}

fun DeepLinkDestination.getDeepLink(context: Context) = context.getString(this.addressRes)
