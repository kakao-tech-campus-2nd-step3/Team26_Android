package org.ktc2.cokaen.wouldyouin

data class KakaoSearchData(
    val documents: List<Document>,
    val meta: Meta
)

data class Meta(
    val is_end: Boolean,
    val pageable_count: Int,
    val same_name: SameName,
    val total_count: Int
)

data class SameName(
    val keyword: String,
    val region: List<Any>,
    val selected_region: String
)

data class Document(
    val address_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val category_name: String,
    val distance: String,
    val id: String,
    val phone: String,
    val place_name: String,
    val place_url: String,
    val road_address_name: String,
    val x: String,
    val y: String
)