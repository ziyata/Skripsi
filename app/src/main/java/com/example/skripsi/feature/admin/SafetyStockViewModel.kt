package com.example.skripsi.feature.admin

import androidx.lifecycle.*
import com.example.skripsi.data.repository.AdminOrderRepository
import kotlinx.coroutines.launch

class SafetyStockViewModel(
    private val repo: AdminOrderRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<AdminOrderRepository.SafetyStockItem>>()
    val items: LiveData<List<AdminOrderRepository.SafetyStockItem>> = _items

    fun loadSafetyStock(
        daysHistory: Int = 30,
        bufferDays: Int = 3
    ) {
        viewModelScope.launch {
            _items.value = repo.getSafetyStockList(daysHistory, bufferDays)
        }
    }
}

class SafetyStockViewModelFactory(
    private val repo: AdminOrderRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SafetyStockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SafetyStockViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
