package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.StudentSubmission
import com.example.ui.theme.*

@Composable
fun TeacherDashboardScreen(
    selectedClass: String,
    submissions: List<StudentSubmission>,
    onSelectClass: (String) -> Unit,
    onGradeSubmission: (String, Int, String) -> Unit,
    onAssignHomework: (String, String, String) -> Unit,
    onInitiateStudentLink: (name: String, surname: String) -> String = { _, _ -> "839214" },
    onConfirmStudentLink: (code: String, name: String, surname: String) -> Boolean = { _, _, _ -> true },
    onRequestAiTasks: (subject: String, topic: String, grade: String, onResult: (String) -> Unit) -> Unit = { _, _, _, cb ->
        cb("1. Знайдіть корені рівняння x² - 5x + 6 = 0\n2. Розв'яжіть через дискримінант 2x² + 3x - 5 = 0\n3. Складіть квадратне рівняння, корені якого дорівнюють 3 і -4.")
    }
) {
    val context = LocalContext.current
    var showAssignDialog by remember { mutableStateOf(false) }
    var showLinkStudentDialog by remember { mutableStateOf(false) }
    var showAiTaskGeneratorDialog by remember { mutableStateOf(false) }

    var checkingSubmission by remember { mutableStateOf<StudentSubmission?>(null) }
    var enteredGrade by remember { mutableStateOf("11") }
    var enteredTeacherComment by remember { mutableStateOf("Чудова робота!") }

    // Student linking dialog state
    var linkStudentStep by remember { mutableIntStateOf(1) }
    var linkStudentFirstName by remember { mutableStateOf("") }
    var linkStudentLastName by remember { mutableStateOf("") }
    var linkStudentGeneratedCode by remember { mutableStateOf("") }
    var linkStudentEnteredCode by remember { mutableStateOf("") }

    // AI generator state
    var aiSubjectInput by remember { mutableStateOf("Алгебра") }
    var aiTopicInput by remember { mutableStateOf("Квадратні рівняння та теорема Вієта") }
    var aiGeneratedResult by remember { mutableStateOf("") }
    var isGeneratingAiTasks by remember { mutableStateOf(false) }

    val classes = listOf("7-А клас (24)", "8-Б клас (26)", "9-А клас (28)", "Репетиторство (3)")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Class Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            classes.forEach { cName ->
                val base = cName.substringBefore(" (")
                val isSelected = selectedClass.startsWith(base)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) UkrFlagBlue else UkrNavyCard)
                        .clickable { onSelectClass(base) }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                        .testTag("class_tab_$base")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Groups,
                            contentDescription = null,
                            tint = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = cName,
                            color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Action: Assign Homework & AI Tools
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📚 Домашнє завдання для $selectedClass",
                            color = UkrTextPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Прив'язка до тем, AI-генерація та зв'язок з учнями",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                    }

                    Button(
                        onClick = { showAssignDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Видати ДЗ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { showAiTaskGeneratorDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, UkrFlagYellow.copy(alpha = 0.8f)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrFlagYellow),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("✨ ШІ-Генератор завдань", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showLinkStudentDialog = true },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        border = BorderStroke(1.dp, UkrFlagBlueLight),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrFlagBlueLight),
                        contentPadding = PaddingValues(vertical = 6.dp)
                    ) {
                        Text("🔑 Підключити учня", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Next Teaching Session Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.dp, UkrFlagBlue.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "📹", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Найближче заняття (Сьогодні, 18:30)",
                            color = UkrFlagYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Text(text = "7-А клас", color = UkrTextSecondary, fontSize = 12.sp)
                }

                Text(
                    text = "Алгебра: «Розв'язування дробово-раціональних рівнянь»",
                    color = UkrTextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Підручник: стор. 112–115 • Платформа: Zoom / Google Meet",
                    color = UkrTextSecondary,
                    fontSize = 12.sp
                )

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://meet.google.com/xyz-algebra-ukr"))
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Увійти в зустріч онлайн >", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Submissions for Review
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📝 Відповіді учнів на перевірку",
                            color = UkrTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(UkrRedUrgent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${submissions.size}",
                                color = UkrTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                submissions.forEach { sub ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(UkrNavySurface)
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(UkrNavyCardElevated),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "📸", fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "${sub.studentName} (${sub.className})",
                                    color = UkrTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${sub.subject} • ${sub.taskTitle}",
                                    color = UkrTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = sub.teacherGrade?.let { "Оцінено: $it/12" } ?: "🤖 AI пропонує: ${sub.aiGradeSuggestion}/12",
                                    color = if (sub.teacherGrade != null) UkrGreenSuccess else UkrFlagYellow,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Button(
                            onClick = {
                                checkingSubmission = sub
                                enteredGrade = (sub.teacherGrade ?: sub.aiGradeSuggestion).toString()
                                enteredTeacherComment = sub.teacherComment ?: "Відмінно, розв'язано вірно!"
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "Перевірити >", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    // Checking Submission Modal with AI assistance
    if (checkingSubmission != null) {
        val sub = checkingSubmission!!
        AlertDialog(
            onDismissRequest = { checkingSubmission = null },
            title = {
                Text(
                    text = "Перевірка: ${sub.studentName}",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Завдання: ${sub.subject} • ${sub.taskTitle}",
                        color = UkrFlagYellow,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text("Надісланий розв'язок:", color = UkrTextSecondary, fontSize = 12.sp)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavySurface)
                            .padding(10.dp)
                    ) {
                        Text(text = sub.solutionText, color = UkrTextPrimary, fontSize = 12.sp)
                    }

                    // AI Assistant Summary
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavyCardElevated)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text(
                                text = "🤖 Аналіз ШІ (Рекомендація: ${sub.aiGradeSuggestion}/12)",
                                color = UkrGreenSuccess,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = sub.aiEvaluationSummary,
                                color = UkrTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = enteredGrade,
                            onValueChange = { enteredGrade = it },
                            label = { Text("Оцінка (1-12)") },
                            modifier = Modifier.width(110.dp)
                        )
                        OutlinedTextField(
                            value = enteredTeacherComment,
                            onValueChange = { enteredTeacherComment = it },
                            label = { Text("Коментар вчителя") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val grade = enteredGrade.toIntOrNull() ?: 11
                        onGradeSubmission(sub.id, grade, enteredTeacherComment)
                        checkingSubmission = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrGreenSuccess, contentColor = UkrNavyBackground)
                ) {
                    Text("Виставити оцінку", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { checkingSubmission = null }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // Assign Homework Dialog
    if (showAssignDialog) {
        var topic by remember { mutableStateOf("") }
        var task by remember { mutableStateOf("") }
        var pages by remember { mutableStateOf("стор. 115") }

        AlertDialog(
            onDismissRequest = { showAssignDialog = false },
            title = { Text("Видати ДЗ для $selectedClass", color = UkrTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Тема уроку") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = task,
                        onValueChange = { task = it },
                        label = { Text("Опис завдань (№ 548, 550)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = pages,
                        onValueChange = { pages = it },
                        label = { Text("Підручник і сторінки") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (task.isNotBlank()) {
                            onAssignHomework(topic, task, pages)
                            showAssignDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                ) {
                    Text("Опублікувати для класу", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAssignDialog = false }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // AI Task Generator Dialog
    if (showAiTaskGeneratorDialog) {
        AlertDialog(
            onDismissRequest = { showAiTaskGeneratorDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✨ ШІ-Генератор завдань (Gemini)", color = UkrTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                ) {
                    OutlinedTextField(
                        value = aiSubjectInput,
                        onValueChange = { aiSubjectInput = it },
                        label = { Text("Предмет") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = aiTopicInput,
                        onValueChange = { aiTopicInput = it },
                        label = { Text("Тема або концепт") },
                        placeholder = { Text("Квадратні рівняння") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            isGeneratingAiTasks = true
                            onRequestAiTasks(aiSubjectInput, aiTopicInput, selectedClass) { res ->
                                aiGeneratedResult = res
                                isGeneratingAiTasks = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isGeneratingAiTasks) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), color = UkrNavyBackground, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(if (isGeneratingAiTasks) "Генерую завдання..." else "🤖 Згенерувати завдання через ШІ", fontWeight = FontWeight.Bold)
                    }

                    if (aiGeneratedResult.isNotBlank()) {
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(text = "Згенеровані завдання:", color = UkrFlagYellow, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(text = aiGeneratedResult, color = UkrTextPrimary, fontSize = 12.sp, lineHeight = 16.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (aiGeneratedResult.isNotBlank()) {
                            onAssignHomework(aiTopicInput, aiGeneratedResult, "Створено ШІ-асистентом")
                            showAiTaskGeneratorDialog = false
                            aiGeneratedResult = ""
                        }
                    },
                    enabled = aiGeneratedResult.isNotBlank(),
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue, contentColor = UkrTextPrimary)
                ) {
                    Text("Видати як ДЗ класу", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAiTaskGeneratorDialog = false }) {
                    Text("Закрити", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // 2-Step Student Linking Dialog for Teacher/Tutor
    if (showLinkStudentDialog) {
        AlertDialog(
            onDismissRequest = {
                showLinkStudentDialog = false
                linkStudentStep = 1
                linkStudentEnteredCode = ""
            },
            title = {
                Text(
                    text = if (linkStudentStep == 1) "🔑 Підключити учня (Крок 1/2)" else "Код підтвердження (Крок 2/2)",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (linkStudentStep == 1) {
                        Text(
                            text = "Введіть ім'я та прізвище учня. Йому в додаток прийде 6-значний код:",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                        OutlinedTextField(
                            value = linkStudentFirstName,
                            onValueChange = { linkStudentFirstName = it },
                            label = { Text("Ім'я учня") },
                            placeholder = { Text("напр. Андрій") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = linkStudentLastName,
                            onValueChange = { linkStudentLastName = it },
                            label = { Text("Прізвище учня") },
                            placeholder = { Text("напр. Коваленко") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Запит надіслано учневі $linkStudentFirstName $linkStudentLastName! Введіть код, який він отримав:",
                            color = UkrFlagYellow,
                            fontSize = 12.sp
                        )
                        OutlinedTextField(
                            value = linkStudentEnteredCode,
                            onValueChange = { linkStudentEnteredCode = it },
                            label = { Text("6-значний код") },
                            placeholder = { Text(linkStudentGeneratedCode) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (linkStudentGeneratedCode.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { linkStudentEnteredCode = linkStudentGeneratedCode },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Вставити код з повідомлення: $linkStudentGeneratedCode",
                                    fontSize = 11.sp,
                                    color = UkrFlagBlueLight
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (linkStudentStep == 1) {
                    Button(
                        onClick = {
                            if (linkStudentFirstName.isNotBlank()) {
                                linkStudentGeneratedCode = onInitiateStudentLink(linkStudentFirstName.trim(), linkStudentLastName.trim())
                                linkStudentStep = 2
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                    ) {
                        Text("Надіслати код учневі", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            val ok = onConfirmStudentLink(linkStudentEnteredCode.trim(), linkStudentFirstName.trim(), linkStudentLastName.trim())
                            if (ok) {
                                showLinkStudentDialog = false
                                linkStudentStep = 1
                                linkStudentFirstName = ""
                                linkStudentLastName = ""
                                linkStudentEnteredCode = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UkrGreenSuccess, contentColor = Color.White)
                    ) {
                        Text("Підтвердити зв'язок", fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showLinkStudentDialog = false
                    linkStudentStep = 1
                }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}
