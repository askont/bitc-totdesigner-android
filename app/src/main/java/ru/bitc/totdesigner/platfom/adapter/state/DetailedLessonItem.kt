package ru.bitc.totdesigner.platfom.adapter.state

/**
 * Created on 05.03.2020
 * @author YWeber */

sealed class DetailedLessonItem

data class ImageGalleryDetailedItem(
    val mainImage: String,
    val otherImages: List<Image>
) : DetailedLessonItem() {
    data class Image(
        val image: String,
        val isSelect: Boolean = false
    )
}

data class TitleDetailedItem(val title: String) : DetailedLessonItem()
data class DescriptionDetailedItem(val description: String) : DetailedLessonItem()