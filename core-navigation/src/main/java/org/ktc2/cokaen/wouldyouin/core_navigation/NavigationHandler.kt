import android.content.Context
import android.content.Intent
import android.net.Uri
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

    private fun navigateToFragment(command: NavigationCommand) {
        val deepLinkRequest = buildDeepLinkRequest(command)
        command.navOptions?.let { navController.navigate(it) }
    }
    private fun buildDeepLinkRequest(command: NavigationCommand): NavDeepLinkRequest {
        val deepLinkUri = buildDeepLinkUri(command)
        return NavDeepLinkRequest.Builder.fromUri(deepLinkUri).build()
    }

    private fun buildDeepLinkUri(command: NavigationCommand): Uri {
        val destination = command.destination as NavigationDestination.Activity
        val baseUri = Uri.parse(destination.deepLink) // Parse the deep link string into a Uri object

        // Build the URI using the base URI and any additional parameters from the command
        val uriBuilder = Uri.Builder().scheme(baseUri.scheme).authority(baseUri.host).path(baseUri.path)

        // Add query parameters if necessary
        command.data.forEach { (key, value) ->
            uriBuilder.appendQueryParameter(key, value)
        }

        return uriBuilder.build()
    }
    private fun navigateToActivity(command: NavigationCommand) {
        val intent = Intent(Intent.ACTION_VIEW, buildDeepLinkUri(command))
        applyActivityOptions(intent, command.activityOptions)
        context.startActivity(intent)
    }

    private fun applyActivityOptions(intent: Intent, options: ActivityNavigationOptions?) {
        options?.let {
            var flags = it.flags
            if (it.clearTop) flags = flags or Intent.FLAG_ACTIVITY_CLEAR_TOP
            if (it.singleTop) flags = flags or Intent.FLAG_ACTIVITY_SINGLE_TOP
            intent.addFlags(flags)
        }
    }

    // 기타 필요한 메서드들...
}