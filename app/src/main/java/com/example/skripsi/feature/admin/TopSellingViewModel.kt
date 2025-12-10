package com.example.skripsi.feature.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.repository.AdminOrderRepository
import kotlinx.coroutines.launch

class TopSellingViewModel(
    private val repo: AdminOrderRepository
) : ViewModel() {

    private val _topItems = MutableLiveData<List<TopSellingItem>>()
    val topItems: LiveData<List<TopSellingItem>> = _topItems

    fun loadTopSelling() {
        viewModelScope.launch {
            _topItems.value = repo.getTopSellingItems()
        }
    }
}

class TopSellingViewModelFactory(
    private val repo: AdminOrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopSellingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopSellingViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
