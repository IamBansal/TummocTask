package com.example.tummoctask.viewmodel

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel

class CategoryViewModel : ViewModel() {
//    val expandedStates = mutableStateOf(mutableMapOf<String, Boolean>())
//
//    fun isCategoryExpanded(categoryName: String): Boolean {
//        return expandedStates.value[categoryName] ?: false
//    }
//
//    fun setCategoryExpanded(categoryName: String, isExpanded: Boolean) {
//        expandedStates.value[categoryName] = isExpanded
//    }
val expandedStates = mutableStateMapOf<String, Boolean>()
}

//@Composable
//fun rememberCategoryViewModel() = remember { viewModel<CategoryViewModel>() }
