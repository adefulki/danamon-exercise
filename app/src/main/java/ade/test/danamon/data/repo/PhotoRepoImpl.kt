package ade.test.danamon.data.repo

import ade.test.danamon.data.datasource.PhotoRemoteDataSource
import ade.test.danamon.data.repo.paging.PhotoPagingSource
import ade.test.danamon.domain.model.Photo
import ade.test.danamon.domain.repo.PhotoRepo
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PhotoRepoImpl @Inject constructor(
    private val remoteDataSource: PhotoRemoteDataSource
) : PhotoRepo {

    override suspend fun getPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                PhotoPagingSource(remoteDataSource)
            }
        ).flow
    }
}