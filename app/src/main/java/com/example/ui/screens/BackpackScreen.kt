package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
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
import com.example.model.BackpackItem
import com.example.ui.theme.*

@Composable
fun BackpackScreen(
    backpackItems: List<BackpackItem>,
    onToggleItem: (String) -> Unit,
    onAddItem: (name: String, subject: String, category: String) -> Unit = { _, _, _ -> },
    onAutoPack: () -> Unit = {}
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var newItemSubject by remember { mutableStateOf("Загальне") }
    var newItemCategory by remember { mutableStateOf("Інше") }

    val packedCount = backpackItems.count { it.isPacked }
    val totalCount = backpackItems.size
    val isAllPacked = packedCount == totalCount && totalCount > 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, if (isAllPacked) UkrGreenSuccess else UkrFlagYellow),
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
                        Text(text = "🎒", fontSize = 26.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Збери рюкзак на завтра",
                                color = UkrTextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Сформовано автоматично за розкладом п'ятниці",
                                color = UkrTextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    Text(
                        text = "$packedCount/$totalCount",
                        color = if (isAllPacked) UkrGreenSuccess else UkrFlagYellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                LinearProgressIndicator(
                    progress = { if (totalCount > 0) packedCount.toFloat() / totalCount else 0f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (isAllPacked) UkrGreenSuccess else UkrFlagYellow,
                    trackColor = UkrNavySurface
                )

                if (isAllPacked) {
                    Text(
                        text = "🎉 Чудово! Всі підручники та зошити зібрано. Батькам надіслано підтвердження (+10⭐)!",
                        color = UkrGreenSuccess,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onAutoPack() },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("⚡ Зібрати все", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    OutlinedButton(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, UkrNavyBorderActive),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("+ Додати річ", color = UkrFlagBlueLight, fontSize = 12.sp)
                    }
                }
            }
        }

        // List of items
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(backpackItems, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isPacked) UkrNavySurface else UkrNavyCard
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (item.isPacked) UkrGreenSuccess.copy(alpha = 0.5f) else UkrNavyBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("backpack_item_${item.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = item.isPacked,
                                onCheckedChange = { onToggleItem(item.id) },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = UkrGreenSuccess,
                                    checkmarkColor = UkrNavyBackground,
                                    uncheckedColor = UkrTextSecondary
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = item.name,
                                    color = if (item.isPacked) UkrTextSecondary else UkrTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${item.category} • ${item.subject}",
                                    color = UkrFlagYellow,
                                    fontSize = 11.sp
                                )
                            }
                        }

                        if (item.isPacked) {
                            Text(text = "Зібрано ✓", color = UkrGreenSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("🎒 Додати предмет у рюкзак", color = UkrTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Назва речі") },
                        placeholder = { Text("напр. Калькулятор Casio") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newItemSubject,
                        onValueChange = { newItemSubject = it },
                        label = { Text("Предмет") },
                        placeholder = { Text("напр. Алгебра / Фізика") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = newItemCategory,
                        onValueChange = { newItemCategory = it },
                        label = { Text("Категорія") },
                        placeholder = { Text("Канцелярія / Підручник") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newItemName.isNotBlank()) {
                            onAddItem(newItemName, newItemSubject, newItemCategory)
                            showAddDialog = false
                            newItemName = ""
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground)
                ) {
                    Text("Додати", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("Скасувати", color = UkrTextSecondary)
                }
            },
            containerColor = UkrNavyCardElevated
        )
    }
}
