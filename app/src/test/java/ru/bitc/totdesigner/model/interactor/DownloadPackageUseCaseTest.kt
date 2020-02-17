package ru.bitc.totdesigner.model.interactor

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import ru.bitc.totdesigner.model.entity.PreviewLessons
import ru.bitc.totdesigner.model.entity.loading.LoadingPackage
import ru.bitc.totdesigner.model.repository.DownloadPackageRepository
import ru.bitc.totdesigner.model.repository.LessonRepository

class DownloadPackageUseCaseTest {

    private lateinit var downloadUseCase: DownloadPackageUseCase
    private val downloadRepository = mock<DownloadPackageRepository> {
        on { downloadPackage(any()) }.thenReturn(flow { emit(LoadingPackage.Finish("test")) })
    }
    private val lessonRepository = mock<LessonRepository> {
        onBlocking { getPreviewLessons() }.thenReturn(
            PreviewLessons(
                listOf(
                    PreviewLessons.Lesson(
                        "test",
                        "imageUrl", PreviewLessons.Category.FREE, "lessonurl"
                    )
                )
            )
        )
    }

    @Before
    fun init() {
        downloadUseCase = DownloadPackageUseCase(downloadRepository, lessonRepository)
    }


    @Test
    fun getListLoadingPackage() {
        runBlockingTest {
            downloadUseCase.getAllLoadingPackage("test.cur")
                .collect {
                    println(it)
                }
        }
        runBlockingTest {
            downloadUseCase.getListLoadingPackage()
                .collect { it ->
                    it.forEach {
                        println(it)
                    }
                }
        }
    }

    @Test
    fun getAllLoadingPackage() {
    }

    @Test
    fun cancelAllJob() {
    }
}