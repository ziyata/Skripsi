package com.example.skripsi.ui.pembelian

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.TransaksiEntity
import com.example.skripsi.repository.BarangRepository
import com.example.skripsi.repository.TransaksiRepository
import kotlinx.coroutines.launch

class PembelianViewModel(
    private val barangRepo: BarangRepository,
    private val transaksiRepo: TransaksiRepository
) : ViewModel() {

    val allBarang: LiveData<List<BarangEntity>> = barangRepo.allBarang
    val allTransaksi: LiveData<List<TransaksiEntity>> = transaksiRepo.allTransaksiLive()

    fun insertTransaksi(transaksi: TransaksiEntity) = viewModelScope.launch {
        val barang = barangRepo.getBarangById(transaksi.barangId) ?: return@launch
        val sisa = barang.stok - transaksi.jumlah
        if (sisa < 0) return@launch

        transaksiRepo.insert(transaksi)
        val updatedBarang = barang.copy(stok = sisa)
        barangRepo.updateBarang(updatedBarang)
    }
}
