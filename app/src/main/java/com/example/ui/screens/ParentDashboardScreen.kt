package com.example.ui.screens

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SampleData
import com.example.model.ChildProfile
import com.example.model.DayFormat
import com.example.model.RewardItem
import com.example.model.RewardStatus
import com.example.model.TextbookItem
import com.example.ui.components.PdfReaderDialog
import com.example.ui.theme.*

@Composable
fun ParentDashboardScreen(
    selectedChild: String,
    childrenList: List<ChildProfile> = SampleData.childrenList,
    dayFormat: DayFormat,
    rewards: List<RewardItem>,
    onSelectChild: (String) -> Unit,
    onAddChild: (name: String, className: String, school: String) -> Unit = { _, _, _ -> },
    onInitiateChildLink: (name: String, surname: String) -> String = { _, _ -> "839214" },
    onConfirmChildLink: (code: String, name: String, surname: String) -> Boolean = { _, _, _ -> true },
    onRequestAiAdvice: ((childName: String, onResult: (String) -> Unit) -> Unit)? = null,
    onChangeDayFormat: (DayFormat) -> Unit,
    onApproveReward: (String) -> Unit,
    onRejectReward: (String, String) -> Unit,
    onCreateReward: (String, String, Int) -> Unit
) {
    var showCreateRewardDialog by remember { mutableStateOf(false) }
    var showAddChildDialog by remember { mutableStateOf(false) }
    var linkStep by remember { mutableIntStateOf(1) } // 1: Name/Surname, 2: Code
    var newChildFirstName by remember { mutableStateOf("") }
    var newChildLastName by remember { mutableStateOf("") }
    var generatedChildCode by remember { mutableStateOf("") }
    var enteredVerificationCode by remember { mutableStateOf("") }
    var aiAdviceText by remember { mutableStateOf("💡 ШІ-Порада: Сьогодні у дитини насичений день. Після школи рекомендуємо 30 хв відпочинку без гаджетів перед виконанням ДЗ з геометрії.") }
    var isLoadingAiAdvice by remember { mutableStateOf(false) }

    var selectedPdfBook by remember { mutableStateOf<TextbookItem?>(null) }
    var showRejectDialogForId by remember { mutableStateOf<String?>(null) }
    var rejectionComment by remember { mutableStateOf("Потрібно завершити ДЗ з алгебри") }

    val pendingRequests = rewards.filter { it.status == RewardStatus.PENDING }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Child Switcher Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            childrenList.forEach { childProfile ->
                val isSelected = selectedChild == childProfile.name
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) UkrFlagBlue else UkrNavyCard)
                        .clickable { onSelectChild(childProfile.name) }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("child_tab_${childProfile.name}")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = childProfile.avatarEmoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = childProfile.name,
                                color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = childProfile.className,
                                color = if (isSelected) UkrFlagYellow else UkrTextMuted,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { showAddChildDialog = true },
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, UkrNavyBorderActive),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(text = "+ Додати дитину", color = UkrFlagBlueLight, fontSize = 12.sp)
            }
        }


        // AI Parent Advice Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.dp, UkrFlagYellow.copy(alpha = 0.6f)),
            modifier = Modifier.fillMaxWidth()
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
                        Text(text = "🤖", fontSize = 18.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ШІ-Асистент для батьків",
                            color = UkrFlagYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }

                    if (onRequestAiAdvice != null) {
                        TextButton(
                            onClick = {
                                isLoadingAiAdvice = true
                                onRequestAiAdvice(selectedChild) { advice ->
                                    aiAdviceText = advice
                                    isLoadingAiAdvice = false
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (isLoadingAiAdvice) "Аналізую..." else "Оновити через AI",
                                fontSize = 11.sp,
                                color = UkrFlagBlueLight
                            )
                        }
                    }
                }
                Text(
                    text = aiAdviceText,
                    color = UkrTextPrimary,
                    fontSize = 12.sp,
                    lineHeight = 17.sp
                )
            }
        }

        // Child Live Status Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagYellow.copy(alpha = 0.5f)),
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
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(UkrFlagBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🏫", fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "У школі з 8:15",
                                color = UkrGreenSuccess,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Заняття: Алгебра, Каб. 304, 3 поверх",
                                color = UkrTextPrimary,
                                fontSize = 12.sp
                            )
                        }
                    }

                    // Format Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavySurface)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${dayFormat.emoji} ${dayFormat.title}",
                            color = UkrFlagYellow,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(UkrNavySurface)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Балів цього тижня", color = UkrTextSecondary, fontSize = 10.sp)
                            Text("⭐ 128", color = UkrFlagYellow, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(UkrNavySurface)
                            .padding(10.dp)
                    ) {
                        Column {
                            Text("Ударної активності", color = UkrTextSecondary, fontSize = 10.sp)
                            Text("🔥 12 днів", color = UkrGreenSuccess, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Emergency Format Switcher
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Змінити режим дня:", color = UkrTextSecondary, fontSize = 12.sp)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        DayFormat.values().forEach { f ->
                            val isSel = dayFormat == f
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) UkrFlagBlue else UkrNavySurface)
                                    .clickable { onChangeDayFormat(f) }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "${f.emoji} ${f.title}",
                                    color = if (isSel) UkrTextPrimary else UkrTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Incoming Reward Requests Section
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
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
                        Text(
                            text = "🎁 Запити на нагороди",
                            color = UkrTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(UkrRedUrgent),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${pendingRequests.size}",
                                color = UkrTextPrimary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (pendingRequests.isEmpty()) {
                    Text(
                        text = "Всі поточні запити оброблено. Нових запитів немає.",
                        color = UkrTextSecondary,
                        fontSize = 12.sp
                    )
                } else {
                    pendingRequests.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(UkrNavySurface)
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = item.iconEmoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = item.title,
                                        color = UkrTextPrimary,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${item.costStars}⭐ • ${item.requestedBy ?: "Андрій"} (${item.requestTime ?: "щойно"})",
                                        color = UkrFlagYellow,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = { onApproveReward(item.id) },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UkrGreenSuccess)
                                        .testTag("approve_reward_${item.id}")
                                ) {
                                    Icon(Icons.Default.Check, contentDescription = "Видати", tint = UkrNavyBackground)
                                }

                                IconButton(
                                    onClick = { showRejectDialogForId = item.id },
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UkrRedUrgent)
                                        .testTag("reject_reward_${item.id}")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = "Відхилити", tint = UkrTextPrimary)
                                }
                            }
                        }
                    }
                }

                // Create reward button
                Button(
                    onClick = { showCreateRewardDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Створити нагороду для дитини", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Textbook Library (PDF readers)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
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
                    Text(
                        text = "📚 Бібліотека підручників",
                        color = UkrTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "5 класів • PDF",
                        color = UkrFlagBlueLight,
                        fontSize = 11.sp
                    )
                }

                SampleData.textbooks.take(3).forEach { book ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(UkrNavySurface)
                            .clickable { selectedPdfBook = book }
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "📕", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = book.title,
                                    color = UkrTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${book.author} • ${book.pagesCount} стор. • ${book.pdfSize}",
                                    color = UkrTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = UkrTextSecondary)
                    }
                }
            }
        }

        // Weekly Activity Chart Card
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
                    Text(
                        text = "📊 Активність за тиждень",
                        color = UkrTextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "128 балів за 7 днів",
                        color = UkrFlagYellow,
                        fontSize = 12.sp
                    )
                }

                // Mock Bar Chart
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(90.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    val bars = listOf(
                        Pair("Пн", 60),
                        Pair("Вт", 80),
                        Pair("Ср", 45),
                        Pair("Чт", 95),
                        Pair("Пт", 70),
                        Pair("Сб", 30),
                        Pair("Нд", 20)
                    )
                    bars.forEach { (day, heightPercent) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height((heightPercent * 0.7).dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (day == "Чт") UkrFlagYellow else UkrFlagBlue)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = day, color = UkrTextSecondary, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    // PDF Viewer Dialog
    if (selectedPdfBook != null) {
        val b = selectedPdfBook!!
        AlertDialog(
            onDismissRequest = { selectedPdfBook = null },
            title = {
                Text(text = "📖 ${b.title}", color = UkrTextPrimary, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Автор: ${b.author}", color = UkrTextSecondary)
                    Text("Розмір файлу: ${b.pdfSize} | Сторінок: ${b.pagesCount}", color = UkrTextSecondary)
                    Text("Відкрито сторінку 112 (Квадратні рівняння)", color = UkrFlagYellow, fontWeight = FontWeight.Bold)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(UkrNavySurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "📄 Прев'ю сторінки підручника\n«Формули коренів квадратного рівняння»",
                            color = UkrTextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedPdfBook = null }) {
                    Text("Закрити", color = UkrFlagYellow)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // Create Reward Dialog
    if (showCreateRewardDialog) {
        var title by remember { mutableStateOf("") }
        var emoji by remember { mutableStateOf("🎮") }
        var costText by remember { mutableStateOf("100") }

        AlertDialog(
            onDismissRequest = { showCreateRewardDialog = false },
            title = { Text("Створити нагороду", color = UkrTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Назва нагороди (напр. Похід у зоопарк)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = emoji,
                        onValueChange = { emoji = it },
                        label = { Text("Емодзі (напр. 🦁)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = costText,
                        onValueChange = { costText = it },
                        label = { Text("Вартість у зірках ⭐") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val cost = costText.toIntOrNull() ?: 100
                        if (title.isNotBlank()) {
                            onCreateReward(title, emoji, cost)
                            showCreateRewardDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                ) {
                    Text("Створити", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showCreateRewardDialog = false }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // PDF Reader Modal
    if (selectedPdfBook != null) {
        PdfReaderDialog(
            book = selectedPdfBook!!,
            onDismiss = { selectedPdfBook = null }
        )
    }

    // Add Child Dialog with 2-Step Code Verification
    if (showAddChildDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddChildDialog = false
                linkStep = 1
                enteredVerificationCode = ""
            },
            title = {
                Text(
                    text = if (linkStep == 1) "👶 Додати дитину (Крок 1/2)" else "🔑 Підтвердження кодом (Крок 2/2)",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (linkStep == 1) {
                        Text(
                            text = "Введіть ім'я та прізвище дитини. На її пристрій буде надіслано одноразовий код підтвердження:",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                        OutlinedTextField(
                            value = newChildFirstName,
                            onValueChange = { newChildFirstName = it },
                            label = { Text("Ім'я дитини") },
                            placeholder = { Text("напр. Андрій") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = newChildLastName,
                            onValueChange = { newChildLastName = it },
                            label = { Text("Прізвище дитини") },
                            placeholder = { Text("напр. Коваленко") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Запит надіслано учневі $newChildFirstName $newChildLastName! Введіть 6-значний код, який отримала дитина на своєму екрані:",
                            color = UkrFlagYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        OutlinedTextField(
                            value = enteredVerificationCode,
                            onValueChange = { enteredVerificationCode = it },
                            label = { Text("6-значний код підтвердження") },
                            placeholder = { Text("напр. $generatedChildCode") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (generatedChildCode.isNotEmpty()) {
                            OutlinedButton(
                                onClick = { enteredVerificationCode = generatedChildCode },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "Вставити отриманий код: $generatedChildCode",
                                    fontSize = 11.sp,
                                    color = UkrFlagBlueLight
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                if (linkStep == 1) {
                    Button(
                        onClick = {
                            if (newChildFirstName.isNotBlank()) {
                                generatedChildCode = onInitiateChildLink(newChildFirstName.trim(), newChildLastName.trim())
                                linkStep = 2
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                    ) {
                        Text("Надіслати запит дитині", fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = {
                            val ok = onConfirmChildLink(enteredVerificationCode.trim(), newChildFirstName.trim(), newChildLastName.trim())
                            if (ok) {
                                onAddChild("$newChildFirstName $newChildLastName", "8-А клас", "Ліцей №15")
                                showAddChildDialog = false
                                linkStep = 1
                                newChildFirstName = ""
                                newChildLastName = ""
                                enteredVerificationCode = ""
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
                    showAddChildDialog = false
                    linkStep = 1
                }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    // Reject Reward Dialog
    if (showRejectDialogForId != null) {
        val rewId = showRejectDialogForId!!
        AlertDialog(
            onDismissRequest = { showRejectDialogForId = null },
            title = { Text("Відхилити запит на нагороду", color = UkrRedUrgent, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Вкажіть причину для дитини:", color = UkrTextSecondary, fontSize = 12.sp)
                    OutlinedTextField(
                        value = rejectionComment,
                        onValueChange = { rejectionComment = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onRejectReward(rewId, rejectionComment)
                        showRejectDialogForId = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrRedUrgent)
                ) {
                    Text("Відхилити", color = UkrTextPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRejectDialogForId = null }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}

