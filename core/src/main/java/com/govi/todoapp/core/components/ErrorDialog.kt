package com.govi.todoapp.core.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

/**
 * Created by Govi on 06,March,2025
 */
@Composable
fun ErrorDialog(
    title: String,
    message: String,
    confirmBtnTxt: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(text = confirmBtnTxt)
            }
        }
    )
}