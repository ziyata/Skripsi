package com.example.skripsi.feature.admin

data class TopSellingItem(
    val barangId: Int,
    val nama: String,
    val harga: Long,
    val totalQty: Int,
    val totalRevenue: Long
)
