package com.govi.todoapp.add_todo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.govi.todoapp.core.theme.Purple40
import com.govi.todoapp.core.util.UIState
import com.govi.todoapp.home.R

/**
 * Created by Govi on 27,February,2025
 */
@Composable
fun AddTodoScreen(
    viewModel: AddTodoViewModel = hiltViewModel(),
    sharedViewModel: SharedViewModel = hiltViewModel()
) {
    var eventTitle by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.resetUiState()
    }

    LaunchedEffect(uiState) {
        if (uiState is UIState.Success<*> || uiState is UIState.Error<*>) {
            if (uiState is UIState.Error<*>)
                sharedViewModel.setErrorMessage((uiState as UIState.Error<*>).message.toString())
            viewModel.navigateBack()
        }
    }

    Scaffold(
        topBar = { AddTodoTopBar() },
    ) { paddingValues ->
        AddTodoContent(
            paddingValues = paddingValues,
            eventTitle = eventTitle,
            onEventTitleChange = { eventTitle = it },
            onAddTodoClicked = {
                viewModel.addTodo(eventTitle)
            },
            uiState = uiState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoTopBar() {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.add_todo),
                color = Color.White,
                fontWeight = FontWeight(900),
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Purple40
        )
    )
}

@Composable
fun AddTodoContent(
    paddingValues: PaddingValues,
    eventTitle: String,
    onEventTitleChange: (String) -> Unit,
    onAddTodoClicked: () -> Unit,
    uiState: UIState<*>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = eventTitle,
            onValueChange = onEventTitleChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            label = { Text(stringResource(id = R.string.enter_event_name)) }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AddTodoButton(onAddTodoClicked = onAddTodoClicked, uiState = uiState)
    }
}

@Composable
fun AddTodoButton(onAddTodoClicked: () -> Unit, uiState: UIState<*>) {
    Button(
        onClick = onAddTodoClicked,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Purple40,
            contentColor = Color.White
        ),
        enabled = uiState !is UIState.Loading
    ) {
        if (uiState is UIState.Loading) {
            CircularProgressIndicator(color = Color.White)
        } else {
            Text(
                text = stringResource(id = R.string.add_todo),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight(900)
            )
        }
    }
}