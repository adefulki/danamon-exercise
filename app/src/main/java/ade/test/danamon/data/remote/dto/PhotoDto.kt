package ade.test.danamon.data.remote.dto

import ade.test.danamon.domain.model.Photo
import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("albumId") var albumId: Int? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("thumbnailUrl") var thumbnailUrl: String? = null
)

fun List<PhotoDto>.toPhoto(): List<Photo> = this.map {
    Photo(
        id = it.id ?: 0,
        albumId = it.albumId ?: 0,
        title = it.title.orEmpty(),
        url = it.url.orEmpty(),
        thumbnailUrl = it.thumbnailUrl.orEmpty()
    )
}