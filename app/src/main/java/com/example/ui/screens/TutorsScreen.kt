package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TutorProfile
import com.example.ui.theme.*

@Composable
fun TutorsScreen(
    tutorsList: List<TutorProfile>,
    studentCode: String,
    onAddTutor: (TutorProfile) -> Unit,
    onOpenChat: (String) -> Unit
) {
    val context = LocalContext.current
    var showAddTutorDialog by remember { mutableStateOf(false) }
    var selectedSubjectFilter by remember { mutableStateOf<String?>(null) }

    // Dialog state
    var newTutorName by remember { mutableStateOf("") }
    var newTutorSubject by remember { mutableStateOf("Хімія") }
    var newTutorPhone by remember { mutableStateOf("+380 50 123 4567") }
    var newTutorRate by remember { mutableStateOf("350 грн/год") }

    val subjects = listOf("Всі", "Математика / Алгебра", "Англійська мова", "Фізика", "Українська мова", "Історія України")

    val displayedTutors = remember(tutorsList, selectedSubjectFilter) {
        if (selectedSubjectFilter == null || selectedSubjectFilter == "Всі") tutorsList
        else tutorsList.filter { it.subject.contains(selectedSubjectFilter!!, ignoreCase = true) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagYellow.copy(alpha = 0.7f)),
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
                    Column {
                        Text(
                            text = "🧑‍🏫 Репетитори за предметами",
                            color = UkrTextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Індивідуальні заняття, підготовка до НМТ та ДПА",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Button(
                        onClick = { showAddTutorDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("add_tutor_button")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "+ Додати", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Student Linking Code Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(UkrNavySurface)
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔑", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Мій персональний код учня:",
                                    color = UkrTextSecondary,
                                    fontSize = 11.sp
                                )
                                Text(
                                    text = studentCode,
                                    color = UkrFlagYellow,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp
                                )
                            }
                        }
                        Text(
                            text = "Для зв'язку з репетитором",
                            color = UkrTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }

        // Subject Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            subjects.take(3).forEach { subj ->
                val isSelected = (selectedSubjectFilter == null && subj == "Всі") || selectedSubjectFilter == subj
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedSubjectFilter = if (subj == "Всі") null else subj },
                    label = { Text(subj, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = UkrFlagBlue,
                        containerColor = UkrNavyCard,
                        labelColor = UkrTextPrimary
                    )
                )
            }
        }

        // Tutors List
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(displayedTutors, key = { it.id }) { tutor ->
                TutorCardItem(
                    tutor = tutor,
                    onJoinMeet = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(tutor.meetUrl))
                        context.startActivity(intent)
                    },
                    onOpenChat = { onOpenChat(tutor.name) }
                )
            }
        }
    }

    // Dialog: Add Tutor
    if (showAddTutorDialog) {
        AlertDialog(
            onDismissRequest = { showAddTutorDialog = false },
            title = {
                Text(text = "🧑‍🏫 Додати репетитора за предметом", color = UkrTextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = newTutorName,
                        onValueChange = { newTutorName = it },
                        label = { Text("ПІБ репетитора") },
                        placeholder = { Text("Наприклад: Віктор Коваленко") },
                        modifier = Modifier.fillMaxWidth().testTag("new_tutor_name_input")
                    )

                    OutlinedTextField(
                        value = newTutorSubject,
                        onValueChange = { newTutorSubject = it },
                        label = { Text("Предмет") },
                        placeholder = { Text("Наприклад: Хімія / Біологія") },
                        modifier = Modifier.fillMaxWidth().testTag("new_tutor_subject_input")
                    )

                    OutlinedTextField(
                        value = newTutorPhone,
                        onValueChange = { newTutorPhone = it },
                        label = { Text("Телефон або Telegram") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = newTutorRate,
                        onValueChange = { newTutorRate = it },
                        label = { Text("Вартість (грн/год)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newTutorName.isNotBlank()) {
                            onAddTutor(
                                TutorProfile(
                                    id = "tut_${System.currentTimeMillis()}",
                                    name = newTutorName.trim(),
                                    subject = newTutorSubject.trim(),
                                    phone = newTutorPhone.trim(),
                                    rating = 5.0,
                                    hourlyRate = newTutorRate.trim(),
                                    isOnlineLessonReady = true,
                                    nextLessonTime = "За розкладом",
                                    meetUrl = "https://meet.google.com/new-tutor-ukr",
                                    avatarEmoji = "🧑‍🏫",
                                    bio = "Індивідуальні онлайн-заняття"
                                )
                            )
                            showAddTutorDialog = false
                            newTutorName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                    modifier = Modifier.testTag("confirm_add_tutor_button")
                ) {
                    Text("Зберегти", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTutorDialog = false }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}

@Composable
fun TutorCardItem(
    tutor: TutorProfile,
    onJoinMeet: () -> Unit,
    onOpenChat: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
        border = BorderStroke(1.dp, UkrNavyBorder),
        modifier = Modifier.fillMaxWidth().testTag("tutor_card_${tutor.id}")
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
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(UkrNavySurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = tutor.avatarEmoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = tutor.name,
                            color = UkrTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Предмет: ${tutor.subject}",
                            color = UkrFlagYellow,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(UkrGreenSuccess.copy(alpha = 0.2f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "⭐ ${tutor.rating}", color = UkrGreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Text(
                text = tutor.bio,
                color = UkrTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            // Next lesson & rate
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(UkrNavySurface)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, contentDescription = null, tint = UkrFlagYellow, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Урок: ${tutor.nextLessonTime}",
                        color = UkrTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Text(
                    text = tutor.hourlyRate,
                    color = UkrTextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onJoinMeet,
                    modifier = Modifier.weight(1f).testTag("join_tutor_meet_${tutor.id}"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue, contentColor = UkrTextPrimary),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Videocam, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Google Meet", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onOpenChat,
                    modifier = Modifier.weight(1f).testTag("chat_tutor_${tutor.id}"),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, UkrNavyBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrTextPrimary),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Чат", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
