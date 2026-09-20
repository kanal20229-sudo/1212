package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.random.Random

data class MathChallenge(
    val question: String,
    val options: List<Int>,
    val correctIndex: Int
)

fun generateMathProblem(): MathChallenge {
    val type = Random.nextInt(4)
    return when (type) {
        0 -> {
            val a = Random.nextInt(6, 15)
            val b = Random.nextInt(6, 15)
            val ans = a * b
            val fake1 = ans + Random.nextInt(1, 5)
            val fake2 = ans - Random.nextInt(1, 5)
            val fake3 = ans + 10
            val opts = listOf(ans, fake1, fake2, fake3).shuffled()
            MathChallenge("$a × $b = ?", opts, opts.indexOf(ans))
        }
        1 -> {
            val a = Random.nextInt(25, 99)
            val b = Random.nextInt(15, 65)
            val ans = a + b
            val fake1 = ans + 2
            val fake2 = ans - 10
            val fake3 = ans + 10
            val opts = listOf(ans, fake1, fake2, fake3).shuffled()
            MathChallenge("$a + $b = ?", opts, opts.indexOf(ans))
        }
        2 -> {
            val b = Random.nextInt(3, 12)
            val ans = Random.nextInt(4, 15)
            val a = b * ans
            val fake1 = ans + 1
            val fake2 = ans - 2
            val fake3 = ans + 3
            val opts = listOf(ans, fake1, fake2, fake3).shuffled()
            MathChallenge("$a ÷ $b = ?", opts, opts.indexOf(ans))
        }
        else -> {
            val n = Random.nextInt(5, 13)
            val ans = n * n
            val fake1 = ans + 1
            val fake2 = ans - 4
            val fake3 = (n + 1) * (n + 1)
            val opts = listOf(ans, fake1, fake2, fake3).shuffled()
            MathChallenge("$n² = ?", opts, opts.indexOf(ans))
        }
    }
}

@Composable
fun MiniGamesScreen(
    onRewardEarned: (Int) -> Unit
) {
    var mathScore by remember { mutableStateOf(0) }
    var currentProblem by remember { mutableStateOf(generateMathProblem()) }
    var streak by remember { mutableStateOf(0) }
    var lastAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    val stressWords = listOf(
        Pair("чорнОслив", listOf("чОрнослив", "чорнОслив", "чорнослИв")),
        Pair("одинАдцять", listOf("одИнадцять", "одинАдцять", "одинадцЯть")),
        Pair("вирАзний", listOf("вИразний", "вирАзний", "виразнИй")),
        Pair("фОльга", listOf("фОльга", "фольгА"))
    )
    var currentStressIdx by remember { mutableStateOf(0) }
    var stressAnswered by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrGreenSuccess),
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
                    Text(text = "🎮", fontSize = 28.sp)
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Навчальний аркадний хаб",
                            color = UkrTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Грай, тренуй швидкість та заробляй бали ⭐",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(UkrNavySurface)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🔥 Серія: $streak",
                        color = UkrFlagYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Game 1: Math Sprint
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⚡ Математичний спринт",
                        color = UkrTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Бали: $mathScore (+5⭐ кожні 3)",
                        color = UkrFlagYellow,
                        fontSize = 12.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(UkrNavySurface)
                        .padding(vertical = 18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentProblem.question,
                        color = UkrFlagYellow,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Options grid
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    currentProblem.options.take(2).forEachIndexed { index, option ->
                        Button(
                            onClick = {
                                if (index == currentProblem.correctIndex) {
                                    mathScore += 1
                                    streak += 1
                                    lastAnswerCorrect = true
                                    if (streak % 3 == 0) onRewardEarned(5)
                                } else {
                                    streak = 0
                                    lastAnswerCorrect = false
                                }
                                currentProblem = generateMathProblem()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UkrNavySurface),
                            border = BorderStroke(1.dp, UkrFlagBlue),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Text(text = "$option", color = UkrTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    currentProblem.options.drop(2).take(2).forEachIndexed { relativeIdx, option ->
                        val actualIdx = relativeIdx + 2
                        Button(
                            onClick = {
                                if (actualIdx == currentProblem.correctIndex) {
                                    mathScore += 1
                                    streak += 1
                                    lastAnswerCorrect = true
                                    if (streak % 3 == 0) onRewardEarned(5)
                                } else {
                                    streak = 0
                                    lastAnswerCorrect = false
                                }
                                currentProblem = generateMathProblem()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UkrNavySurface),
                            border = BorderStroke(1.dp, UkrFlagBlue),
                            modifier = Modifier.weight(1f).height(48.dp)
                        ) {
                            Text(text = "$option", color = UkrTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Game 2: Ukrainian Stress Quiz
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            val (correctWord, variants) = stressWords[currentStressIdx % stressWords.size]

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "🇺🇦 ЗНО-наголоси",
                    color = UkrTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Оберіть правильний нормативний наголос:",
                    color = UkrTextSecondary,
                    fontSize = 12.sp
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    variants.forEach { variant ->
                        val isCorrect = variant == correctWord
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (stressAnswered != null && isCorrect) UkrGreenSuccess.copy(alpha = 0.3f) else UkrNavySurface
                            ),
                            border = BorderStroke(1.dp, UkrNavyBorder),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    stressAnswered = isCorrect
                                    if (isCorrect) onRewardEarned(3)
                                }
                        ) {
                            Text(
                                text = variant,
                                color = UkrTextPrimary,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                if (stressAnswered != null) {
                    TextButton(
                        onClick = {
                            currentStressIdx += 1
                            stressAnswered = null
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Наступне слово >", color = UkrFlagYellow)
                    }
                }
            }
        }
    }
}
