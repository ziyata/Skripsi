package com.example.skripsi.feature.transaksi.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skripsi.core.db.AppDatabase
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.data.repository.TransaksiListRepository

class TransaksiListViewModel(
    private val repo: TransaksiListRepository
) : ViewModel() {

    private val source: LiveData<List<TransaksiHeaderEntity>> = repo.getAllLive()
    val filtered = MediatorLiveData<List<TransaksiHeaderEntity>>()

    private var currentQuery: String = ""
    private var filterToday: Boolean = false

    init {
        filtered.addSource(source) { applyFilters() }
    }

    fun setQuery(q: String) {
        currentQuery = q.trim()
        applyFilters()
    }

    fun setTodayOnly(todayOnly: Boolean) {
        filterToday = todayOnly
        applyFilters()
    }

    private fun applyFilters() {
        val list = source.value.orEmpty()
        val q = currentQuery.lowercase()

        val base = if (q.isEmpty()) list else list.filter {
            it.metode.lowercase().contains(q) || it.id.toString().contains(q)
        }

        val result = if (!filterToday) base else base.filter {
            isSameDay(it.tanggal, System.currentTimeMillis())
        }

        filtered.value = result
    }

    private fun isSameDay(t1: Long, t2: Long): Boolean {
        val cal1 = java.util.Calendar.getInstance().apply { timeInMillis = t1 }
        val cal2 = java.util.Calendar.getInstance().apply { timeInMillis = t2 }
        return cal1.get(java.util.Calendar.YEAR) == cal2.get(java.util.Calendar.YEAR) &&
                cal1.get(java.util.Calendar.DAY_OF_YEAR) == cal2.get(java.util.Calendar.DAY_OF_YEAR)
    }
}

@Suppress("UNCHECKED_CAST")
class TransaksiListViewModelFactory(context: Context) : ViewModelProvider.Factory {
    private val db = AppDatabase.getDatabase(context.applicationContext)
    private val repo = TransaksiListRepository(db.transaksiHeaderDao())
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransaksiListViewModel::class.java)) {
            return TransaksiListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
