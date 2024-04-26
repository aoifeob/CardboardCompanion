package com.example.cardboardcompanion

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cardboardcompanion.ui.components.TopNavBar
import com.example.cardboardcompanion.ui.navigation.Collection
import com.example.cardboardcompanion.ui.navigation.Scanner
import com.example.cardboardcompanion.ui.navigation.Tutorial
import com.example.cardboardcompanion.ui.navigation.topBarNavScreens
import com.example.cardboardcompanion.ui.theme.CardboardCompanionTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }

    @Composable
    fun MainView() {
        CardboardCompanionTheme {
            val navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val currentDestination = currentBackStack?.destination
            val currentScreen = topBarNavScreens.find { it.route == currentDestination?.route } ?: Collection

            Scaffold(
                topBar = {
                    TopNavBar(
                        allScreens = topBarNavScreens,
                        onScreenSelected = { screen ->
                            navController.navigateSingleTopTo(screen.route)
                        },
                        currentScreen = currentScreen
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = Collection.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    composable(route = Collection.route) {
                        Text("Collection")
                        Collection.screen()
                    }
                    composable(route = Scanner.route) {
                        Text("Scanner")
                        Scanner.screen()
                    }
                    composable(route = Tutorial.route) {
                        Text("Tutorial")
                        Tutorial.screen()
                    }
                }
            }

        }
    }

    fun NavHostController.navigateSingleTopTo(route: String) = this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ){
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }


    @Preview(name = "Light Mode")
    @Preview(
        uiMode = Configuration.UI_MODE_NIGHT_YES,
        showBackground = true,
        name = "Dark Mode"
    )
    @Composable
    fun MainViewPreview() {
        MainView()
    }
}