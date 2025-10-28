package com.example.skripsi.ui.pembelian

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.repository.BarangRepository
import com.example.skripsi.repository.TransaksiRepository

@Suppress("UNCHECKED_CAST")
class PembelianViewModelFactory(
    private val barangRepo: BarangRepository,
    private val transaksiRepo: TransaksiRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PembelianViewModel::class.java)) {
            return PembelianViewModel(barangRepo, transaksiRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
