package com.example.skripsi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skripsi.data.entity.TransaksiDetailEntity

@Dao
interface TransaksiDetailDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(details: List<TransaksiDetailEntity>)

    @Query("SELECT * FROM transaksi_detail WHERE transaksiId = :transaksiId")
    suspend fun getByTransaksiId(transaksiId: Int): List<TransaksiDetailEntity>
}
