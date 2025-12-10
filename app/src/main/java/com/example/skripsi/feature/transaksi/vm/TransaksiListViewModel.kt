package com.example.skripsi.feature.transaksi.vm

import android.content.Context
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.data.repository.TransaksiListRepository
import java.util.Calendar

class TransaksiListViewModel(private val repo: TransaksiListRepository) : ViewModel() {
    private val source = repo.getAllLive()
    val filtered = MediatorLiveData<List<TransaksiHeaderEntity>>()

    private var q: String = ""
    private var todayOnly = false

    init { filtered.addSource(source) { apply() } }

    fun setQuery(query: String) { q = query.trim().lowercase(); apply() }
    fun setTodayOnly(enable: Boolean) { todayOnly = enable; apply() }

    private fun apply() {
        val base = source.value.orEmpty().filter {
            q.isEmpty() || it.metode.lowercase().contains(q) || it.id.toString().contains(q)
        }
        filtered.value = if (!todayOnly) base else base.filter { sameDay(it.tanggal, System.currentTimeMillis()) }
    }

    private fun sameDay(a: Long, b: Long): Boolean {
        val c1 = Calendar.getInstance().apply { timeInMillis = a }
        val c2 = Calendar.getInstance().apply { timeInMillis = b }
        return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
    }
}

@Suppress("UNCHECKED_CAST")
class TransaksiListViewModelFactory(ctx: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(ctx.applicationContext)
    private val repo = TransaksiListRepository(db.transaksiHeaderDao())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaksiListViewModel::class.java)) return TransaksiListViewModel(repo) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
