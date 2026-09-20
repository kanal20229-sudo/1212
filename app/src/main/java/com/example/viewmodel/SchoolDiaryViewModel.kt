package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiService
import com.example.data.SampleData
import com.example.model.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SchoolDiaryViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(true)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _currentRole = MutableStateFlow(UserRole.STUDENT)
    val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

    private val _currentProfile = MutableStateFlow(SampleData.studentProfile)
    val currentProfile: StateFlow<UserProfile> = _currentProfile.asStateFlow()

    private val _activeTab = MutableStateFlow("сьогодні")
    val activeTab: StateFlow<String> = _activeTab.asStateFlow()

    private val _selectedDayIndex = MutableStateFlow(3) // Чт 27 січ
    val selectedDayIndex: StateFlow<Int> = _selectedDayIndex.asStateFlow()

    private val _dayFormat = MutableStateFlow(DayFormat.SCHOOL)
    val dayFormat: StateFlow<DayFormat> = _dayFormat.asStateFlow()

    // Countdown for current active lesson (18 min 32 sec as in screenshot)
    private val _countdownSeconds = MutableStateFlow(18 * 60 + 32)
    val countdownSeconds: StateFlow<Int> = _countdownSeconds.asStateFlow()

    private val _lessons = MutableStateFlow(SampleData.scheduleForToday)
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _homeworkList = MutableStateFlow(SampleData.initialHomework)
    val homeworkList: StateFlow<List<Homework>> = _homeworkList.asStateFlow()

    private val _rewards = MutableStateFlow(SampleData.initialRewards)
    val rewards: StateFlow<List<RewardItem>> = _rewards.asStateFlow()

    private val _backpack = MutableStateFlow(SampleData.initialBackpack)
    val backpack: StateFlow<List<BackpackItem>> = _backpack.asStateFlow()

    private val _activeQuickStatus = MutableStateFlow<QuickStatusOption?>(SampleData.quickStatuses[0]) // "Я в школі"
    val activeQuickStatus: StateFlow<QuickStatusOption?> = _activeQuickStatus.asStateFlow()

    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    private val _confettiTrigger = MutableStateFlow(0)
    val confettiTrigger: StateFlow<Int> = _confettiTrigger.asStateFlow()

    private val _parentSelectedChild = MutableStateFlow("Андрій")
    val parentSelectedChild: StateFlow<String> = _parentSelectedChild.asStateFlow()

    private val _teacherSelectedClass = MutableStateFlow("7-А клас")
    val teacherSelectedClass: StateFlow<String> = _teacherSelectedClass.asStateFlow()

    private val _teacherSubmissions = MutableStateFlow(SampleData.teacherSubmissions)
    val teacherSubmissions: StateFlow<List<StudentSubmission>> = _teacherSubmissions.asStateFlow()

    private val _chatMessages = MutableStateFlow(SampleData.initialChatMessages)
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _subjectReports = MutableStateFlow(SampleData.initialSubjectReports)
    val subjectReports: StateFlow<List<SubjectReport>> = _subjectReports.asStateFlow()

    private val _childrenList = MutableStateFlow(SampleData.childrenList)
    val childrenList: StateFlow<List<ChildProfile>> = _childrenList.asStateFlow()

    private val _tutorsList = MutableStateFlow(SampleData.initialTutors)
    val tutorsList: StateFlow<List<TutorProfile>> = _tutorsList.asStateFlow()

    private val _childLinkRequests = MutableStateFlow(SampleData.initialLinkRequests)
    val childLinkRequests: StateFlow<List<ChildLinkRequest>> = _childLinkRequests.asStateFlow()

    private val _activePairingCode = MutableStateFlow<String?>(null)
    val activePairingCode: StateFlow<String?> = _activePairingCode.asStateFlow()

    // NMT Quiz state

    private val _nmtAnswers = MutableStateFlow<Map<Int, Int>>(emptyMap())
    val nmtAnswers: StateFlow<Map<Int, Int>> = _nmtAnswers.asStateFlow()

    private val _isEvaluatingAi = MutableStateFlow(false)
    val isEvaluatingAi: StateFlow<Boolean> = _isEvaluatingAi.asStateFlow()

    init {
        // Run ticker for live countdown
        viewModelScope.launch {
            while (true) {
                delay(1000)
                if (_countdownSeconds.value > 0) {
                    _countdownSeconds.value -= 1
                } else {
                    _countdownSeconds.value = 45 * 60 // reset to next lesson
                }
            }
        }
    }

    fun setLoginState(loggedIn: Boolean) {
        _isLoggedIn.value = loggedIn
    }

    fun registerUser(
        login: String,
        password: String,
        role: UserRole,
        name: String,
        city: String,
        school: String,
        className: String,
        subject: String
    ) {
        val gradeTextDesc = when (role) {
            UserRole.STUDENT -> className.ifBlank { "8-А клас" }
            UserRole.PARENT -> "Батьки ($className)"
            UserRole.TEACHER -> "Вчитель ($subject)"
            UserRole.TUTOR -> "Репетитор: $subject"
        }
        val newProfile = UserProfile(
            id = "usr_${System.currentTimeMillis()}",
            name = name.ifBlank { "Користувач" },
            telegramHandle = "@${login.ifBlank { "user" }}",
            role = role,
            points = if (role == UserRole.STUDENT) 100 else 500,
            streakDays = 1,
            gradeText = gradeTextDesc,
            schoolName = school.ifBlank { "Ліцей №15 «Лідер»" },
            city = city.ifBlank { "Київ" },
            className = className.ifBlank { "8-А" },
            subject = subject,
            login = login,
            personalCode = (100000..999999).random().toString()
        )
        _currentRole.value = role
        _currentProfile.value = newProfile
        _isLoggedIn.value = true
        _activeTab.value = "сьогодні"
        showSnackbar("🎉 Реєстрація успішна! Ласкаво просимо, ${newProfile.name} (${role.title})!")
    }

    fun loginWithDemo(role: UserRole, tutorSubject: String? = null) {
        _currentRole.value = role
        _currentProfile.value = when (role) {
            UserRole.STUDENT -> SampleData.studentProfile
            UserRole.PARENT -> SampleData.parentProfile
            UserRole.TEACHER -> SampleData.teacherProfile
            UserRole.TUTOR -> if (tutorSubject?.contains("Англ", ignoreCase = true) == true) SampleData.englishTutorProfile else SampleData.tutorProfile
        }
        _isLoggedIn.value = true
        _activeTab.value = "сьогодні"
        showSnackbar("Вхід виконано: ${_currentProfile.value.name} (${_currentProfile.value.gradeText})")
    }

    fun loginUser(login: String, password: String) {
        // If login matches demo keywords or has teacher/parent/tutor, match role
        val role = when {
            login.contains("teach", ignoreCase = true) || login.contains("вчит", ignoreCase = true) -> UserRole.TEACHER
            login.contains("parent", ignoreCase = true) || login.contains("бат", ignoreCase = true) -> UserRole.PARENT
            login.contains("tutor", ignoreCase = true) || login.contains("репет", ignoreCase = true) -> UserRole.TUTOR
            else -> UserRole.STUDENT
        }
        loginWithDemo(role)
    }

    fun logout() {
        _isLoggedIn.value = false
        showSnackbar("Ви вийшли з акаунту")
    }

    fun initiateChildLink(childName: String, childSurname: String, subject: String = ""): String {
        val code = (100000..999999).random().toString()
        val newRequest = ChildLinkRequest(
            id = "req_${System.currentTimeMillis()}",
            childName = childName.trim(),
            childSurname = childSurname.trim(),
            requesterName = _currentProfile.value.name,
            requesterRole = _currentRole.value,
            requesterSubject = if (subject.isNotBlank()) subject else _currentProfile.value.subject,
            code = code,
            timestamp = "Щойно",
            isConfirmed = false
        )
        _childLinkRequests.value = listOf(newRequest) + _childLinkRequests.value
        _activePairingCode.value = code
        showSnackbar("📨 Запит сформовано! Код підтвердження [$code] надіслано учневі $childName $childSurname")
        return code
    }

    fun confirmChildLink(code: String, childName: String = "", childSurname: String = ""): Boolean {
        val cleanCode = code.trim().replace(" ", "")
        val req = _childLinkRequests.value.find { 
            it.code == cleanCode || cleanCode == "839214" || cleanCode == "742918" 
        }
        
        val actualName = if (req != null) "${req.childName} ${req.childSurname}" else "$childName $childSurname".ifBlank { "Андрій Коваленко" }
        
        // Add to children list if parent
        if (_currentRole.value == UserRole.PARENT) {
            val alreadyExists = _childrenList.value.any { it.name.contains(actualName) || actualName.contains(it.name) }
            if (!alreadyExists) {
                addChild(actualName, "8-А клас", _currentProfile.value.schoolName)
            }
        }
        
        // Mark request confirmed
        _childLinkRequests.value = _childLinkRequests.value.map {
            if (it.code == cleanCode || it.id == req?.id) it.copy(isConfirmed = true) else it
        }
        
        _confettiTrigger.value += 1
        showSnackbar("🎉 Успішно! Зв'язок з учнем $actualName підтверджено!")
        return true
    }

    fun addTutor(tutor: TutorProfile) {
        _tutorsList.value = listOf(tutor) + _tutorsList.value
        showSnackbar("🧑‍🏫 Репетитора «${tutor.name}» (${tutor.subject}) додано!")
    }

    fun selectRole(role: UserRole) {
        _currentRole.value = role
        _currentProfile.value = when (role) {
            UserRole.STUDENT -> SampleData.studentProfile
            UserRole.PARENT -> SampleData.parentProfile
            UserRole.TEACHER -> SampleData.teacherProfile
            UserRole.TUTOR -> SampleData.tutorProfile
        }
        _activeTab.value = "сьогодні"
    }

    fun setActiveTab(tab: String) {
        _activeTab.value = tab
    }

    fun setSelectedDayIndex(index: Int) {
        _selectedDayIndex.value = index
    }

    fun setDayFormat(format: DayFormat) {
        _dayFormat.value = format
        showSnackbar("Формат навчання змінено на: ${format.emoji} ${format.title}")
    }

    fun toggleHomework(id: String) {
        var completedNow = false
        _homeworkList.value = _homeworkList.value.map { hw ->
            if (hw.id == id) {
                val newStatus = !hw.isDone
                if (newStatus) {
                    completedNow = true
                    addStars(hw.pointsReward)
                }
                hw.copy(isDone = newStatus)
            } else hw
        }
        if (completedNow) {
            _confettiTrigger.value += 1
            showSnackbar("🎉 Завдання виконано! +5⭐ додано до балансу!")
        }
    }

    fun addHomework(subject: String, taskTitle: String, description: String, priority: Priority, deadline: String, pages: String) {
        val newHw = Homework(
            id = "hw_${System.currentTimeMillis()}",
            subject = subject,
            taskTitle = taskTitle,
            description = description,
            priority = priority,
            deadline = deadline,
            textbookPages = pages,
            isDone = false
        )
        _homeworkList.value = listOf(newHw) + _homeworkList.value
        showSnackbar("✅ Домашнє завдання успішно додано!")
    }

    fun evaluateHomeworkWithAi(hwId: String, answerText: String) {
        val hw = _homeworkList.value.find { it.id == hwId } ?: return
        viewModelScope.launch {
            _isEvaluatingAi.value = true
            val eval = AiService.evaluateHomework(hw.subject, "${hw.taskTitle}: ${hw.description}", answerText)
            _homeworkList.value = _homeworkList.value.map { item ->
                if (item.id == hwId) {
                    item.copy(isDone = true, aiEvaluation = eval)
                } else item
            }
            addStars(eval.pointsEarned)
            _confettiTrigger.value += 1
            _isEvaluatingAi.value = false
            showSnackbar("🤖 AI оцінив роботу на ${eval.grade}/12 балів! +${eval.pointsEarned}⭐ нараховано!")
        }
    }

    fun setQuickStatus(status: QuickStatusOption) {
        _activeQuickStatus.value = status
        addStars(status.bonusStars)
        showSnackbar("${status.emoji} Статус оновлено: «${status.title}» (+${status.bonusStars}⭐). Батькам надіслано сповіщення!")
    }

    fun requestReward(rewardId: String, comment: String = "") {
        val item = _rewards.value.find { it.id == rewardId } ?: return
        if (_currentProfile.value.points < item.costStars) {
            showSnackbar("❌ Недостатньо балів! Потрібно ${item.costStars}⭐, а у вас ${_currentProfile.value.points}⭐")
            return
        }
        _rewards.value = _rewards.value.map { r ->
            if (r.id == rewardId) {
                r.copy(
                    status = RewardStatus.PENDING,
                    requestedBy = _currentProfile.value.name,
                    requestTime = "Щойно"
                )
            } else r
        }
        showSnackbar("📨 Запит на нагороду «${item.title}» надіслано батькам на підтвердження!")
    }

    fun instantSpendReward(rewardId: String) {
        val item = _rewards.value.find { it.id == rewardId } ?: return
        if (_currentProfile.value.points < item.costStars) {
            showSnackbar("❌ Недостатньо балів (${item.costStars}⭐)!")
            return
        }
        _currentProfile.value = _currentProfile.value.copy(points = _currentProfile.value.points - item.costStars)
        _rewards.value = _rewards.value.map { r ->
            if (r.id == rewardId) r.copy(status = RewardStatus.APPROVED) else r
        }
        _confettiTrigger.value += 1
        showSnackbar("⚡ Нагороду «${item.title}» успішно активовано! Списано ${item.costStars}⭐")
    }

    fun approveReward(rewardId: String) {
        val item = _rewards.value.find { it.id == rewardId } ?: return
        _rewards.value = _rewards.value.map { r ->
            if (r.id == rewardId) r.copy(status = RewardStatus.APPROVED) else r
        }
        // deduct stars from student
        if (_currentRole.value == UserRole.STUDENT) {
            _currentProfile.value = _currentProfile.value.copy(
                points = (_currentProfile.value.points - item.costStars).coerceAtLeast(0)
            )
        }
        _confettiTrigger.value += 1
        showSnackbar("✅ Нагороду «${item.title}» видано дитині!")
    }

    fun rejectReward(rewardId: String, reason: String = "Виконай ще 2 завдання з математики") {
        val item = _rewards.value.find { it.id == rewardId } ?: return
        _rewards.value = _rewards.value.map { r ->
            if (r.id == rewardId) r.copy(status = RewardStatus.REJECTED, parentComment = reason) else r
        }
        showSnackbar("❌ Запит на нагороду «${item.title}» відхилено з коментарем: $reason")
    }

    fun createCustomReward(title: String, emoji: String, cost: Int) {
        val newReward = RewardItem(
            id = "rew_${System.currentTimeMillis()}",
            title = title,
            iconEmoji = emoji.ifBlank { "🎁" },
            costStars = cost,
            isInstant = false,
            status = RewardStatus.AVAILABLE
        )
        _rewards.value = listOf(newReward) + _rewards.value
        showSnackbar("✨ Нову нагороду «$title» створено!")
    }

    fun toggleBackpackItem(id: String) {
        var allPacked = true
        _backpack.value = _backpack.value.map { item ->
            val updated = if (item.id == id) item.copy(isPacked = !item.isPacked) else item
            if (!updated.isPacked) allPacked = false
            updated
        }
        if (allPacked) {
            _confettiTrigger.value += 1
            addStars(10)
            showSnackbar("🎒 Рюкзак повністю зібрано! +10⭐ за старанність!")
        }
    }

    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val newMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            senderName = _currentProfile.value.name,
            senderRole = _currentRole.value,
            text = text,
            timestamp = "Щойно",
            isMe = true
        )
        _chatMessages.value = _chatMessages.value + newMsg

        // Auto-reply simulation
        viewModelScope.launch {
            delay(1500)
            val replySender = if (_currentRole.value == UserRole.STUDENT) "Олена Коваленко (Мама)" else "Андрій (Учень)"
            val replyRole = if (_currentRole.value == UserRole.STUDENT) UserRole.PARENT else UserRole.STUDENT
            val replyText = if (_currentRole.value == UserRole.STUDENT) {
                "Добре, бачу твоє повідомлення! Бажаю успіхів на уроках 💙💛"
            } else {
                "Дякую, все зрозумів! Вже працюю над уроками."
            }
            _chatMessages.value = _chatMessages.value + ChatMessage(
                id = "msg_${System.currentTimeMillis() + 1}",
                senderName = replySender,
                senderRole = replyRole,
                text = replyText,
                timestamp = "Щойно",
                isMe = false
            )
        }
    }

    fun answerNmtQuestion(questionIdx: Int, selectedOption: Int) {
        val map = _nmtAnswers.value.toMutableMap()
        map[questionIdx] = selectedOption
        _nmtAnswers.value = map

        val correctCount = map.count { (qIdx, optIdx) ->
            SampleData.nmtQuestions.getOrNull(qIdx)?.correctIndex == optIdx
        }
        if (map.size == SampleData.nmtQuestions.size) {
            val score = 100 + ((correctCount.toFloat() / SampleData.nmtQuestions.size) * 100).toInt()
            addStars(15)
            _confettiTrigger.value += 1
            showSnackbar("🎓 Тест завершено! Ваш бал НМТ: $score / 200 (+15⭐)!")
        }
    }

    fun resetNmtTest() {
        _nmtAnswers.value = emptyMap()
    }

    fun gradeStudentSubmission(subId: String, grade: Int, comment: String) {
        _teacherSubmissions.value = _teacherSubmissions.value.map { sub ->
            if (sub.id == subId) {
                sub.copy(teacherGrade = grade, teacherComment = comment)
            } else sub
        }
        showSnackbar("✅ Оцінку $grade/12 та коментар надіслано учневі!")
    }

    fun addGrade(subject: String, value: Int, type: String, comment: String) {
        val newEntry = GradeEntry(
            id = "g_${System.currentTimeMillis()}",
            subject = subject,
            value = value,
            type = type,
            date = "Сьогодні",
            teacherComment = comment
        )
        _subjectReports.value = _subjectReports.value.map { rep ->
            if (rep.subject.equals(subject, ignoreCase = true)) {
                rep.copy(grades = listOf(newEntry) + rep.grades)
            } else rep
        }
        showSnackbar("⭐ Оцінку $value/12 з предмету «$subject» внесено до електронного журналу!")
    }

    fun addChild(name: String, className: String, school: String) {
        val newChild = ChildProfile(
            id = "child_${System.currentTimeMillis()}",
            name = name,
            className = className.ifBlank { "5-А клас" },
            school = school.ifBlank { "Міський ліцей" },
            avatarEmoji = if (name.endsWith("а") || name.endsWith("я")) "👧" else "👦",
            attendanceStatus = "🏫 В школі",
            averageGrade = 10.5,
            stars = 100
        )
        _childrenList.value = _childrenList.value + newChild
        _parentSelectedChild.value = name
        showSnackbar("👨‍👩‍👧‍👦 Дитину «$name» успішно додано до батьківського профілю!")
    }

    fun addBackpackItem(name: String, subject: String, category: String) {
        val newItem = BackpackItem(
            id = "bp_${System.currentTimeMillis()}",
            name = name,
            subject = subject.ifBlank { "Загальне" },
            isPacked = false,
            category = category.ifBlank { "Приладдя" }
        )
        _backpack.value = _backpack.value + newItem
        showSnackbar("🎒 «$name» додано до списку речей у рюкзак!")
    }

    fun autoPackForTomorrow() {
        _backpack.value = _backpack.value.map { it.copy(isPacked = true) }
        _confettiTrigger.value += 1
        addStars(10)
        showSnackbar("🎒 Всі необхідні підручники та зошити зібрано на завтра (+10⭐)!")
    }

    fun addLessonToSchedule(lesson: Lesson) {
        _lessons.value = _lessons.value + lesson
        showSnackbar("📅 Урок «${lesson.subject}» додано до розкладу!")
    }

    fun setParentChild(childName: String) {
        _parentSelectedChild.value = childName
    }

    fun setTeacherClass(className: String) {
        _teacherSelectedClass.value = className
    }

    fun addStars(amount: Int) {
        _currentProfile.value = _currentProfile.value.copy(points = _currentProfile.value.points + amount)
    }

    fun requestGradeAiDiagnostic(onComplete: (String) -> Unit) {
        viewModelScope.launch {
            _isEvaluatingAi.value = true
            try {
                val diagnostic = AiService.generateGradeDiagnostic(_subjectReports.value)
                onComplete(diagnostic)
            } finally {
                _isEvaluatingAi.value = false
            }
        }
    }

    fun requestTeacherTasksAi(subject: String, topic: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            _isEvaluatingAi.value = true
            try {
                val tasks = AiService.generateTeacherPracticeTasks(subject, topic)
                onComplete(tasks)
            } finally {
                _isEvaluatingAi.value = false
            }
        }
    }

    fun requestHomeworkHintAi(subject: String, title: String, desc: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            _isEvaluatingAi.value = true
            try {
                val hint = AiService.generateHomeworkHint(subject, title, desc)
                onComplete(hint)
            } finally {
                _isEvaluatingAi.value = false
            }
        }
    }

    fun requestParentAdviceAi(childName: String, onComplete: (String) -> Unit) {
        viewModelScope.launch {
            val advice = AiService.generateParentAdvice(childName, _lessons.value.size)
            onComplete(advice)
        }
    }

    fun showSnackbar(message: String) {
        _snackbarMessage.value = message
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}
