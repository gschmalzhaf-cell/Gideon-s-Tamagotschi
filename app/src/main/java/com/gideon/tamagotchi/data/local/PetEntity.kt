package com.gideon.tamagotchi.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    @ColumnInfo(name = "color_hex") val colorHex: Long,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    val hunger: Int,
    val happiness: Int,
    val energy: Int,
    val hygiene: Int,
    @ColumnInfo(name = "is_alive") val isAlive: Boolean,
    @ColumnInfo(name = "is_sleeping") val isSleeping: Boolean = false,
    @ColumnInfo(name = "last_tick_at") val lastTickAt: Long = 0,
    val bond: Int = 0,
    @ColumnInfo(name = "last_check_in_at") val lastCheckInAt: Long = 0
)
