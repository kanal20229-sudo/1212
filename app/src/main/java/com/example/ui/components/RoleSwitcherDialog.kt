package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun RoleSwitcherDialog(
    currentRole: UserRole,
    onRoleSelected: (UserRole) -> Unit,
    onDismiss: () -> Unit
) {
    val roles = listOf(
        Triple(UserRole.STUDENT, "Андрій Коваленко (8-А клас)", "Розклад, ДЗ з AI, рюкзак, НМТ та нагороди"),
        Triple(UserRole.PARENT, "Олена Коваленко (Мама)", "Моніторинг статусів, оцінки, підручники, схвалення нагород"),
        Triple(UserRole.TEACHER, "Оксана Петренко (Вчитель)", "Видача ДЗ, перевірка робіт, виставлення оцінок 1-12"),
        Triple(UserRole.TUTOR, "Олег Михайлович (Репетитор)", "Індивідуальні заняття, НМТ тренінг, онлайн-уроки")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👤 Перемикання ролі",
                    color = UkrTextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Закрити", tint = UkrTextSecondary)
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Оберіть профіль для роботи в системі. Всі дані та розклад синхронізовані:",
                    color = UkrTextSecondary,
                    fontSize = 12.sp
                )

                roles.forEach { (role, name, desc) ->
                    val isSelected = currentRole == role
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) UkrFlagBlue.copy(alpha = 0.25f) else UkrNavySurface
                        ),
                        border = BorderStroke(
                            1.5.dp,
                            if (isSelected) UkrFlagYellow else UkrNavyBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onRoleSelected(role)
                                onDismiss()
                            }
                            .testTag("role_select_${role.name}")
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
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) UkrFlagYellow else UkrNavyCardElevated),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = role.emoji, fontSize = 22.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = role.title,
                                            color = if (isSelected) UkrFlagYellow else UkrTextPrimary,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        text = name,
                                        color = UkrTextPrimary,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = desc,
                                        color = UkrTextSecondary,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(UkrFlagYellow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = UkrNavyBackground,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Скасувати", color = UkrTextMuted)
            }
        },
        containerColor = UkrNavyCardElevated
    )
}
