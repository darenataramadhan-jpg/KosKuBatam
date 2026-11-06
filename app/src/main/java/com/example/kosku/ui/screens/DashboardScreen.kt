@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kosku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kosku.data.SortOption
import com.example.kosku.ui.components.EmptyState
import com.example.kosku.ui.components.KosCard
import com.example.kosku.util.NetworkUtils
import com.example.kosku.viewmodel.KosViewModel

/** HALAMAN 3: Dashboard (RecyclerView-equivalent: LazyColumn) */
@Composable
fun DashboardScreen(
    vm: KosViewModel,
    userName: String,
    snackbar: SnackbarHostState,
    onDetail: (Int) -> Unit,
    onFavorites: () -> Unit,
    onHistory: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val list by vm.kosList.collectAsStateWithLifecycle()
    val query by vm.query.collectAsStateWithLifecycle()
    val type by vm.type.collectAsStateWithLifecycle()
    val sort by vm.sort.collectAsStateWithLifecycle()
    val favs by vm.favoriteIds.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()

    var showLogout by rememberSaveable { mutableStateOf(false) }
    var sortMenu by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("KosKu Batam", style = MaterialTheme.typography.titleLarge)
                        Text("Halo, $userName", style = MaterialTheme.typography.bodySmall)
                    }
                },
                actions = {
                    IconButton(onClick = { vm.refresh(NetworkUtils.isOnline(context)) }) {
                        Icon(Icons.Default.Refresh, "Muat ulang")
                    }
                    IconButton(onClick = onFavorites) { Icon(Icons.Default.Favorite, "Favorit") }
                    IconButton(onClick = onHistory) { Icon(Icons.Default.DateRange, "Riwayat") }
                    IconButton(onClick = { showLogout = true }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, "Keluar")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { pad ->
        Column(Modifier.padding(pad).fillMaxSize()) {
            OutlinedTextField(
                value = query,
                onValueChange = vm::onQueryChange,
                placeholder = { Text("Cari nama kos atau lokasi...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { vm.onQueryChange("") }) { Icon(Icons.Default.Clear, "Hapus") }
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("Semua", "Putra", "Putri", "Campur")) { t ->
                    FilterChip(selected = type == t, onClick = { vm.onTypeChange(t) }, label = { Text(t) })
                }
            }

            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${list.size} kos ditemukan", style = MaterialTheme.typography.bodyMedium)
                Box {
                    TextButton(onClick = { sortMenu = true }) { Text("Urutkan: ${sort.label}") }
                    DropdownMenu(expanded = sortMenu, onDismissRequest = { sortMenu = false }) {
                        SortOption.entries.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                onClick = { vm.onSortChange(option); sortMenu = false }
                            )
                        }
                    }
                }
            }

            when {
                loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
                list.isEmpty() -> EmptyState(
                    "Kos tidak ditemukan",
                    "Coba kata kunci lain atau ubah filter tipe kos."
                )
                else -> LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(list, key = { it.id }) { kos ->
                        KosCard(
                            kos = kos,
                            isFavorite = kos.id in favs,
                            onClick = { onDetail(kos.id) },
                            onFavorite = { vm.toggleFavorite(kos) }
                        )
                    }
                }
            }
        }
    }

    if (showLogout) {
        AlertDialog(
            onDismissRequest = { showLogout = false },
            title = { Text("Keluar") },
            text = { Text("Apakah Anda yakin ingin keluar dari akun?") },
            confirmButton = { TextButton(onClick = { showLogout = false; onLogout() }) { Text("Ya") } },
            dismissButton = { TextButton(onClick = { showLogout = false }) { Text("Batal") } }
        )
    }
}
