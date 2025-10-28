package com.example.skripsi.ui.barang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.repository.BarangRepository

@Suppress("UNCHECKED_CAST")
class BarangViewModelFactory(private val repository: BarangRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BarangViewModel::class.java)) {
            return BarangViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
