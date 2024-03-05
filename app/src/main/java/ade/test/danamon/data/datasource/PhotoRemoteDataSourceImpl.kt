package ade.test.danamon.data.datasource

import ade.test.danamon.data.remote.PhotosApi
import ade.test.danamon.data.remote.dto.PhotoDto
import javax.inject.Inject

class PhotoRemoteDataSourceImpl @Inject constructor(
    private val api: PhotosApi
) : PhotoRemoteDataSource {

    override suspend fun getPhotos(pageNumber: Int): List<PhotoDto> {
        return api.getPhotos(page = pageNumber, limit = 10)
    }

}