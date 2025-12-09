package com.example.skripsi.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.skripsi.data.entity.BarangEntity

@Dao
interface BarangDao {
    @Query("SELECT * FROM barang ORDER BY id DESC")
    fun getAllBarang(): LiveData<List<BarangEntity>>

    // Tambahan: ambil list sekali, dipakai di OrderActivity
    @Query("SELECT * FROM barang ORDER BY id DESC")
    suspend fun getAllBarangOnce(): List<BarangEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBarang(barang: BarangEntity)

    @Update
    suspend fun updateBarang(barang: BarangEntity)

    @Delete
    suspend fun deleteBarang(barang: BarangEntity)

    @Query("SELECT * FROM barang WHERE nama = :namaBarang LIMIT 1")
    suspend fun getBarangByNama(namaBarang: String): BarangEntity?

    @Query("SELECT * FROM barang WHERE id = :id LIMIT 1")
    suspend fun getBarangById(id: Int): BarangEntity?

    @Query("SELECT * FROM barang WHERE stok <= :minStok ORDER BY stok ASC")
    suspend fun getLowStock(minStok: Int = 5): List<BarangEntity>

}
