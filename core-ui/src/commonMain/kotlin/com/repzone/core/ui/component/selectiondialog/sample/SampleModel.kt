package com.repzone.core.ui.component.selectiondialog.sample

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.repzone.core.ui.component.selectiondialog.GenericPopupList
import com.repzone.core.ui.component.selectiondialog.SelectionMode
import com.repzone.core.ui.manager.theme.ThemeManager
import org.koin.compose.koinInject

data class CustomerSample(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)

/**
 * Example data class for Product
 */
data class ProductSample(
    val id: Int,
    val name: String,
    val price: Double,
    val category: String,
    val inStock: Boolean
)

/**
 * Custom row for displaying Customer item
 */
@Composable
fun CustomerRow(customer: CustomerSample, isSelected: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = customer.name,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = customer.email,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = customer.phone,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Custom row for displaying Product item
 */
@Composable
fun ProductRow(product: ProductSample, isSelected: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Icon(
                    imageVector = if (product.inStock) Icons.Default.CheckCircle else Icons.Default.Warning,
                    contentDescription = if (product.inStock) "Stokta" else "Stokta Yok",
                    modifier = Modifier.size(16.dp),
                    tint = if (product.inStock)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )

                Text(
                    text = if (product.inStock) "Stokta" else "Stokta Yok",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (product.inStock)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.error
                )
            }
        }

        Text(
            text = "₺${product.price}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExampleUsageScreen() {
    var showCustomerDialog by rememberSaveable { mutableStateOf(false) }
    var showProductBottomSheet by rememberSaveable { mutableStateOf(false) }
    var selectedCustomer by remember { mutableStateOf<CustomerSample?>(null) }
    var selectedProducts by remember { mutableStateOf<List<ProductSample>>(emptyList()) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Sample data
    val customers = rememberSaveable {
        listOf(
            CustomerSample(1, "Ahmet Yılmaz", "ahmet@example.com", "+90 532 123 4567"),
            CustomerSample(2, "Ayşe Demir", "ayse@example.com", "+90 533 234 5678"),
            CustomerSample(3, "Mehmet Kaya", "mehmet@example.com", "+90 534 345 6789"),
            CustomerSample(4, "Fatma Şahin", "fatma@example.com", "+90 535 456 7890"),
            CustomerSample(5, "Ali Çelik", "ali@example.com", "+90 536 567 8901")
        )
    }

    val products = rememberSaveable {
        listOf(
            ProductSample(1, "Laptop", 15000.0, "Elektronik", true),
            ProductSample(2, "Mouse", 250.0, "Elektronik", true),
            ProductSample(3, "Klavye", 500.0, "Elektronik", false),
            ProductSample(4, "Monitör", 3000.0, "Elektronik", true),
            ProductSample(5, "Kulaklık", 800.0, "Elektronik", true),
            ProductSample(6, "Webcam", 1200.0, "Elektronik", false),
            ProductSample(7, "Mikrofon", 900.0, "Elektronik", true)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "GenericPopupList Örnekleri",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Example 1: Single Selection with Dialog
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Örnek 1: Müşteri Seçimi (Dialog + Single)",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Seçili müşteri: ${selectedCustomer?.name ?: "Henüz seçilmedi"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = { showCustomerDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Müşteri Seç")
                }
            }
        }

        // Example 2: Multiple Selection with BottomSheet
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Örnek 2: Ürün Seçimi (BottomSheet + Multiple)",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Seçili ürünler (${selectedProducts.size}): ${
                        if (selectedProducts.isEmpty()) "Henüz seçilmedi"
                        else selectedProducts.joinToString(", ") { it.name }
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Button(
                    onClick = { showProductBottomSheet = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ürün Seç")
                }
            }
        }
    }

    // Customer Dialog
    if (showCustomerDialog) {
        Dialog(onDismissRequest = {
            showCustomerDialog = false
            searchQuery = ""
        }) {
            GenericPopupList(
                items = customers,
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                selectionMode = SelectionMode.MULTIPLE,
                selectedItems = selectedCustomer?.let { listOf(it) },
                itemContent = { customer, isSelected ->
                    CustomerRow(customer = customer, isSelected = isSelected)
                },
                itemKey = { it.id },
                searchEnabled = true,
                searchPredicate = { customer, query ->
                    customer.name.contains(query, ignoreCase = true) ||
                            customer.email.contains(query, ignoreCase = true) ||
                            customer.phone.contains(query, ignoreCase = true)
                },
                searchPlaceholder = "Müşteri ara...",
                confirmButtonText = "Seç",
                cancelButtonText = "İptal",
                onConfirm = { selected ->
                    selectedCustomer = selected.firstOrNull()
                    showCustomerDialog = false
                },
                onDismiss = {
                    showCustomerDialog = false
                    searchQuery = ""
                }
            )
        }
    }

    // Product BottomSheet
    if (showProductBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showProductBottomSheet = false }
        ) {
            GenericPopupList(
                items = products,
                selectionMode = SelectionMode.MULTIPLE,
                selectedItems = selectedProducts,
                itemContent = { product, isSelected ->
                    ProductRow(product = product, isSelected = isSelected)
                },
                itemKey = { it.id },
                searchEnabled = true,
                searchPredicate = { product, query ->
                    product.name.contains(query, ignoreCase = true) ||
                            product.category.contains(query, ignoreCase = true)
                },
                searchPlaceholder = "Ürün ara...",
                confirmButtonText = "Onayla",
                cancelButtonText = "Vazgeç",
                confirmButtonColor = MaterialTheme.colorScheme.primary,
                onConfirm = { selected ->
                    selectedProducts = selected
                    showProductBottomSheet = false
                },
                onDismiss = { showProductBottomSheet = false }
            )
        }
    }
}