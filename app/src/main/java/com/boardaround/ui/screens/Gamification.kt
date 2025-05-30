package com.boardaround.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    val objectives by userViewModel.achievementList.collectAsState(initial = emptyList())
    val currentProgress = if (objectives.isNotEmpty()) {
        objectives.count { it.isUnlocked } / objectives.size.toFloat()
    } else { 0f }

    LaunchedEffect(Unit) {
        userViewModel.getAllAchievements()
    }

    ScreenTemplate(
        title = "Achievements",
        currentRoute = Route.Gamification,
        navController = navController,
        userViewModel = userViewModel
    ) {
        item {
            ProgressBar(
                progress = currentProgress,
                modifier = Modifier.padding(10.dp)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                thickness = 10.dp,
                color = MaterialTheme.colorScheme.surface,
            )
        }

        items(objectives.size) { objectiveIndex ->
            val currentAchievement = objectives[objectiveIndex]

            ObjectiveItem(
                title = currentAchievement.description,
                isUnlocked = currentAchievement.isUnlocked,
                onClick = {  }
            )
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
        Color(0xFF4CAF50)
    } else {
        MaterialTheme.colorScheme.primary
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(200.dp)
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
                contentDescription = "Achievements unlocked",
                tint = progressColor,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(80.dp)
            )
        } else {
            Text(
                text = "$percentage%",
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
        val iconRes = if (isUnlocked) {
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