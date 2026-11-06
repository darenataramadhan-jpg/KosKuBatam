package com.example.kosku.data

/** Semua data pada aplikasi ini adalah DUMMY (tidak ada backend / database). */
object DummyData {
    const val VALID_EMAIL = "mahasiswa@kosku.com"
    const val VALID_PASSWORD = "kosku123"

    val kosList: List<Kos> = listOf(
        Kos(1, "Kos Melati Batam Centre", "Batam Centre", 1_200_000, "Putri", 4.7,
            listOf("AC", "WiFi", "Kamar Mandi Dalam", "Kasur", "Lemari"),
            "Kos nyaman dekat Politeknik Negeri Batam dan Mall BCS. Lingkungan aman, ada CCTV dan akses 24 jam.",
            "081234567801"),
        Kos(2, "Kos Putra Nagoya Hill", "Nagoya", 950_000, "Putra", 4.2,
            listOf("WiFi", "Kasur", "Parkir Motor", "Dapur Bersama"),
            "Dekat pusat perbelanjaan Nagoya, cocok untuk pekerja dan mahasiswa. Parkir motor luas.",
            "081234567802"),
        Kos(3, "Kos Campur Sei Panas", "Sei Panas", 800_000, "Campur", 3.9,
            listOf("WiFi", "Kipas Angin", "Dapur Bersama"),
            "Kos ekonomis untuk mahasiswa, akses mudah ke angkutan umum dan warung makan.",
            "081234567803"),
        Kos(4, "Kos Eksklusif Bengkong", "Bengkong", 1_800_000, "Putri", 4.9,
            listOf("AC", "WiFi", "Kamar Mandi Dalam", "Water Heater", "Laundry", "Dapur"),
            "Kos eksklusif dengan fasilitas lengkap, cocok untuk karyawan perusahaan.",
            "081234567804"),
        Kos(5, "Kos Putra Batu Aji", "Batu Aji", 700_000, "Putra", 4.0,
            listOf("Kipas Angin", "Kasur", "Parkir Motor"),
            "Kos sederhana dan bersih dekat kawasan industri Batu Aji.",
            "081234567805"),
        Kos(6, "Kos Harmoni Sekupang", "Sekupang", 1_100_000, "Campur", 4.3,
            listOf("AC", "WiFi", "Kasur", "Lemari", "Parkir Motor"),
            "Dekat pelabuhan Sekupang. Cocok untuk pekerja yang sering bepergian ke Singapura.",
            "081234567806"),
        Kos(7, "Kos Mawar Jodoh", "Jodoh", 1_400_000, "Putri", 4.5,
            listOf("AC", "WiFi", "Kamar Mandi Dalam", "Lemari"),
            "Lokasi strategis di pusat kota, dekat Dataran Engku Putri dan pusat kuliner.",
            "081234567807"),
        Kos(8, "Kos Bahari Tiban", "Tiban", 900_000, "Putra", 4.1,
            listOf("WiFi", "Kasur", "Lemari", "Parkir Motor"),
            "Dekat kampus dan area Tiban. Suasana tenang.",
            "081234567808")
    )
}
