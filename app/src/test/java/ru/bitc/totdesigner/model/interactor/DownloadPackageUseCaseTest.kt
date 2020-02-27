package ru.bitc.totdesigner.model.interactor

import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.bitc.totdesigner.fake.lessonInfoFake
import ru.bitc.totdesigner.fake.previewLesson
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.entity.loading.ProcessDownloading
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.flow.CoroutineTestRule
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.flow.TestDispatcher

class DownloadPackageUseCaseTest {

    private lateinit var downloadUseCase: DownloadPackageUseCase
    private val downloadRepository = mock<DownloadPackageRepository> {
        on { downloadPackage(any()) }.thenReturn(flow {
            emit(LoadingPackage.Loading(lessonInfoFake.lessonUrl, 100))
        })
    }
    private val lessonRepository = mock<LessonRepository> {
        onBlocking { getPreviewLessons() }.thenReturn(PreviewLessons(listOf(previewLesson)))
    }
    private lateinit var dispatcher: DispatcherProvider

    @get:Rule
    var coroutineTest = CoroutineTestRule()

    @Before
    fun init() {
        dispatcher = TestDispatcher()
        downloadUseCase = DownloadPackageUseCase(downloadRepository, lessonRepository, dispatcher)
    }

    @Test
    fun `when new task event subscribe should be return job process count `() {
        //given
        coroutineTest.test {
            downloadUseCase.processTaskEventLoadingCount(lessonInfoFake.lessonUrl, false)
                .collect {
                    println(it)
                    Assertions.assertThat(it).isInstanceOf(ProcessDownloading.Count::class.java)
                }
        }
        verifyBlocking(downloadRepository, {
            downloadPackage(any())
        })
    }

    @Test
    fun `when process task event finish should return process loading count and finish`() {
        //when
        coroutineTest.test {
            whenever(downloadRepository.downloadPackage(any())).thenReturn(flow {
                emit(LoadingPackage.Loading(lessonInfoFake.lessonUrl, 2000))
                emit(LoadingPackage.Finish(lessonInfoFake.lessonUrl))
            })
        }
        //given
        coroutineTest.test {
            downloadUseCase.processTaskEventLoadingCount(lessonInfoFake.lessonUrl, false)
                .collect {
                    println(it)
                    Assertions.assertThat(it)
                        .isInstanceOfAny(
                            ProcessDownloading.Count::class.java,
                            ProcessDownloading.Finish::class.java
                        )
                }
        }
        verifyBlocking(downloadRepository, {
            downloadPackage(any())
        })
    }

    @Test
    fun `when put url contains this and flag delete should return new event emit and delete job`() {
        //when
        coroutineTest.test {
            whenever(downloadRepository.downloadPackage(any())).thenReturn(flow {
                emit(LoadingPackage.Loading(lessonInfoFake.lessonUrl, 2000))
            })
        }
        //given
        coroutineTest.test {
            downloadUseCase.processTaskEventLoadingCount(lessonInfoFake.lessonUrl, false)
                .collect {
                    println(it)
                    Assertions.assertThat(it)
                        .isInstanceOf(ProcessDownloading.Count::class.java)
                }
        }
        verifyBlocking(downloadRepository, times(1), {
            downloadPackage(any())
        })
        coroutineTest.test {
            downloadUseCase.processTaskEventLoadingCount(lessonInfoFake.lessonUrl, true)
                .collect {
                    println(it)
                    Assertions.assertThat(it)
                        .isInstanceOf(ProcessDownloading.Finish::class.java)
                }
        }
    }


}