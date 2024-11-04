package org.ktc2.cokaen.wouldyouin.data.model

// 추후 수정 필요

data class CurationRequest(
    val title: String,
    val content: String,
    val area: String,
    val hashtags: String,
    val blocks: List<Block>,
    val eventList: List<String>
)

data class CurationRespond(
    val curationId: String,
    val generatedAt: String,
    val title: String,
    val content: String,
    val area: String,
    val hashtags: String,
    val blocks: List<Block>,
    val eventList: List<String>
)

data class Block(
    val title: String,
    val images: List<String>,
    val body: String
)

