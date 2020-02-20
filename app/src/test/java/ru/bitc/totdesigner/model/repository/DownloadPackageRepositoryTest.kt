package ru.bitc.totdesigner.model.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.TestDispatcher

class DownloadPackageRepositoryTest {
    private lateinit var repository: DownloadPackageRepository
    private lateinit var dispatcher: TestDispatcher
    private val api: SoapApi = mock {
        onBlocking { downloadLessonPackage(any()) }.thenReturn(ResponseBody.create(MediaType.parse(""), "test"))
    }

    @Before
    fun init() {
        dispatcher = TestDispatcher()
        repository = DownloadPackageRepository(api, dispatcher)
    }

    @Test
    fun `when invoke downloadPackage success should event loading and finish`() {
        //given
        runBlockingTest {
            val count = repository.downloadPackage("LessonsInfo.xml/package")
                    .count()
            assertThat(count).isEqualTo(2)
        }
        runBlockingTest {
            repository.downloadPackage("LessonsInfo.xml/package")
                    .collect {
                        assertThat(it.urlId).isEqualTo("LessonsInfo.xml/package")
                        assertThat(it).isInstanceOfAny(
                                LoadingPackage.Loading::class.java,
                                LoadingPackage.Finish::class.java
                        )
                    }
        }
    }

    @Test
    fun `when invoke downloadPackage error api should event loading and error`() {
        //when
        runBlockingTest {
            whenever(api.downloadLessonPackage(any())).doAnswer {
                throw RuntimeException("server error code")
            }
        }
        //given
        runBlockingTest {
            val count = repository.downloadPackage("LessonsInfo.xml/package")
                    .count()
            assertThat(count).isEqualTo(2)
        }
        runBlocking {
            repository.downloadPackage("LessonsInfo.xml/package")
                    .collect {
                        assertThat(it.urlId).isEqualTo("LessonsInfo.xml/package")
                        assertThat(it).isInstanceOfAny(
                                LoadingPackage.Loading::class.java,
                                LoadingPackage.Error::class.java
                        )
                    }
        }
    }
}