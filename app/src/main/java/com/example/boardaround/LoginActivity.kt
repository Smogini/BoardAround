package com.example.boardaround

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.boardaround.ui.theme.BoardAroundTheme
import com.example.boardaround.ui.theme.BoardAroundTheme

class LoginActivity : ComponentActivity() {
    private val registeredUsers = mutableListOf("user@example.com")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BoardAroundTheme {
                LoginScreen()
            }
        }
    }

    @Composable
    fun LoginScreen() {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var loginError by remember { mutableStateOf("") }
        var registerSuccess by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Username
            TextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            //Password
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )


            if (loginError.isNotEmpty()) {
                Text(text = loginError, color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(8.dp))
            }

            //Accedi
            Button(
                onClick = {
                    if (username == "user@example.com" && password == "password123") {
                        Toast.makeText(
                            this@LoginActivity,
                            "Accesso riuscito!",
                            Toast.LENGTH_SHORT
                        ).show()
                        loginError = ""

                        val intent = Intent(this@LoginActivity, HomepageActivity::class.java)
                        startActivity(intent)
                    } else {
                        loginError = "Username o password errati!"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Text("Accedi")
            }

            //Registrati
            Button(
                onClick = {
                            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                            startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrati")
                    }
        }
    }
}
