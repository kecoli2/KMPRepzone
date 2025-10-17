package com.repzone.presentation.legacy.ui.rotalist

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.EditNotifications
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Room
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.EditNotifications
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.Room
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.repzone.core.ui.manager.theme.ThemeManager
import com.repzone.core.ui.model.NavigationItem
import com.repzone.core.ui.util.enum.NavigationItemType
import com.repzone.core.util.extensions.fromResource
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import repzonemobile.core.generated.resources.customernotestitle
import repzonemobile.core.generated.resources.dailyoperationstitle
import repzonemobile.core.generated.resources.documents
import repzonemobile.core.generated.resources.eagleeyelogstitle
import repzonemobile.core.generated.resources.exit
import repzonemobile.core.generated.resources.generalsettings
import repzonemobile.core.generated.resources.indir
import repzonemobile.core.generated.resources.notificationlogpagetitle
import repzonemobile.core.generated.resources.onlinehubtitle
import repzonemobile.core.generated.resources.profile
import repzonemobile.presentation_legacy.generated.resources.Res
import repzonemobile.presentation_legacy.generated.resources.img_generic_logo_min

@Composable
fun RotaScreenLegacy(themeManager: ThemeManager){
    var selectedTab by remember { mutableIntStateOf(0) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    var selectedItemIndex by rememberSaveable { mutableStateOf(-1) }
    val drawerItems = getNavigationItems()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(280.dp)) {

                // TÜM içerik Column; en alta ÇIKIŞ koyabilmek için
                Column(
                    modifier = Modifier
                        .fillMaxHeight() // ← altı “sabitlemek” için şart
                ) {

                    // Drawer Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .background(MaterialTheme.colorScheme.onSecondaryContainer),
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
                                error = painterResource(repzonemobile.core.generated.resources.Res.drawable.profile),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center
                            )
                            Column(
                                modifier = Modifier.padding(start = 16.dp),
                                horizontalAlignment = Alignment.Start,
                            ) {
                                Text(
                                    "Salih Yücel",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    "salih.yucel@repzone.com",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Light,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Menü öğeleri: Kayabilir/çoksa taşmasın diye LazyColumn + weight(1f)
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f, fill = true) // ← boşluğu doldur, alt butonu aşağı it
                    ) {
                        itemsIndexed(drawerItems) { index, item ->
                            NavigationDrawerItem(
                                label = { Text(item.title, style = MaterialTheme.typography.bodyMedium) },
                                selected = index == selectedItemIndex,
                                onClick = {
                                    selectedItemIndex = index
                                    scope.launch { drawerState.close() }
                                    when (drawerItems[index].navigationItemType) {
                                        NavigationItemType.GENERAL_SETTINGS -> {}
                                        NavigationItemType.SHARED_DOCUMENT -> {}
                                        NavigationItemType.DAILY_OPERATIONS -> {}
                                        NavigationItemType.GPS_OPERATIONS -> {}
                                        NavigationItemType.NOTIFICATION_HISTORY -> {}
                                        NavigationItemType.ONLINE_CENTER -> {}
                                        NavigationItemType.CUSTOMER_NOTES -> {}
                                        NavigationItemType.LOG -> {}
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
                        label = { Text(repzonemobile.core.generated.resources.Res.string.exit.fromResource(), style = MaterialTheme.typography.bodyMedium) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
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
        Scaffold { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Custom Top Bar
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sol taraf - Hamburger Menu
                        IconButton(onClick = {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }

                        // Orta - Logo (weight ile tam ortada)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(modifier = Modifier, color = MaterialTheme.colorScheme.onSecondaryContainer) {
                                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    Image(
                                        painter = painterResource(Res.drawable.img_generic_logo_min),
                                        contentDescription = null,
                                        modifier = Modifier.matchParentSize(),
                                        contentScale = ContentScale.Inside
                                    )
                                }
                            }
                        }

                        // Sağ taraf - 3 buton
                        IconButton(enabled = false, onClick = {
                            println("Search clicked")
                        }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            println("Notifications clicked")
                        }) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = {
                            println("Profile clicked")
                        }) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White
                            )
                        }
                    }
                }

                // LazyColumn içinde kaybolacak alan + Tab + content
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF5F5F5))
                ) {
                    // Kaybolacak Alan
                    item {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            color = Color(0xFF4CAF50)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Kaydırınca Kaybolacak Alan",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Bu alan scroll yapınca yukarı kaybolacak",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Tab Layout - Sticky
                    stickyHeader {
                        TabRow(
                            selectedTabIndex = selectedTab,
                            containerColor = Color.White,
                            contentColor = Color(0xFF9C27B0)
                        ) {
                            repeat(3) { index ->
                                Tab(
                                    selected = selectedTab == index,
                                    onClick = { selectedTab = index },
                                    text = { Text("Tab ${index + 1}") },
                                    selectedContentColor = Color(0xFF9C27B0),
                                    unselectedContentColor = Color.Gray
                                )
                            }
                        }
                    }

                    // Tab Content Items
                    val itemCount = when (selectedTab) {
                        0 -> 50
                        1 -> 100
                        else -> 2000
                    }

                    val bgColor = when (selectedTab) {
                        0 -> Color(0xFFFFCC80)
                        1 -> Color(0xFFF8BBD0)
                        else -> Color(0xFFB2DFDB)
                    }

                    items(itemCount) { index ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(bgColor.copy(alpha = 0.3f))
                                    .padding(16.dp)
                            ) {
                                Text("İçerik Kartı ${index + 1}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getNavigationItems(): List<NavigationItem>{
    return listOf(
        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.generalsettings.fromResource(),
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            navigationItemType = NavigationItemType.GENERAL_SETTINGS
        ),
        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.documents.fromResource(),
            selectedIcon = Icons.Filled.DocumentScanner,
            unselectedIcon = Icons.Outlined.DocumentScanner,
            navigationItemType = NavigationItemType.SHARED_DOCUMENT
        ),
        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.dailyoperationstitle.fromResource(),
            selectedIcon = Icons.Filled.AccessTimeFilled,
            unselectedIcon = Icons.Outlined.AccessTime,
            navigationItemType = NavigationItemType.DAILY_OPERATIONS
        ),

        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.eagleeyelogstitle.fromResource(),
            selectedIcon = Icons.Filled.Room,
            unselectedIcon = Icons.Outlined.Room,
            navigationItemType = NavigationItemType.GPS_OPERATIONS
        ),

        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.notificationlogpagetitle.fromResource(),
            selectedIcon = Icons.Filled.EditNotifications,
            unselectedIcon = Icons.Outlined.EditNotifications,
            navigationItemType = NavigationItemType.NOTIFICATION_HISTORY,
            badgeCount = 4
        ),

        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.onlinehubtitle.fromResource(),
            selectedIcon = Icons.Filled.Cloud,
            unselectedIcon = Icons.Filled.Cloud,
            navigationItemType = NavigationItemType.ONLINE_CENTER
        ),

        NavigationItem(
            title = repzonemobile.core.generated.resources.Res.string.customernotestitle.fromResource(),
            selectedIcon = Icons.Filled.People,
            unselectedIcon = Icons.Filled.People,
            navigationItemType = NavigationItemType.CUSTOMER_NOTES
        )
    )
}