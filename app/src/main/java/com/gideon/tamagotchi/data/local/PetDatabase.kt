package com.gideon.tamagotchi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PetEntity::class], version = 1)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}
