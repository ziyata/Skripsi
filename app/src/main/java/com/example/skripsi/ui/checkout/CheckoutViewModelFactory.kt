package com.example.skripsi.ui.checkout

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.data.AppDatabase
import com.example.skripsi.repository.CheckoutRepository
import com.example.skripsi.repository.PaymentRepository

@Suppress("UNCHECKED_CAST")
class CheckoutViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val db = AppDatabase.getDatabase(context.applicationContext)
    private val checkoutRepo = CheckoutRepository(
        barangDao = db.barangDao(),
        headerDao = db.transaksiHeaderDao(),
        detailDao = db.transaksiDetailDao(),
        stockMutationDao = db.stockMutationDao()
    )
    private val paymentRepo = PaymentRepository(db.paymentDao())

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
            return CheckoutViewModel(checkoutRepo, paymentRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
