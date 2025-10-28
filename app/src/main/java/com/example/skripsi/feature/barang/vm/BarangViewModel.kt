package com.example.skripsi.feature.barang.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.repository.BarangRepository
import kotlinx.coroutines.launch

class BarangViewModel(private val repository: BarangRepository) : ViewModel() {
    val allBarang: LiveData<List<BarangEntity>> = repository.allBarang

    fun insert(barang: BarangEntity) = viewModelScope.launch {
        repository.insert(barang)
    }

    fun update(barang: BarangEntity) = viewModelScope.launch {
        repository.updateBarang(barang)
    }

    fun delete(barang: BarangEntity) = viewModelScope.launch {
        repository.delete(barang)
    }
}
