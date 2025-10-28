package com.example.skripsi.data.repository

import com.example.skripsi.data.dao.UserDao
import com.example.skripsi.data.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class AuthRepository(private val userDao: UserDao) {

    suspend fun registerIfEmpty(): Unit = withContext(Dispatchers.IO) {
        // seed 1 admin, 1 user bila belum ada
        if (userDao.findByUsername("admin") == null) {
            userDao.insert(UserEntity(username = "admin", passwordHash = hash("admin123"), role = "ADMIN"))
        }
        if (userDao.findByUsername("user") == null) {
            userDao.insert(UserEntity(username = "user", passwordHash = hash("user123"), role = "USER"))
        }
    }

    suspend fun login(username: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        val u = userDao.findByUsername(username) ?: return@withContext null
        if (u.passwordHash == hash(password)) u else null
    }

    private fun hash(s: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        return md.digest(s.toByteArray()).joinToString("") { "%02x".format(it) }
    }
}
