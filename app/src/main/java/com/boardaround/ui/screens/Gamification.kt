package com.boardaround.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.R
import com.boardaround.navigation.Route
import com.boardaround.viewmodel.UserViewModel

@Composable
fun GamificationScreen(navController: NavController, userViewModel: UserViewModel) {
    val objectives by userViewModel.objectives.collectAsState()

    ScreenTemplate(
        title = "Obiettivi",
        currentRoute = Route.Gamification,
        navController = navController,
        userViewModel = userViewModel
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
            )
        ) {
            // Grafico di progressione
            val currentProgress = objectives.values.count { it } / objectives.size.toFloat()
            ProgressBar(
                progress = currentProgress,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize().padding(bottom = 100.dp)
            ) {
                items(objectives.size) { objectiveIndex ->
                    val (currentObjective, isUnlocked) = objectives.entries.elementAt(objectiveIndex)

                    ObjectiveItem(
                        title = currentObjective,
                        isUnlocked = isUnlocked,
                        onClick = { userViewModel.unlockObjective(currentObjective) }
                    )
                }
            }
        }
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500),
        label = "Progress Animation"
    )
    val percentage = (animatedProgress * 100).toInt()
    val progressColor = if (percentage == 100) {
        Color(0xFF4CAF50) // Verde
    } else {
        MaterialTheme.colorScheme.primary // Colore primario predefinito
    }
    Box( // Utilizza un Box per sovrapporre il testo all'indicatore
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp) // Aumenta la dimensione
    ) {
        CircularProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxSize(),
            color = progressColor,
            strokeWidth = 12.dp,
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
        )
        if (percentage == 100) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Obiettivi completati",
                tint = progressColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp) // Regola la dimensione dell'icona
            )
        } else {
            Text(
                text = "$percentage%", // Mostra la percentuale al centro
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ObjectiveItem(title: String, isUnlocked: Boolean, onClick: () -> Unit) {
    val gamificationIcon = listOf(
        R.drawable.gamification1,
        R.drawable.gamification2,
        R.drawable.gamification3,
        R.drawable.gamification4,
        R.drawable.gamification5,
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        // Se l'obiettivo è sbloccato, cambia l'icona
        val iconRes = if (isUnlocked) {
            // Se l'obiettivo è sbloccato, mostra l'icona corrispondente gamification-1, gamification-2, etc.
            gamificationIcon.random()
        } else {
            R.drawable.lock
        }
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Objective Icon",
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
    }
}