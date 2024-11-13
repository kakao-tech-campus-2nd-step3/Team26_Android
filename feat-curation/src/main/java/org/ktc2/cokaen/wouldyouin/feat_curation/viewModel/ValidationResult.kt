package org.ktc2.cokaen.wouldyouin.feat_curation.viewModel

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import org.ktc2.cokaen.wouldyouin.data.model.Block

// ValidationResult.kt
sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
}

// CurationValidator.kt
object CurationValidator {
    private const val MAX_IMAGES_PER_BLOCK = 5
    private const val MIN_BODY_LENGTH = 20
    private const val MAX_BODY_LENGTH = 1000

    fun validateBlock(block: Block): ValidationResult {
        // 제목 검증
        if (block.title.isNullOrBlank()) {
            return ValidationResult.Error("제목은 필수입니다.")
        }

        // 본문 길이 검증
        if (block.body.length !in MIN_BODY_LENGTH..MAX_BODY_LENGTH) {
            return ValidationResult.Error("본문은 ${MIN_BODY_LENGTH}자 이상 ${MAX_BODY_LENGTH}자 이하여야 합니다.")
        }

        // 이미지 개수 검증
        if (block.images.size > MAX_IMAGES_PER_BLOCK) {
            return ValidationResult.Error("이미지는 최대 ${MAX_IMAGES_PER_BLOCK}개까지만 추가할 수 있습니다.")
        }

        return ValidationResult.Success
    }

    fun validateCuration(title: String, content: String, blocks: List<Block>): ValidationResult {
        // 큐레이션 제목 검증
        if (title.isBlank()) {
            return ValidationResult.Error("큐레이션 제목은 필수입니다.")
        }

        // 큐레이션 본문 길이 검증
        if (content.length !in MIN_BODY_LENGTH..MAX_BODY_LENGTH) {
            return ValidationResult.Error("큐레이션 본문은 ${MIN_BODY_LENGTH}자 이상 ${MAX_BODY_LENGTH}자 이하여야 합니다.")
        }

        // 각 블록 검증
        blocks.forEachIndexed { index, block ->
            when (val result = validateBlock(block)) {
                is ValidationResult.Error -> {
                    return ValidationResult.Error("${index + 1}번째 블록: ${result.message}")
                }
                ValidationResult.Success -> {} // continue
            }
        }

        return ValidationResult.Success
    }
}