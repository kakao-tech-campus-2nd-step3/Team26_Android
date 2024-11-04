package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import javax.inject.Inject

@HiltViewModel
class HomeCurationViewModel @Inject constructor(
    private val curationRepository: CurationRepository
) : ViewModel() {

    private val _curationList = MutableLiveData<List<CurationRespond>?>()
    val curationList: MutableLiveData<List<CurationRespond>?> = _curationList

    private val _selectedCuration = MutableLiveData<CurationRespond>()
    val selectedCuration: LiveData<CurationRespond> = _selectedCuration

    init {
        loadCurationList()
    }

    private fun loadCurationList() {
        viewModelScope.launch {
            try {
                val curations = curationRepository.getAllCurations()
                _curationList.value = curations
            } catch (e: Exception) {
                // 에러 처리
            }
        }
    }

    fun selectCuration(curation: CurationRespond) {
        _selectedCuration.value = curation
    }
}