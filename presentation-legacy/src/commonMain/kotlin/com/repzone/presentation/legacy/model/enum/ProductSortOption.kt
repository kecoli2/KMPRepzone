package com.repzone.presentation.legacy.model.enum

enum class ProductSortOption(val label: String) {
    NAME_ASC("Ada Göre (A-Z)"),
    NAME_DESC("Ada Göre (Z-A)"),
    PRICE_ASC("Fiyata Göre (Artan)"),
    PRICE_DESC("Fiyata Göre (Azalan)"),
    STOCK_ASC("Stoka Göre (Artan)"),
    STOCK_DESC("Stoka Göre (Azalan)")
}