package com.example.skripsi.feature.checkout.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.repository.BarangRepository

class PilihBarangViewModel(
    private val repo: BarangRepository
) : ViewModel() {
    val allBarang: LiveData<List<BarangEntity>> = repo.allBarang
}
