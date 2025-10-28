package com.example.skripsi.core.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtil {
    private val fmt = SimpleDateFormat("dd MMM yyyy HH:mm", Locale("in","ID"))
    fun longToHuman(millis: Long): String = fmt.format(Date(millis))
}