package ru.bitc.totdesigner.platfom.converter

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import ru.bitc.totdesigner.fake.lessonsFake
import ru.bitc.totdesigner.model.models.Lessons

class ModelLessonToEntityPreviewConverterTest {

    private lateinit var converter: ModelLessonToEntityPreviewConverter

    @Before
    fun init() {
        converter = ModelLessonToEntityPreviewConverter()
    }

    @Test
    fun `when input model lessons should return entity preview lessons`() {
        val model = Lessons(lessonsFake)
        val entity = converter.convertModelToEntity(model)
        assertThat(model.lessonsInfo.size).isEqualTo(entity.previews.size)
        assertThat(model.lessonsInfo.first().lessonUrl).isEqualTo(entity.previews.first().lessonUrl)
        assertThat(model.lessonsInfo.first().name).isEqualTo(entity.previews.first().title)
        assertThat(model.lessonsInfo.first().previewIcon).isEqualTo(entity.previews.first().imageUrl)
        assertThat(model.lessonsInfo.first().category).isEqualTo(entity.previews.first().category.category)
    }

    @Test
    fun `when input model Info should return entity lesson`() {
        val modelInfo = lessonsFake.first()
        val entity = converter.convertModelInfoToLesson(modelInfo)
        assertThat(modelInfo.category).isEqualTo(entity.category.category)
        assertThat(modelInfo.previewIcon).isEqualTo(entity.imageUrl)
        assertThat(modelInfo.lessonUrl).isEqualTo(entity.lessonUrl)
        assertThat(modelInfo.name).isEqualTo(entity.title)
    }


}