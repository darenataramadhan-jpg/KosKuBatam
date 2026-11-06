package com.example.kosku.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kosku.data.Booking
import com.example.kosku.data.DummyData
import com.example.kosku.data.Kos
import com.example.kosku.data.SortOption
import com.example.kosku.util.Validators
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel utama. State disimpan di sini sehingga TIDAK hilang saat
 * layar diputar (rotasi) maupun saat berpindah halaman.
 */
class KosViewModel : ViewModel() {

    private val allKos = MutableStateFlow(DummyData.kosList)
    private val _query = MutableStateFlow("")
    private val _type = MutableStateFlow("Semua")
    private val _sort = MutableStateFlow(SortOption.DEFAULT)
    private val _favorites = MutableStateFlow<Set<Int>>(emptySet())
    private val _bookings = MutableStateFlow<List<Booking>>(emptyList())
    private val _loading = MutableStateFlow(false)
    private val _events = MutableSharedFlow<String>(extraBufferCapacity = 8)

    val query: StateFlow<String> = _query.asStateFlow()
    val type: StateFlow<String> = _type.asStateFlow()
    val sort: StateFlow<SortOption> = _sort.asStateFlow()
    val favoriteIds: StateFlow<Set<Int>> = _favorites.asStateFlow()
    val bookings: StateFlow<List<Booking>> = _bookings.asStateFlow()
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    val events = _events.asSharedFlow()          // pesan sekali tampil (Snackbar)

    /** FITUR 1: pencarian + filter tipe + pengurutan (dinamis). */
    val kosList: StateFlow<List<Kos>> =
        combine(allKos, _query, _type, _sort) { list, q, t, s ->
            val key = q.trim()
            val filtered = list.filter { kos ->
                (t == "Semua" || kos.type == t) &&
                        (key.isEmpty() ||
                                kos.name.contains(key, ignoreCase = true) ||
                                kos.location.contains(key, ignoreCase = true))
            }
            when (s) {
                SortOption.DEFAULT -> filtered
                SortOption.PRICE_LOW -> filtered.sortedBy { it.price }
                SortOption.PRICE_HIGH -> filtered.sortedByDescending { it.price }
                SortOption.RATING -> filtered.sortedByDescending { it.rating }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DummyData.kosList)

    /** FITUR 2: daftar favorit. */
    val favoriteKos: StateFlow<List<Kos>> =
        combine(allKos, _favorites) { list, favs -> list.filter { it.id in favs } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(value: String) { _query.value = value }
    fun onTypeChange(value: String) { _type.value = value }
    fun onSortChange(value: SortOption) { _sort.value = value }

    fun findKos(id: Int): Kos? = allKos.value.firstOrNull { it.id == id }

    fun notify(message: String) { _events.tryEmit(message) }

    fun toggleFavorite(kos: Kos) {
        val added = kos.id !in _favorites.value
        _favorites.update { if (added) it + kos.id else it - kos.id }
        notify(if (added) "${kos.name} ditambahkan ke favorit" else "${kos.name} dihapus dari favorit")
    }

    /** Simulasi muat ulang data. Jika offline, tampilkan peringatan (tidak crash). */
    fun refresh(isOnline: Boolean) {
        viewModelScope.launch {
            _loading.value = true
            try {
                delay(900)
                if (!isOnline) notify("Tidak ada koneksi internet. Menampilkan data lokal.")
                else notify("Data berhasil diperbarui")
                allKos.value = DummyData.kosList
            } catch (e: Exception) {
                notify("Gagal memuat data: ${e.message ?: "kesalahan tidak diketahui"}")
            } finally {
                _loading.value = false
            }
        }
    }

    /** FITUR 3: pemesanan kos dengan validasi. Return true jika berhasil. */
    fun submitBooking(kos: Kos, name: String, phone: String, date: String, months: Int): Boolean {
        val error = Validators.validateName(name)
            ?: Validators.validatePhone(phone)
            ?: Validators.validateDate(date)
        if (error != null) { notify(error); return false }

        val booking = Booking(
            id = System.currentTimeMillis(),
            kosName = kos.name,
            tenantName = name.trim(),
            phone = phone.trim(),
            startDate = date.trim(),
            months = months,
            total = kos.price * months
        )
        _bookings.update { listOf(booking) + it }
        return true
    }
}
