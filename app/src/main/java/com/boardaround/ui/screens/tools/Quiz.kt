package com.boardaround.ui.screens.tools

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.boardaround.navigation.Route
import com.boardaround.ui.components.CustomButton
import com.boardaround.ui.components.CustomTextField
import com.boardaround.ui.screens.ScreenTemplate
import com.boardaround.viewmodel.TriviaViewModel

@Composable
private fun ShowTriviaScreen(triviaViewModel: TriviaViewModel) {
    val questions by triviaViewModel.questions.collectAsState()

    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var showResult by remember { mutableStateOf(false) }
    val answersStatus = remember { mutableStateListOf<AnswerStatus>() }

    val userPoints by triviaViewModel.userPoints.collectAsState()
    val reloadQuiz = remember { mutableStateOf(false) }

    LaunchedEffect(questions) {
        answersStatus.clear()
        answersStatus.addAll(List(questions.size) { AnswerStatus.NOT_ANSWERED })
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (questions.isEmpty()) {
            CircularProgressIndicator()
        } else if (currentQuestionIndex < questions.size) {
            val currentQuestion = questions[currentQuestionIndex]
            Text(text = currentQuestion.question)
            val answers = currentQuestion.incorrectAnswers + currentQuestion.correctAnswer

            Column {
                answers.forEach { answer ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedAnswer == answer,
                            onClick = { selectedAnswer = answer }
                        )
                        Text(
                            text = answer,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedAnswer != null) {
                        showResult = true
                        val isCorrect = selectedAnswer == currentQuestion.correctAnswer
                        if (isCorrect) {
                            answersStatus[currentQuestionIndex] = AnswerStatus.CORRECT
                            triviaViewModel.correctAnswer()
                        } else {
                            answersStatus[currentQuestionIndex] = AnswerStatus.INCORRECT
                        }
                    }
                },
                enabled = selectedAnswer != null && !showResult
            ) {
                Text(text = "Invia")
            }

            if (showResult) {
                val isCorrect = selectedAnswer == currentQuestion.correctAnswer
                Text(
                    text = if (isCorrect) "Risposta corretta!" else "Risposta errata. La risposta corretta è: ${currentQuestion.correctAnswer}"
                )

                Spacer(modifier = Modifier.size(10.dp))

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
                    Text("Punteggio finale $userPoints")
                    CustomButton(
                        onClick = { reloadQuiz.value = true },
                        text = "Gioca di nuovo"
                    )
                }
            }
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
fun SetupTriviaScreen(navController: NavController, triviaViewModel: TriviaViewModel) {
    var questionCount by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("Board Games") }
    var selectedDifficulty by remember { mutableStateOf("easy") }
    var quizStarted by remember { mutableStateOf(false) }

    val categories = listOf("Board Games" to 16, "Video Games" to 15)
    val difficulties = listOf("easy", "medium", "hard")

    ScreenTemplate(
        title = "Quizzettone",
        navController = navController,
        currentRoute = Route.UtilTools,
        showBottomBar = false
    ) {
        item {
            if (!quizStarted) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(
                        label = "Numero di domande",
                        value = questionCount,
                        onValueChange = {  questionCount = it },
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.fillMaxWidth()
                    )

                    DropdownSelector(
                        label = "Categoria",
                        options = categories.map { it.first },
                        selectedOption = selectedCategory,
                        onOptionSelected = { selectedCategory = it }
                    )

                    DropdownSelector(
                        label = "Difficoltà",
                        options = difficulties,
                        selectedOption = selectedDifficulty,
                        onOptionSelected = { selectedDifficulty = it }
                    )

                    Button(
                        onClick = {
                            val amount = questionCount.text.toIntOrNull() ?: 10
                            val categoryId = categories.find { it.first == selectedCategory }?.second ?: 16
                            triviaViewModel.loadTriviaQuestions(amount, categoryId, selectedDifficulty)
                            quizStarted = true
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Avvia Quiz")
                    }
                }
            } else {
                ShowTriviaScreen(triviaViewModel = triviaViewModel)
            }
        }
    }
}

@Composable
private fun DropdownSelector(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedTextField(
            value = selectedOption,
            onValueChange = {},
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    Modifier.clickable { expanded = !expanded }
                )
            }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

enum class AnswerStatus {
    CORRECT, INCORRECT, NOT_ANSWERED
}