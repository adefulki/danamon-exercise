package ade.test.danamon.di

import ade.test.danamon.data.repo.PhotoRepoImpl
import ade.test.danamon.data.repo.UserRepoImpl
import ade.test.danamon.domain.repo.PhotoRepo
import ade.test.danamon.domain.repo.UserRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepoModule {

    @Binds
    abstract fun bindUserRepo(impl: UserRepoImpl): UserRepo

    @Binds
    abstract fun bindMealRepo(impl: PhotoRepoImpl): PhotoRepo
}