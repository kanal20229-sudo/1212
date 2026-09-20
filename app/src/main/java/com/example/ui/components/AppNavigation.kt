package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*

data class NavTabItem(
    val id: String,
    val title: String,
    val emoji: String
)

@Composable
fun AppNavigationTabs(
    currentRole: UserRole,
    activeTab: String,
    onTabSelected: (String) -> Unit
) {
    val tabs = when (currentRole) {
        UserRole.STUDENT -> listOf(
            NavTabItem("сьогодні", "Сьогодні", "📅"),
            NavTabItem("оцінки", "Оцінки", "📊"),
            NavTabItem("дз", "ДЗ та AI", "📝"),
            NavTabItem("репетитори", "Репетитори", "🧑‍🏫"),
            NavTabItem("рюкзак", "Рюкзак", "🎒"),
            NavTabItem("нагороди", "Нагороди", "🏆"),
            NavTabItem("нмт", "НМТ Тренажер", "🎓"),
            NavTabItem("репетитор", "AI-Тьютор", "🤖"),
            NavTabItem("ігри", "Міні-ігри", "🎮"),
            NavTabItem("чат", "Чат", "💬")
        )
        UserRole.PARENT -> listOf(
            NavTabItem("сьогодні", "Моніторинг", "📊"),
            NavTabItem("оцінки", "Табель дитини", "📈"),
            NavTabItem("репетитори", "Репетитори", "🧑‍🏫"),
            NavTabItem("нагороди", "Запити нагород", "🎁"),
            NavTabItem("бібліотека", "Підручники", "📚"),
            NavTabItem("дз", "Завдання дитини", "📝"),
            NavTabItem("чат", "Чат з ліцеєм", "💬")
        )
        UserRole.TEACHER, UserRole.TUTOR -> listOf(
            NavTabItem("сьогодні", "Класи та ДЗ", "👥"),
            NavTabItem("оцінки", "Журнал оцінок", "📊"),
            NavTabItem("перевірка", "Перевірка робіт", "📝"),
            NavTabItem("розклад", "Мої уроки", "📅"),
            NavTabItem("чат", "Повідомлення", "💬")
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(UkrNavySurface)
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEach { tab ->
            val isSelected = activeTab == tab.id
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isSelected) UkrFlagBlue else UkrNavyCard)
                    .clickable { onTabSelected(tab.id) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .testTag("nav_tab_${tab.id}"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = tab.emoji,
                        fontSize = 14.sp
                    )
                    Text(
                        text = tab.title,
                        color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}
