package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Lesson
import com.example.ui.theme.*

@Composable
fun OnlineLessonDialog(
    lesson: Lesson,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var isMicOn by remember { mutableStateOf(false) }
    var isCamOn by remember { mutableStateOf(true) }
    var isHandRaised by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
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
                            .background(UkrGreenSuccess)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Онлайн-урок: ${lesson.subject}",
                        color = UkrTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрити", tint = UkrTextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Video Preview Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF071228)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🧑‍🏫", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = lesson.teacher,
                            color = UkrTextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Демонстрація екрану: ${lesson.topic}",
                            color = UkrFlagYellow,
                            fontSize = 11.sp
                        )
                    }

                    // Classroom participant count
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavyCardElevated.copy(alpha = 0.9f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "👥 24 онлайн",
                            color = UkrTextPrimary,
                            fontSize = 11.sp
                        )
                    }
                }

                // Meeting controls (Mic, Cam, Raise Hand)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isMicOn = !isMicOn },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isMicOn) UkrFlagBlue else UkrNavySurface)
                    ) {
                        Icon(
                            imageVector = if (isMicOn) Icons.Default.Mic else Icons.Default.MicOff,
                            contentDescription = "Мікрофон",
                            tint = if (isMicOn) UkrTextPrimary else UkrRedUrgent
                        )
                    }

                    IconButton(
                        onClick = { isCamOn = !isCamOn },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isCamOn) UkrFlagBlue else UkrNavySurface)
                    ) {
                        Icon(
                            imageVector = if (isCamOn) Icons.Default.Videocam else Icons.Default.VideocamOff,
                            contentDescription = "Камера",
                            tint = if (isCamOn) UkrTextPrimary else UkrTextMuted
                        )
                    }

                    IconButton(
                        onClick = { isHandRaised = !isHandRaised },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(if (isHandRaised) UkrFlagYellow else UkrNavySurface)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PanTool,
                            contentDescription = "Підняти руку",
                            tint = if (isHandRaised) UkrNavyBackground else UkrTextPrimary
                        )
                    }
                }

                // Lesson materials info
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📖 Матеріали уроку:",
                            color = UkrFlagYellow,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Підручник: ${lesson.textbookPages}",
                            color = UkrTextPrimary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = "Кабінет у школі: ${lesson.room}, ${lesson.floor}",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }

                // Launch external Meet / Zoom if available
                Button(
                    onClick = {
                        val url = lesson.meetUrl.ifBlank { "https://meet.google.com/xyz-algebra-ukr" }
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UkrFlagBlue,
                        contentColor = UkrTextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Відкрити Meet у браузері / додатку", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Вийти з уроку", color = UkrRedUrgent)
            }
        },
        containerColor = UkrNavyCardElevated
    )
}
