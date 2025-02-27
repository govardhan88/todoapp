package com.govi.todoapp.core.navigation

/**
 * Created by Govi on 27,February,2025
 */
sealed class Routes(val route: String) {
    object Home: Routes("home")
}