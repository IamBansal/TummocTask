package com.example.tummoctask.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tummoctask.model.remote.ItemX

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val itemName: String,
    val quantity: Int,
    val price: Double,
    val icon: String
)

