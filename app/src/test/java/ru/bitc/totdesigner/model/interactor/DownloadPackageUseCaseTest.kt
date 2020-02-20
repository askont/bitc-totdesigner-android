package ru.bitc.totdesigner.model.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import ru.bitc.totdesigner.fake.previewLesson
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.AllLoadingJob
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository
import ru.bitc.totdesigner.system.flow.DispatcherProvider
import ru.bitc.totdesigner.system.flow.TestDispatcher

class DownloadPackageUseCaseTest {

    private lateinit var downloadUseCase: DownloadPackageUseCase
    private val downloadRepository = mock<DownloadPackageRepository> {
        on { downloadPackage(any()) }.thenReturn(flow {
            emit(LoadingPackage.Loading("test.cur3", 100))
            delay(1000)
            emit(LoadingPackage.Finish("test.cur0"))
            emit(LoadingPackage.Finish("test.cur1"))
            emit(LoadingPackage.Finish("test.cur2"))
            emit(LoadingPackage.Finish("test.cur3"))
        })
    }
    private val lessonRepository = mock<LessonRepository> {
        onBlocking { getPreviewLessons() }.thenReturn(PreviewLessons(listOf(previewLesson)))
    }
    private lateinit var dispatcher: DispatcherProvider

    @Before
    fun init() {
        dispatcher = TestDispatcher()
        downloadUseCase = DownloadPackageUseCase(downloadRepository, lessonRepository, dispatcher)
    }


    @Test
    fun `when range event lesson load should return even count loading and finish success`() {
        runBlockingTest {
            (0..2).asFlow()
                .flatMapLatest { downloadUseCase.getCountAllLoadingPackage("test.cur$it") }
                .collect {
                    println(it)
                    Assertions.assertThat(it).isInstanceOfAny(
                        AllLoadingJob.Progress::class.java,
                        AllLoadingJob.Finish::class.java
                    )
                }
        }
    }

    @Test
    fun `when get all event list job should return list events`() {
        whenever(downloadRepository.downloadPackage(any())).thenReturn(flow {
            emit(LoadingPackage.Loading("http://test/package", 100))
        })
        runBlockingTest {
            (0..1).asFlow()
                .flatMapLatest { downloadUseCase.getCountAllLoadingPackage("test.cur$it") }
                .collect()
        }
        runBlockingTest {
            downloadUseCase.getListPairLoadingAndPreview()
                .collect {
                    println(it)
                }
        }

    }

    @Test
    fun cancelAllJob() {
    }
}