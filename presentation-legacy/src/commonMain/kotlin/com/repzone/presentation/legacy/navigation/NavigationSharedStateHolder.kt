package com.repzone.presentation.legacy.navigation

import com.repzone.domain.model.CustomerItemModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationSharedStateHolder {
    private val _selectedCustomer = MutableStateFlow<CustomerItemModel?>(null)
    val selectedCustomer: StateFlow<CustomerItemModel?> = _selectedCustomer.asStateFlow()
    fun setSelectedActiveCustomer(customer: CustomerItemModel) {
        _selectedCustomer.value = customer
    }

    fun clearCustomer() {
        _selectedCustomer.value = null
    }
}