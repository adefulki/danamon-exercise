package ade.test.danamon.data.remote

import ade.test.danamon.data.remote.dto.PhotoDto
import retrofit2.http.GET
import retrofit2.http.Query

interface PhotosApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): List<PhotoDto>
}