package com.example.tummoctask.model.remote

import com.example.tummoctask.model.remote.Category

data class Item(
    val categories: List<Category>,
    val error: Any,
    val message: String,
    val status: Boolean
)