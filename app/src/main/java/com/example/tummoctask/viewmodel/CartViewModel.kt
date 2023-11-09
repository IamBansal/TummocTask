package com.example.tummoctask.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tummoctask.database.AppDatabase
import com.example.tummoctask.model.local.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CartViewModel(application: Application) : AndroidViewModel(application) {
    private val cartItemDao = AppDatabase.getDatabase(application).cartItemDao()

    val allCartItems: Flow<List<CartItem>> = cartItemDao.getAllCartItems()
    val totalPrice: Flow<Int> = cartItemDao.getTotalCartPrice()
    val itemCount: Flow<Int> = cartItemDao.getTotalCartItemCount()

    fun insertCartItem(item: CartItem) {
        viewModelScope.launch {
            cartItemDao.insertCartItem(item)
        }
    }

    fun deleteCartItem(item: CartItem) {
        viewModelScope.launch {
            cartItemDao.deleteCartItem(item)
        }
    }

    fun isPresent(item: String): Flow<Boolean> {
        return cartItemDao.doesItemExist(item)
    }
}
