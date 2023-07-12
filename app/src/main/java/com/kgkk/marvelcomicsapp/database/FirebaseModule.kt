package com.kgkk.marvelcomicsapp.database

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseDbComicHelper(): IDbComicHelper {
        return FirebaseDbComicHelper()
    }
}
