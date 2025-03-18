package com.example.boardaround.screens

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun LoginScreen(username: OutlinedTextFieldDefaults) {
    val username = remember { TextField(value = username) }

    OutlinedTextFieldDefaults(

    )

}
