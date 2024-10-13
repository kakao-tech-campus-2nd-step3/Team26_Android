import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil

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

    private fun navigateToActivity(command: NavigationCommand) {
        val intent = Intent(Intent.ACTION_VIEW, buildDeepLinkUri(command))
        applyActivityOptions(intent, command.activityOptions)
        context.startActivity(intent)
    }

    private fun buildDeepLinkRequest(command: NavigationCommand): NavDeepLinkRequest {
        val deepLinkUri = buildDeepLinkUri(command)
        return NavDeepLinkRequest.Builder.fromUri(deepLinkUri).build()
    }

    private fun buildDeepLinkUri(command: NavigationCommand): Uri {
        val deepLink = when (val link = command.destination.deepLink) {
            is Int -> context.resources.getString(link)
            is String -> link
            else -> throw IllegalArgumentException("deepLink must be either Int or String")
        }

        return Uri.parse(deepLink).buildUpon().apply {
            command.data.forEach { (key, value) ->
                appendQueryParameter(key, value)
            }
        }.build()
    }

    private fun navigateToFragment(command: NavigationCommand) {
        val deepLinkRequest = buildDeepLinkRequest(command)
        try {
            navController.navigate(deepLinkRequest)
        } catch (e: IllegalArgumentException) {
            Log.e("NavigationHandler", "Navigation failed: ${e.message}")
        }
    }

    private fun applyActivityOptions(intent: Intent, options: ActivityNavigationOptions?) {
        options?.let {
            var flags = it.flags
            if (it.clearTop) flags = flags or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (it.singleTop) flags = flags or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.addFlags(flags)
        }
    }
}