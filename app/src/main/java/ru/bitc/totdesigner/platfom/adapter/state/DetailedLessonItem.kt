package ru.bitc.totdesigner.platfom.adapter.state

/**
 * Created on 05.03.2020
 * @author YWeber */

sealed class DetailedLessonItem(open val id: Int)

data class Preview(val path: String, val isSelect: Boolean = false) : DetailedLessonItem(path.hashCode())

data class GalleryPreview(val otherImages: List<Preview>) : DetailedLessonItem(otherImages.size)

data class TitleDetailedItem(val title: String) : DetailedLessonItem(title.hashCode())
data class DescriptionDetailedItem(val description: String) : DetailedLessonItem(description.hashCode())