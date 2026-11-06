@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kosku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kosku.ui.components.BackButton
import com.example.kosku.ui.components.EmptyState
import com.example.kosku.ui.components.KosCard
import com.example.kosku.util.toRupiah
import com.example.kosku.viewmodel.KosViewModel

/** HALAMAN 6: Daftar Favorit */
@Composable
fun FavoritesScreen(
    vm: KosViewModel,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onDetail: (Int) -> Unit
) {
    val favorites by vm.favoriteKos.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Kos Favorit") }, navigationIcon = { BackButton(onBack) }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { pad ->
        Box(Modifier.padding(pad)) {
            if (favorites.isEmpty()) {
                EmptyState("Belum ada favorit", "Tekan ikon hati pada kos untuk menyimpannya di sini.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(favorites, key = { it.id }) { kos ->
                        KosCard(kos, true, { onDetail(kos.id) }, { vm.toggleFavorite(kos) })
                    }
                }
            }
        }
    }
}

/** HALAMAN 7: Riwayat Pemesanan */
@Composable
fun HistoryScreen(vm: KosViewModel, snackbar: SnackbarHostState, onBack: () -> Unit) {
    val bookings by vm.bookings.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Riwayat Pemesanan") }, navigationIcon = { BackButton(onBack) }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { pad ->
        Box(Modifier.padding(pad)) {
            if (bookings.isEmpty()) {
                EmptyState("Belum ada pemesanan", "Pesan kos dari halaman detail untuk melihat riwayat di sini.")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookings, key = { it.id }) { b ->
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(b.kosName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                Text("Penyewa: ${b.tenantName} (${b.phone})")
                                Text("Mulai: ${b.startDate} • ${b.months} bulan")
                                Text("Total: ${b.total.toRupiah()}", color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }
    }
}
