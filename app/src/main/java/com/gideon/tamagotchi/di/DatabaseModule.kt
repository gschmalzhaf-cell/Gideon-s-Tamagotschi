package com.gideon.tamagotchi.di

import android.content.Context
import androidx.room.Room
import com.gideon.tamagotchi.data.PetRepository
import com.gideon.tamagotchi.data.local.PetDao
import com.gideon.tamagotchi.data.local.PetDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePetDatabase(@ApplicationContext context: Context): PetDatabase =
        Room.databaseBuilder(context, PetDatabase::class.java, "gideon.db").build()

    @Provides
    fun providePetDao(database: PetDatabase): PetDao = database.petDao()

    @Provides
    fun providePetRepository(petDao: PetDao): PetRepository = PetRepository(petDao)
}
