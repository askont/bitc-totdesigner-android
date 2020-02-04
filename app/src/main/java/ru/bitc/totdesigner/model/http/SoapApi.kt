package ru.bitc.totdesigner.model.http

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url
import ru.bitc.totdesigner.model.models.Lessons

/*
 * Created on 2019-12-18
 * @author YWeber
 */
interface SoapApi {
    @GET("LessonsInfo.xml")
    suspend fun getLessonsPreview(): Lessons

    @GET
    suspend fun downloadLessonPackage(@Url lessonUrl: String): ResponseBody
}