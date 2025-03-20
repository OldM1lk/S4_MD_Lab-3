package com.example.lab_3.data.remote

import com.example.lab_3.data.model.CharacterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://rickandmortyapi.com/api/"

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

interface RickAndMortyApiService {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") pageNumber: Int
    ): CharacterResponse
}

class RickAndMortyApi {
    companion object {
        fun create(): RickAndMortyApiService {
            val retrofitService: RickAndMortyApiService by lazy {
                retrofit.create(RickAndMortyApiService::class.java)
            }
            return retrofitService
        }
    }
}