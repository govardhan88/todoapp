package com.govi.todoapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.govi.todoapp.core.SharedViewModel
import com.govi.todoapp.core.components.ErrorDialog
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.domain.model.Todo

/**
 * Created by Govi on 26,February,2025
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val filteredTodos by viewModel.filteredTodos.collectAsState()
    val allTodos by viewModel.allTodos.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val showSearchBar = allTodos.isNotEmpty()
    val isDebouncing by viewModel.isDebouncing.collectAsState()

    val errorMessage by sharedViewModel.errorMessage.collectAsState()

    LaunchedEffect(errorMessage) {
        errorMessage?.let { errorMsg ->
            viewModel.setErrorState(errorMsg)
            sharedViewModel.clearErrorMessage()
        }
    }
    LaunchedEffect(key1 = Unit) {
        viewModel.clearSearchQuery()
    }

    Scaffold(
        floatingActionButton = {
            HomeFloatingActionButton(onAddTodoClicked = {
                viewModel.navigateToAddTodo()
                searchText = ""
            })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = if (uiState is UIState.Loading) Arrangement.Center else Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showSearchBar) {
                SearchBar(
                    searchText = searchText,
                    onSearchTextChange = {
                        searchText = it
                        viewModel.onSearchQueryChange(it)
                    },
                    isDebouncing = isDebouncing
                )
            }
            when (uiState) {
                is UIState.Loading -> LoadingIndicator()
                is UIState.Success<*> -> {
                    val todos = filteredTodos
                    if (allTodos.isEmpty() && searchText.isBlank()) {
                        EmptyTodoList()
                    } else if (todos.isEmpty() && searchText.isNotBlank()) {
                        NoSearchResults()
                    } else {
                        TodoList(todos = todos)
                    }
                }

                is UIState.Error<*> -> ErrorDialog(
                    title = stringResource(R.string.error),
                    message = (uiState as UIState.Error<*>).message.toString(),
                    confirmBtnTxt = stringResource(R.string.ok),
                    onDismiss = {}
                )

                is UIState.Idle -> {}
            }
        }
    }
}

@Composable
fun HomeFloatingActionButton(onAddTodoClicked: () -> Unit) {
    FloatingActionButton(onClick = onAddTodoClicked) {
        Icon(Icons.Filled.Add, stringResource(id = R.string.add))
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit, isDebouncing: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(id = R.string.search)) },
            shape = RoundedCornerShape(25.dp),
            maxLines = 1,
            trailingIcon = {
                if (isDebouncing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(Icons.Filled.Search, stringResource(id = R.string.search))
                }
            }
        )
    }
}

@Composable
fun LoadingIndicator() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(64.dp),
            color = Color.Blue,
            strokeWidth = 10.dp
        )
    }
}

@Composable
fun EmptyTodoList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.press_to_add_todo),
            fontWeight = FontWeight(900),
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun NoSearchResults() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.no_search_results_found),
            fontWeight = FontWeight(900),
            fontSize = 26.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TodoList(todos: List<Todo>) {
    Column(modifier = Modifier.padding(8.dp)) {
        todos.forEach { todo ->
            TodoItem(todo = todo)
        }
    }
}

@Composable
fun TodoItem(todo: Todo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(
            text = todo.eventTitle,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            fontSize = 18.sp,
            textAlign = TextAlign.Start,
            fontWeight = FontWeight(500),
            color = Color.DarkGray
        )
    }
}