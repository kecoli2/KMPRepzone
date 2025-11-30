package com.repzone.presentation.legacy.ui.document.productlist.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.repzone.core.model.StringResource
import com.repzone.core.ui.component.textfield.RepzoneTextField
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.util.extensions.fromResource
import com.repzone.domain.model.product.ProductFilterState
import com.repzone.presentation.legacy.ui.customerlist.FilterButtonWithBadge

@Composable
fun ProductListFilterBar(
    filterState: ProductFilterState,
    onSearchQueryChanged: (String) -> Unit,
    onFilterButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    themeManager: ThemeManager
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(themeManager.getCurrentColorScheme().colorPalet.secondary20)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Search bar
        SearchBar(
            query = filterState.searchQuery,
            onQueryChange = onSearchQueryChanged,
            modifier = Modifier.weight(1f)
        )

        // Filter Button with Badge
        FilterButtonWithBadge(
            filterCount = filterState.activeFilterCount,
            onClick = onFilterButtonClick
        )
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    RepzoneTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = StringResource.SEARCHPRODUCT.fromResource(),
        modifier = modifier,
        onSearch = {
            focusManager.clearFocus()
        },
        onClear = {
            onQueryChange("")
            focusManager.clearFocus()
        }
    )
}