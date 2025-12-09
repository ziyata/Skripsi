package com.example.skripsi.feature.order.ui

data class AdminOrderItem(
    val orderId: Int,
    val tableId: String?,
    val total: Long,
    val paymentMethod: String,
    val paymentStatus: String
)
