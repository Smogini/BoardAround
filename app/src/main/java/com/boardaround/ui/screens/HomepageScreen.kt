package com.boardaround.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.boardaround.navigation.Route

@Composable
fun HomepageScreen(navController: NavController) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // Titolo della homepage
        Text(text = "Benvenuto su BoardAround", style = androidx.compose.material3.MaterialTheme.typography.headlineLarge)

        // Bottone per navigare alla schermata di Login
        Button(
            onClick = {
                navController.navigate(Route.Login.route) // Naviga alla schermata di login
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Accedi")
        }

        // Bottone per navigare alla schermata di Registrazione
        Button(
            onClick = {
                navController.navigate(Route.Register.route) // Naviga alla schermata di registrazione
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Registrati")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomepageScreen() {
    // Usa un NavController "dummy" per la preview
    HomepageScreen(navController = rememberNavController())
}
