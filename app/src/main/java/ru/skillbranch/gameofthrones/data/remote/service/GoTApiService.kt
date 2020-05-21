package ru.skillbranch.gameofthrones.data.remote.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.skillbranch.gameofthrones.data.remote.res.CharacterRes
import ru.skillbranch.gameofthrones.data.remote.res.HouseRes

const val BASE_URL = "https://anapioficeandfire.com/api/"

interface GoTApiService {

    @GET("houses")
    suspend fun fetchAllHouses(
        @Query("page") page: Int = 1,
        @Query("pageSize") pageSize: Int = 50
    ) : List<HouseRes>

    @GET("houses/{id}")
    suspend fun fetchHouseById(
        @Path("id") id: String
    ): HouseRes

    @GET("houses")
    suspend fun fetchHouseByName(
        @Query("name") name: String
    ): List<HouseRes>

    @GET("characters/{id}")
    suspend fun fetchCharacterById(
        @Path("id") id: String
    ): CharacterRes

    companion object Factory {

        private val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        fun create(): GoTApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

            return retrofit.create(GoTApiService::class.java)
        }
    }
}