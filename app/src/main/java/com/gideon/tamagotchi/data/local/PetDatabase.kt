package com.gideon.tamagotchi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PetEntity::class], version = 3)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}
