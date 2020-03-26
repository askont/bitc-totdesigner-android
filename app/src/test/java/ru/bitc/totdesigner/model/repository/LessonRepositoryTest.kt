package ru.bitc.totdesigner.model.repository

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import ru.bitc.totdesigner.fake.lessonsFake
import ru.bitc.totdesigner.fake.previewLesson
import ru.bitc.totdesigner.platfom.converter.ModelLessonToEntityPreviewConverter
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.model.models.Lessons

class LessonRepositoryTest {

    private val soapApi = mock<SoapApi> {
        onBlocking { getLessonsPreview() }.thenReturn(Lessons((lessonsFake)))
    }
    private val converter = mock<ModelLessonToEntityPreviewConverter> {
        on { convertModelToEntity(any()) }.thenReturn(PreviewLessons(listOf(previewLesson)))
    }

    private val pathDao = mock<PathDao>()

    private lateinit var repository: LessonRepository

    @Before
    fun init() {
        repository = LessonRepository(soapApi, converter,pathDao)
    }

    @Test
    fun `when get all remote lesson preview should return PreviewLessons`() {
        //given
        runBlockingTest {
            val lesson = repository.getAllRemoteLesson().previews.first()
            assertThat(lesson).isEqualTo(previewLesson)
        }
        verifyBlocking(soapApi, times(1), {
            getLessonsPreview()
        })
        verify(converter, times(1)).convertModelToEntity(any())
    }

    @Test(expected = HttpException::class)
    fun `when get all remote lesson preview should return error server code`() {
        runBlockingTest {
            whenever(soapApi.getLessonsPreview()).doAnswer {
                throw HttpException(
                    Response.error<Lessons>(
                        400,
                        ResponseBody.create(MediaType.get("text/json"), "not found")
                    )
                )
            }
        }
        runBlockingTest { repository.getAllRemoteLesson() }
    }
}