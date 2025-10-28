package com.example.skripsi.core.util

import java.util.Locale

object CurrencyFormatter {
    private val locale = Locale("in","ID")
    fun rupiah(v: Long) = "Rp %,d".format(locale, v)
}