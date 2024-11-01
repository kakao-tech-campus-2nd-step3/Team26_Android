package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.data.model.CurationRespond
import javax.inject.Inject

@HiltViewModel
class CurationDetailViewModel @Inject constructor(
    private val curationRepository: CurationRepository
) : ViewModel() {

    private val _curation = MutableLiveData<CurationRespond?>()
    val curation: MutableLiveData<CurationRespond?> = _curation

    private val _curationBlocks = MutableLiveData<List<Block>>()
    val curationBlocks: LiveData<List<Block>> = _curationBlocks

    private val _hashtags = MutableLiveData<List<String>>()
    val hashtags: LiveData<List<String>> = _hashtags

    fun loadCurationDetail(curationId: String) {
        viewModelScope.launch {
            val curationDetail = curationRepository.getCurationDetail(curationId)
            _curation.value = curationDetail
            if (curationDetail != null) {
                _curationBlocks.value = curationDetail.blocks
            }
            if (curationDetail != null) {
                _hashtags.value = curationDetail.hashtags.split("#").map { it.trim() }
            }
        }
    }
}