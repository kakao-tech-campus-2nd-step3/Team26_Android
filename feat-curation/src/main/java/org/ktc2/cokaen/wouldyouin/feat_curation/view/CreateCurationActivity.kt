package org.ktc2.cokaen.wouldyouin.feat_curation.view

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ktc2.cokaen.wouldyouin.feat_curation.R
import org.ktc2.cokaen.wouldyouin.feat_curation.adapter.CreateCurationBlockAdapter
import org.ktc2.cokaen.wouldyouin.feat_curation.databinding.ActivityCreateCurationBinding
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.CreateCurationViewModel
import org.ktc2.cokaen.wouldyouin.feat_curation.viewModel.NavigationEvent

@AndroidEntryPoint
class CreateCurationActivity : AppCompatActivity(), CreateCurationBlockAdapter.DeleteClickListener {
    val viewModel: CreateCurationViewModel by viewModels()
    private lateinit var binding: ActivityCreateCurationBinding
    private var currentPosition: Int = -1
    private lateinit var adapter: CreateCurationBlockAdapter

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openGallery()
        } else {
            showPermissionRequiredDialog()
        }
    }

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { selectedUri ->
            if (currentPosition != -1) {
                viewModel.handleSelectedImage(currentPosition, selectedUri)
            }
        }
    }
    override fun onDeleteClick(position: Int) {
        viewModel.removeBlock(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_curation)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

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
        setupRegionSpinner()

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
        when {
            // Android 13 이상
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        openGallery()
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_IMAGES) -> {
                        showPermissionRationaleDialog()
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                    }
                }
            }
            // Android 13 미만
            else -> {
                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        openGallery()
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        showPermissionRationaleDialog()
                    }
                    else -> {
                        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                    }
                }
            }
        }
    }

    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun showPermissionRationaleDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 필요")
            .setMessage("이미지를 선택하기 위해서는 저장소 접근 권한이 필요합니다.")
            .setPositiveButton("권한 요청") { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showPermissionRequiredDialog() {
        AlertDialog.Builder(this)
            .setTitle("권한 거부됨")
            .setMessage("저장소 접근 권한이 거부되어 이미지를 선택할 수 없습니다. 설정에서 권한을 허용해주세요.")
            .setPositiveButton("설정으로 이동") { _, _ ->
                // 앱 설정 화면으로 이동
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
            }
        }
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("작성 취소")
            .setMessage("작성 중인 내용이 있습니다. 정말 나가시겠습니까?")
            .setPositiveButton("나가기") { _, _ -> finish() }
            .setNegativeButton("계속 작성하기", null)
            .show()
    }
}