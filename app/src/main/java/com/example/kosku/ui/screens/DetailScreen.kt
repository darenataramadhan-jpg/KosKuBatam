@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)

package com.example.kosku.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.kosku.data.Kos
import com.example.kosku.ui.components.BackButton
import com.example.kosku.ui.components.EmptyState
import com.example.kosku.util.toRupiah
import com.example.kosku.viewmodel.KosViewModel

/** HALAMAN 4: Detail Kos */
@Composable
fun DetailScreen(
    kos: Kos?,
    vm: KosViewModel,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onBook: () -> Unit
) {
    val favs by vm.favoriteIds.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kos") },
                navigationIcon = { BackButton(onBack) },
                actions = {
                    if (kos != null) {
                        val isFav = kos.id in favs
                        IconButton(onClick = { vm.toggleFavorite(kos) }) {
                            Icon(
                                if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "Favorit",
                                tint = if (isFav) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbar) },
        bottomBar = {
            if (kos != null) {
                Button(
                    onClick = onBook,
                    modifier = Modifier.fillMaxWidth().padding(16.dp).height(50.dp)
                ) { Text("PESAN SEKARANG") }
            }
        }
    ) { pad ->
        // Penanganan error: id kos tidak ditemukan -> tidak crash
        if (kos == null) {
            Box(Modifier.padding(pad)) {
                EmptyState("Data tidak ditemukan", "Kos yang Anda cari tidak tersedia.")
            }
            return@Scaffold
        }

        Column(
            Modifier.padding(pad).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                Modifier.fillMaxWidth().height(180.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.shapes.large),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, null, Modifier.size(80.dp), tint = MaterialTheme.colorScheme.primary)
            }
            Text(kos.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, Modifier.size(18.dp))
                Text(" ${kos.location} • Kos ${kos.type}")
                Spacer(Modifier.weight(1f))
                Icon(Icons.Default.Star, null, Modifier.size(18.dp), tint = Color(0xFFFFB300))
                Text(" ${kos.rating}")
            }
            Text(
                "${kos.price.toRupiah()} / bulan",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold
            )
            Text("Deskripsi", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(kos.description)
            Text("Fasilitas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                kos.facilities.forEach { AssistChip(onClick = {}, label = { Text(it) }) }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Phone, null, Modifier.size(18.dp))
                Text(" Pemilik: ${kos.ownerPhone}")
            }
        }
    }
}
