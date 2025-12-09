package com.example.skripsi.feature.stok.vm

import androidx.lifecycle.*
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.repository.BarangRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LowStockViewModel(private val repo: BarangRepository) : ViewModel() {

    private val _items = MutableLiveData<List<BarangEntity>>()
    val items: LiveData<List<BarangEntity>> = _items

    fun load(minStok: Int = 5) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repo.getLowStock(minStok)
            _items.postValue(list)
        }
    }
}

class LowStockViewModelFactory(private val repo: BarangRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LowStockViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LowStockViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
