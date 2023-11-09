package com.example.tummoctask.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tummoctask.model.local.FavoriteItem
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteItemDao {
    @Query("SELECT * FROM favorite_items")
    fun getAllFavoriteItems(): Flow<List<FavoriteItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteItem(item: FavoriteItem)

    @Delete
    suspend fun deleteFavoriteItem(item: FavoriteItem)

    @Query("SELECT CASE WHEN EXISTS (SELECT * FROM favorite_items WHERE itemName = :itemName) THEN 1 ELSE 0 END")
    fun doesItemExist(itemName: String): Flow<Boolean>

    @Query("SELECT * FROM favorite_items WHERE itemName = :itemName")
    suspend fun getItemByName(itemName: String): FavoriteItem

    @Query("DELETE FROM favorite_items WHERE itemName = :itemName")
    suspend fun deleteItemByName(itemName: String)

}
