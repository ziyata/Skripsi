package com.example.skripsi.ui.pilihBarang

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.repository.BarangRepository

class PilihBarangViewModel(
    private val repo: BarangRepository
) : ViewModel() {
    val allBarang: LiveData<List<BarangEntity>> = repo.allBarang
}
