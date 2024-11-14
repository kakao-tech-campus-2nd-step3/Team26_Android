package org.ktc2.cokaen.wouldyouin.feat_profile.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import org.ktc2.cokaen.wouldyouin.data.model.ReviewCreateRequest
import org.ktc2.cokaen.wouldyouin.feat_profile.databinding.DialogReviewBinding
import org.ktc2.cokaen.wouldyouin.feat_profile.viewModel.EventReviewViewModel

// ReviewDialog.kt
@AndroidEntryPoint
class ReviewDialog : DialogFragment() {
    private var _binding: DialogReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EventReviewViewModel by activityViewModels()
    private var eventId: Long = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogReviewBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .setTitle("리뷰 작성")
            .setPositiveButton("등록", null) // null로 설정하고 아래에서 따로 처리
            .setNegativeButton("취소") { _, _ -> dismiss() }
            .create()
            .apply {
                // Dialog 생성 후 Positive 버튼 동작 설정
                setOnShowListener { dialog ->
                    (dialog as AlertDialog).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                        if (validateInput()) {
                            submitReview()
                            dialog.dismiss()
                        }
                    }
                }
            }
    }

    private fun validateInput(): Boolean {
        val rating = binding.ratingBar.rating
        val content = binding.editTextReview.text.toString()

        when {
            rating == 0f -> {
                Toast.makeText(context, "평점을 입력해주세요", Toast.LENGTH_SHORT).show()
                return false
            }
            content.isBlank() -> {
                binding.editTextReview.error = "내용을 입력해주세요"
                return false
            }
            content.length >= 50 -> {
                binding.editTextReview.error = "내용은 50자 미만으로 입력해주세요"
                return false
            }
        }
        return true
    }

    private fun submitReview() {
        val review = ReviewCreateRequest(
            eventId = eventId,
            score = binding.ratingBar.rating.toInt(),
            content = binding.editTextReview.text.toString()
        )
        viewModel.submitReview(review)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(eventId: Long) = ReviewDialog().apply {
            this.eventId = eventId
        }
    }
}