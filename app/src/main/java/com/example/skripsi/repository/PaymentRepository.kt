package com.example.skripsi.repository

import com.example.skripsi.data.dao.PaymentDao
import com.example.skripsi.data.entity.PaymentEntity

class PaymentRepository(private val dao: PaymentDao) {
    suspend fun insert(payment: PaymentEntity): Long = dao.insert(payment)
}
