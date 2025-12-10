package com.example.skripsi.feature.stok.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.StockMutationEntity
import com.example.skripsi.data.repository.StockRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StockViewModel(private val repo: StockRepository) : ViewModel() {

    private val _mutasi = MutableLiveData<List<StockMutationEntity>>()
    val mutasi: LiveData<List<StockMutationEntity>> = _mutasi

    fun addIn(barangId: Int, qty: Int, note: String?) {
        viewModelScope.launch {
            try {
                repo.adjustIn(barangId, qty, note)
                loadMutasi(barangId)
            } catch (e: Throwable) {
                // emit error via another LiveData if needed
            }
        }
    }

    fun correct(barangId: Int, qtyDiff: Int, note: String?) {
        viewModelScope.launch {
            try {
                repo.adjustCorrection(barangId, qtyDiff, note)
                loadMutasi(barangId)
            } catch (e: Throwable) {
                // emit error
            }
        }
    }

    fun loadMutasi(barangId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repo.listMutasi(barangId)
            _mutasi.postValue(list)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class StockViewModelFactory(ctx: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(ctx.applicationContext)
    private val repo = StockRepository(db.barangDao(), db.stockMutationDao())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StockViewModel::class.java)) {
            return StockViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
