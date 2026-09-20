package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.NmtQuestion
import com.example.ui.theme.*

@Composable
fun NmtPrepScreen(
    answers: Map<Int, Int>,
    onSelectOption: (questionIndex: Int, optionIndex: Int) -> Unit,
    onReset: () -> Unit
) {
    var activeQuestionIndex by remember { mutableStateOf(0) }
    val questions = SampleData.nmtQuestions
    val currentQuestion = questions.getOrNull(activeQuestionIndex) ?: questions[0]
    val selectedOption = answers[activeQuestionIndex]

    val answeredCount = answers.size
    val correctCount = answers.count { (qIdx, optIdx) ->
        questions.getOrNull(qIdx)?.correctIndex == optIdx
    }
    val nmtScore = if (answeredCount > 0) {
        100 + ((correctCount.toFloat() / questions.size) * 100).toInt()
    } else 100

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card with Score
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagBlue),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🎓", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Тренажер НМТ / ЗНО 2025",
                            color = UkrTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Шкала оцінювання МОН: 100 – 200 балів",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(UkrNavySurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Орієнтовний бал", color = UkrTextMuted, fontSize = 10.sp)
                        Text(
                            text = "$nmtScore / 200",
                            color = UkrFlagYellow,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Question Switcher Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            questions.forEachIndexed { index, q ->
                val isAnswered = answers.containsKey(index)
                val isCorrect = answers[index] == q.correctIndex
                val isCurrent = index == activeQuestionIndex

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when {
                                isCurrent -> UkrFlagYellow
                                isAnswered && isCorrect -> UkrGreenSuccess.copy(alpha = 0.4f)
                                isAnswered && !isCorrect -> UkrRedUrgent.copy(alpha = 0.4f)
                                else -> UkrNavyCard
                            }
                        )
                        .clickable { activeQuestionIndex = index }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        color = if (isCurrent) UkrNavyBackground else UkrTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Active Question Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrFlagBlue)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = currentQuestion.subject,
                            color = UkrTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(
                        text = "Запитання ${activeQuestionIndex + 1} з ${questions.size}",
                        color = UkrTextSecondary,
                        fontSize = 12.sp
                    )
                }

                Text(
                    text = currentQuestion.question,
                    color = UkrTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )

                // Options
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentQuestion.options.forEachIndexed { optIndex, optionText ->
                        val isThisChosen = selectedOption == optIndex
                        val isThisCorrect = optIndex == currentQuestion.correctIndex
                        val showResult = selectedOption != null

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    showResult && isThisCorrect -> UkrGreenSuccess.copy(alpha = 0.2f)
                                    showResult && isThisChosen && !isThisCorrect -> UkrRedUrgent.copy(alpha = 0.2f)
                                    isThisChosen -> UkrFlagBlue.copy(alpha = 0.3f)
                                    else -> UkrNavySurface
                                }
                            ),
                            border = BorderStroke(
                                1.dp,
                                when {
                                    showResult && isThisCorrect -> UkrGreenSuccess
                                    showResult && isThisChosen && !isThisCorrect -> UkrRedUrgent
                                    else -> UkrNavyBorder
                                }
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (selectedOption == null) {
                                        onSelectOption(activeQuestionIndex, optIndex)
                                    }
                                }
                                .testTag("nmt_opt_${activeQuestionIndex}_$optIndex")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = optionText,
                                    color = UkrTextPrimary,
                                    fontSize = 13.sp,
                                    modifier = Modifier.weight(1f)
                                )

                                if (showResult) {
                                    if (isThisCorrect) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = UkrGreenSuccess, modifier = Modifier.size(18.dp))
                                    } else if (isThisChosen) {
                                        Icon(Icons.Default.Close, contentDescription = null, tint = UkrRedUrgent, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Detailed Official Explanation
                if (selectedOption != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(UkrNavyCardElevated)
                            .padding(12.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = UkrFlagYellow, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Пояснення та розбір правила:",
                                    color = UkrFlagYellow,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = currentQuestion.explanation,
                                color = UkrTextSecondary,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Next Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { onReset() }) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = UkrTextSecondary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Скинути спробу", color = UkrTextSecondary, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            if (activeQuestionIndex < questions.size - 1) {
                                activeQuestionIndex += 1
                            }
                        },
                        enabled = activeQuestionIndex < questions.size - 1,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue)
                    ) {
                        Text("Наступне питання >", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
