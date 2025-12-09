package com.example.skripsi.feature.checkout.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.PaymentEntity
import com.example.skripsi.data.repository.CheckoutRepository
import com.example.skripsi.data.repository.PaymentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val checkoutRepo: CheckoutRepository,
    private val paymentRepo: PaymentRepository
) : ViewModel() {

    data class CartLine(
        val barang: BarangEntity,
        var qty: Int
    ) {
        val subtotal: Long get() = barang.harga * qty.toLong()
    }

    val cart = MutableLiveData<List<CartLine>>(emptyList())

    fun addToCart(barang: BarangEntity, qty: Int = 1) {
        val current = cart.value.orEmpty().toMutableList()
        val idx = current.indexOfFirst { it.barang.id == barang.id }
        if (idx >= 0) {
            val line = current[idx]
            current[idx] = line.copy(qty = line.qty + qty)
        } else {
            current.add(CartLine(barang, qty))
        }
        cart.value = current
    }

    fun removeFromCart(barangId: Int) {
        cart.value = cart.value.orEmpty().filter { it.barang.id != barangId }
    }

    fun increase(barangId: Int) {
        cart.value = cart.value.orEmpty().map {
            if (it.barang.id == barangId) it.copy(qty = it.qty + 1) else it
        }
    }

    fun decrease(barangId: Int) {
        cart.value = cart.value.orEmpty().mapNotNull {
            if (it.barang.id == barangId) {
                val newQty = it.qty - 1
                if (newQty <= 0) null else it.copy(qty = newQty)
            } else it
        }
    }

    val total: Long
        get() = cart.value.orEmpty().sumOf { it.subtotal }

    fun payCash(
        bayar: Long,
        onSuccess: (headerId: Int, change: Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        val items = cart.value.orEmpty()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val headerId = checkoutRepo.checkout(
                    items = items.map { CheckoutRepository.CartItem(it.barang, it.qty) },
                    metode = "CASH"
                )
                val change = (bayar - total).coerceAtLeast(0L)

                paymentRepo.insert(
                    PaymentEntity(
                        orderId = 0,
                        transaksiId = headerId,
                        method = "CASH",
                        amount = bayar,
                        changeAmount = change,
                        refNumber = null,
                        status = "SUCCESS",
                        paidAt = System.currentTimeMillis()
                    )
                )

                launch(Dispatchers.Main) {
                    cart.value = emptyList()
                    onSuccess(headerId, change)
                }
            } catch (e: Throwable) {
                launch(Dispatchers.Main) { onError(e) }
            }
        }
    }
}
