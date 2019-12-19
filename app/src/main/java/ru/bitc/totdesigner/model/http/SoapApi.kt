package ru.bitc.totdesigner.model.http

import retrofit2.http.GET
import ru.bitc.totdesigner.model.models.Lessons

/*
 * Created on 2019-12-18
 * @author YWeber
 */
interface SoapApi {
    @GET("LessonsInfo.xml")
    suspend fun getLessonsPreview(): Lessons
}