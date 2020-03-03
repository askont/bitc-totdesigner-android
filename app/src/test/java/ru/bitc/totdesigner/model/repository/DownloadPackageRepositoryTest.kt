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
import ru.bitc.totdesigner.model.database.dao.PathDao
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.TestDispatcher
import ru.bitc.totdesigner.system.path.PathManager
import ru.bitc.totdesigner.system.zip.UnpackingZip

class DownloadPackageRepositoryTest {
    private lateinit var repository: DownloadPackageRepository
    private val api: SoapApi = mock {
        onBlocking { downloadLessonPackage(any()) }.thenReturn(ResponseBody.create(MediaType.parse(""), "test"))
    }
    private val pathManager = mock<PathManager> { }
    private val unpackingZip = mock<UnpackingZip> { }
    private val pathDao = mock<PathDao>()

    @Before
    fun init() {
        repository = DownloadPackageRepository(api, pathManager, unpackingZip, pathDao, TestDispatcher())
    }

    @Test
    fun `when invoke downloadPackage success should event loading and finish`() {
        //given
        runBlockingTest {
            val count = repository.downloadPackage("LessonsInfo.xml/package", "test")
                .count()
            assertThat(count).isEqualTo(2)
        }
        runBlockingTest {
            repository.downloadPackage("LessonsInfo.xml/package", "test")
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
            val count = repository.downloadPackage("LessonsInfo.xml/package", "test")
                .count()
            assertThat(count).isEqualTo(2)
        }
        runBlocking {
            repository.downloadPackage("LessonsInfo.xml/package", "test")
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