package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.UserRole
import com.example.ui.theme.*

@Composable
fun SimpleAuthScreen(
    onRegister: (login: String, password: String, role: UserRole, name: String, city: String, school: String, className: String, subject: String) -> Unit,
    onLogin: (login: String, password: String) -> Unit,
    onDemoLogin: (role: UserRole, tutorSubject: String?) -> Unit
) {
    var isRegisterTab by remember { mutableStateOf(true) }

    // Registration Form Fields
    var loginInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var nameInput by remember { mutableStateOf("") }
    var cityInput by remember { mutableStateOf("Київ") }
    var schoolInput by remember { mutableStateOf("Ліцей №15 «Лідер»") }
    var classInput by remember { mutableStateOf("8-А") }
    var tutorSubjectInput by remember { mutableStateOf("Математика") }

    val tutorSubjectOptions = listOf("Математика", "Англійська мова", "Фізика", "Українська мова", "Історія України", "Хімія")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UkrNavyBackground)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Branding Banner
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF0F265C), Color(0xFF1E3A8A), Color(0xFF423306))
                    )
                )
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF2563EB), Color(0xFFFBBF24))
                            )
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Шкільний Щоденник UA",
                        color = UkrTextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Освітня платформа нового покоління",
                        color = UkrFlagYellow,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Tab Switcher: Реєстрація / Вхід
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(UkrNavyCard)
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isRegisterTab) UkrFlagBlue else Color.Transparent)
                    .clickable { isRegisterTab = true }
                    .padding(vertical = 10.dp)
                    .testTag("auth_tab_register"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "📝 Реєстрація",
                    color = if (isRegisterTab) UkrTextPrimary else UkrTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (!isRegisterTab) UkrFlagBlue else Color.Transparent)
                    .clickable { isRegisterTab = false }
                    .padding(vertical = 10.dp)
                    .testTag("auth_tab_login"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🔑 Вхід",
                    color = if (!isRegisterTab) UkrTextPrimary else UkrTextSecondary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        if (isRegisterTab) {
            // REGISTRATION FORM
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
                border = BorderStroke(1.dp, UkrNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Створення облікового запису",
                        color = UkrTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Role Selection
                    Text(
                        text = "1. Оберіть вашу роль:",
                        color = UkrTextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        UserRole.values().forEach { role ->
                            val isSelected = selectedRole == role
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) UkrFlagBlue else UkrNavySurface)
                                    .clickable { selectedRole = role }
                                    .padding(vertical = 8.dp, horizontal = 4.dp)
                                    .testTag("role_select_${role.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(text = role.emoji, fontSize = 20.sp)
                                    Text(
                                        text = role.title,
                                        color = if (isSelected) UkrTextPrimary else UkrTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Immutable Role Notice
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(UkrFlagYellow.copy(alpha = 0.15f))
                            .padding(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = UkrFlagYellow, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Обрана роль призначається раз і в подальшому не змінюється",
                                color = UkrFlagYellow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Subject if Tutor
                    if (selectedRole == UserRole.TUTOR) {
                        Text(
                            text = "Предмет викладання:",
                            color = UkrTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            tutorSubjectOptions.take(3).forEach { subj ->
                                val isSubjSelected = tutorSubjectInput == subj
                                FilterChip(
                                    selected = isSubjSelected,
                                    onClick = { tutorSubjectInput = subj },
                                    label = { Text(subj, fontSize = 10.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = UkrFlagBlue,
                                        containerColor = UkrNavySurface,
                                        labelColor = UkrTextPrimary
                                    )
                                )
                            }
                        }
                    }

                    // Credentials: Login & Password
                    OutlinedTextField(
                        value = loginInput,
                        onValueChange = { loginInput = it },
                        label = { Text("Логін (ім'я користувача)") },
                        placeholder = { Text("andriy_2026") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_login_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Пароль") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth().testTag("reg_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    // Profile Fields: Name, City, School, Class
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("Ім'я та Прізвище") },
                        placeholder = { Text("Андрій Коваленко") },
                        leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = cityInput,
                            onValueChange = { cityInput = it },
                            label = { Text("Місто") },
                            placeholder = { Text("Київ") },
                            modifier = Modifier.weight(1f).testTag("reg_city_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UkrFlagYellow,
                                unfocusedBorderColor = UkrNavyBorder,
                                focusedTextColor = UkrTextPrimary,
                                unfocusedTextColor = UkrTextPrimary
                            )
                        )

                        OutlinedTextField(
                            value = classInput,
                            onValueChange = { classInput = it },
                            label = { Text("Клас") },
                            placeholder = { Text("8-А") },
                            modifier = Modifier.weight(1f).testTag("reg_class_input"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = UkrFlagYellow,
                                unfocusedBorderColor = UkrNavyBorder,
                                focusedTextColor = UkrTextPrimary,
                                unfocusedTextColor = UkrTextPrimary
                            )
                        )
                    }

                    OutlinedTextField(
                        value = schoolInput,
                        onValueChange = { schoolInput = it },
                        label = { Text("Школа / Ліцей") },
                        placeholder = { Text("Ліцей №15 «Лідер»") },
                        leadingIcon = { Icon(Icons.Default.School, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("reg_school_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    // Register Button
                    Button(
                        onClick = {
                            val finalName = nameInput.ifBlank { "Користувач" }
                            val finalLogin = loginInput.ifBlank { "user_${System.currentTimeMillis() % 1000}" }
                            onRegister(
                                finalLogin,
                                passwordInput,
                                selectedRole,
                                finalName,
                                cityInput,
                                schoolInput,
                                classInput,
                                if (selectedRole == UserRole.TUTOR) tutorSubjectInput else ""
                            )
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagYellow, contentColor = UkrNavyBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("submit_registration_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Зареєструватися та увійти",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // LOGIN TAB
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = UkrNavyCard),
                border = BorderStroke(1.dp, UkrNavyBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Вхід до кабінету",
                        color = UkrTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    OutlinedTextField(
                        value = loginInput,
                        onValueChange = { loginInput = it },
                        label = { Text("Логін") },
                        placeholder = { Text("andriy_koval") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth().testTag("login_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        label = { Text("Пароль") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().testTag("password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = UkrFlagYellow,
                            unfocusedBorderColor = UkrNavyBorder,
                            focusedTextColor = UkrTextPrimary,
                            unfocusedTextColor = UkrTextPrimary
                        )
                    )

                    Button(
                        onClick = {
                            onLogin(loginInput.ifBlank { "andriy_koval" }, passwordInput)
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = UkrFlagBlue, contentColor = UkrTextPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("submit_login_button")
                    ) {
                        Text(text = "Увійти", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Quick Demo Login Section (Very convenient for testing every role instantly)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = UkrNavyCardElevated),
            border = BorderStroke(1.dp, UkrNavyBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "⚡", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Швидкий демонстраційний вхід:",
                        color = UkrTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DemoProfileChip(
                        title = "🎒 Учень",
                        subtitle = "Андрій",
                        modifier = Modifier.weight(1f),
                        onClick = { onDemoLogin(UserRole.STUDENT, null) },
                        tag = "demo_student"
                    )
                    DemoProfileChip(
                        title = "👨‍👩‍👧 Батьки",
                        subtitle = "Олена",
                        modifier = Modifier.weight(1f),
                        onClick = { onDemoLogin(UserRole.PARENT, null) },
                        tag = "demo_parent"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    DemoProfileChip(
                        title = "👩‍🏫 Вчитель",
                        subtitle = "Олена П.",
                        modifier = Modifier.weight(1f),
                        onClick = { onDemoLogin(UserRole.TEACHER, null) },
                        tag = "demo_teacher"
                    )
                    DemoProfileChip(
                        title = "🧑‍🏫 Репетитор",
                        subtitle = "Математика",
                        modifier = Modifier.weight(1f),
                        onClick = { onDemoLogin(UserRole.TUTOR, "Математика") },
                        tag = "demo_tutor_math"
                    )
                    DemoProfileChip(
                        title = "🇬🇧 Репетитор",
                        subtitle = "English",
                        modifier = Modifier.weight(1f),
                        onClick = { onDemoLogin(UserRole.TUTOR, "Англійська") },
                        tag = "demo_tutor_eng"
                    )
                }
            }
        }
    }
}

@Composable
fun DemoProfileChip(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(UkrNavySurface)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 6.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, color = UkrTextPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(text = subtitle, color = UkrFlagYellow, fontSize = 10.sp)
        }
    }
}
