package ade.test.danamon.di

import ade.test.danamon.data.local.model.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.Realm
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RealmModule {

    @Provides
    @Singleton
    fun providesRealmConfigs(): Realm {
        val config = RealmConfiguration.create(setOf(User::class))
        return Realm.open(config)
    }
}