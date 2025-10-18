package com.example.practica1moviles.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.practica1moviles.presentation.home.HomeScreen
import com.example.practica1moviles.presentation.watercalculator.WaterCalculatorScreen
import com.example.practica1moviles.presentation.fitness.FitnessTrackerScreen
import com.example.practica1moviles.presentation.sportscars.SportsCarsScreen

// Rutas de navegación
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object WaterCalculator : Screen("water_calculator")
    object PhysicalActivity : Screen("physical_activity")
    object SportsCatalog : Screen("sports_catalog")
}

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWaterCalculator = {
                    navController.navigate(Screen.WaterCalculator.route)
                },
                onNavigateToPhysicalActivity = {
                    navController.navigate(Screen.PhysicalActivity.route)
                },
                onNavigateToSportsCatalog = {
                    navController.navigate(Screen.SportsCatalog.route)
                }
            )
        }

        composable(Screen.WaterCalculator.route) {
            WaterCalculatorScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.PhysicalActivity.route) {
            FitnessTrackerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.SportsCatalog.route) {
            SportsCarsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
