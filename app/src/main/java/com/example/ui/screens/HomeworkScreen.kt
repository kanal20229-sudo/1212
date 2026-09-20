package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import com.example.model.Homework
import com.example.model.Priority
import com.example.ui.theme.*

@Composable
fun HomeworkScreen(
    homeworkList: List<Homework>,
    isEvaluatingAi: Boolean,
    onToggleHomework: (String) -> Unit,
    onAddHomework: (subject: String, taskTitle: String, description: String, priority: Priority, deadline: String, pages: String) -> Unit,
    onEvaluateWithAi: (hwId: String, answerText: String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var activeAiCheckHomework by remember { mutableStateOf<Homework?>(null) }
    var studentAnswerInput by remember { mutableStateOf("") }
    var filterPriority by remember { mutableStateOf<Priority?>(null) }

    val completedCount = homeworkList.count { it.isDone }
    val totalCount = homeworkList.size
    val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

    val displayedList = remember(homeworkList, filterPriority) {
        if (filterPriority == null) homeworkList else homeworkList.filter { it.priority == filterPriority }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Progress Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "📝 Прогрес виконання завдань",
                        color = UkrTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$completedCount з $totalCount (${(progress * 100).toInt()}%)",
                        color = UkrFlagYellow,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = UkrGreenSuccess,
                    trackColor = UkrNavySurface,
                )
            }
        }

        // Actions & Filters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                FilterChip(
                    selected = filterPriority == null,
                    onClick = { filterPriority = null },
                    label = { Text("Всі", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UkrFlagBlue,
                        containerColor = UkrNavyCard,
                        labelColor = UkrTextPrimary
                    )
                )
                FilterChip(
                    selected = filterPriority == Priority.URGENT,
                    onClick = { filterPriority = if (filterPriority == Priority.URGENT) null else Priority.URGENT },
                    label = { Text("Термінові 🔴", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UkrRedUrgent,
                        containerColor = UkrNavyCard,
                        labelColor = UkrTextPrimary
                    )
                )
            }

            Button(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.testTag("add_homework_button")
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Додати ДЗ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        // List of Homework
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(displayedList, key = { it.id }) { hw ->
                HomeworkItemCard(
                    homework = hw,
                    onToggle = { onToggleHomework(hw.id) },
                    onStartAiCheck = {
                        activeAiCheckHomework = hw
                        studentAnswerInput = ""
                    }
                )
            }
        }
    }

    // AI Check Modal
    if (activeAiCheckHomework != null) {
        val hw = activeAiCheckHomework!!
        AlertDialog(
            onDismissRequest = { if (!isEvaluatingAi) activeAiCheckHomework = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "🤖 AI-перевірка ДЗ", color = UkrFlagYellow, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Предмет: ${hw.subject} • ${hw.taskTitle}",
                        color = UkrTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = hw.description,
                        color = UkrTextSecondary,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = studentAnswerInput,
                        onValueChange = { studentAnswerInput = it },
                        label = { Text("Введіть ваш розв'язок або відповідь:") },
                        placeholder = { Text("Наприклад: D = 64 - 48 = 16, x1 = 3, x2 = 1...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .testTag("homework_answer_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    // Mock OCR Photo button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                studentAnswerInput = "2x² - 8x + 6 = 0; D = (-8)² - 4·2·6 = 64 - 48 = 16. √D = 4. x₁ = (8+4)/4 = 3; x₂ = (8-4)/4 = 1. Перевірка за Вієтом: 3·1 = 3 (c/a = 6/2 = 3). Вірно!"
                            }
                        ) {
                            Text("📸 Завантажити фото зошита (OCR)", color = UkrFlagBlueLight, fontSize = 11.sp)
                        }
                    }

                    if (isEvaluatingAi) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(22.dp), color = UkrFlagYellow)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("ШІ перевіряє розв'язок за шкалою 1-12...", color = UkrFlagYellow, fontSize = 12.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onEvaluateWithAi(hw.id, studentAnswerInput.ifBlank { "Розв'язок виконано згідно правил" })
                        activeAiCheckHomework = null
                    },
                    enabled = !isEvaluatingAi,
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                    modifier = Modifier.testTag("submit_ai_check_button")
                ) {
                    Text("Перевірити ШІ (+10⭐)", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { activeAiCheckHomework = null }, enabled = !isEvaluatingAi) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // Add Homework Dialog
    if (showAddDialog) {
        var subject by remember { mutableStateOf("Алгебра") }
        var title by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        var deadline by remember { mutableStateOf("Завтра") }
        var priority by remember { mutableStateOf(Priority.NORMAL) }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Додати нове ДЗ", color = UkrTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Предмет") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Номери завдань (напр. № 542)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = desc,
                        onValueChange = { desc = it },
                        label = { Text("Опис") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = deadline,
                        onValueChange = { deadline = it },
                        label = { Text("Термін здачі") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            onAddHomework(subject, title, desc, priority, deadline, "стор. 110")
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue)
                ) {
                    Text("Зберегти", color = UkrTextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}

@Composable
fun HomeworkItemCard(
    homework: Homework,
    onToggle: () -> Unit,
    onStartAiCheck: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (homework.isDone) UkrNavySurface else UkrNavyCard
        ),
        border = BorderStroke(
            1.dp,
            if (homework.isDone) UkrGreenSuccess.copy(alpha = 0.5f) else UkrNavyBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hw_card_${homework.id}")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = homework.isDone,
                        onCheckedChange = { onToggle() },
                        colors = CheckboxDefaults.colors(
                            checkedColor = UkrGreenSuccess,
                            checkmarkColor = UkrNavyBackground,
                            uncheckedColor = UkrTextSecondary
                        ),
                        modifier = Modifier.testTag("hw_checkbox_${homework.id}")
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column {
                        Text(
                            text = "${homework.subject} • ${homework.taskTitle}",
                            color = if (homework.isDone) UkrTextSecondary else UkrTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Термін: ${homework.deadline} | ${homework.textbookPages}",
                            color = UkrTextMuted,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(homework.priority.colorHex).copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = homework.priority.title,
                        color = Color(homework.priority.colorHex),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = homework.description,
                color = UkrTextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp)
            )

            // AI Evaluation details if available
            homework.aiEvaluation?.let { eval ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(UkrNavyCardElevated)
                        .padding(10.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🤖 Перевірено AI: оцінка ${eval.grade}/12 балів",
                                color = UkrFlagYellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "+${eval.pointsEarned}⭐",
                                color = UkrGreenSuccess,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = eval.summary,
                            color = UkrTextPrimary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "💡 Порада: ${eval.advice}",
                            color = UkrFlagBlueLight,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // AI Check button if not yet checked
            if (homework.aiEvaluation == null && !homework.isDone) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onStartAiCheck,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UkrNavySurface,
                            contentColor = UkrFlagYellow
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        border = BorderStroke(1.dp, UkrFlagYellow.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Перевірити з AI", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
