package com.example.skripsi.repository

import androidx.lifecycle.LiveData
import com.example.skripsi.data.dao.TransaksiDao
import com.example.skripsi.data.entity.TransaksiEntity

class TransaksiRepository(private val transaksiDao: TransaksiDao) {

    fun allTransaksiLive(): LiveData<List<TransaksiEntity>> = transaksiDao.getAllTransaksiLive()

    suspend fun insert(transaksi: TransaksiEntity) = transaksiDao.insertTransaksi(transaksi)

    suspend fun getAll(): List<TransaksiEntity> = transaksiDao.getAllTransaksi()
}
