package org.ktc2.cokaen.wouldyouin.feat_profile.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.MemberResponse
import org.ktc2.cokaen.wouldyouin.network.repository.MemberAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: MemberAPIRetrofitRepository
) : ViewModel() {
    private val _memberProfile = MutableLiveData<MemberResponse?>()
    val memberProfile: LiveData<MemberResponse?> get() = _memberProfile

    fun fetchMemberProfile(memberId: Long) {
        viewModelScope.launch {
            val response = repository.getMemberProfile(memberId)
            _memberProfile.value = response?.data
        }
    }
}