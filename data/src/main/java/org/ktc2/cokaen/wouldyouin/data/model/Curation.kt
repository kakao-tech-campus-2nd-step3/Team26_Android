package org.ktc2.cokaen.wouldyouin.data.model

data class Curation(
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

