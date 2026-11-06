# KosKu Batam - Aplikasi Pencari Kos (ATS Praktikum PPB RPL317)

Aplikasi mobile Android (Kotlin + Jetpack Compose + Material 3) berfokus pada Frontend, memakai dummy data.

## Cara menjalankan
1. Buka folder ini di **Android Studio (Koala / terbaru)**.
2. Tunggu Gradle Sync selesai (butuh internet pertama kali).
3. Jalankan di emulator / HP (Android 8.0+ / API 26+).

Akun demo: `mahasiswa@kosku.com` / `kosku123`

## Halaman (7)
Splash, Login, Dashboard, Detail Kos, Form Pemesanan, Favorit, Riwayat Pemesanan.

## Fitur dinamis
1. Pencarian + filter tipe + pengurutan kos (real-time).
2. Favorit (tambah/hapus, daftar favorit).
3. Pemesanan kos dengan hitung total otomatis dan riwayat.

## State & Lifecycle
ViewModel (StateFlow) + rememberSaveable + NavController -> data aman saat rotasi layar dan pindah halaman.

## Error handling
Validasi input (email, password, nama, HP, tanggal), cek koneksi internet (ConnectivityManager),
try/catch-finally pada pemuatan data, empty state, dan penanganan data tidak ditemukan.
