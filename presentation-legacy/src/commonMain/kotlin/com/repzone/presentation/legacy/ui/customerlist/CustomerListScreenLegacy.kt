package com.repzone.presentation.legacy.ui.customerlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.EditNotifications
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.ModeComment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.EditNotifications
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import com.repzone.core.interfaces.IUserSession
import com.repzone.core.ui.base.ViewModelHost
import com.repzone.core.ui.component.floatactionbutton.SmartFabScaffold
import com.repzone.core.ui.component.textfield.RepzoneTextField
import com.repzone.core.ui.component.selectiondialog.GenericPopupList
import com.repzone.core.ui.component.selectiondialog.SelectionMode
import com.repzone.core.ui.component.textfield.SearchTextField
import com.repzone.core.ui.component.topappbar.RepzoneTopAppBar
import com.repzone.core.ui.component.topappbar.TopBarAction
import com.repzone.core.ui.component.topappbar.TopBarLeftIcon
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.model.NavigationItem
import com.repzone.core.ui.util.enum.NavigationItemType
import com.repzone.core.util.extensions.addDays
import com.repzone.core.util.extensions.fromResource
import com.repzone.core.util.extensions.now
import com.repzone.core.util.extensions.toInstant
import com.repzone.domain.model.CustomerItemModel
import com.repzone.presentation.legacy.viewmodel.customerlist.CustomerListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import repzonemobile.core.generated.resources.Res
import repzonemobile.core.generated.resources.customernotestitle
import repzonemobile.core.generated.resources.dailyoperationstitle
import repzonemobile.core.generated.resources.documents
import repzonemobile.core.generated.resources.eagleeyelogstitle
import repzonemobile.core.generated.resources.exit
import repzonemobile.core.generated.resources.generalsettings
import repzonemobile.core.generated.resources.notificationlogpagetitle
import repzonemobile.core.generated.resources.onlinehubtitle
import repzonemobile.core.generated.resources.profile
import repzonemobile.core.generated.resources.routeothers
import repzonemobile.core.generated.resources.routepagechatbtntext
import repzonemobile.core.generated.resources.routepagetasksbtntext
import repzonemobile.core.generated.resources.routesearchcustomer
import repzonemobile.core.generated.resources.routetoday
import repzonemobile.core.generated.resources.routetomorrow
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun CustomerListScreenLegacy(onNavigationDrawer: (type: NavigationItemType) -> Unit, onCustomerClick: (CustomerItemModel) -> Unit) = ViewModelHost<CustomerListViewModel> { viewModel ->
    val themeManager: ThemeManager = koinInject()
    val iUserSessionInfo: IUserSession = koinInject()
    val uiState by viewModel.state.collectAsState()
    var selectedTab by rememberSaveable  { mutableIntStateOf(2) }
    var previousTab by rememberSaveable { mutableIntStateOf(selectedTab) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableStateOf(-1) }
    val drawerItems = getNavigationItems()
    var searchQuery by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var showFilterSheet by remember { mutableStateOf(false) }
    val selectedGroups = uiState.selectedFilterGroups
    val selectedSort = uiState.selectedSortOption
    val customerList = uiState.filteredCustomers
    val representSummary = uiState.representSummary
    val userNameSurName by remember { mutableStateOf("${iUserSessionInfo.getActiveSession()?.firstName} ${iUserSessionInfo.getActiveSession()?.lastName}") }
    val userMail by remember { mutableStateOf(iUserSessionInfo.getActiveSession()?.email ?: "") }
    var isInitialLoad by rememberSaveable { mutableStateOf(true) }
    var showParentCustomer by rememberSaveable { mutableStateOf(false) }
    var searchParrentCustomer by rememberSaveable { mutableStateOf("") }


    LaunchedEffect(Unit){
        viewModel.events.collect { event ->
            when(event){
                is CustomerListViewModel.Event.NavigateVisitPage ->{
                    onCustomerClick(event.selectedCustomer)
                }
                is CustomerListViewModel.Event.ShowDialogParentCustomer -> {
                    showParentCustomer = true
                }
                else -> {

                }
            }
        }
    }

    LaunchedEffect(Unit){
        if (isInitialLoad) {
            when(selectedTab) {
                0 -> viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(now().toInstant()))
                1 -> {
                    val date = now().toInstant().addDays(1)
                    viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(date))
                }
                2 -> viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(null))
            }
            isInitialLoad = false
        }
    }

    LaunchedEffect(selectedTab) {
        if (!isInitialLoad && selectedTab != previousTab) {
            when(selectedTab) {
                0 -> viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(now().toInstant()))
                1 -> {
                    val date = now().toInstant().addDays(1)
                    viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(date))
                }
                2 -> viewModel.onEvent(CustomerListViewModel.Event.LoadCustomerList(null))
            }
            previousTab = selectedTab
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {
                Column(modifier = Modifier.fillMaxHeight()) {
                    // Drawer Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(themeManager.getCurrentColorScheme().colorPalet.secondary20),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(start = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = null,
                                contentDescription = null,
                                modifier = Modifier.size(50.dp).clip(CircleShape),
                                error = painterResource(Res.drawable.profile),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                            Column(
                                modifier = Modifier.padding(start = 16.dp),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    userNameSurName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    userMail,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    LazyColumn(modifier = Modifier.weight(1f, fill = true)) {
                        itemsIndexed(drawerItems) { index, item ->
                            NavigationDrawerItem(
                                label = {
                                    Text(
                                        item.title,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                selected = index == selectedItemIndex,
                                onClick = {
                                    selectedItemIndex = index
                                    scope.launch { drawerState.close() }
                                    when (drawerItems[index].navigationItemType) {
                                        NavigationItemType.GENERAL_SETTINGS -> {
                                            onNavigationDrawer(NavigationItemType.GENERAL_SETTINGS)
                                        }
                                        NavigationItemType.SHARED_DOCUMENT -> {}
                                        NavigationItemType.DAILY_OPERATIONS -> {}
                                        NavigationItemType.GPS_OPERATIONS -> {
                                            onNavigationDrawer(NavigationItemType.GPS_OPERATIONS)
                                        }
                                        NavigationItemType.NOTIFICATION_HISTORY -> {}
                                        NavigationItemType.ONLINE_CENTER -> {

                                        }
                                        NavigationItemType.CUSTOMER_NOTES -> {}
                                        NavigationItemType.LOG -> {}
                                        else -> {}
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.selectedIcon
                                        } else {
                                            item.unselectedIcon
                                        },
                                        contentDescription = item.title,
                                        tint = if (index == selectedItemIndex)
                                            MaterialTheme.colorScheme.onSecondaryContainer
                                        else
                                            MaterialTheme.colorScheme.primary
                                    )
                                },
                                badge = {
                                    item.badgeCount?.let { Text(it.toString()) }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    }

                    // ALT: ÇIKIŞ
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = {
                            Text(
                                Res.string.exit.fromResource(),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                viewModel.onEvent(CustomerListViewModel.Event.LogOut)
                                onNavigationDrawer(NavigationItemType.LOGOUT)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Logout,
                                contentDescription = "Çıkış",
                                modifier = Modifier,
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                NavigationDrawerItemDefaults.ItemPadding
                            )
                    )
                }
            }
        }
    ) {
        if(showParentCustomer){
            Dialog(onDismissRequest = {
                showParentCustomer = false
                viewModel.onEvent(CustomerListViewModel.Event.ClearParentCustomer)
                searchParrentCustomer = ""
            }, properties = DialogProperties(

            )){
                GenericPopupList(
                    items = uiState.customerParentModel?.parrentCustomers!!,
                    selectionMode = SelectionMode.SINGLE,
                    itemContent = { customer, isSelected ->

                        CustomerCardParent(customer, modifier = Modifier,
                            themeManager = themeManager,
                            onCustomerClick = { customer ->
                                showParentCustomer = false
                                searchParrentCustomer = ""
                                viewModel.scope.launch {
                                    viewModel.onEvent(CustomerListViewModel.Event.OnClickCustomerItem(customer) )
                                }
                            }, uiState = uiState)
                    },
                    itemKey = {"${it.customerId}-${it.date}"},
                    searchEnabled = true,
                    searchQuery = searchParrentCustomer,
                    onSearchQueryChange = {searchParrentCustomer = it},
                    searchPredicate = { customer, query ->
                        (customer.name ?: "").contains(query, ignoreCase = true)
                                || (customer.address ?: "").contains(query, ignoreCase = true)
                                || (customer.customerCode ?: "").contains(query, ignoreCase = true)
                                || (customer.customerCode ?: "").contains(query, ignoreCase = true)
                                || (customer.customerId).toString().contains(query, ignoreCase = true)
                    },
                    searchPlaceholder = Res.string.routesearchcustomer.fromResource(),
                    onConfirm = { selected ->
                        showParentCustomer = false
                        viewModel.scope.launch {
                            viewModel.onEvent(CustomerListViewModel.Event.OnClickCustomerItem(selected.first()) )
                        }
                    },
                    onDismiss = {
                        showParentCustomer = false
                        viewModel.onEvent(CustomerListViewModel.Event.ClearParentCustomer)
                        searchParrentCustomer = ""
                    }
                )
            }
        }


        SmartFabScaffold (
            fabAction = if(uiState.floatActionButtonList != null ){
                uiState.floatActionButtonList
            }else{
                null
            },
            onFabClick = {
                viewModel.onEvent(CustomerListViewModel.Event.OnClickFab)
            },
            onMenuItemClick = { item ->
                viewModel.onEvent(CustomerListViewModel.Event.OnClickFabMenuItem(item))
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Custom Top Bar
                RepzoneTopAppBar(modifier = Modifier,
                    themeManager = themeManager,
                    leftIconType = TopBarLeftIcon.Menu(
                        onClick = {
                            scope.launch {
                                if (drawerState.isClosed) drawerState.open()
                                else drawerState.close()
                            }
                        }
                    ),
                    rightIcons = listOf(
                        TopBarAction(Icons.Default.Timer, "Timer", Color.White, {

                        }),
                        TopBarAction(Icons.Default.Map, "Map",Color.White, {

                        }),
                        TopBarAction(Icons.Default.Sync, "Sync",Color.White, {
                            viewModel.scope.launch {
                                viewModel.onEvent(event = CustomerListViewModel.Event.StartSync)
                            }
                        }),
                    ),
                )
                if(uiState.isSyncInProgress){
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp),
                        color = themeManager.getCurrentColorScheme().colorPalet.primary50
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .background(themeManager.getCurrentColorScheme().colorPalet.secondary20),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    SearchTextField(
                        value = searchQuery,
                        onValueChange = {
                             searchQuery = it
                            viewModel.scope.launch {
                                delay(500)
                                viewModel.onEvent(CustomerListViewModel.Event.FilterCustomerList(searchQuery))
                            }
                       },
                        placeholder = Res.string.routesearchcustomer.fromResource(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        onSearch = {
                            viewModel.scope.launch {
                                viewModel.onEvent(CustomerListViewModel.Event.FilterCustomerList(searchQuery))
                            }
                            focusManager.clearFocus()
                        },
                        onClear = {
                            searchQuery = ""
                            focusManager.clearFocus()
                            viewModel.scope.launch {
                                viewModel.onEvent(CustomerListViewModel.Event.FilterCustomerList(""))
                            }
                        },
                    )

                    FilterButtonWithBadge(
                        filterCount = selectedGroups.size,
                        onClick = { showFilterSheet = true },
                        modifier = Modifier
                    )

                    // BottomSheet
                    FilterBottomSheet(
                        showBottomSheet = showFilterSheet,
                        onDismiss = { showFilterSheet = false },
                        selectedGroups = selectedGroups,
                        onGroupsChange = { /* Sadece local state güncellenecek */ },
                        selectedSort = selectedSort,
                        onSortChange = { /* Sadece local state güncellenecek */ },
                        onApplyFilters = {groups, short ->
                            viewModel.scope.launch {
                                viewModel.onEvent(CustomerListViewModel.Event.ApplyFilter(groups, short))
                            }
                            showFilterSheet = false
                        } ,
                        onClearFilters = {
                            viewModel.scope.launch {
                                viewModel.onEvent(CustomerListViewModel.Event.ClearFilters)
                            }
                            showFilterSheet = false
                        },
                        customerGroups = uiState.activeCustomerGroup
                    )
                }

                CustomerListLoadingHandler(
                    customerListState = uiState.customerListState,
                    themeManager = themeManager,
                    modifier = Modifier.weight(1f),
                    onRetry = {
                        // Retry butonu için
                        viewModel.onEvent(
                            CustomerListViewModel.Event.LoadCustomerList(uiState.selectedDate)
                        )
                    }
                ) {
                    // LazyColumn içinde kaybolacak alan + Tab + content
                    LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f)) {
                        // Kaybolacak Alan
                        item {
                            if(uiState.isDashboardActive){
                                Surface(
                                    modifier = Modifier.fillMaxWidth().height(180.dp),
                                    color = themeManager.getCurrentColorScheme().colorPalet.primary60
                                ) {
                                    RepresentSummary(representSummary, themeManager)
                                }
                            }
                        }

                        // Tab Layout - Sticky
                        if (uiState.isTabActive) {
                            stickyHeader {
                                PrimaryTabRow(selectedTabIndex = selectedTab,) {
                                    repeat(3) { index ->
                                        Tab(
                                            selected = selectedTab == index,
                                            onClick = { selectedTab = index },
                                            text = {
                                                when(index +1){
                                                    1 -> Text(Res.string.routetoday.fromResource())
                                                    2 -> Text(Res.string.routetomorrow.fromResource())
                                                    3 -> Text(Res.string.routeothers.fromResource())
                                                }
                                            },
                                            unselectedContentColor = MaterialTheme.colorScheme.outlineVariant
                                        )
                                    }
                                }
                            }
                        }

                        itemsIndexed(
                            items = customerList,
                            key = { _, customer -> "${customer.customerId}-${customer.date}" }
                        ) { index, customer ->
                            CustomerCard(customer = customer, themeManager = themeManager, modifier = Modifier, onCustomerClick = {
                                viewModel.scope.launch {
                                    viewModel.onEvent(
                                        CustomerListViewModel.Event.OnClickCustomerItem(
                                            customer
                                        )
                                    )
                                }
                            }, uiState = uiState)
                            HorizontalDivider()
                        }
                    }
                }
                // BOTTOM ROW - TASKS + CHAT
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .background(themeManager.getCurrentColorScheme().colorPalet.secondary20),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(uiState.taskButtonContainerVisibility){
                        TextButton(modifier = Modifier.fillMaxWidth().weight(1f).padding(end = 8.dp),
                            onClick = { },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White,
                                disabledContentColor = themeManager.getCurrentColorScheme().colorPalet.neutral60
                            ),
                        ){
                            Icon(imageVector = Icons.Default.Task, modifier = Modifier.padding(end = 8.dp), contentDescription = null)
                            Text(Res.string.routepagetasksbtntext.fromResource())
                        }
                    }

                    if(uiState.isChatButtonContainer){
                        TextButton(modifier = Modifier.fillMaxWidth().weight(1f),
                            onClick = { },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = Color.White,
                                disabledContentColor = themeManager.getCurrentColorScheme().colorPalet.neutral60
                            ),
                        ){
                            Icon(imageVector = Icons.Default.ModeComment, modifier = Modifier.padding(end = 8.dp), contentDescription = null)
                            Text(Res.string.routepagechatbtntext.fromResource())
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun getNavigationItems(): List<NavigationItem>{
    return listOf(
        NavigationItem(
            title = Res.string.generalsettings.fromResource(),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            navigationItemType = NavigationItemType.GENERAL_SETTINGS
        ),
        NavigationItem(
            title = Res.string.documents.fromResource(),
            selectedIcon = Icons.Filled.DocumentScanner,
            unselectedIcon = Icons.Outlined.DocumentScanner,
            navigationItemType = NavigationItemType.SHARED_DOCUMENT
        ),
        NavigationItem(
            title = Res.string.dailyoperationstitle.fromResource(),
            selectedIcon = Icons.Filled.AccessTimeFilled,
            unselectedIcon = Icons.Outlined.AccessTime,
            navigationItemType = NavigationItemType.DAILY_OPERATIONS
        ),

        NavigationItem(
            title = Res.string.eagleeyelogstitle.fromResource(),
            selectedIcon = Icons.Filled.Room,
            unselectedIcon = Icons.Outlined.Room,
            navigationItemType = NavigationItemType.GPS_OPERATIONS
        ),

        NavigationItem(
            title = Res.string.notificationlogpagetitle.fromResource(),
            selectedIcon = Icons.Filled.EditNotifications,
            unselectedIcon = Icons.Outlined.EditNotifications,
            navigationItemType = NavigationItemType.NOTIFICATION_HISTORY,
            badgeCount = 4
        ),

        NavigationItem(
            title = Res.string.onlinehubtitle.fromResource(),
            selectedIcon = Icons.Filled.Cloud,
            unselectedIcon = Icons.Filled.Cloud,
            navigationItemType = NavigationItemType.ONLINE_CENTER
        ),

        NavigationItem(
            title = Res.string.customernotestitle.fromResource(),
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Filled.People,
            navigationItemType = NavigationItemType.CUSTOMER_NOTES
        )
    )
}