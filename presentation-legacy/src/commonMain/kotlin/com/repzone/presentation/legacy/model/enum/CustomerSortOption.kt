package com.repzone.presentation.legacy.model.enum

enum class CustomerSortOption(val label: String) {
    NAME_ASC("İsim (A-Z)"),
    NAME_DESC("İsim (Z-A)"),
    DATE_ASC("Tarih (Eskiden Yeniye)"),
    DATE_DESC("Tarih (Yeniden Eskiye)")
}