package ade.test.danamon.domain.repo

import ade.test.danamon.domain.model.Photo
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface PhotoRepo {

    suspend fun getPhotos(): Flow<PagingData<Photo>>
}