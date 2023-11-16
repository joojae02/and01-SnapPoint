package com.boostcampwm2023.snappoint.presentation.createpost

data class CreatePostUiState(
    val postBlocks: List<PostBlock> = mutableListOf(),
    val onTextChanged: (position: Int, content: String) -> Unit,
)

sealed class PostBlock(open val content: String) {
    data class STRING(override val content: String = "") : PostBlock(content)
    data class IMAGE(override val content: String = "", val position: Position) : PostBlock(content)
    data class VIDEO(override val content: String = "", val position: Position) : PostBlock(content)
}

data class Position(
    val latitude: Double,
    val longitude: Double
)