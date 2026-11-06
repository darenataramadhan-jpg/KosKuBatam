package com.example.kosku.util

import android.util.Patterns
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.format.ResolverStyle
import java.util.Locale

/** Semua fungsi mengembalikan pesan error (String) atau null jika valid. */
object Validators {
    private val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    fun validateEmail(email: String): String? = when {
        email.isBlank() -> "Email tidak boleh kosong"
        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() -> "Format email tidak valid"
        else -> null
    }

    fun validatePassword(password: String): String? = when {
        password.isBlank() -> "Password tidak boleh kosong"
        password.length < 6 -> "Password minimal 6 karakter"
        else -> null
    }

    fun validateName(name: String): String? = when {
        name.isBlank() -> "Nama tidak boleh kosong"
        name.trim().length < 3 -> "Nama minimal 3 karakter"
        !name.all { it.isLetter() || it == ' ' || it == '.' } -> "Nama hanya boleh berisi huruf"
        else -> null
    }

    fun validatePhone(phone: String): String? = when {
        phone.isBlank() -> "Nomor HP tidak boleh kosong"
        !Regex("^(08|\\+628)[0-9]{8,11}$").matches(phone.trim()) ->
            "Nomor HP tidak valid (contoh: 081234567890)"
        else -> null
    }

    fun validateDate(input: String): String? {
        if (input.isBlank()) return "Tanggal masuk tidak boleh kosong"
        return try {
            val date = LocalDate.parse(input.trim(), dateFormatter)
            if (date.isBefore(LocalDate.now())) "Tanggal tidak boleh sebelum hari ini" else null
        } catch (e: DateTimeParseException) {
            "Format tanggal harus dd-MM-yyyy (contoh: 15-11-2026)"
        }
    }
}

fun Int.toRupiah(): String = "Rp " + String.format(Locale("id", "ID"), "%,d", this)
