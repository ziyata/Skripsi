package com.example.skripsi.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skripsi.data.entity.TransaksiHeaderEntity

@Dao
interface TransaksiHeaderDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(header: TransaksiHeaderEntity): Long

    @Query("SELECT * FROM transaksi_header ORDER BY tanggal DESC")
    fun getAllLive(): LiveData<List<TransaksiHeaderEntity>>
}
