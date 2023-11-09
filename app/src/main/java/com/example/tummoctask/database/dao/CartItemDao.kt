package com.example.tummoctask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tummoctask.model.local.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartItemDao {
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItem)

    @Delete
    suspend fun deleteCartItem(item: CartItem)

    @Query("SELECT SUM(quantity) FROM cart_items")
    fun getTotalCartItemCount(): Flow<Int>

    @Query("SELECT SUM(price) FROM cart_items")
    fun getTotalCartPrice(): Flow<Int>

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM cart_items WHERE itemName = :itemName) THEN 1 ELSE 0 END")
    fun doesItemExist(itemName: String): Flow<Boolean>
}
