package com.example.tummoctask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoctask.database.AppDatabase
import com.example.tummoctask.model.local.FavoriteItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavViewModel(application: Application) : AndroidViewModel(application) {
    private val favItemDao = AppDatabase.getDatabase(application).favoriteItemDao()
    val allFavItems: Flow<List<FavoriteItem>> = favItemDao.getAllFavoriteItems()

    fun insertFavItem(item: FavoriteItem) {
        viewModelScope.launch {
            favItemDao.insertFavoriteItem(item)
        }
    }

    fun deleteFavItem(item: FavoriteItem) {
        viewModelScope.launch {
            favItemDao.deleteFavoriteItem(item)
        }
    }

    fun deleteFavItemByName(item: String) {
        viewModelScope.launch {
            favItemDao.deleteItemByName(item)
        }
    }

    fun isPresent(item: String): Flow<Boolean> {
        return favItemDao.doesItemExist(item)
    }

}
