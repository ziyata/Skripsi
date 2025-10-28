package com.example.skripsi.data.repository

import androidx.lifecycle.LiveData
import com.example.skripsi.data.dao.TransaksiHeaderDao
import com.example.skripsi.data.entity.TransaksiHeaderEntity

class TransaksiListRepository(private val dao: TransaksiHeaderDao) {
    fun getAllLive(): LiveData<List<TransaksiHeaderEntity>> = dao.getAllLive()
}
