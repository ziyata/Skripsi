package com.example.skripsi.feature.transaksi.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.TransaksiDetailEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransaksiDetailViewModel(
    private val db: AppDatabase
) : ViewModel() {

    private val _details = MutableLiveData<List<TransaksiDetailEntity>>()
    val details: LiveData<List<TransaksiDetailEntity>> = _details

    fun load(transaksiId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = db.transaksiDetailDao().getByTransaksiId(transaksiId)
            _details.postValue(list)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class TransaksiDetailViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(context.applicationContext)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaksiDetailViewModel::class.java)) {
            return TransaksiDetailViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
