package com.example.skripsi.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skripsi.data.entity.TransaksiEntity

@Dao
interface TransaksiDao {
    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    fun getAllTransaksiLive(): LiveData<List<TransaksiEntity>>

    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    suspend fun getAllTransaksi(): List<TransaksiEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaksi(transaksi: TransaksiEntity)
}
