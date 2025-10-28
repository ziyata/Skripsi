package com.example.skripsi.core.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.skripsi.data.dao.BarangDao
import com.example.skripsi.data.dao.OrderDao
import com.example.skripsi.data.dao.PaymentDao
import com.example.skripsi.data.dao.StockMutationDao
import com.example.skripsi.data.dao.TransaksiDetailDao
import com.example.skripsi.data.dao.TransaksiHeaderDao
import com.example.skripsi.data.dao.UserDao
import com.example.skripsi.data.entity.BarangEntity
import com.example.skripsi.data.entity.OrderDetailEntity
import com.example.skripsi.data.entity.OrderHeaderEntity
import com.example.skripsi.data.entity.PaymentEntity
import com.example.skripsi.data.entity.StockMutationEntity
import com.example.skripsi.data.entity.TransaksiDetailEntity
import com.example.skripsi.data.entity.TransaksiHeaderEntity
import com.example.skripsi.data.entity.UserEntity

@Database(
    entities = [
        BarangEntity::class,
        TransaksiHeaderEntity::class,
        TransaksiDetailEntity::class,
        StockMutationEntity::class,
        OrderHeaderEntity::class,
        OrderDetailEntity::class,
        PaymentEntity::class,
        UserEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun barangDao(): BarangDao
    abstract fun transaksiHeaderDao(): TransaksiHeaderDao
    abstract fun transaksiDetailDao(): TransaksiDetailDao
    abstract fun stockMutationDao(): StockMutationDao
    abstract fun orderDao(): OrderDao
    abstract fun paymentDao(): PaymentDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pos_db"
                )
                    // Cepat untuk dev; produksi sebaiknya tulis Migration manual
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
