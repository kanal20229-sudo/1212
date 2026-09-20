package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChildLinkRequest
import com.example.model.UserProfile
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun AppTopBar(
    userProfile: UserProfile,
    childLinkRequests: List<ChildLinkRequest> = emptyList(),
    onRoleSelected: ((UserRole) -> Unit)? = null,
    onLogout: () -> Unit = {}
) {
    var showNotificationsDialog by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }

    val pendingRequests = remember(childLinkRequests) { childLinkRequests.filter { !it.isConfirmed } }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(UkrNavyBackground)
    ) {
        // Ukrainian Flag Heart Top Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0F265C),
                            Color(0xFF1D3E8A),
                            Color(0xFF4A3E10),
                            Color(0xFF2E2405)
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(Color(0xFF2563EB), Color(0xFFFBBF24))
                                )
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Шкільний Щоденник • UA",
                        color = UkrTextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Разом творимо майбутнє України! 💙💛",
                    color = UkrFlagYellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // Main App Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // User Avatar and info
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { showProfileDialog = true }
                    .padding(4.dp)
                    .testTag("user_profile_chip")
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(UkrNavyCardElevated),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userProfile.role.emoji,
                        fontSize = 22.sp
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = userProfile.name,
                            color = UkrTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(UkrFlagYellow)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = userProfile.role.title,
                                color = UkrNavyBackground,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "${userProfile.gradeText} • ${userProfile.city}",
                        color = UkrTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            // Points, Notifications and Profile/Logout
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Stars pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(UkrNavyCard)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⭐",
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.points} бали",
                            color = UkrFlagYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Notifications Bell
                BadgedBox(
                    badge = {
                        if (pendingRequests.isNotEmpty()) {
                            Badge(containerColor = UkrRedUrgent) {
                                Text("${pendingRequests.size}")
                            }
                        }
                    }
                ) {
                    IconButton(
                        onClick = { showNotificationsDialog = true },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(UkrNavyCard)
                            .testTag("notifications_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Сповіщення",
                            tint = if (pendingRequests.isNotEmpty()) UkrFlagYellow else UkrTextPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Profile / Logout Button
                IconButton(
                    onClick = { showProfileDialog = true },
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(UkrNavyCard)
                        .testTag("profile_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Мій профіль",
                        tint = UkrTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }

    // Profile Dialog with Logout
    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${userProfile.role.emoji} Особистий профіль", color = UkrTextPrimary, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Text(text = "ПІБ: ${userProfile.name}", color = UkrTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(UkrNavySurface)
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = UkrFlagYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Роль зафіксована: ${userProfile.role.title} (не змінюється)",
                                color = UkrFlagYellow,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Text(text = "Місто: ${userProfile.city}", color = UkrTextSecondary, fontSize = 13.sp)
                    Text(text = "Заклад: ${userProfile.schoolName}", color = UkrTextSecondary, fontSize = 13.sp)
                    Text(text = "Клас: ${userProfile.className}", color = UkrTextSecondary, fontSize = 13.sp)
                    if (userProfile.subject.isNotBlank()) {
                        Text(text = "Предмет: ${userProfile.subject}", color = UkrFlagYellow, fontSize = 13.sp)
                    }

                    // Personal Child Code
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
                        border = BorderStroke(1.dp, UkrFlagYellow.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = "🔑 Код учня для зв'язку (батьки/репетитори):", color = UkrTextSecondary, fontSize = 11.sp)
                            Text(text = userProfile.personalCode, color = UkrFlagYellow, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showProfileDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrRedUrgent, contentColor = Color.White),
                    modifier = Modifier.testTag("logout_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Вийти з акаунту")
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Закрити", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }

    if (showNotificationsDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationsDialog = false },
            title = {
                Text(
                    text = "🔔 Сповіщення та запити",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (pendingRequests.isNotEmpty()) {
                        Text(
                            text = "📨 Нові запити на підключення учня:",
                            color = UkrFlagYellow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        pendingRequests.forEach { req ->
                            Card(
                                shape = RoundedCornerShape(10.dp),
                                colors = CardDefaults.cardColors(containerColor = UkrNavySurface),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Від: ${req.requesterName} (${req.requesterRole.title} ${req.requesterSubject})",
                                        color = UkrTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Text(
                                        text = "Учень: ${req.childName} ${req.childSurname}",
                                        color = UkrTextSecondary,
                                        fontSize = 11.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Код підтвердження: ${req.code}",
                                        color = UkrFlagYellow,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Text(text = "Немає нових запитів на підключення.", color = UkrTextSecondary, fontSize = 12.sp)
                    }

                    Divider(color = UkrNavyBorder)

                    Text(
                        text = "• 08:00 — Урок Алгебри розпочато у Каб. 304",
                        color = UkrTextSecondary,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "• 08:15 — Батькам надіслано статус «🏫 Я в школі»",
                        color = UkrTextSecondary,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showNotificationsDialog = false }) {
                    Text("Зрозуміло", color = UkrFlagYellow)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}
