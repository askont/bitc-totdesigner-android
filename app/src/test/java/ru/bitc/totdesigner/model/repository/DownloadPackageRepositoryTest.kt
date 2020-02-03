package ru.bitc.totdesigner.model.repository

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.bitc.totdesigner.model.http.SoapApi
import ru.bitc.totdesigner.system.flow.TestDispatcher
import kotlinx.coroutines.test.runBlockingTest as runBlockingTest1

internal class DownloadPackageRepositoryTest {
    private lateinit var repository: DownloadPackageRepository
    private val api: SoapApi = mock { soap ->
        runBlocking {
            whenever(soap.downloadLessonPackage(any())).thenReturn(ResponseBody.create(MediaType.parse(""), "test"))
        }
    }

    private lateinit var dispatcher: TestDispatcher
    @BeforeEach
    fun init() {
        dispatcher = TestDispatcher()
        repository = DownloadPackageRepository(api, dispatcher)
    }

    @Test
    fun downloadPackage() {
        runBlockingTest1 {
            repository.downloadPackage("tesst")
                    .collect {
                    }
        }
    }
}