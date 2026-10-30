package dev.lchang.appuesan.data.remote.apifootball

import dev.lchang.appuesan.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = BuildConfig.API_BASE_URL
private const val API_KEY = BuildConfig.API_KEY

object RetrofitInstance {

    // true si local.properties tiene API_BASE_URL y API_KEY
    val isConfigured: Boolean = BASE_URL.isNotBlank() && API_KEY.isNotBlank()

    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("x-apisports-key", API_KEY)
                .build()
            chain.proceed(newRequest)
        }
        .apply {
            // BASIC no imprime headers, así la API key no aparece en Logcat
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BASIC
                })
            }
        }
        .build()

    val api: ApiFootballService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiFootballService::class.java)

    }
}
