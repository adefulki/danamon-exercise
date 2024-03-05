package ade.test.danamon.domain.usecase

import ade.test.danamon.base.BaseUseCase
import ade.test.danamon.domain.model.Photo
import ade.test.danamon.domain.repo.PhotoRepo
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: PhotoRepo
) : BaseUseCase<Unit, Flow<PagingData<Photo>>> {
    override suspend fun execute(input: Unit): Flow<PagingData<Photo>> {
        return repository.getPhotos()
    }
}