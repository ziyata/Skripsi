package com.example.skripsi.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.skripsi.data.entity.TransaksiDetailEntity
import com.example.skripsi.feature.admin.TopSellingItem

@Dao
interface TransaksiDetailDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(details: List<TransaksiDetailEntity>)

    @Query("SELECT * FROM transaksi_detail WHERE transaksiId = :transaksiId")
    suspend fun getByTransaksiId(transaksiId: Int): List<TransaksiDetailEntity>

    @Query(
        """
        SELECT
            b.id AS barangId,
            b.nama AS nama,
            b.harga AS harga,
            SUM(td.qty) AS totalQty,
            SUM(td.subtotal) AS totalRevenue
        FROM transaksi_detail td
        JOIN barang b ON td.barangId = b.id
        WHERE td.createdAt >= :startDate
        GROUP BY b.id, b.nama, b.harga
        ORDER BY totalQty DESC
        LIMIT 10
        """
    )
    suspend fun getTopSellingItems(startDate: Long): List<TopSellingItem>

    data class DailyUsage(
        val barangId: Int,
        val nama: String,
        val avgDailyQty: Double
    )

    @Query(
        """
    SELECT 
        b.id AS barangId,
        b.nama AS nama,
        SUM(td.qty) * 1.0 / COUNT(DISTINCT date(td.createdAt / 1000, 'unixepoch')) AS avgDailyQty
    FROM transaksi_detail td
    JOIN barang b ON td.barangId = b.id
    WHERE td.createdAt >= :startDate
    GROUP BY b.id, b.nama
    """
    )
    suspend fun getDailyUsage(startDate: Long): List<DailyUsage>

}

