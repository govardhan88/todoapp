package com.govi.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.govi.todoapp.add_todo.AddTodoScreen
import com.govi.todoapp.core.SharedViewModel
import com.govi.todoapp.core.navigation.Routes.AddTodo
import com.govi.todoapp.core.navigation.Routes.Home
import com.govi.todoapp.core.theme.TodoAppTheme
import com.govi.todoapp.home.HomeScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TodoAppTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxSize()
                ) {
                    NavHost(
                        navController = viewModel.navHostController,
                        startDestination = Home.route
                    ) {
                        composable(Home.route) {
                            HomeScreen(sharedViewModel = sharedViewModel)
                        }
                        composable(AddTodo.route) {
                            AddTodoScreen(sharedViewModel = sharedViewModel)
                        }
                    }
                }
            }
        }
    }
}

