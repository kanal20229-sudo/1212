package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.UserRole
import com.example.ui.components.AppNavigationTabs
import com.example.ui.components.AppTopBar
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.UkrNavyBackground
import com.example.viewmodel.SchoolDiaryViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: SchoolDiaryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SchoolDiaryApp(viewModel)
            }
        }
    }
}

@Composable
fun SchoolDiaryApp(viewModel: SchoolDiaryViewModel) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
    val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
    val currentProfile by viewModel.currentProfile.collectAsStateWithLifecycle()
    val activeTab by viewModel.activeTab.collectAsStateWithLifecycle()
    val selectedDayIndex by viewModel.selectedDayIndex.collectAsStateWithLifecycle()
    val dayFormat by viewModel.dayFormat.collectAsStateWithLifecycle()
    val countdownSeconds by viewModel.countdownSeconds.collectAsStateWithLifecycle()
    val homeworkList by viewModel.homeworkList.collectAsStateWithLifecycle()
    val rewards by viewModel.rewards.collectAsStateWithLifecycle()
    val backpack by viewModel.backpack.collectAsStateWithLifecycle()
    val activeQuickStatus by viewModel.activeQuickStatus.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbarMessage.collectAsStateWithLifecycle()
    val parentSelectedChild by viewModel.parentSelectedChild.collectAsStateWithLifecycle()
    val childrenList by viewModel.childrenList.collectAsStateWithLifecycle()
    val subjectReports by viewModel.subjectReports.collectAsStateWithLifecycle()
    val teacherSelectedClass by viewModel.teacherSelectedClass.collectAsStateWithLifecycle()
    val teacherSubmissions by viewModel.teacherSubmissions.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val nmtAnswers by viewModel.nmtAnswers.collectAsStateWithLifecycle()
    val isEvaluatingAi by viewModel.isEvaluatingAi.collectAsStateWithLifecycle()
    val tutorsList by viewModel.tutorsList.collectAsStateWithLifecycle()
    val childLinkRequests by viewModel.childLinkRequests.collectAsStateWithLifecycle()
    val activePairingCode by viewModel.activePairingCode.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let { msg ->
            snackbarHostState.showSnackbar(msg, duration = SnackbarDuration.Short)
            viewModel.clearSnackbar()
        }
    }

    if (!isLoggedIn) {
        SimpleAuthScreen(
            onRegister = { login, password, role, fullName, city, school, className, subject ->
                viewModel.registerUser(login, password, role, fullName, city, school, className, subject)
            },
            onLogin = { login, password ->
                viewModel.loginUser(login, password)
            },
            onDemoLogin = { role, subject ->
                viewModel.selectRole(role)
            }
        )
    } else {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                AppTopBar(
                    userProfile = currentProfile,
                    childLinkRequests = childLinkRequests,
                    onRoleSelected = { viewModel.selectRole(it) },
                    onLogout = { viewModel.logout() }
                )
            },
            bottomBar = {
                AppNavigationTabs(
                    currentRole = currentRole,
                    activeTab = activeTab,
                    onTabSelected = { viewModel.setActiveTab(it) }
                )
            },
            containerColor = UkrNavyBackground,
            contentWindowInsets = WindowInsets.safeDrawing
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(UkrNavyBackground)
            ) {
                // Screen Routing according to Role and Tab
                when (currentRole) {
                    UserRole.STUDENT -> {
                        when (activeTab) {
                            "сьогодні" -> StudentHomeScreen(
                                countdownSeconds = countdownSeconds,
                                selectedDayIndex = selectedDayIndex,
                                dayFormat = dayFormat,
                                activeQuickStatus = activeQuickStatus,
                                activePairingCode = activePairingCode,
                                onSelectDayIndex = { viewModel.setSelectedDayIndex(it) },
                                onSetQuickStatus = { viewModel.setQuickStatus(it) },
                                onNavigateToTab = { viewModel.setActiveTab(it) }
                            )
                            "оцінки" -> GradebookScreen(
                                currentRole = currentRole,
                                subjectReports = subjectReports,
                                onAddGrade = { s, sc, t, tp -> viewModel.addGrade(s, sc, t, tp) },
                                onRequestAiDiagnostic = { _, cb -> viewModel.requestGradeAiDiagnostic(cb) }
                            )
                            "дз" -> HomeworkScreen(
                                homeworkList = homeworkList,
                                isEvaluatingAi = isEvaluatingAi,
                                onToggleHomework = { viewModel.toggleHomework(it) },
                                onAddHomework = { subject, title, desc, prio, dead, pages ->
                                    viewModel.addHomework(subject, title, desc, prio, dead, pages)
                                },
                                onEvaluateWithAi = { hwId, answer ->
                                    viewModel.evaluateHomeworkWithAi(hwId, answer)
                                }
                            )
                            "репетитори" -> TutorsScreen(
                                tutorsList = tutorsList,
                                studentCode = currentProfile.personalCode,
                                onAddTutor = { viewModel.addTutor(it) },
                                onOpenChat = { viewModel.setActiveTab("чат") }
                            )
                            "рюкзак" -> BackpackScreen(
                                backpackItems = backpack,
                                onToggleItem = { viewModel.toggleBackpackItem(it) },
                                onAddItem = { name, subject, category -> viewModel.addBackpackItem(name, subject, category) },
                                onAutoPack = { viewModel.autoPackForTomorrow() }
                            )
                            "нагороди" -> RewardShopScreen(
                                currentStars = currentProfile.points,
                                rewards = rewards,
                                onRequestReward = { viewModel.requestReward(it) },
                                onInstantSpend = { viewModel.instantSpendReward(it) }
                            )
                            "нмт" -> NmtPrepScreen(
                                answers = nmtAnswers,
                                onSelectOption = { qIdx, optIdx -> viewModel.answerNmtQuestion(qIdx, optIdx) },
                                onReset = { viewModel.resetNmtTest() }
                            )
                            "репетитор" -> AiTutorScreen()
                            "ігри" -> MiniGamesScreen(
                                onRewardEarned = { viewModel.addStars(it) }
                            )
                            "чат" -> ChatScreen(
                                messages = chatMessages,
                                onSendMessage = { viewModel.sendChatMessage(it) }
                            )
                            else -> StudentHomeScreen(
                                countdownSeconds = countdownSeconds,
                                selectedDayIndex = selectedDayIndex,
                                dayFormat = dayFormat,
                                activeQuickStatus = activeQuickStatus,
                                activePairingCode = activePairingCode,
                                onSelectDayIndex = { viewModel.setSelectedDayIndex(it) },
                                onSetQuickStatus = { viewModel.setQuickStatus(it) },
                                onNavigateToTab = { viewModel.setActiveTab(it) }
                            )
                        }
                    }
                    UserRole.PARENT -> {
                        when (activeTab) {
                            "сьогодні", "нагороди", "бібліотека" -> ParentDashboardScreen(
                                selectedChild = parentSelectedChild,
                                childrenList = childrenList,
                                dayFormat = dayFormat,
                                rewards = rewards,
                                onSelectChild = { viewModel.setParentChild(it) },
                                onAddChild = { name, cls, school -> viewModel.addChild(name, cls, school) },
                                onInitiateChildLink = { name, surname -> viewModel.initiateChildLink(name, surname) },
                                onConfirmChildLink = { code, name, surname -> viewModel.confirmChildLink(code, name, surname) },
                                onRequestAiAdvice = { childName, cb -> viewModel.requestParentAdviceAi(childName, cb) },
                                onChangeDayFormat = { viewModel.setDayFormat(it) },
                                onApproveReward = { viewModel.approveReward(it) },
                                onRejectReward = { id, reason -> viewModel.rejectReward(id, reason) },
                                onCreateReward = { title, emoji, cost -> viewModel.createCustomReward(title, emoji, cost) }
                            )
                            "оцінки" -> GradebookScreen(
                                currentRole = currentRole,
                                subjectReports = subjectReports,
                                onAddGrade = { s, sc, t, tp -> viewModel.addGrade(s, sc, t, tp) },
                                onRequestAiDiagnostic = { _, cb -> viewModel.requestGradeAiDiagnostic(cb) }
                            )
                            "дз" -> HomeworkScreen(
                                homeworkList = homeworkList,
                                isEvaluatingAi = isEvaluatingAi,
                                onToggleHomework = { viewModel.toggleHomework(it) },
                                onAddHomework = { subject, title, desc, prio, dead, pages ->
                                    viewModel.addHomework(subject, title, desc, prio, dead, pages)
                                },
                                onEvaluateWithAi = { hwId, answer -> viewModel.evaluateHomeworkWithAi(hwId, answer) }
                            )
                            "репетитори" -> TutorsScreen(
                                tutorsList = tutorsList,
                                studentCode = currentProfile.personalCode,
                                onAddTutor = { viewModel.addTutor(it) },
                                onOpenChat = { viewModel.setActiveTab("чат") }
                            )
                            "чат" -> ChatScreen(
                                messages = chatMessages,
                                onSendMessage = { viewModel.sendChatMessage(it) }
                            )
                            else -> ParentDashboardScreen(
                                selectedChild = parentSelectedChild,
                                childrenList = childrenList,
                                dayFormat = dayFormat,
                                rewards = rewards,
                                onSelectChild = { viewModel.setParentChild(it) },
                                onAddChild = { name, cls, school -> viewModel.addChild(name, cls, school) },
                                onInitiateChildLink = { name, surname -> viewModel.initiateChildLink(name, surname) },
                                onConfirmChildLink = { code, name, surname -> viewModel.confirmChildLink(code, name, surname) },
                                onRequestAiAdvice = { childName, cb -> viewModel.requestParentAdviceAi(childName, cb) },
                                onChangeDayFormat = { viewModel.setDayFormat(it) },
                                onApproveReward = { viewModel.approveReward(it) },
                                onRejectReward = { id, reason -> viewModel.rejectReward(id, reason) },
                                onCreateReward = { title, emoji, cost -> viewModel.createCustomReward(title, emoji, cost) }
                            )
                        }
                    }
                    UserRole.TEACHER, UserRole.TUTOR -> {
                        when (activeTab) {
                            "сьогодні", "перевірка" -> TeacherDashboardScreen(
                                selectedClass = teacherSelectedClass,
                                submissions = teacherSubmissions,
                                onSelectClass = { viewModel.setTeacherClass(it) },
                                onGradeSubmission = { id, grade, comment -> viewModel.gradeStudentSubmission(id, grade, comment) },
                                onAssignHomework = { topic, task, pages ->
                                    viewModel.addHomework("Алгебра", task, topic, com.example.model.Priority.IMPORTANT, "Завтра", pages)
                                },
                                onInitiateStudentLink = { name, surname -> viewModel.initiateChildLink(name, surname) },
                                onConfirmStudentLink = { code, name, surname -> viewModel.confirmChildLink(code, name, surname) },
                                onRequestAiTasks = { subj, topic, _, cb -> viewModel.requestTeacherTasksAi(subj, topic, cb) }
                            )
                            "оцінки" -> GradebookScreen(
                                currentRole = currentRole,
                                subjectReports = subjectReports,
                                onAddGrade = { s, sc, t, tp -> viewModel.addGrade(s, sc, t, tp) },
                                onRequestAiDiagnostic = { _, cb -> viewModel.requestGradeAiDiagnostic(cb) }
                            )
                            "розклад" -> StudentHomeScreen(
                                countdownSeconds = countdownSeconds,
                                selectedDayIndex = selectedDayIndex,
                                dayFormat = dayFormat,
                                activeQuickStatus = activeQuickStatus,
                                activePairingCode = activePairingCode,
                                onSelectDayIndex = { viewModel.setSelectedDayIndex(it) },
                                onSetQuickStatus = { viewModel.setQuickStatus(it) },
                                onNavigateToTab = { viewModel.setActiveTab(it) }
                            )
                            "чат" -> ChatScreen(
                                messages = chatMessages,
                                onSendMessage = { viewModel.sendChatMessage(it) }
                            )
                            else -> TeacherDashboardScreen(
                                selectedClass = teacherSelectedClass,
                                submissions = teacherSubmissions,
                                onSelectClass = { viewModel.setTeacherClass(it) },
                                onGradeSubmission = { id, grade, comment -> viewModel.gradeStudentSubmission(id, grade, comment) },
                                onAssignHomework = { topic, task, pages ->
                                    viewModel.addHomework("Алгебра", task, topic, com.example.model.Priority.IMPORTANT, "Завтра", pages)
                                },
                                onInitiateStudentLink = { name, surname -> viewModel.initiateChildLink(name, surname) },
                                onConfirmStudentLink = { code, name, surname -> viewModel.confirmChildLink(code, name, surname) },
                                onRequestAiTasks = { subj, topic, _, cb -> viewModel.requestTeacherTasksAi(subj, topic, cb) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
