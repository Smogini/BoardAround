package com.boardaround.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.boardaround.data.trivia.TriviaQuestion
import com.boardaround.viewmodel.TriviaViewModel

@Composable
fun TriviaScreen() {
    val viewModel: TriviaViewModel = viewModel()
    val questions by viewModel.questions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    val answersStatus = remember { mutableStateListOf<AnswerStatus>(*(Array(5) { AnswerStatus.NOT_ANSWERED })) } // Inizializza lo stato delle risposte

    // Carica le domande all'avvio (o quando necessario)
    LaunchedEffect(Unit) {
        // Per i giochi da tavolo, potresti dover cercare l'ID della categoria o usare null per domande casuali
        viewModel.loadTriviaQuestions(amount = 5, category = null)
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (error != null) {
            Text(text = "Errore: $error")
        } else if (questions.isNotEmpty() && currentQuestionIndex < questions.size) {
            // ... (codice per visualizzare la domanda e le risposte, come prima) ...
            val currentQuestion = questions[currentQuestionIndex]
            Text(text = currentQuestion.question)
            val answers = currentQuestion.incorrectAnswers + currentQuestion.correctAnswer
            Log.d("TriviaScreen", "Risposte per la domanda ${currentQuestionIndex + 1}: $answers")

            // Mescola le risposte per evitare che la risposta corretta sia sempre l'ultima
            val shuffledAnswers = answers.shuffled()

            Column {
                shuffledAnswers.forEach { answer ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = selectedAnswer == answer,
                            onClick = { selectedAnswer = answer }
                        )
                        Text(text = answer)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedAnswer != null) {
                        showResult = true
                        val isCorrect = selectedAnswer == currentQuestion.correctAnswer
                        answersStatus[currentQuestionIndex] = if (isCorrect) AnswerStatus.CORRECT else AnswerStatus.INCORRECT // Aggiorna lo stato della risposta
                    }
                },
                enabled = selectedAnswer != null
            ) {
                Text(text = "Invia")
            }

            if (showResult) {
                val isCorrect = selectedAnswer == currentQuestion.correctAnswer
                Text(
                    text = if (isCorrect) "Risposta corretta!" else "Risposta errata. La risposta corretta è: ${currentQuestion.correctAnswer}"
                )
                if (currentQuestionIndex < questions.size - 1) {
                    Button(
                        onClick = {
                            currentQuestionIndex++
                            selectedAnswer = null
                            showResult = false
                        }
                    ) {
                        Text(text = "Prossima domanda")
                    }
                } else {
                    Text(text = "Quiz completato!")
                    // Qui potresti aggiungere la logica per mostrare il punteggio o altre informazioni di riepilogo
                }
            }
        } else {
            Text(text = "Nessuna domanda disponibile.")
        }
        Spacer(modifier = Modifier.height(20.dp))
        // Visualizzazione dei pallini
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            answersStatus.forEachIndexed { index, status ->
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = when (status) {
                                AnswerStatus.CORRECT -> Color.Green
                                AnswerStatus.INCORRECT -> Color.Red
                                AnswerStatus.NOT_ANSWERED -> Color.Gray
                            },
                            radius = size.minDimension / 2,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                }
                if (index < answersStatus.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun MiniTrivia(
    questions: List<TriviaQuestion>,
    onQuizFinished: () -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    val currentQuestion = questions.getOrNull(currentQuestionIndex)

    if (currentQuestion != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = currentQuestion.question, style = MaterialTheme.typography.bodyMedium)
            val answers = currentQuestion.incorrectAnswers + currentQuestion.correctAnswer
            Log.d("MiniTrivia", "Risposte per la domanda ${currentQuestionIndex + 1}: $answers")

            val shuffledAnswers = answers.shuffled()

            Column {
                shuffledAnswers.forEach { answer ->
                    val isSelected = selectedAnswer == answer
                    val isCorrectAnswer = answer == currentQuestion.correctAnswer
                    val backgroundColor = if (showResult) {
                        when {
                            isSelected && isCorrectAnswer -> Color.Green.copy(alpha = 0.5f) // Verde chiaro per la risposta corretta selezionata
                            isSelected && !isCorrectAnswer -> Color.Red.copy(alpha = 0.5f)   // Rosso chiaro per la risposta errata selezionata
                            isCorrectAnswer -> Color.Green.copy(alpha = 0.5f)                // Verde chiaro per la risposta corretta (non selezionata)
                            else -> Color.Transparent
                        }
                    } else {
                        Color.Transparent
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(backgroundColor)
                            .padding(vertical = 2.dp, horizontal = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedAnswer == answer,
                            onClick = { selectedAnswer = answer },
                            enabled = !showResult // Disabilita la selezione dopo l'invio
                        )
                        Text(text = answer, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (selectedAnswer != null) {
                        showResult = true
                    }
                },
                enabled = selectedAnswer != null && !showResult // Disabilita il pulsante dopo l'invio
            ) {
                Text(text = "Invia", style = MaterialTheme.typography.bodySmall)
            }

            if (showResult && currentQuestionIndex < questions.size - 1) {
                Button(
                    onClick = {
                        currentQuestionIndex++
                        selectedAnswer = null
                        showResult = false
                    }
                ) {
                    Text(text = "Prossima", style = MaterialTheme.typography.bodySmall)
                }
            } else if (showResult) {
                Text(text = "Completato!", style = MaterialTheme.typography.bodySmall)
                onQuizFinished()
            }
        }
    } else {
        Text(text = "Caricamento...", style = MaterialTheme.typography.bodySmall)
    }
}



enum class AnswerStatus {
    CORRECT, INCORRECT, NOT_ANSWERED
}