package com.repzone.core.ui.component.selectiondialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import kotlinx.coroutines.delay
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.*

@Composable
fun <T> GenericPopupList(
    items: List<T>,
    selectionMode: SelectionMode,
    selectedItems: List<T>? = null,
    itemContent: @Composable (T, Boolean) -> Unit,
    itemKey: (T) -> Any,
    searchEnabled: Boolean = true,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    searchPredicate: (T, String) -> Boolean,
    searchPlaceholder: String = Res.string.searchinform.fromResource(),
    confirmButtonText: String = Res.string.dialogok.fromResource(),
    cancelButtonText: String = Res.string.dialogcancel.fromResource(),
    confirmButtonColor: Color? = null,
    cancelButtonColor: Color? = null,
    onConfirm: (List<T>) -> Unit,
    onDismiss: () -> Unit
) {
    var debouncedSearchQuery by remember { mutableStateOf("") }
    val selectedItemsState = remember { mutableStateListOf<T>().apply { selectedItems?.let { addAll(it) } } }
    val themeManager: ThemeManager = koinInject()

    LaunchedEffect(searchQuery) {
        delay(300)
        debouncedSearchQuery = searchQuery
    }

    val filteredItems = remember(items, debouncedSearchQuery) {
        if (debouncedSearchQuery.isEmpty()) {
            items
        } else {
            items.filter { searchPredicate(it, debouncedSearchQuery) }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (searchEnabled) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .background(
                        color = themeManager.getCurrentColorScheme().colorPalet.secondary20,
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                SearchBar(
                    query = searchQuery,
                    onQueryChange = onSearchQueryChange,
                    placeholder = searchPlaceholder,
                    onClear = { onSearchQueryChange("") }
                )
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .background(MaterialTheme.colorScheme.background)
                .then(if (selectionMode == SelectionMode.SINGLE) Modifier.selectableGroup() else Modifier)
        ) {
            if (filteredItems.isEmpty()) {
                item {
                    EmptyState()
                }
            } else {
                items(
                    items = filteredItems,
                    key = { itemKey(it) }
                ) { item ->
                    val isSelected = selectedItemsState.contains(item)
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = isSelected,
                                    onClick = {
                                        when (selectionMode) {
                                            SelectionMode.SINGLE -> {
                                                selectedItemsState.clear()
                                                selectedItemsState.add(item)
                                            }
                                            SelectionMode.MULTIPLE -> {
                                                if (isSelected) {
                                                    selectedItemsState.remove(item)
                                                } else {
                                                    selectedItemsState.add(item)
                                                }
                                            }
                                        }
                                    },
                                    role = if (selectionMode == SelectionMode.SINGLE) Role.RadioButton else Role.Checkbox
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Selection indicator (Checkbox or RadioButton)
                            when (selectionMode) {
                                SelectionMode.SINGLE -> {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = null
                                    )
                                }
                                SelectionMode.MULTIPLE -> {
                                    Checkbox(
                                        checked = isSelected,
                                        onCheckedChange = null
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // Custom item content
                            Box(modifier = Modifier.weight(1f)) {
                                itemContent(item, isSelected)
                            }
                        }

                        // Divider
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 56.dp),
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }

        // Action Buttons - Beyaz background
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ActionButtons(
                confirmButtonText = confirmButtonText,
                cancelButtonText = cancelButtonText,
                confirmButtonColor = confirmButtonColor,
                cancelButtonColor = cancelButtonColor,
                isConfirmEnabled = selectedItemsState.isNotEmpty(),
                onConfirm = { onConfirm(selectedItemsState.toList()) },
                onCancel = onDismiss
            )
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    onClear: () -> Unit) {
    val focusManager = LocalFocusManager.current
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
            .background(Color.White, RoundedCornerShape(22.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = Color.Black),
        singleLine = true,
        cursorBrush = SolidColor(Color.Red),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onQueryChange
                focusManager.clearFocus()
            }
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Search Icon
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // TextField + Placeholder
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            placeholder,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    innerTextField()
                }

                // Clear Icon
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            focusManager.clearFocus()
                            onClear()
                        },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear",
                            tint = Color.Gray,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = Res.string.list_empty.fromResource(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ActionButtons(
    confirmButtonText: String,
    cancelButtonText: String,
    confirmButtonColor: Color?,
    cancelButtonColor: Color?,
    isConfirmEnabled: Boolean,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    HorizontalDivider()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
    ) {
        // Cancel Button
        TextButton(
            onClick = onCancel,
            colors = cancelButtonColor?.let {
                ButtonDefaults.textButtonColors(contentColor = it)
            } ?: ButtonDefaults.textButtonColors()
        ) {
            Text(cancelButtonText)
        }

        // Confirm Button
        Button(
            onClick = onConfirm,
            enabled = isConfirmEnabled,
            colors = confirmButtonColor?.let {
                ButtonDefaults.buttonColors(containerColor = it)
            } ?: ButtonDefaults.buttonColors()
        ) {
            Text(confirmButtonText)
        }
    }
}