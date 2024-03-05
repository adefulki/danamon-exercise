package ade.test.danamon.data.datasource

import ade.test.danamon.data.remote.dto.PhotoDto

interface PhotoRemoteDataSource {

    suspend fun getPhotos(
        pageNumber: Int
    ): List<PhotoDto>
}
