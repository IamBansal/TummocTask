package com.example.tummoctask.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_items")
data class FavoriteItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemName: String,
    val icon: String,
    val price: Double
)
