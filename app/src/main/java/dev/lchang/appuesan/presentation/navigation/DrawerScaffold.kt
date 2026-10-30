package dev.lchang.appuesan.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import dev.lchang.appuesan.data.remote.FirebaseAuthManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerScaffold(navController: NavController,
                   content: @Composable () -> Unit)
{
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // Cierra el drawer y navega sin apilar la misma pantalla varias veces
    fun goTo(route: String) {
        scope.launch { drawerState.close() }
        if (route != currentRoute) {
            navController.navigate(route) {
                popUpTo("home")
                launchSingleTop = true
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(12.dp))
                Text("Menu Principal", modifier = Modifier.padding(16.dp))
                //NavigationDrawerItem
                NavigationDrawerItem(
                    label = { Text("Home")},
                    selected = currentRoute == "home",
                    onClick = { goTo("home") }
                )
                NavigationDrawerItem(
                    label = { Text("Permissions")},
                    selected = currentRoute == "permissions",
                    onClick = { goTo("permissions") }
                )
                NavigationDrawerItem(
                    label = { Text("Real Time")},
                    selected = currentRoute == "realtime",
                    onClick = { goTo("realtime") }
                )
                NavigationDrawerItem(
                    label = { Text("Supabase Storage")},
                    selected = currentRoute == "supabase",
                    onClick = { goTo("supabase") }
                )
                NavigationDrawerItem(
                    label = { Text("API Football")},
                    selected = currentRoute == "apifootball",
                    onClick = { goTo("apifootball") }
                )
                NavigationDrawerItem(
                    label = { Text("Firebase Logout")},
                    selected = false,
                    onClick = {
                        scope.launch { drawerState.close() }
                        FirebaseAuthManager.logout()
                        // Limpia todo el back stack para no volver atrás a pantallas privadas
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {Text("UESAN App")},
                    navigationIcon = {
                        IconButton(
                            onClick =  {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(Icons.Default.Menu, "Menu")
                        }
                    })
            }
        ) {
            paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)){
                content()
            }
        }
    }
}