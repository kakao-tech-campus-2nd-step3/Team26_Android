package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.CurationCardResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.repository.CurationRepository
import javax.inject.Inject

@HiltViewModel
class CurationDetailViewModel @Inject constructor(
    private val curationRepository: CurationRepository,
    application: Application
) : ViewModel() {
    private val context = application.applicationContext

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _curation = MutableLiveData<CurationResponse?>()
    val curation: LiveData<CurationResponse?> = _curation

    private val _curationBlocks = MutableLiveData<List<CurationCardResponse>>()
    val curationBlocks: LiveData<List<CurationCardResponse>> = _curationBlocks

    private val _hashtags = MutableLiveData<List<String>>()
    val hashtags: LiveData<List<String>> = _hashtags

    fun loadCurationDetail(curationId: Long) {
        if (isLoading.value == true) return

        _isLoading.value = true
        viewModelScope.launch {
            try {
                val curationDetail = curationRepository.getCurationDetail(curationId)

                // CurationResponse 값을 변수에 적용
                _curation.value = curationDetail
                _curationBlocks.value = curationDetail.curationCards
                _hashtags.value = curationDetail.hashTag

            } catch (e: Exception) {
                ToastUtils.showShortToast(context, e.message ?: "큐레이션 상세 정보 조회에 실패했습니다. 다시 시도해주세요.")
                Log.e("CurationDetailViewModel", "Error loading curation detail", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}