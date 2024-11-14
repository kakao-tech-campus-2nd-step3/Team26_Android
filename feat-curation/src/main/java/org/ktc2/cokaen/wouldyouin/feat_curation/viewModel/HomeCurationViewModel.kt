package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.CurationResponse
import org.ktc2.cokaen.wouldyouin.network.repository.CurationAPIRetrofitRepository
import javax.inject.Inject

@HiltViewModel
class HomeCurationViewModel @Inject constructor(
    private val curationRepository: CurationAPIRetrofitRepository,
    application: Application
) : ViewModel() {

    private val context = application.applicationContext

    private val sharedPreferences = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE)
    private var selectedArea: String = sharedPreferences.getString("selectedRegion", "전체") ?: "전체"

    private val _curationList = MutableLiveData<List<CurationResponse>?>(emptyList())
    val curationList: MutableLiveData<List<CurationResponse>?> = _curationList

    private val _selectedCuration = MutableLiveData<CurationResponse>()
    val selectedCuration: LiveData<CurationResponse> = _selectedCuration

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private var lastId: Long = Long.MAX_VALUE
    private var isLastPage: Boolean = false
    private var currentPage: Int = 0


    init {
        loadCurationList()
    }

    fun loadCurationList(area: String = selectedArea, page: Int = currentPage, size: Int = 10) {
        if (isLoading.value == true || isLastPage) return

        _isLoading.value = true
        Log.d("CurationList", "Called for page: $page with lastId: $lastId")

        viewModelScope.launch {
            try {
                val response = curationRepository.getCurationList(area, page, size, lastId)
                if (response.curations.isNotEmpty()) {
                    _curationList.postValue((_curationList.value.orEmpty() + response.curations).distinctBy { it.id })
                    lastId = response.curations.last().id
                    currentPage++
                } else {
                    isLastPage = true
                }
            } catch (e: Exception) {
                ToastUtils.showShortToast(context, e.message ?: "큐레이션 목록 조회에 실패했습니다. 다시 시도해주세요.")
                Log.e("CurationViewModel", "Error loading curation list", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun resetPagination() {
        currentPage = 0
        lastId = Long.MAX_VALUE // 초기 lastId로 리셋
        isLastPage = false
        _curationList.value = emptyList()
    }

    fun updateSelectedArea(newArea: String) {
        selectedArea = newArea
        resetPagination() // 선택된 지역이 바뀌면 페이지네이션 초기화
        loadCurationList() // 초기화 후 첫 페이지 로드
    }

    fun selectCuration(curation: CurationResponse) {
        _selectedCuration.value = curation
    }
}