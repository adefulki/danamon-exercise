package ade.test.danamon.data.repo.paging

import ade.test.danamon.data.datasource.PhotoRemoteDataSource
import ade.test.danamon.data.remote.dto.toPhoto
import ade.test.danamon.domain.model.Photo
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

class PhotoPagingSource(
    private val remoteDataSource: PhotoRemoteDataSource,
) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            val currentPage = params.key ?: 1
            val photos = remoteDataSource.getPhotos(
                pageNumber = currentPage
            )
            LoadResult.Page(
                data = photos.toPhoto(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (photos.isEmpty()) null else currentPage + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition
    }

}