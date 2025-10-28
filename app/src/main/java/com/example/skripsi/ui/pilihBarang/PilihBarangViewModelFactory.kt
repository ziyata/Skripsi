package com.example.skripsi.ui.pilihBarang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.repository.BarangRepository

@Suppress("UNCHECKED_CAST")
class PilihBarangViewModelFactory(
    private val repo: BarangRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PilihBarangViewModel::class.java)) {
            return PilihBarangViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
