package com.example.skripsi.feature.order.vm

import android.content.Context
import androidx.lifecycle.*
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.OrderHeaderEntity
import com.example.skripsi.data.repository.OrderRepository
import com.example.skripsi.feature.order.ui.AdminOrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AdminOrderViewModel(private val repo: OrderRepository, private val appContext: Context) : ViewModel() {

    private val _orders = MutableLiveData<List<AdminOrderItem>>()
    val orders: LiveData<List<AdminOrderItem>> = _orders

    fun loadUnpaid() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = repo.getUnpaidOrdersWithPayment()
            _orders.postValue(list)
        }
    }

    fun confirm(orderId: Int, onSuccess: () -> Unit, onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            repo.confirmOrder(
                context = appContext,
                orderId = orderId,
                onSuccess = { onSuccess() },
                onError = { onError(it) }
            )
        }
    }
}

@Suppress("UNCHECKED_CAST")
class AdminOrderViewModelFactory(ctx: Context) : ViewModelProvider.Factory {
    private val appCtx = ctx.applicationContext
    private val db = AppDatabase.getDatabase(appCtx)
    private val repo = OrderRepository(
        db.orderDao(),
        db.barangDao(),
        db.transaksiHeaderDao(),
        db.transaksiDetailDao(),
        db.stockMutationDao(),
        db.paymentDao()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminOrderViewModel::class.java)) {
            return AdminOrderViewModel(repo, appCtx) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
