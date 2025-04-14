import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.boardaround.R

@Composable
fun GamificationScreen(navController: NavController) {
    // Lista di obiettivi
    val objectives = listOf(
        "Registrati a BoardAround!",
        "Pubblica il tuo primo post!",
        "Crea il tuo primo evento!",
        "Attiva il tema scuro!",
        "Invita un amico a un tuo evento!"
    )

    // Stato per il progresso degli obiettivi (true = sbloccato, false = bloccato)
    var unlockedObjectives by remember { mutableStateOf(listOf(false, false, false, false, false)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally, // Centra orizzontalmente
        verticalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.CenterVertically) // Centra verticalmente e mantiene lo spazio
    ) {
        Text(
            text = "Gamification",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Grafico di progressione
        val currentProgress = unlockedObjectives.count { it } / objectives.size.toFloat()
        ProgressBar(
            progress = currentProgress,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(objectives.size) { index ->
                ObjectiveItem(
                    title = objectives[index],
                    isUnlocked = unlockedObjectives[index],
                    onClick = {
                        // Quando l'utente clicca su un obiettivo
                        unlockedObjectives = unlockedObjectives.toMutableList().apply {
                            this[index] = true // Sblocca l'obiettivo
                        }
                    },
                    objectiveIndex = index + 1 // Per indicare l'obiettivo (1, 2, 3, etc.)
                )
            }
        }
    }
}

@Composable
fun ProgressBar(progress: Float, modifier: Modifier = Modifier) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500), // Personalizza la durata dell'animazione
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
            progress = animatedProgress, // Usa animatedProgress qui
            modifier = Modifier.fillMaxSize(), // Riempire il Box
            color = progressColor, // Usa il colore condizionale
            trackColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
            strokeWidth = 12.dp // Aumenta lo spessore (valore predefinito è 4.dp)
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
fun ObjectiveItem(title: String, isUnlocked: Boolean, onClick: () -> Unit, objectiveIndex: Int) {
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
            when (objectiveIndex) {
                1 -> R.drawable.gamification1
                2 -> R.drawable.gamification2
                3 -> R.drawable.gamification3
                4 -> R.drawable.gamification4
                5 -> R.drawable.gamification5
                else -> R.drawable.gamification1
            }
        } else {
            // Se l'obiettivo è bloccato, mostra l'icona di lock
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