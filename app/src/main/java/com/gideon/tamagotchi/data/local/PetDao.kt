package com.gideon.tamagotchi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {

    @Query("SELECT * FROM pet LIMIT 1")
    fun observePet(): Flow<PetEntity?>

    @Query("SELECT * FROM pet LIMIT 1")
    suspend fun getPet(): PetEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pet: PetEntity)

    @Update
    suspend fun update(pet: PetEntity)

    @Query("DELETE FROM pet")
    suspend fun deleteAll()
}
