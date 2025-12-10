package com.example.skripsi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.skripsi.data.entity.StockMutationEntity

@Dao
interface StockMutationDao {
    @Insert
    suspend fun insert(mutation: StockMutationEntity): Long

    @Query("SELECT * FROM stock_mutation WHERE barangId = :barangId ORDER BY waktu DESC")
    suspend fun listByBarang(barangId: Int): List<StockMutationEntity>

}
