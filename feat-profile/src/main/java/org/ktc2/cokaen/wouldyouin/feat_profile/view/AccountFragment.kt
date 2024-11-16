package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.core_navigation.ActivityNavigationOptions
import org.ktc2.cokaen.wouldyouin.core_navigation.DeepLinkDestinations
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommand
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationCommandBuilder
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationDestination
import org.ktc2.cokaen.wouldyouin.core_navigation.NavigationUtil
import org.ktc2.cokaen.wouldyouin.feat_profile.R
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.FragmentAccountBinding
import org.ktc2.cokaen.wouldyouin.network.AuthPreferenceManager
import javax.inject.Inject

@AndroidEntryPoint
class AccountFragment : Fragment() {
    @Inject
    lateinit var navigationUtil: NavigationUtil
    private lateinit var binding: FragmentAccountBinding

    @Inject
    lateinit var authPrefs: AuthPreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            myReview.setOnClickListener {
                startActivityTo(DeepLinkDestinations.MY_EVENT_REVIEW_ACTIVITY)
            }

            payHistory.setOnClickListener {
                startActivityTo(DeepLinkDestinations.BOOKING_LIST_ACTIVITY)
            }

            ask.setOnClickListener {
                openWebPage("https://www.google.com")
            }

            logout.setOnClickListener{
                logout()
            }
        }
    }

    private fun startActivityTo(activity: Int) {
        val command = NavigationCommand(
            destination = NavigationDestination.Activity(activity),
            data = mapOf("key" to "value"),
            activityOptions = ActivityNavigationOptions(
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
        navigationUtil.navigate(command)
    }

    private fun openWebPage(url: String) {
        val webpage: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, webpage)
        startActivity(intent)
    }

    private fun logout() {
        authPrefs.clearAll()  // 모든 사용자 정보 삭제
        navigationUtil.navigate(
            NavigationCommand(
                destination = NavigationDestination.Activity(DeepLinkDestinations.MAIN_ACTIVITY),
                activityOptions = ActivityNavigationOptions(
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                ),
            )
        )
        requireActivity().finish()
    }
}
