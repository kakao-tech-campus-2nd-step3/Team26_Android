package org.ktc2.cokaen.wouldyouin.data.model

data class ImageUploadRequest(
    val type: String, // type: "MEMBER", "CURATION", "EVENT", or "ADVERTISEMENT"
    val images: List<String> // 이미지 데이터 목록 (업로드할 이미지 목록)
)
