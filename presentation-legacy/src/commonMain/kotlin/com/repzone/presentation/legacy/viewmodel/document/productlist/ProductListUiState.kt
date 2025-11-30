package com.repzone.presentation.legacy.viewmodel.document.productlist

import com.repzone.core.model.HasUiFrame
import com.repzone.core.model.UiFrame
import com.repzone.domain.document.model.DiscountDialogState
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.domain.model.product.ProductFilters

data class ProductListUiState(
    override val uiFrame: UiFrame = UiFrame(),

    val filterState: ProductFilterState = ProductFilterState(),
    val availableFilters: ProductFilters? = null,
    val discountDialogState: DiscountDialogState? = null
): HasUiFrame {

    override fun copyWithUiFrame(newUiFrame: UiFrame): ProductListUiState {
        return copy(uiFrame = newUiFrame)
    }

    override fun resetUiFrame(): ProductListUiState {
        return copy(uiFrame = UiFrame())
    }

    val hasActiveFilters: Boolean
        get() = filterState.hasActiveFilters
}
