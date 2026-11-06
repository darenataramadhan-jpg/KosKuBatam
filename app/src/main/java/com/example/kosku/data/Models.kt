package com.example.kosku.data

data class Kos(
    val id: Int,
    val name: String,
    val location: String,
    val price: Int,            // per bulan (Rupiah)
    val type: String,          // Putra / Putri / Campur
    val rating: Double,
    val facilities: List<String>,
    val description: String,
    val ownerPhone: String
)

data class Booking(
    val id: Long,
    val kosName: String,
    val tenantName: String,
    val phone: String,
    val startDate: String,
    val months: Int,
    val total: Int
)

enum class SortOption(val label: String) {
    DEFAULT("Rekomendasi"),
    PRICE_LOW("Termurah"),
    PRICE_HIGH("Termahal"),
    RATING("Rating tertinggi")
}
