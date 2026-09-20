package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Sync
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
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun TelegramAuthScreen(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onContinue: () -> Unit
) {
    var selectedRole by remember { mutableStateOf(currentRole) }
    var mockTelegramUser by remember { mutableStateOf("Олег Петренко (@oleg_petrenko)") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // App Logo & Ukrainian Banner
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(UkrNavyCard)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2563EB), Color(0xFFFBBF24))
                            )
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Шкільний щоденник та помічник",
                    color = UkrTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Big Greeting
        Text(
            text = "👋 Вхід через Telegram",
            color = UkrTextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Швидко, безпечно. Почніть навчання разом з нами!",
            color = UkrTextSecondary,
            fontSize = 14.sp
        )

        // Telegram Account Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(UkrNavyCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "👨‍🎓",
                            fontSize = 28.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = "Ваш Telegram акаунт",
                            color = UkrTextSecondary,
                            fontSize = 12.sp
                        )
                        Text(
                            text = mockTelegramUser,
                            color = UkrTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Дані отримано з Telegram WebApp",
                            color = UkrFlagYellow,
                            fontSize = 11.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = {
                        mockTelegramUser = if (mockTelegramUser.contains("Олег")) "Олена Коваленко (@olena_ua)" else "Олег Петренко (@oleg_petrenko)"
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = UkrFlagBlueLight),
                    border = BorderStroke(1.dp, UkrNavyBorderActive),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Змінити",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Section Title: Select Role
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Оберіть свою роль",
                color = UkrTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = UkrTextSecondary,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Кожна роль має свої можливості",
                    color = UkrTextSecondary,
                    fontSize = 11.sp
                )
            }
        }

        // 4 Role Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UserRole.values().forEach { role ->
                val isSelected = selectedRole == role
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) UkrNavyCardElevated else UkrNavyCard
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) UkrFlagYellow else UkrNavyBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedRole = role }
                        .testTag("role_option_${role.name}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) UkrNavyBorderActive else UkrNavySurface),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = role.emoji,
                                    fontSize = 24.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(14.dp))
                            Column {
                                Text(
                                    text = role.title,
                                    color = UkrTextPrimary,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = role.subtitle,
                                    color = UkrTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        Icon(
                            imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (isSelected) UkrFlagYellow else UkrTextMuted,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Continue Button
        Button(
            onClick = {
                onRoleSelected(selectedRole)
                onContinue()
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = UkrFlagBlue,
                contentColor = UkrTextPrimary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("continue_auth_button")
        ) {
            Text(
                text = "Продовжити як ${selectedRole.title}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
