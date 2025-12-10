package com.example.skripsi.data.repository

import android.content.Context
import android.util.Log
import com.example.skripsi.core.util.StockNotification
import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.OrderDao
import com.example.skripsi.data.dao.PaymentDao
import com.example.skripsi.data.dao.StockMutationDao
import com.example.skripsi.data.dao.TransaksiDetailDao
import com.example.skripsi.data.dao.TransaksiHeaderDao
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity
import com.example.skripsi.data.entity.PaymentEntity
import com.example.skripsi.data.entity.StockMutationEntity
import com.example.skripsi.data.entity.TransaksiDetailEntity
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.feature.order.ui.AdminOrderItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrderRepository(
    private val orderDao: OrderDao,
    private val barangDao: BarangDao,
    private val transaksiHeaderDao: TransaksiHeaderDao,
    private val transaksiDetailDao: TransaksiDetailDao,
    private val stockMutationDao: StockMutationDao,
    private val paymentDao: PaymentDao
) {

    suspend fun getOrCreateDraft(tableId: String?): Int = withContext(Dispatchers.IO) {
        val existing = orderDao.findDraft(tableId)
        existing?.id
            ?: orderDao.insertHeader(
                OrderHeaderEntity(
                    id = 0,
                    status = "DRAFT",
                    tableId = tableId,
                    createdAt = System.currentTimeMillis(),
                    total = 0L
                )
            ).toInt()
    }

    suspend fun addItem(orderId: Int, barangId: Int, qty: Int) = withContext(Dispatchers.IO) {
        Log.d("OrderRepo", "addItem orderId=$orderId barangId=$barangId qty=$qty")
        val barang = barangDao.getBarangById(barangId)
            ?: throw IllegalArgumentException("Barang tidak ditemukan")

        val detail = orderDao.findDetail(orderId, barangId)
        if (detail == null) {
            orderDao.insertDetail(
                OrderDetailEntity(
                    id = 0,
                    orderId = orderId,
                    barangId = barangId,
                    namaSnapshot = barang.nama,
                    hargaSatuan = barang.harga,
                    qty = qty,
                    subtotal = barang.harga * qty.toLong()
                )
            )
        } else {
            val newQty = detail.qty + qty
            orderDao.updateDetail(
                detail.copy(
                    qty = newQty,
                    subtotal = detail.hargaSatuan * newQty.toLong()
                )
            )
        }
        updateOrderTotal(orderId)
    }

    private suspend fun updateOrderTotal(orderId: Int) {
        val details = orderDao.listDetails(orderId)
        val total = details.sumOf { it.subtotal }
        orderDao.updateTotal(orderId, total)
    }

    suspend fun listDetails(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.listDetails(orderId)
    }

    suspend fun markUnpaid(orderId: Int, method: String) = withContext(Dispatchers.IO) {
        updateOrderTotal(orderId)
        orderDao.updateStatus(orderId, "UNPAID")

        val header = orderDao.getOrderById(orderId)
        if (header != null) {
            paymentDao.insert(
                PaymentEntity(
                    orderId = orderId,
                    transaksiId = null,
                    method = method,
                    amount = header.total,
                    changeAmount = 0L,
                    refNumber = null,
                    status = "PENDING",
                    paidAt = System.currentTimeMillis()
                )
            )

        }
    }


    suspend fun getUnpaidOrdersWithPayment(): List<AdminOrderItem> = withContext(Dispatchers.IO) {
        val headers = orderDao.listUnpaidOrders()  // list OrderHeaderEntity status UNPAID
        val payments = paymentDao.getByOrders(headers.map { it.id })

        // map orderId -> payment (ambil yang terbaru)
        val paymentMap = payments.groupBy { it.orderId }.mapValues { (_, list) ->
            list.maxByOrNull { it.paidAt }   // atau pakai id terbesar
        }

        headers.map { h ->
            val p = paymentMap[h.id]
            AdminOrderItem(
                orderId = h.id,
                tableId = h.tableId,
                total = h.total,
                paymentMethod = p?.method ?: "-",
                paymentStatus = p?.status ?: "-"
            )
        }
    }


    suspend fun confirmOrder(
        context: Context,
        orderId: Int,
        onSuccess: (Long) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val details = orderDao.listDetails(orderId)
                if (details.isEmpty()) throw IllegalStateException("Order kosong")

                val header = orderDao.getOrderById(orderId)
                    ?: throw IllegalStateException("Order tidak ditemukan")

                val total = header.total
                val now = System.currentTimeMillis()

                // Buat TransaksiHeader
                val transaksiId = transaksiHeaderDao.insert(
                    TransaksiHeaderEntity(
                        id = 0,
                        tanggal = now,
                        total = total,
                        metode = "TRANSFER",
                        status = "PAID",
                        customerId = null
                    )
                )

                // Buat list TransaksiDetail untuk insertAll
                val transaksiDetails = details.map { d ->
                    TransaksiDetailEntity(
                        id = 0,
                        transaksiId = transaksiId.toInt(),  // sesuai entity kamu
                        barangId = d.barangId,
                        namaBarangSnapshot = d.namaSnapshot,
                        hargaSatuan = d.hargaSatuan,
                        qty = d.qty,
                        subtotal = d.subtotal,
                        createdAt = now
                    )
                }

                // Insert semua detail sekaligus dengan insertAll
                transaksiDetailDao.insertAll(transaksiDetails)

                // Update stok dan mutation
                details.forEach { d ->
                    val barang = barangDao.getBarangById(d.barangId)
                    if (barang != null) {
                        val newStok = barang.stok - d.qty
                        barangDao.updateBarang(barang.copy(stok = newStok))

                        if (newStok <= 5) {
                            // panggil helper
                            withContext(Dispatchers.Main) {
                                StockNotification.showLowStock(context, barang.nama, newStok)
                            }
                        }
                    }
                    stockMutationDao.insert(
                        StockMutationEntity(
                            id = 0,
                            barangId = d.barangId,
                            tipe = "OUT",
                            qty = d.qty,
                            waktu = now,
                            refType = "ORDER",
                            refId = orderId
                        )
                    )
                }

                // Update Payment jadi SUCCESS
                paymentDao.updateStatusByOrder(orderId, "SUCCESS")

                // Update order status jadi PAID
                orderDao.updateStatus(orderId, "PAID")

                withContext(Dispatchers.Main) { onSuccess(transaksiId) }

            } catch (e: Throwable) {
                withContext(Dispatchers.Main) { onError(e) }
            }
        }
    }

    suspend fun clear(orderId: Int) = withContext(Dispatchers.IO) {
        orderDao.deleteDetails(orderId)
        orderDao.deleteHeader(orderId)
    }
}
