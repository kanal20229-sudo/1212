package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ai.AiService
import com.example.ui.theme.*

import kotlinx.coroutines.launch

data class TutorMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: String = "Щойно"
)

@Composable
fun AiTutorScreen() {
    val coroutineScope = rememberCoroutineScope()
    var depthLevel by remember { mutableStateOf("Детально") }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val messages = remember {
        mutableStateListOf(
            TutorMessage(
                "msg_intro",
                "👋 Привіт! Я твій персональний AI-репетитор з української шкільної програми та НМТ. Можу пояснити будь-яку тему простими словами, навести приклади або розібрати складні формули. Обери тему або запитай мене!",
                false
            )
        )
    }

    val quickTopics = listOf(
        "Теорема Вієта",
        "Апостроф в українській мові",
        "Запорізька Січ",
        "Закон Архімеда",
        "Present Perfect Continuous"
    )

    fun sendQuestion(query: String) {
        if (query.isBlank()) return
        messages.add(TutorMessage("usr_${System.currentTimeMillis()}", query, true))
        inputText = ""
        isLoading = true

        coroutineScope.launch {
            val response = AiService.askTutor(query, depthLevel, emptyList())
            messages.add(TutorMessage("bot_${System.currentTimeMillis()}", response, false))
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Header & Depth Switcher
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.img_tutor_mascot),
                            contentDescription = "AI-Учитель Сова",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Рівень:",
                            color = UkrTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        listOf("Просто", "Детально", "Поглиблено").forEach { level ->
                            val isSel = depthLevel == level
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSel) UkrFlagYellow else UkrNavySurface)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = level,
                                    color = if (isSel) UkrNavyBackground else UkrTextSecondary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Quick Topic Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickTopics.forEach { topic ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(UkrNavySurface)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            TextButton(
                                onClick = { sendQuestion(topic) },
                                contentPadding = PaddingValues(0.dp)
                            ) {
                                Text(text = "✨ $topic", color = UkrFlagBlueLight, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Card(
                        shape = RoundedCornerShape(
                            topStart = 14.dp,
                            topEnd = 14.dp,
                            bottomStart = if (msg.isUser) 14.dp else 2.dp,
                            bottomEnd = if (msg.isUser) 2.dp else 14.dp
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = if (msg.isUser) UkrFlagBlue else UkrNavyCard
                        ),
                        border = BorderStroke(
                            1.dp,
                            if (msg.isUser) UkrFlagBlue else UkrNavyBorder
                        ),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = msg.text,
                                color = UkrTextPrimary,
                                fontSize = 13.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            if (isLoading) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = UkrFlagYellow)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AI-репетитор формує детальне пояснення...", color = UkrFlagYellow, fontSize = 12.sp)
                    }
                }
            }
        }

        // Input Field
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Запитай про формулу чи правило...") },
                modifier = Modifier.weight(1f).testTag("tutor_query_input"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = UkrFlagYellow,
                    unfocusedBorderColor = UkrNavyBorder,
                    focusedTextColor = UkrTextPrimary,
                    unfocusedTextColor = UkrTextPrimary
                ),
                shape = RoundedCornerShape(14.dp)
            )

            IconButton(
                onClick = { sendQuestion(inputText) },
                enabled = inputText.isNotBlank() && !isLoading,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(if (inputText.isNotBlank()) UkrFlagYellow else UkrNavyCard)
                    .testTag("send_tutor_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Надіслати",
                    tint = if (inputText.isNotBlank()) UkrNavyBackground else UkrTextMuted
                )
            }
        }
    }
}
