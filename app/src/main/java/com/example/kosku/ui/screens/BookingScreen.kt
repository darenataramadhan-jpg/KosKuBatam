@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.kosku.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.kosku.data.Kos
import com.example.kosku.ui.components.BackButton
import com.example.kosku.ui.components.EmptyState
import com.example.kosku.util.Validators
import com.example.kosku.util.toRupiah
import com.example.kosku.viewmodel.KosViewModel

/** HALAMAN 5: Form Pemesanan (validasi input + hitung total otomatis) */
@Composable
fun BookingScreen(
    kos: Kos?,
    vm: KosViewModel,
    snackbar: SnackbarHostState,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    // Semua input memakai rememberSaveable -> aman saat rotasi layar
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var date by rememberSaveable { mutableStateOf("") }
    var months by rememberSaveable { mutableIntStateOf(1) }
    var agree by rememberSaveable { mutableStateOf(false) }
    var attempted by rememberSaveable { mutableStateOf(false) }
    var showSuccess by rememberSaveable { mutableStateOf(false) }

    val nameError = if (attempted) Validators.validateName(name) else null
    val phoneError = if (attempted) Validators.validatePhone(phone) else null
    val dateError = if (attempted) Validators.validateDate(date) else null

    Scaffold(
        topBar = { TopAppBar(title = { Text("Form Pemesanan") }, navigationIcon = { BackButton(onBack) }) },
        snackbarHost = { SnackbarHost(snackbar) }
    ) { pad ->
        if (kos == null) {
            Box(Modifier.padding(pad)) { EmptyState("Data tidak ditemukan", "Kos tidak tersedia.") }
            return@Scaffold
        }

        Column(
            Modifier.padding(pad).fillMaxSize().imePadding()
                .verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(kos.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${kos.price.toRupiah()} / bulan", color = MaterialTheme.colorScheme.primary)

            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Nama lengkap") },
                isError = nameError != null, supportingText = { nameError?.let { Text(it) } },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = phone, onValueChange = { phone = it },
                label = { Text("Nomor HP") },
                isError = phoneError != null, supportingText = { phoneError?.let { Text(it) } },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = date, onValueChange = { date = it },
                label = { Text("Tanggal masuk (dd-MM-yyyy)") },
                placeholder = { Text("15-11-2026") },
                isError = dateError != null, supportingText = { dateError?.let { Text(it) } },
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )

            Text("Durasi sewa", style = MaterialTheme.typography.titleSmall)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(1, 3, 6, 12).forEach { m ->
                    FilterChip(selected = months == m, onClick = { months = m }, label = { Text("$m bln") })
                }
            }

            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Total pembayaran", style = MaterialTheme.typography.bodySmall)
                    Text(
                        (kos.price * months).toRupiah(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = agree, onCheckedChange = { agree = it })
                Text("Saya menyetujui syarat & ketentuan sewa")
            }

            Button(
                onClick = {
                    attempted = true
                    when {
                        !agree -> vm.notify("Anda harus menyetujui syarat & ketentuan")
                        vm.submitBooking(kos, name, phone, date, months) -> showSuccess = true
                    }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) { Text("KONFIRMASI PEMESANAN") }
        }
    }

    if (showSuccess) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Pemesanan Berhasil") },
            text = { Text("Pesanan Anda telah tercatat. Lihat di Riwayat Pemesanan.") },
            confirmButton = { TextButton(onClick = { showSuccess = false; onSuccess() }) { Text("OK") } }
        )
    }
}
