package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SampleData
import com.example.model.DayFormat
import com.example.model.Lesson
import com.example.model.QuickStatusOption
import com.example.ui.components.OnlineLessonDialog
import com.example.ui.theme.*


@Composable
fun StudentHomeScreen(
    countdownSeconds: Int,
    selectedDayIndex: Int,
    dayFormat: DayFormat,
    activeQuickStatus: QuickStatusOption?,
    activePairingCode: String? = null,
    onSelectDayIndex: (Int) -> Unit,
    onSetQuickStatus: (QuickStatusOption) -> Unit,
    onNavigateToTab: (String) -> Unit
) {
    val context = LocalContext.current
    var showLessonDetailModal by remember { mutableStateOf<Lesson?>(null) }
    var activeOnlineLessonModal by remember { mutableStateOf<Lesson?>(null) }

    // Pulsating animation for active lesson indicator
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Format countdown
    val minutes = countdownSeconds / 60
    val seconds = countdownSeconds % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    val currentDaySchedule = SampleData.getScheduleForDay(selectedDayIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero School Banner Card
        Card(
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            border = BorderStroke(1.dp, UkrFlagYellow.copy(alpha = 0.4f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_school_banner),
                    contentDescription = "Шкільний банер",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xEE0A192F))
                            )
                        )
                )
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = "Привіт, Андрію! 👋",
                            color = UkrTextPrimary,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "8-А клас • Ліцей №15 «Лідер»",
                            color = UkrFlagYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavyCardElevated.copy(alpha = 0.9f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "🇺🇦 Освіта — сила!",
                            color = UkrTextPrimary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Active Child Pairing Request Banner (When parent/tutor sends link request)
        if (activePairingCode != null) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
                border = BorderStroke(1.5.dp, UkrFlagYellow),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .testTag("student_pairing_code_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(UkrFlagYellow.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔑", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Запит на підключення від батьків/репетитора",
                            color = UkrTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Повідомте цей 6-значний код:",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = activePairingCode,
                            color = UkrFlagYellow,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }
        }

        // Week Ribbon
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            SampleData.weekDays.forEachIndexed { index, (dayName, dateStr) ->
                val isSelected = index == selectedDayIndex
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) UkrFlagBlue else UkrNavyCard)
                        .clickable { onSelectDayIndex(index) }
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                        .testTag("day_chip_$index"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = dayName,
                            color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = dateStr,
                            color = if (isSelected) UkrFlagYellow else UkrTextMuted,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Quick Status Notification Floating Bar
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
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
                    Text(
                        text = "📍 Швидкі статуси для батьків",
                        color = UkrTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    activeQuickStatus?.let {
                        Text(
                            text = "Поточний: ${it.emoji} ${it.title}",
                            color = UkrFlagYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SampleData.quickStatuses.forEach { statusOption ->
                        val isActive = activeQuickStatus?.id == statusOption.id
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isActive) UkrFlagYellow else UkrNavyCard)
                                .clickable { onSetQuickStatus(statusOption) }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .testTag("quick_status_${statusOption.id}")
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = statusOption.emoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = statusOption.title,
                                    color = if (isActive) UkrNavyBackground else UkrTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+${statusOption.bonusStars}⭐",
                                    color = if (isActive) UkrNavyBackground else UkrFlagYellow,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Active Lesson Card: "Урок прямо зараз"
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagYellow.copy(alpha = 0.8f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("active_lesson_card")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Top status indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(UkrGreenSuccess.copy(alpha = pulseAlpha))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Урок прямо зараз",
                            color = UkrGreenSuccess,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavySurface)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "09:00 – 09:45",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Lesson details and Timer
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left info
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(UkrFlagBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "√x",
                                color = UkrTextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Алгебра",
                                color = UkrTextPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "👤 Олег Петренко",
                                color = UkrTextSecondary,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Кабінет 304, 3 поверх",
                                color = UkrFlagYellowLight,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Підручник: стор. 112–115",
                                color = UkrTextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Countdown Display
                    Column(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = "До кінця уроку:",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = timeFormatted,
                            color = UkrFlagYellow,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "хв : сек",
                            color = UkrTextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // Topic badge
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(UkrNavySurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Тема: Квадратні рівняння та теорема Вієта",
                        color = UkrTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 1-Click Meeting Launch Button
                Button(
                    onClick = {
                        activeOnlineLessonModal = SampleData.currentLesson
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UkrFlagBlue,
                        contentColor = UkrTextPrimary
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("join_meet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.VideoCall,
                        contentDescription = null,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "📹 Увійти в Google Meet / Zoom",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        // Next Lesson Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(UkrPurple.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🏛️",
                            fontSize = 22.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "⏰ Наступний урок",
                                color = UkrFlagYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "Історія України",
                            color = UkrTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Кабінет 106 | Підручник: стор. 55–62",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Початок через: 1:12:32",
                            color = UkrFlagBlueLight,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                OutlinedButton(
                    onClick = { showLessonDetailModal = SampleData.nextLesson },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrTextPrimary),
                    border = BorderStroke(1.dp, UkrNavyBorderActive)
                ) {
                    Text(text = "Деталі >", fontSize = 12.sp)
                }
            }
        }

        // Full Daily Schedule Timeline
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📋 Розклад уроків на обраний день",
                    color = UkrTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${currentDaySchedule.size} уроків",
                    color = UkrFlagYellow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            currentDaySchedule.forEachIndexed { idx, lesson ->
                val isCurrent = lesson.id == SampleData.currentLesson.id
                val orderNum = idx + 1
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCurrent) UkrNavyCardElevated else UkrNavyCard
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isCurrent) UkrFlagYellow else UkrNavyBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showLessonDetailModal = lesson }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(UkrNavySurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = lesson.iconEmoji, fontSize = 18.sp)
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$orderNum. ${lesson.subject}",
                                        color = UkrTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (isCurrent) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(UkrGreenSuccess)
                                                .padding(horizontal = 6.dp, vertical = 1.dp)
                                        ) {
                                            Text("Зараз", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Text(
                                    text = "${lesson.teacher} • ${lesson.room}",
                                    color = UkrTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${lesson.startTime} - ${lesson.endTime}",
                                color = UkrFlagYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (lesson.isOnline) {
                                TextButton(
                                    onClick = { activeOnlineLessonModal = lesson },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text("📹 Увійти", color = UkrFlagBlueLight, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Four Key Stat Cards (Grid matching screenshot)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = "✅",
                    title = "Виконані завдання",
                    value = "3 з 5",
                    accentColor = UkrGreenSuccess,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("дз") }
                )
                StatCard(
                    icon = "🏆",
                    title = "Ударний темп",
                    value = "12 днів",
                    accentColor = UkrFlagYellow,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("нагороди") }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatCard(
                    icon = "📊",
                    title = "Середній бал",
                    value = "10.6",
                    accentColor = UkrFlagBlueLight,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("оцінки") }
                )
                StatCard(
                    icon = "🎯",
                    title = "Неперевірені",
                    value = "2 завдання",
                    accentColor = UkrRedUrgent,
                    modifier = Modifier.weight(1f),
                    onClick = { onNavigateToTab("дз") }
                )
            }
        }

        // Smart Actions Quick Links
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "🚀 Швидкі інструменти учня",
                    color = UkrTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigateToTab("рюкзак") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrNavyCard),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🎒 Збери рюкзак", fontSize = 12.sp, color = UkrTextPrimary)
                    }
                    Button(
                        onClick = { onNavigateToTab("репетитор") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrNavyCard),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🤖 AI-Учитель", fontSize = 12.sp, color = UkrFlagYellow)
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onNavigateToTab("нмт") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrNavyCard),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🎓 Тренажер НМТ", fontSize = 12.sp, color = UkrFlagBlueLight)
                    }
                    Button(
                        onClick = { onNavigateToTab("ігри") },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrNavyCard),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(text = "🎮 Міні-ігри (+⭐)", fontSize = 12.sp, color = UkrGreenSuccess)
                    }
                }
            }
        }
    }

    if (showLessonDetailModal != null) {
        val l = showLessonDetailModal!!
        AlertDialog(
            onDismissRequest = { showLessonDetailModal = null },
            title = {
                Text(
                    text = "${l.iconEmoji} ${l.subject}",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Вчитель: ${l.teacher}", color = UkrTextSecondary)
                    Text("Розклад: ${l.startTime} – ${l.endTime}", color = UkrTextSecondary)
                    Text("Розташування: ${l.room}, ${l.floor}", color = UkrFlagYellow)
                    Text("Підручник: ${l.textbookPages}", color = UkrTextSecondary)
                    Text("Тема: ${l.topic}", color = UkrTextPrimary, fontWeight = FontWeight.SemiBold)
                }
            },
            confirmButton = {
                TextButton(onClick = { showLessonDetailModal = null }) {
                    Text("Закрити", color = UkrFlagYellow)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    if (activeOnlineLessonModal != null) {
        OnlineLessonDialog(
            lesson = activeOnlineLessonModal!!,
            onDismiss = { activeOnlineLessonModal = null }
        )
    }
}

@Composable
fun StatCard(
    icon: String,
    title: String,
    value: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
        border = BorderStroke(1.dp, UkrNavyBorder),
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    color = UkrTextSecondary,
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = value,
                    color = accentColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(text = icon, fontSize = 20.sp)
        }
    }
}
