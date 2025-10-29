package com.tecsup.mediturn.data.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto singleton para configurar Retrofit
 * Proporciona una instancia configurada de la API de MediTurn
 */
object RetrofitClient {

    /**
     * Base URL del servidor Django
     * - Para emulador Android: use 10.0.2.2 (mapea a localhost de la máquina host)
     * - Para dispositivo físico: use la IP de tu máquina en la red local (ej: 192.168.1.X)
     */
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    /**
     * Configuración de Gson para manejar fechas y formatos personalizados
     */
    private val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")  // Formato ISO 8601
        .setLenient()  // Permite ser más flexible con el parseo
        .create()

    /**
     * Interceptor de logging para ver las peticiones HTTP en Logcat
     * Solo se activa en builds de debug
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**
     * Cliente HTTP configurado con timeouts e interceptores
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    /**
     * Instancia de Retrofit configurada
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    /**
     * Instancia del servicio API
     */
    val apiService: MediTurnApiService by lazy {
        retrofit.create(MediTurnApiService::class.java)
    }

    /**
     * Permite cambiar la BASE_URL en tiempo de ejecución si es necesario
     * Útil para pruebas o cambio entre desarrollo/producción
     */
    fun createService(baseUrl: String = BASE_URL): MediTurnApiService {
        val customRetrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return customRetrofit.create(MediTurnApiService::class.java)
    }
}
