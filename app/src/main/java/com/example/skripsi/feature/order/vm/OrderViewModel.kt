package com.example.skripsi.feature.order.vm

import android.content.Context
import androidx.lifecycle.*
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderViewModel(private val repo: OrderRepository) : ViewModel() {

    var orderId: Int? = null
        private set
    var tableId: String? = null
        private set

    private val _items = MutableLiveData<List<OrderDetailEntity>>(emptyList())
    val items: LiveData<List<OrderDetailEntity>> = _items

    val total: Long get() = _items.value.orEmpty().sumOf { it.subtotal }

    fun initDraft(tableId: String?) {
        this.tableId = tableId
        viewModelScope.launch {
            orderId = repo.getOrCreateDraft(tableId)
            refresh()
        }
    }

    fun addItem(barangId: Int, qty: Int = 1) {
        val id = orderId ?: return
        viewModelScope.launch {
            repo.addItem(id, barangId, qty)
            refresh()
        }
    }

    fun markUnpaid(onDone: () -> Unit, onError: (Throwable) -> Unit) {
        val id = orderId ?: return
        viewModelScope.launch {
            try {
                repo.markUnpaid(id)
                onDone()
            } catch (e: Throwable) {
                onError(e)
            }
        }
    }

    private fun refresh() {
        val id = orderId ?: return
        viewModelScope.launch(Dispatchers.IO) {
            val list = repo.listDetails(id)
            _items.postValue(list)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class OrderViewModelFactory(ctx: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(ctx.applicationContext)
    private val repo = OrderRepository(
        db.orderDao(),
        db.barangDao(),
        db.transaksiHeaderDao(),
        db.transaksiDetailDao(),
        db.stockMutationDao(),
        db.paymentDao()
    )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderViewModel::class.java)) {
            return OrderViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
