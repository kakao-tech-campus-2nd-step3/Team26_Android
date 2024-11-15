package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ktc2.cokaen.wouldyouin.core.ToastUtils
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CreateBlockImagesAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CreateCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.SelectedEventsAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCreateCurationBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CurationSearchViewModel
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.NavigationEvent
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.ValidationResult

@AndroidEntryPoint
class CreateCurationActivity : AppCompatActivity() {
    private val viewModel: CreateCurationViewModel by viewModels()
    private val searchViewModel: CurationSearchViewModel by viewModels()
    private lateinit var binding: ActivityCreateCurationBinding
    private var currentPosition: Int = -1
    private var isEditMode = false
    private val curatorID: Long = 1
    private lateinit var imagesAdapter: CreateBlockImagesAdapter
    private lateinit var selectedEventsAdapter: SelectedEventsAdapter
    private lateinit var eventsAdapter: SelectedEventsAdapter

    // 어댑터는 하나의 인스턴스만 유지
    private val adapter by lazy {
        CreateCurationBlockAdapter(
            viewModel = viewModel,
            deleteClickListener = { position ->
                viewModel.removeBlock(position)
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setupInitialState()
        setupViews()
        setupObservers()
        setupListeners()
        setupResultListeners()
    }

    private fun setupResultListeners() {
        supportFragmentManager.setFragmentResultListener("event_selection", this) { _, bundle ->
            val eventId = bundle.getLong("event_id")
            val eventName = bundle.getString("event_name")
            val hostName = bundle.getString("host_name")
            val imageUrl = bundle.getString("image_url")

            // 뷰모델에 데이터 저장
            viewModel.saveSearchEventData(eventId, eventName, hostName, imageUrl)
        }
    }

    private fun setupInitialState() {
        val curationId = if (intent.hasExtra("curationId")) {
            intent.getLongExtra("curationId", -1L)
        } else {
            null
        }

        when (curationId) {
            null -> {
                // 새로운 큐레이션 작성 모드
                isEditMode = false
                viewModel.initialize(null)
            }
            -1L -> {
                // 잘못된 ID 값이 전달된 경우
                ToastUtils.showShortToast(this@CreateCurationActivity, "잘못된 접근입니다.")
                finish()
            }
            else -> {
                // 유효한 ID로 수정 모드 진입
                viewModel.viewModelScope.launch {
                    try {
                        val localCuration = viewModel.getCurationFromLocal(curationId)
                        if (localCuration != null) {
                            isEditMode = true
                            viewModel.initialize(localCuration)
                        } else {
                            ToastUtils.showShortToast(this@CreateCurationActivity, "잘못된 접근입니다.")
                            finish()
                        }
                    } catch (e: Exception) {
                        ToastUtils.showShortToast(this@CreateCurationActivity, "잘못된 접근입니다.")
                        finish()
                    }
                }
            }
        }
    }
    private fun setupViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_curation)
        binding.apply {
            lifecycleOwner = this@CreateCurationActivity
            viewModel = this@CreateCurationActivity.viewModel
            toolbarTitle.text = if (isEditMode) "큐레이션 수정" else "큐레이션 작성"
        }

        setupRecyclerView()
        setUpSelectedEventsView()
        setupNavigation()
        setupRegionSpinner()
        setupShowEvents()
        checkHashtags()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupShowEvents() {
        binding.addEventButton.setOnClickListener {
            val fragment = CurationSearchResultFragment()
            fragment.show(supportFragmentManager, "search_result")
        }
    }

    private fun setUpSelectedEventsView() {
        val selectedEventsAdapter = SelectedEventsAdapter(viewModel)

        binding.rvSelectedEvents.apply {
            adapter = selectedEventsAdapter
            layoutManager = LinearLayoutManager(this@CreateCurationActivity)
            setHasFixedSize(true)
        }

        // 이벤트 데이터 변경 관찰
        viewModel.eventDataList.observe(this) { events ->
            selectedEventsAdapter.submitList(events)
        }
    }

    private fun setupRecyclerView() {
        binding.rvCurationBlocks.apply {
            adapter = this@CreateCurationActivity.adapter
        }
    }

    private fun setupObservers() {
        viewModel.apply {
            // 이미지 피커 이벤트
            imagePickerEvent.observe(this@CreateCurationActivity) { position ->
                currentPosition = position
                checkAndRequestPermission()
            }

            // 블록 추가 버튼 상태
            isAddBlockButtonEnabled.observe(this@CreateCurationActivity) { isEnabled ->
                binding.addCurationBlockButton.isEnabled = isEnabled
            }

            // 이벤트 데이터
            viewModel.eventDataList.observe(this@CreateCurationActivity) { events ->
                selectedEventsAdapter.submitList(events)
            }
        }
        viewModel.curationBlocks.observe(this) { blocks ->
            Log.d("UI_Update", "Received blocks size: ${blocks.size}")
            adapter.submitList(blocks.toList())
        }
    }

    private fun setupListeners() {
        setupTextWatchers()
        setupButtons()

        binding.addCurationBlockButton.setOnClickListener {
            if (viewModel.isAddBlockButtonEnabled.value == true) {
                Log.d("ButtonClick", "Add block button clicked")
                viewModel.addNewBlock()
            } else {
                Toast.makeText(this, "큐레이션 개수를 초과했습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            }
        )

        // 제목 검증
        binding.etTitle.addValidationWatcher { title ->
            if (title.isBlank()) {
                ValidationResult.Error("제목은 필수입니다")
            } else {
                ValidationResult.Success
            }
        }

        // 본문 검증
        binding.etContent.addValidationWatcher { content ->
            when {
                content.length < 20 ->
                    ValidationResult.Error("본문은 20자 이상이어야 합니다")
                content.length > 1000 ->
                    ValidationResult.Error("본문은 1000자 이하여야 합니다")
                else -> ValidationResult.Success
            }
        }

        // 저장 버튼 클릭시 전체 검증
        binding.btnRegister.setOnClickListener {
            if (viewModel.validateAndSave(this)) {
                ToastUtils.showShortToast(this, "업로드 되었습니다.")
                finish()
            }
        }
    }

    private fun setupButtons() {
        binding.btnRegister.apply {
            // 초기 상태 설정
            isEnabled = viewModel.isFormValid.value ?: false
        }
    }

    private fun setupTextWatchers() {
        binding.apply {
            etTitle.addTextChangedListener(SimpleTextWatcher { viewModel?.updateTitle(it) })
            etContent.addTextChangedListener(SimpleTextWatcher { viewModel?.updateContent(it) })
            etHashtag.addTextChangedListener(SimpleTextWatcher { viewModel?.updateHashtags(it) })
        }
    }

    private class SimpleTextWatcher(private val onTextChanged: (String) -> Unit) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            s?.toString()?.let(onTextChanged)
        }
    }

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상에서는 READ_MEDIA_IMAGES 권한만 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQ_GALLERY)
            } else {
                selectGallery()
            }
        } else {
            // Android 12 이하에서는 READ_EXTERNAL_STORAGE 권한을 요청
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQ_GALLERY)
            } else {
                selectGallery()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQ_GALLERY) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우 이미지를 선택
                Log.d("Permission", "Permission granted in onRequestPermissionsResult")
                selectGallery()
            } else {
                // 권한이 거부된 경우
                Log.d("Permission", "Permission denied in onRequestPermissionsResult")
                showPermissionRequiredDialog(permissions.toList())
            }
        }
    }

    private fun showPermissionRequiredDialog(permissions: List<String>) {
        val message = if (permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            "저장소 접근 권한이 거부되어 이미지를 선택할 수 없습니다. 설정에서 권한을 허용해주세요."
        } else {
            "필요한 권한이 거부되어 이미지를 선택할 수 없습니다. 설정에서 권한을 허용해주세요."
        }

        AlertDialog.Builder(this)
            .setTitle("권한 거부됨")
            .setMessage(message)
            .setPositiveButton("설정으로 이동") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun setupRegionSpinner() {
        val regionArray = resources.getStringArray(R.array.region)
        val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, regionArray)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            viewModel.updateRegion(regionArray[position])
        }
    }

    private fun setupNavigation() {
        viewModel.navigationEvent.observe(this) { event ->
            when (event) {
                is NavigationEvent.Back -> finish()
                is NavigationEvent.ShowExitConfirmation -> showExitConfirmationDialog()
                NavigationEvent.Success -> {
                    Toast.makeText(
                        this,
                        if (viewModel.isEditMode) "큐레이션이 수정되었습니다"
                        else "큐레이션이 등록되었습니다",
                        Toast.LENGTH_SHORT
                    ).show()

                    // 현재 화면 종료
                    finish()
                }
                else -> {}
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("작성 취소")
            .setMessage("작성 중인 내용이 있습니다. \n정말 나가시겠습니까?")
            .setPositiveButton("나가기") { _, _ -> finish() }
            .setNegativeButton("계속 작성하기", null)
            .show()
    }

    private fun checkHashtags() {
        val hashtagPattern = "^#(?:[A-Za-z가-힣0-9_]+#?)+$".toRegex()

        binding.etHashtag.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()
                if (hashtagPattern.matches(input)) {
                    binding.etHashtag.error = null
                } else {
                    binding.etHashtag.error = "해시태그 형식이 올바르지 않습니다."
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkCurationTitle() {
        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val title = s.toString().trim()
                if (title.isEmpty()) {
                    binding.etTitle.error = "제목을 입력해주세요."
                } else {
                    binding.etTitle.error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun checkCurationBody() {
        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val body = s.toString().trim()
                if (body.length in 20..1000) {
                    binding.etContent.error = null
                } else {
                    binding.etContent.error = "본문은 20자 이상, 1000자 이내여야 합니다."
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }


    private fun checkCurationCardTitle() {
        // 빈칸, "" 안됨
    }

    private fun checkCurationCardBody() {
        // 20자 이상, 1000자 이내
    }

    private fun checkCurationCardImages() {
        // 각 이미지 url 원소 담은 배열 길이가 5 초과되면 안됨
    }

    private fun checkCurationCardLength() {
        // 큐레이션 카드 개수 10 초과되면 안됨
    }

    private val imageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = result.data?.data ?: return@registerForActivityResult
            val path = absolutelyPath(imageUri, this)
            viewModel.uploadImageFromPath(path, currentPosition)
        }
    }

    private fun selectGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상에서는 READ_MEDIA_IMAGES 권한만 요청
            val permission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_MEDIA_IMAGES
            )
            if (permission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                    REQ_GALLERY
                )
            } else {
                openGallery()
            }
        } else {
            // Android 12 이하에서는 READ_EXTERNAL_STORAGE 권한을 요청
            val writePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            val readPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )

            if (writePermission == PackageManager.PERMISSION_DENIED ||
                readPermission == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ),
                    REQ_GALLERY
                )
            } else {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*"
            )
        }
        imageResult.launch(intent)
    }


    private fun absolutelyPath(path: Uri?, context: Context): String {
        val proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        val c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        val index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        val result = c?.getString(index!!)
        c?.close()
        return result!!
    }

    private fun performSearch(query: String) {
        if (query.isNotEmpty()) {

        } else {
            Toast.makeText(this, "검색어를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EditText.addValidationWatcher(validateFn: (String) -> ValidationResult) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                when (val result = validateFn(s?.toString() ?: "")) {
                    is ValidationResult.Error -> error = result.message
                    ValidationResult.Success -> error = null
                }
            }
        })
    }

    // TextInputEditText용 확장 함수도 추가
    private fun TextInputEditText.addValidationWatcher(validateFn: (String) -> ValidationResult) {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                when (val result = validateFn(s?.toString() ?: "")) {
                    is ValidationResult.Error -> error = result.message
                    ValidationResult.Success -> error = null
                }
            }
        })
    }

    companion object {
        const val REQ_GALLERY = 1
        private const val EVENT_SEARCH_REQUEST_CODE = 1001
    }
}
