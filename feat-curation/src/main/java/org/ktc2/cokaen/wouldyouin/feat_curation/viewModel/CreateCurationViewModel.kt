package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.data.model.CurationRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CreateCurationViewModel @Inject constructor(
    application: Application,
    private val curationRepository: CurationRepository
) : ViewModel() {
    private val context = application.applicationContext

    // 네비게이션
    private val _navigationEvent = MutableLiveData<NavigationEvent>()
    val navigationEvent: LiveData<NavigationEvent> = _navigationEvent

    // UI 상태
    private val _imagePickerEvent = MutableLiveData<Int>()
    val imagePickerEvent: LiveData<Int> = _imagePickerEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // 큐레이션 데이터
    private val _title = MutableLiveData("")
    val title: LiveData<String> = _title

    private val _content = MutableLiveData("")
    val content: LiveData<String> = _content

    private val _hashtags = MutableLiveData("")
    val hashtags: LiveData<String> = _hashtags

    private val _curationBlocks = MutableLiveData<List<Block>>(listOf())
    val curationBlocks: LiveData<List<Block>> = _curationBlocks

    private val _selectedRegion = MutableLiveData<String>()
    val selectedRegion: LiveData<String> = _selectedRegion

    private val _blocksChangedEvent = MutableLiveData<Int>()
    val blocksChangedEvent: LiveData<Int> = _blocksChangedEvent

    private val _hasUnsavedChanges = MutableLiveData(false)
    val hasUnsavedChanges: LiveData<Boolean> = _hasUnsavedChanges

    fun notifyBlocksChanged() {
        _blocksChangedEvent.postValue(-1)
    }

    // 큐레이션 블록 데이터

    fun getBlockImages(position: Int): List<String> {
        return curationBlocks.value?.getOrNull(position)?.images ?: emptyList()
    }

    fun getBlockTitle(position: Int): String {
        return curationBlocks.value?.getOrNull(position)?.title ?: ""
    }

    fun getBlockBody(position: Int): String {
        return curationBlocks.value?.getOrNull(position)?.body ?: ""
    }

    // ViewModel 수정
    fun updateBlockTitle(position: Int, newTitle: String) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        if (currentBlocks.getOrNull(position)?.title != newTitle) {
            currentBlocks.getOrNull(position)?.let { block ->
                currentBlocks[position] = block.copy(title = newTitle)
                _curationBlocks.postValue(currentBlocks)  // setValue 대신 postValue 사용
            }
        }
    }

    fun updateBlockBody(position: Int, newBody: String) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        if (currentBlocks.getOrNull(position)?.body != newBody) {
            currentBlocks.getOrNull(position)?.let { block ->
                currentBlocks[position] = block.copy(body = newBody)
                _curationBlocks.postValue(currentBlocks)  // setValue 대신 postValue 사용
            }
        }
    }

    // 입력 데이터 업데이트 함수들
    fun updateTitle(newTitle: String) {
        _title.value = newTitle
        checkUnsavedChanges()
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
        checkUnsavedChanges()
    }

    fun updateHashtags(newHashtags: String) {
        _hashtags.value = newHashtags
        checkUnsavedChanges()
    }

    fun updateSelectedRegion(region: String) {
        _selectedRegion.value = region
    }

    // 블록 추가
    fun addNewBlock() {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        currentBlocks.add(
            Block(
                title = "",
                images = listOf(),
                body = ""
            )
        )
        _curationBlocks.value = currentBlocks
        notifyBlocksChanged()
    }

    fun onImageClick(position: Int) {
        _imagePickerEvent.value = position
    }


    // 선택된 이미지 URI 처리
    fun handleSelectedImage(position: Int, uri: Uri) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val uploadedImageUrl = curationRepository.uploadImage(uri)

                val currentBlocks = _curationBlocks.value?.toMutableList() ?: return@launch

                if (position < currentBlocks.size) {

                    val currentBlock = currentBlocks[position]
                    currentBlocks[position] = currentBlock.copy(
                        images = currentBlock.images + uploadedImageUrl
                    )

                    _curationBlocks.postValue(currentBlocks)
                }
            } catch (e: Exception) {
                val errorMessage = when (e) {
                    is IOException -> "네트워크 연결을 확인해주세요"
                    is HttpException -> "서버 통신 중 오류가 발생했습니다"
                    else -> e.message ?: "이미지 업로드에 실패했습니다"
                }
                ToastUtils.showShortToast(context, errorMessage)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // 블록 삭제
    fun removeBlock(position: Int) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()

        if (position in currentBlocks.indices) {
            currentBlocks.removeAt(position)
            _curationBlocks.postValue(currentBlocks)
            notifyBlockRemoved(position)
        }
    }

    private fun notifyBlockRemoved(position: Int) {
        _blocksChangedEvent.postValue(position)
    }

    // 큐레이션 생성
    fun createCuration(): CurationRequest {
        // 수정
        return CurationRequest(
            title = _title.value ?: "",
            content = _content.value ?: "",
            area = _selectedRegion.value ?: "",
            hashtags = _hashtags.value ?: "",
            blocks = _curationBlocks.value ?: listOf(),
            eventList = listOf() // 추가
        )
    }

    // 지역 업데이트
    fun updateRegion(region: String) {
        _selectedRegion.value = region
    }

    // 블록 내 이미지 삭제
    fun removeBlockImage(blockPosition: Int, imagePosition: Int) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        currentBlocks.getOrNull(blockPosition)?.let { block ->
            val newImages = block.images.toMutableList().apply {
                if (imagePosition in indices) {
                    removeAt(imagePosition)
                }
            }
            currentBlocks[blockPosition] = block.copy(images = newImages)
            _curationBlocks.value = currentBlocks
        }
    }

    fun onBackPressed() {
        if (hasUnsavedChanges.value == true) {
            _navigationEvent.value = NavigationEvent.ShowExitConfirmation
        } else {
            _navigationEvent.value = NavigationEvent.Back
        }
    }

    private fun checkUnsavedChanges() {
        val hasTitle = !_title.value.isNullOrBlank()
        val hasContent = !_content.value.isNullOrBlank()
        val hasArea = !_selectedRegion.value.isNullOrBlank()
        val hasHashtags = !_hashtags.value.isNullOrBlank()
        val hasBlocks = _curationBlocks.value?.any { block ->
            !block.title.isNullOrBlank() || !block.body.isNullOrBlank()
        } ?: false

        _hasUnsavedChanges.value = hasTitle || hasContent || hasArea || hasHashtags || hasBlocks
    }

    private val _isFormValid = MediatorLiveData<Boolean>().apply {
        fun checkValidity() {
            val hasTitle = !_title.value.isNullOrBlank()
            val hasContent = !_content.value.isNullOrBlank()
            val hasArea = !_selectedRegion.value.isNullOrBlank()
            val hasHashtags = !_hashtags.value.isNullOrBlank()
            val hasValidBlocks = _curationBlocks.value?.all { block ->
                !block.title.isNullOrBlank() && !block.body.isNullOrBlank()
            } ?: false
            val hasAtLeastOneBlock = _curationBlocks.value?.isNotEmpty() ?: false

            value = hasTitle && hasContent && hasArea && hasHashtags &&
                    hasValidBlocks && hasAtLeastOneBlock
        }

        addSource(_title) { checkValidity() }
        addSource(_content) { checkValidity() }
        addSource(_selectedRegion) { checkValidity() }
        addSource(_hashtags) { checkValidity() }
        addSource(_curationBlocks) { checkValidity() }
    }
    val isFormValid: LiveData<Boolean> = _isFormValid
}

sealed class NavigationEvent {
    object Back : NavigationEvent()
    object ShowExitConfirmation : NavigationEvent()
}