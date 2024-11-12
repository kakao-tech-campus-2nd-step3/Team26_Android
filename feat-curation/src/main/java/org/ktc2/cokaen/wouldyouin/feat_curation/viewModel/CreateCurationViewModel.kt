package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity
import org.ktc2.cokaen.wouldyouin.data.model.Block
import org.ktc2.cokaen.wouldyouin.data.model.CurationCardRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationCreateRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequest
import org.ktc2.cokaen.wouldyouin.data.model.CurationEditRequestWrapper
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.LocalCurationCard
import org.ktc2.cokaen.wouldyouin.data.model.SearchEventData
import org.ktc2.cokaen.wouldyouin.data.repository.CurationLocalRepository
import org.ktc2.cokaen.wouldyouin.feat_curation.repository.CurationRepository
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@HiltViewModel
class CreateCurationViewModel @Inject constructor(
    application: Application,
    private val curationRepository: CurationRepository,
    private val curationLocalRepository: CurationLocalRepository
) : ViewModel() {
    private val MAX_BLOCKS = 10
    private val context = application.applicationContext
    var isEditMode = false
    private var originalCuration: CurationEntity? = null

    private var originalBlocks: List<Block>? = null
    private val deletedImages = mutableListOf<ImageResponse>()

    private val _eventDataList = MutableLiveData<List<SearchEventData>>()
    val eventDataList: LiveData<List<SearchEventData>> = _eventDataList

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

    private val _hasUnsavedChanges = MutableLiveData(false)
    val hasUnsavedChanges: LiveData<Boolean> = _hasUnsavedChanges

    private val _isFragmentVisible = MutableLiveData(false)
    val isFragmentVisible: LiveData<Boolean> get() = _isFragmentVisible

    fun showFragment() {
        _isFragmentVisible.value = true
    }

    fun hideFragment() {
        _isFragmentVisible.value = false
    }

    // 큐레이션 블록 데이터

    fun getBlockImages(position: Int): List<String> {
        return curationBlocks.value?.getOrNull(position)?.images?.map { it.url } ?: emptyList()
    }

    fun getBlockTitle(position: Int): String {
        return curationBlocks.value?.getOrNull(position)?.title ?: ""
    }

    fun getBlockBody(position: Int): String {
        return curationBlocks.value?.getOrNull(position)?.body ?: ""
    }

    fun getBlockImageResponses(position: Int): List<ImageResponse> {
        return curationBlocks.value?.getOrNull(position)?.images ?: emptyList()
    }

    // ViewModel 수정
    // 블록 타이틀 업데이트
    fun updateBlockTitle(position: Int, newTitle: String) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        if (currentBlocks.getOrNull(position)?.title != newTitle) {
            currentBlocks.getOrNull(position)?.let { block ->
                currentBlocks[position] = block.copy(title = newTitle)
                _curationBlocks.value = currentBlocks
            }
        }
    }

    // 블록 내용 업데이트
    fun updateBlockBody(position: Int, newBody: String) {
        val currentBlocks = _curationBlocks.value.orEmpty().toMutableList()
        if (currentBlocks.getOrNull(position)?.body != newBody) {
            currentBlocks.getOrNull(position)?.let { block ->
                currentBlocks[position] = block.copy(body = newBody)
                _curationBlocks.value = currentBlocks
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

    // ViewModel
    fun addNewBlock() {
        Log.d("BlockAdd", "Adding new block")
        val currentBlocks = _curationBlocks.value?.toMutableList() ?: mutableListOf()
        Log.d("BlockAdd", "Current blocks size: ${currentBlocks.size}")

        if (currentBlocks.size < MAX_BLOCKS) {
            // 새 블록 추가
            val newBlock = Block(
                title = "",
                images = listOf(),
                body = ""
            )
            currentBlocks.add(newBlock)

            Log.d("BlockAdd", "New block added. New size: ${currentBlocks.size}")
            // 새 리스트로 설정
            _curationBlocks.postValue(currentBlocks)
            updateAddBlockButtonState(currentBlocks.size < MAX_BLOCKS)
        }
    }

    // 블록 삭제
    fun removeBlock(blockPosition: Int) {
        viewModelScope.launch {
            val currentBlocks = _curationBlocks.value?.toMutableList() ?: return@launch
            if (blockPosition < currentBlocks.size) {
                val deletedBlock = currentBlocks.removeAt(blockPosition)

                // 삭제된 블록의 모든 이미지 삭제
                deletedBlock.images.forEach { imageResponse ->
                    try {
                        curationRepository.deleteImage(imageResponse.id)
                    } catch (e: Exception) {
                        Log.e("RemoveBlock", "Failed to delete image: ${imageResponse.id}", e)
                    }
                }

                _curationBlocks.value = currentBlocks
                updateAddBlockButtonState(currentBlocks.size < MAX_BLOCKS)
            }
        }
    }

    private fun observeBlocksSize() {
        curationBlocks.observeForever { blocks ->
            updateAddBlockButtonState(blocks.size < MAX_BLOCKS)
        }
    }

    fun deleteEvent(position: Int) {
        val currentList = _eventDataList.value?.toMutableList() ?: return
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _eventDataList.value = currentList  // 새 리스트 설정
        }
    }

    fun onImageClick(position: Int) {
        _imagePickerEvent.value = position
    }

    fun uploadImageFromPath(path: String, position: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            var originalBitmap: Bitmap? = null
            var scaledBitmap: Bitmap? = null
            var cleanBitmap: Bitmap? = null

            try {
                // 이미지 처리
                originalBitmap = BitmapFactory.decodeFile(path)
                scaledBitmap = scaleBitmap(originalBitmap!!)
                cleanBitmap = cleanImage(scaledBitmap)

                // MultipartBody.Part 생성
                val byteArrayOutputStream = ByteArrayOutputStream()
                cleanBitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                val requestBody = imageBytes.toRequestBody("image/png".toMediaTypeOrNull())
                val part = MultipartBody.Part.createFormData(
                    "images",
                    "image_${System.currentTimeMillis()}.png",
                    requestBody
                )

                // 이미지 업로드
                val imageResponse = curationRepository.uploadImageWithPart(part)
                Log.d("ImageUpload", "이미지 업로드 성공, 응답: $imageResponse")

                val currentBlocks = _curationBlocks.value?.toMutableList() ?: return@launch
                if (position < currentBlocks.size) {
                    val currentBlock = currentBlocks[position]
                    currentBlocks[position] = currentBlock.copy(
                        images = currentBlock.images + imageResponse // URL만 추가
                    )
                    _curationBlocks.value = currentBlocks
                    Log.d("ImageUpload", "업데이트된 이미지 리스트: ${currentBlocks[position].images}")
                }
            } catch (e: Exception) {
                Log.e("ImageUpload", "Upload failed", e)
                ToastUtils.showShortToast(context, e.message ?: "이미지 업로드에 실패했습니다")
            } finally {
                _isLoading.value = false
                originalBitmap?.recycle()
                scaledBitmap?.recycle()
                cleanBitmap?.recycle()
            }
        }
    }

    // 이미지 크기 조정
    private fun scaleBitmap(bitmap: Bitmap): Bitmap {
        val maxSize = 800  // 더 작은 크기로 조정
        val width = bitmap.width
        val height = bitmap.height
        val ratio = width.toFloat() / height.toFloat()

        var newWidth = maxSize
        var newHeight = (maxSize / ratio).toInt()

        if (newHeight > maxSize) {
            newHeight = maxSize
            newWidth = (maxSize * ratio).toInt()
        }

        return Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true)
    }

    // 메타데이터 제거
    private fun cleanImage(bitmap: Bitmap): Bitmap {
        val cleanBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        val canvas = Canvas(cleanBitmap)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        return cleanBitmap
    }

    fun createCurationWithApi() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val hashTags = _hashtags.value?.split(",")?.map { it.trim() } ?: listOf()

                val curationCards = _curationBlocks.value?.map { block ->
                    CurationCardRequest(
                        subtitle = block.title,
                        content = block.body,
                        imageIds = block.images.map { it.id },
                        imageSizeValid = true
                    )
                } ?: listOf()

                val eventIds = _eventDataList.value?.map { it.eventId } ?: listOf()
                val requestBody = CurationCreateRequestWrapper(
                    curationCreateRequest = CurationCreateRequest(
                        title = _title.value ?: "",
                        content = _content.value ?: "",
                        curationCards = curationCards,
                        area = _selectedRegion.value ?: "전체",
                        hashTag = hashTags,
                        eventIds = eventIds
                    )
                )

                val curationResponse = curationRepository.createCuration(requestBody)

                val curationEntity = CurationEntity(
                    id = curationResponse.id,
                    title = curationResponse.title,
                    content = curationResponse.content,
                    modifiedDate = curationResponse.modifiedDate,
                    createdTime = curationResponse.createdTime,
                    curator = Gson().toJson(curationResponse.curator),
                    curationCards = Gson().toJson(curationResponse.curationCards),
                    area = Gson().toJson(curationResponse.area),
                    hashTag = Gson().toJson(curationResponse.hashTag),
                    eventsInfo = Gson().toJson(curationResponse.eventsInfo)
                )

                curationLocalRepository.insertCuration(curationEntity)
                _navigationEvent.value = NavigationEvent.Success

            } catch (e: Exception) {
                Log.e("CreateCuration", "Failed to create curation", e)
                ToastUtils.showShortToast(context, e.message ?: "큐레이션 생성에 실패했습니다")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun handleImageDelete(blockPosition: Int, image: ImageResponse) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = curationRepository.deleteImage(image.id)
                if (success) {
                    // 현재 블록의 이미지 목록에서 해당 이미지만 제거
                    val currentBlocks = _curationBlocks.value?.toMutableList() ?: return@launch
                    if (blockPosition < currentBlocks.size) {
                        val currentBlock = currentBlocks[blockPosition]
                        val updatedImages = currentBlock.images.filterNot { it.id == image.id }
                        currentBlocks[blockPosition] = currentBlock.copy(
                            images = updatedImages
                        )
                        _curationBlocks.value = currentBlocks

                        // 이미지 버튼 상태 업데이트 추가
                        val MAX_IMAGES = 5
                        updateAddImageButtonState(blockPosition, updatedImages.size < MAX_IMAGES)
                    }
                } else {
                    ToastUtils.showShortToast(context, "이미지 삭제에 실패했습니다")
                }
            } catch (e: Exception) {
                ToastUtils.showShortToast(context, e.message ?: "이미지 삭제에 실패했습니다")
            } finally {
                _isLoading.value = false
            }
        }
    }

    // 큐레이션 생성
    fun createCuration(): CurationCreateRequest {
        // curationCards는 _curationBlocks를 사용하여 변환
        val curationCards = _curationBlocks.value?.map { block ->
            CurationCardRequest(
                subtitle = block.title ?: "",  // 각 블록의 subtitle을 가져옴
                content = block.body ?: "",    // 각 블록의 content를 가져옴
                imageIds = block.images?.map { it.id } ?: listOf(),  // 블록에 포함된 이미지 ID 목록
                imageSizeValid = block.images?.all { it.size > 0 } ?: true  // 이미지 크기가 유효한지 여부
            )
        } ?: listOf()

        // eventIds는 예시로 빈 리스트로 처리했으므로, 필요 시 추가 로직을 적용할 수 있음
        val eventIds = listOf<Long>()

        return CurationCreateRequest(
            title = _title.value ?: "",
            content = _content.value ?: "",
            curationCards = curationCards,
            area = _selectedRegion.value ?: "",
            hashTag = listOf(_hashtags.value ?: ""),
            eventIds = eventIds,
            curationCardsSizeValid = curationCards.isNotEmpty()
        )
    }

    // 지역 업데이트
    fun updateRegion(region: String) {
        _selectedRegion.value = region
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

    private fun initializeEditMode(curation: CurationEntity) {
        val localCards = Gson().fromJson<List<LocalCurationCard>>(
            curation.curationCards,
            object : TypeToken<List<LocalCurationCard>>() {}.type
        )

        originalBlocks = localCards.map { card ->
            Block(
                title = card.title,
                body = card.body,
                images = card.images
            )
        }

        originalBlocks?.let { blocks ->
            _curationBlocks.value = blocks
        }

        // 다른 필드들도 초기화
        _title.value = curation.title
        _content.value = curation.content
        _selectedRegion.value = curation.area
        _hashtags.value = Gson().fromJson<List<String>>(
            curation.hashTag,
            object : TypeToken<List<String>>() {}.type
        ).joinToString(",")
    }

    private fun saveEdit(originalCuration: CurationEntity) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 삭제된 이미지 처리
                deletedImages.forEach { image ->
                    try {
                        curationRepository.deleteImage(image.id)
                    } catch (e: Exception) {
                        Log.e("EditCuration", "Failed to delete image: ${image.id}", e)
                    }
                }

                // 해시태그와 큐레이션 카드 준비
                val hashTags = _hashtags.value?.split(",")?.map { it.trim() } ?: listOf()
                val curationCards = _curationBlocks.value?.map { block ->
                    CurationCardRequest(
                        subtitle = block.title,
                        content = block.body,
                        imageIds = block.images.map { it.id},
                        imageSizeValid = true
                    )
                } ?: listOf()

                // API 요청 바디 생성
                val requestBody = CurationEditRequestWrapper(
                    curationEditRequest = CurationEditRequest(
                        title = _title.value ?: "",
                        content = _content.value ?: "",
                        curationCards = curationCards,
                        area = _selectedRegion.value ?: "전체",
                        hashTag = hashTags,
                        eventIds = listOf()
                    )
                )

                // API 호출
                val response = curationRepository.updateCuration(requestBody)

                // 로컬 DB 업데이트
                val localCards = _curationBlocks.value?.map { block ->
                    LocalCurationCard(
                        title = block.title,
                        body = block.body,
                        images = block.images
                    )
                } ?: listOf()

                val updatedCuration = originalCuration.copy(
                    title = _title.value ?: "",
                    content = _content.value ?: "",
                    curationCards = Gson().toJson(localCards),
                    area = _selectedRegion.value ?: "전체",
                    hashTag = Gson().toJson(hashTags)
                )

                curationLocalRepository.updateCuration(updatedCuration)

                // 상태 초기화
                deletedImages.clear()
                originalBlocks = null

                _navigationEvent.value = NavigationEvent.Success

            } catch (e: Exception) {
                Log.e("EditCuration", "Failed to update curation", e)
                ToastUtils.showShortToast(context, e.message ?: "큐레이션 수정에 실패했습니다")
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun initialize(curation: CurationEntity? = null) {
        viewModelScope.launch {
            isEditMode = curation != null
            if (isEditMode && curation != null) {
                // 수정 모드 초기화
                originalCuration = curation
                initializeEditMode(curation)
            } else {
                // 작성 모드 초기화
                _title.value = ""
                _content.value = ""
                _hashtags.value = ""
                _selectedRegion.value = ""
                _curationBlocks.value = listOf()
                deletedImages.clear()
            }
        }
    }

    fun saveCuration() {
        if (isEditMode) {
            originalCuration?.let { saveEdit(it) }
        } else {
            createCurationWithApi()
        }
    }

    fun saveSearchEventData(eventId: Long, eventName: String?, hostName: String?, imageUrl: String?) {
        val newEvent = SearchEventData(eventId, eventName, hostName, imageUrl)
        val currentList = _eventDataList.value ?: emptyList()
        _eventDataList.value = currentList + newEvent
    }

    private val _imageButtonStates = MutableLiveData<Map<Int, Boolean>>(mapOf())
    val imageButtonStates: LiveData<Map<Int, Boolean>> = _imageButtonStates

    private val _isAddBlockButtonEnabled = MutableLiveData(true)
    val isAddBlockButtonEnabled: LiveData<Boolean> = _isAddBlockButtonEnabled

    // 이미지 추가 버튼 상태 업데이트
    // 상태 업데이트 메서드
    fun updateAddImageButtonState(blockPosition: Int, isEnabled: Boolean) {
        val currentStates = _imageButtonStates.value?.toMutableMap() ?: mutableMapOf()
        currentStates[blockPosition] = isEnabled
        _imageButtonStates.value = currentStates
    }

    // 블록 추가 버튼 상태 업데이트
    private fun updateAddBlockButtonState(isEnabled: Boolean) {
        _isAddBlockButtonEnabled.postValue(isEnabled)  // setValue 대신 postValue 사용
    }

}
sealed class NavigationEvent {
    object Back : NavigationEvent()
    object ShowExitConfirmation : NavigationEvent()
    object Success : NavigationEvent()
}