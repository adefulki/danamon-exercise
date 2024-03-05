package ade.test.danamon.di

import ade.test.danamon.data.datasource.PhotoRemoteDataSource
import ade.test.danamon.data.datasource.PhotoRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindPhotoRemoteDataSource(impl: PhotoRemoteDataSourceImpl): PhotoRemoteDataSource
}