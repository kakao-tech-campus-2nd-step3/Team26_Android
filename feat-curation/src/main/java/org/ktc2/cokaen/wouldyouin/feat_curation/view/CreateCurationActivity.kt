package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.ktc2.cokaen.wouldyouin.data.entities.CurationEntity
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CreateBlockImagesAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CreateCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCreateCurationBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.NavigationEvent
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class CreateCurationActivity : AppCompatActivity(), CreateCurationBlockAdapter.DeleteClickListener, CreateCurationBlockAdapter.OnImageClickListener {
    val viewModel: CreateCurationViewModel by viewModels()
    private lateinit var binding: ActivityCreateCurationBinding
    private var currentPosition: Int = -1
    private lateinit var adapter: CreateCurationBlockAdapter
    private lateinit var imagesAdapter: CreateBlockImagesAdapter
    private var isEditMode = false

    private val curatorID: Long = 1

    override fun onDeleteClick(position: Int) {
        viewModel.removeBlock(position)
    }

    override fun onImageDeleteClick(blockPosition: Int, image: ImageResponse) {
        viewModel.handleImageDelete(blockPosition, image)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        @Suppress("DEPRECATION")
        val curationToEdit: CurationEntity? = intent.getParcelableExtra("curation")

        isEditMode = curationToEdit != null
        viewModel.initialize(curationToEdit)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_curation)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.toolbarTitle.text = if (isEditMode) "큐레이션 수정" else "큐레이션 작성"

        adapter = CreateCurationBlockAdapter(viewModel, this).apply {
            viewModel.curationBlocks.observe(this@CreateCurationActivity) { blocks ->
                setBlocks(blocks)
            }

            viewModel.blocksChangedEvent.observe(this@CreateCurationActivity) { position ->
                if (position >= 0) {  // position이 유효할 때만
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, itemCount)
                }
            }
        }

        binding.rvCurationBlocks.adapter = adapter

        binding.rvCurationBlocks.recycledViewPool.setMaxRecycledViews(0, 0)

        viewModel.imagePickerEvent.observe(this) { position ->
            currentPosition = position
            checkAndRequestPermission()
        }

        binding.addCurationBlockButton.setOnClickListener {
            viewModel.addNewBlock()
        }

        viewModel.curationBlocks.observe(this) { blocks ->
            adapter.setBlocks(blocks)
        }

        viewModel.blocksChangedEvent.observe(this) { position ->
            if (position >= 0) {
                adapter.notifyItemRemoved(position)
                adapter.notifyItemRangeChanged(position, adapter.itemCount)
            }
        }

        binding.btnRegister.isEnabled = viewModel.isFormValid.value ?: false

        binding.etTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateTitle(s.toString())
            }
        })

        binding.etContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateContent(s.toString())
            }
        })

        binding.etHashtag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateHashtags(s.toString())
            }
        })

        setupRecyclerView()
        setupNavigation()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onBackPressed()
                }
            }
        )

        // 입력 검사
        setupRegionSpinner()
        checkCurationTitle()
        checkCurationBody()
        checkCurationCardTitle()
        checkCurationCardBody()
        checkHashtags()
        setupButtons()
    }

    private fun setupButtons() {
        binding.btnRegister.setOnClickListener {
            val curatorId = getCurrentUserId()
            viewModel.saveCuration(curatorId)
        }
    }

    private fun getCurrentUserId(): Long {
        val userId: Long = 1;
        return userId
    }


    private fun setupRecyclerView() {
        binding.rvCurationBlocks.apply {
            setHasFixedSize(true)
            itemAnimator = null  // 애니메이션으로 인한 문제 방지
            adapter = this@CreateCurationActivity.adapter  // 이미 onCreate에서 생성한 adapter 사용
        }

        viewModel.curationBlocks.observe(this) { blocks ->
            binding.rvCurationBlocks.post {
                adapter.setBlocks(blocks)
            }
        }
    }

    private fun checkAndRequestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13 이상에서는 READ_MEDIA_IMAGES 권한만 요청
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.READ_MEDIA_IMAGES), REQ_GALLERY)
            } else {
                selectGallery()
            }
        } else {
            // Android 12 이하에서는 READ_EXTERNAL_STORAGE 권한을 요청
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    private fun showPermissionRationaleDialog(permissions: List<String>) {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("이미지를 선택하기 위해서는 저장소 접근 권한이 필요합니다.")
            .setPositiveButton("권한 요청") { _, _ ->
                ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQ_GALLERY)
            }
            .setNegativeButton("취소", null)
            .show()
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
                    binding.textInputLayoutHashtag.error = null
                } else {
                    binding.textInputLayoutHashtag.error = "해시태그 형식이 올바르지 않습니다."
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun checkCurationTitle() {
        // 빈칸, "" 안됨
    }

    private fun checkCurationBody() {
        // 20자 이상, 1000자 이내
    }

    private fun checkCurationCardTitle() {
        // 빈칸, "" 안됨
    }

    private fun checkCurationCardBody() {
        // 20자 이상, 1000자 이내
    }

    private fun checkCurationCardImages() {
        // 각 이미지 url 원소 담은 배열 길이가 5 초과되면 예외
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

    companion object {
        const val REQ_GALLERY = 1
    }
}