package com.example.skripsi.repository

import androidx.lifecycle.LiveData
import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.entity.BarangEntity

class BarangRepository(private val barangDao: BarangDao) {

    val allBarang: LiveData<List<BarangEntity>> = barangDao.getAllBarang()

    suspend fun insert(barang: BarangEntity) = barangDao.insertBarang(barang)

    suspend fun updateBarang(barang: BarangEntity) = barangDao.updateBarang(barang)

    suspend fun delete(barang: BarangEntity) = barangDao.deleteBarang(barang)

    suspend fun getByNama(nama: String): BarangEntity? = barangDao.getBarangByNama(nama)

    suspend fun getBarangById(id: Int): BarangEntity? = barangDao.getBarangById(id)
}
