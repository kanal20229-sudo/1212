package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.model.RewardItem
import com.example.model.RewardStatus
import com.example.ui.theme.*

@Composable
fun RewardShopScreen(
    currentStars: Int,
    rewards: List<RewardItem>,
    onRequestReward: (String) -> Unit,
    onInstantSpend: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Balance Header Card with 3D Cup Illustration
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.5.dp, UkrFlagYellow),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_rewards_cup),
                        contentDescription = "Кубок нагород",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Магазин винагород",
                            color = UkrTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Обмінюйте зароблені бали на цінні призи",
                            color = UkrTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(UkrNavySurface)
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "⭐ $currentStars балів",
                        color = UkrFlagYellow,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }


        // Grid of rewards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(rewards, key = { it.id }) { item ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
                    border = BorderStroke(
                        1.dp,
                        when (item.status) {
                            RewardStatus.APPROVED -> UkrGreenSuccess
                            RewardStatus.PENDING -> UkrFlagYellow
                            else -> UkrNavyBorder
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("reward_card_${item.id}")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = item.iconEmoji, fontSize = 34.sp)
                        Text(
                            text = item.title,
                            color = UkrTextPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2
                        )
                        Text(
                            text = "${item.costStars}⭐ балів",
                            color = UkrFlagYellow,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )

                        when (item.status) {
                            RewardStatus.PENDING -> {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UkrFlagYellow.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.HourglassTop, contentDescription = null, tint = UkrFlagYellow, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Очікує схвалення", color = UkrFlagYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            RewardStatus.APPROVED -> {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(UkrGreenSuccess.copy(alpha = 0.2f))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = UkrGreenSuccess, modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Отримано!", color = UkrGreenSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                            RewardStatus.AVAILABLE -> {
                                val canAfford = currentStars >= item.costStars
                                if (item.isInstant) {
                                    Button(
                                        onClick = { onInstantSpend(item.id) },
                                        enabled = canAfford,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = UkrFlagYellow,
                                            contentColor = UkrNavyBackground
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Bolt, contentDescription = null, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(2.dp))
                                        Text("Отримати зараз", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                } else {
                                    Button(
                                        onClick = { onRequestReward(item.id) },
                                        enabled = canAfford,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = UkrFlagBlue,
                                            contentColor = UkrTextPrimary
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text("Запит батькам", fontSize = 11.sp)
                                    }
                                }
                            }
                            RewardStatus.REJECTED -> {
                                Text("Відхилено батьками", color = UkrRedUrgent, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
