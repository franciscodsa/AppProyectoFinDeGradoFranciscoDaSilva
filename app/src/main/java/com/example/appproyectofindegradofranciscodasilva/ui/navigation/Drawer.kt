package com.example.appproyectofindegradofranciscodasilva.ui.navigation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.appproyectofindegradofranciscodasilva.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(

) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navState by navController.currentBackStackEntryAsState()
    val currentRoute = navState?.destination?.route

    val screensList: List<Screens> = screens



    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavBarHeader()
                Spacer(modifier = Modifier.padding(dimensionResource(id = R.dimen.medium_size_space)))
                NavBarBody(items = screensList, currentRoute = currentRoute, onClick = {
                    navController.navigate(it.route) {
                        popUpTo(route = "registro") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    scope.launch {
                        drawerState.close()
                    }
                })

            }
        }, drawerState = drawerState
    ) {
        if (currentRoute != "login" && currentRoute != "registro" && currentRoute != "resumen") {
            Scaffold() { innerPadding ->
                Navigation(navController = navController, innerPadding = innerPadding)
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text(text = "")
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "menu"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)){
                    Spacer(modifier = Modifier.height(IntrinsicSize.Min))
                    Navigation(navController = navController, innerPadding = innerPadding)
                }
            }

        }
    }

}


@Composable
fun NavBarHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "logo",
            modifier = Modifier
                .size(100.dp)
                .padding(top = 10.dp)
        )
        Text(
            text = "ContaEasy",
            modifier = Modifier.padding(top = 10.dp)
        )
    }
}


@Composable
fun NavBarBody(
    items: List<Screens>,
    currentRoute: String?,
    onClick: (Screens) -> Unit,
) {
    items.forEachIndexed { index, screen ->
        NavigationDrawerItem(
            colors = NavigationDrawerItemDefaults.colors(

            ),
            label = {
                Text(text = screen.titulo)
            }, selected = currentRoute == screen.route, onClick = {
                onClick(screen)
            }, icon = {
                Icon(
                    imageVector = if (currentRoute == screen.route) {
                        screen.iconoSeleccionado
                    } else {
                        screen.iconoDeseleccionado
                    }, contentDescription = screen.titulo
                )
            },
            //todo: esto se puede implementar si coloco un state para indicar si tiene mensajes pendientes
            /*  badge = {
                  screen.badgeCount?.let {
                      Text(text = it.toString())
                  }
              },*/
            modifier = Modifier.padding(
                PaddingValues(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
            )
        )
    }
}