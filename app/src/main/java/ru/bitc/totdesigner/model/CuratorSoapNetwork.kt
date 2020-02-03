package ru.bitc.totdesigner.model

import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import ru.bitc.totdesigner.BuildConfig
import ru.bitc.totdesigner.model.http.SoapApi
import java.util.concurrent.TimeUnit

object CuratorSoapNetwork {

    private const val READ_TIMEOUT = 40L
    private const val CONNECTION_TIMEOUT = 30L

    fun createHttpClient(): OkHttpClient = with(OkHttpClient.Builder()) {
        connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(logger)
        }
        build()
    }

    fun createRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.ENDPOINT)
        .client(client)
        .addConverterFactory(TikXmlConverterFactory.create())
        .build()

    fun createSoapApi(retrofit: Retrofit): SoapApi = retrofit.create(SoapApi::class.java)

}