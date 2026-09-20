package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Star
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
import com.example.model.GradeEntry
import com.example.model.SubjectReport
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun GradebookScreen(
    currentRole: UserRole,
    subjectReports: List<SubjectReport>,
    onAddGrade: (subject: String, grade: Int, type: String, comment: String) -> Unit,
    onRequestAiDiagnostic: (subject: String, onResult: (String) -> Unit) -> Unit = { s, cb ->
        cb("ШІ-Діагностика: середній бал з $s становить 11.2/12. Рекомендовано закріпити задачі підвищеної складності.")
    }
) {
    var selectedSubjectDetail by remember { mutableStateOf<SubjectReport?>(null) }
    var showAddGradeDialog by remember { mutableStateOf(false) }
    var showAiDiagnosticDialog by remember { mutableStateOf(false) }
    var aiDiagnosticText by remember { mutableStateOf("") }
    var isGeneratingDiagnostic by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("I семестр") }

    // Dialog form state
    var formSubject by remember { mutableStateOf(subjectReports.firstOrNull()?.subject ?: "Алгебра") }
    var formGrade by remember { mutableIntStateOf(11) }
    var formType by remember { mutableStateOf("Самостійна") }
    var formComment by remember { mutableStateOf("Гарне володіння матеріалом") }

    val overallAverage = if (subjectReports.isNotEmpty()) {
        Math.round(subjectReports.map { it.averageGrade }.average() * 10.0) / 10.0
    } else 10.5

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Overall GPA Summary Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagYellow.copy(alpha = 0.8f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "📊 Електронний табель оцінок",
                            color = UkrTextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Державний 12-бальний стандарт України",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(UkrFlagYellow)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "$overallAverage / 12",
                            color = UkrNavyBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }

                // Semester Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("I семестр", "II семестр", "Річна").forEach { period ->
                        val isSelected = selectedPeriod == period
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) UkrFlagBlue else UkrNavySurface)
                                .clickable { selectedPeriod = period }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = period,
                                color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // AI Diagnostic Button
                OutlinedButton(
                    onClick = {
                        showAiDiagnosticDialog = true
                        isGeneratingDiagnostic = true
                        onRequestAiDiagnostic("Усі навчальні предмети") { res ->
                            aiDiagnosticText = res
                            isGeneratingDiagnostic = false
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, UkrFlagYellow),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrFlagYellow),
                    modifier = Modifier.fillMaxWidth().testTag("ai_diagnostic_button")
                ) {
                    Text("✨ ШІ-Аналітика та виявлення прогалин у знаннях", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                // Teacher Add Grade Action
                if (currentRole == UserRole.TEACHER || currentRole == UserRole.TUTOR) {
                    Button(
                        onClick = { showAddGradeDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UkrFlagYellow,
                            contentColor = UkrNavyBackground
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Поставити оцінку в журнал", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Subjects Grade List
        Text(
            text = "Навчальні предмети (${subjectReports.size})",
            color = UkrTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        subjectReports.forEach { report ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
                border = BorderStroke(1.dp, UkrNavyBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedSubjectDetail = report }
                    .testTag("grade_subject_${report.subject}")
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(UkrNavySurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = report.iconEmoji, fontSize = 20.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = report.subject,
                                    color = UkrTextPrimary,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = report.teacher,
                                    color = UkrTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Average badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    when {
                                        report.averageGrade >= 10.0 -> UkrGreenSuccess.copy(alpha = 0.2f)
                                        report.averageGrade >= 7.0 -> UkrFlagYellow.copy(alpha = 0.2f)
                                        else -> UkrRedUrgent.copy(alpha = 0.2f)
                                    }
                                )
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Сер: ${report.averageGrade}",
                                color = when {
                                    report.averageGrade >= 10.0 -> UkrGreenSuccess
                                    report.averageGrade >= 7.0 -> UkrFlagYellow
                                    else -> UkrRedUrgent
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Grade bubbles
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Оцінки:",
                            color = UkrTextMuted,
                            fontSize = 11.sp
                        )
                        report.grades.forEach { entry ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            entry.value >= 10 -> UkrFlagBlue
                                            entry.value >= 7 -> UkrNavySurface
                                            else -> UkrRedUrgent.copy(alpha = 0.5f)
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${entry.value}",
                                    color = UkrTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Modal for a single subject
    if (selectedSubjectDetail != null) {
        val subject = selectedSubjectDetail!!
        AlertDialog(
            onDismissRequest = { selectedSubjectDetail = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = subject.iconEmoji, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = subject.subject,
                        color = UkrTextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Вчитель: ${subject.teacher}",
                        color = UkrTextSecondary,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "Середній бал за семестр: ${subject.averageGrade} / 12",
                        color = UkrFlagYellow,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(color = UkrNavyBorder)

                    Text(
                        text = "Деталізація поточних оцінок:",
                        color = UkrTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    subject.grades.forEach { entry ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = entry.type,
                                            color = UkrFlagBlueLight,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = entry.date,
                                            color = UkrTextMuted,
                                            fontSize = 11.sp
                                        )
                                    }
                                    if (entry.teacherComment.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = entry.teacherComment,
                                            color = UkrTextSecondary,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(if (entry.value >= 10) UkrFlagYellow else UkrFlagBlue),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${entry.value}",
                                        color = if (entry.value >= 10) UkrNavyBackground else UkrTextPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedSubjectDetail = null }) {
                    Text("Закрити", color = UkrFlagYellow)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // Add Grade Dialog for Teachers
    if (showAddGradeDialog) {
        AlertDialog(
            onDismissRequest = { showAddGradeDialog = false },
            title = {
                Text(
                    text = "✍️ Поставити оцінку в журнал",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("Предмет:", color = UkrTextSecondary, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        subjectReports.map { it.subject }.forEach { sub ->
                            val isSel = formSubject == sub
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) UkrFlagBlue else UkrNavySurface)
                                    .clickable { formSubject = sub }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = sub,
                                    color = if (isSel) UkrTextPrimary else UkrTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    Text("Бал (1–12):", color = UkrTextSecondary, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        (1..12).forEach { g ->
                            val isSel = formGrade == g
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isSel) UkrFlagYellow else UkrNavySurface)
                                    .clickable { formGrade = g },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$g",
                                    color = if (isSel) UkrNavyBackground else UkrTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Text("Тип роботи:", color = UkrTextSecondary, fontSize = 12.sp)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Відповідь", "Самостійна", "Контрольна", "ДЗ", "Тематична").forEach { t ->
                            val isSel = formType == t
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) UkrFlagBlue else UkrNavySurface)
                                    .clickable { formType = t }
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = t,
                                    color = if (isSel) UkrTextPrimary else UkrTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = formComment,
                        onValueChange = { formComment = it },
                        label = { Text("Коментар вчителя") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary,
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddGrade(formSubject, formGrade, formType, formComment)
                        showAddGradeDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                ) {
                    Text("Зберегти оцінку", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGradeDialog = false }) {
                    Text("Скасувати", color = UkrTextMuted)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    if (showAiDiagnosticDialog) {
        AlertDialog(
            onDismissRequest = { showAiDiagnosticDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "✨ ШІ-Аналітика успішності (Gemini)", color = UkrTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
                ) {
                    if (isGeneratingDiagnostic) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = UkrFlagYellow)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = "Аналізую оцінки та динаміку успішності...", color = UkrTextSecondary, fontSize = 13.sp)
                        }
                    } else {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = aiDiagnosticText.ifEmpty { "Аналіз успішності завершено. Рекомендовано повторити властивості функцій з алгебри та лексику Unit 4 з англійської." },
                                    color = UkrTextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAiDiagnosticDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                ) {
                    Text("Зрозуміло", fontWeight = FontWeight.Bold)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}
