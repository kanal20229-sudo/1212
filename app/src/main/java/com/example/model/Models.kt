package com.example.model

enum class UserRole(val title: String, val subtitle: String, val emoji: String) {
    STUDENT("Учень", "розклад, ДЗ, бали", "🎒"),
    PARENT("Батьки", "контроль і нагороди", "👨‍👩‍👧‍👦"),
    TEACHER("Вчитель", "класи і домашні завдання", "🎓"),
    TUTOR("Репетитор", "учні і заняття", "📚")
}

enum class DayFormat(val title: String, val emoji: String) {
    SCHOOL("Школа", "🏫"),
    ONLINE("Онлайн", "💻"),
    MIXED("Змішаний", "🔄")
}

enum class Priority(val title: String, val colorHex: Long) {
    NORMAL("Звичайне", 0xFF3B82F6),
    IMPORTANT("Важливе", 0xFFF59E0B),
    URGENT("Термінове", 0xFFEF4444)
}

enum class RewardStatus {
    AVAILABLE,
    PENDING,
    APPROVED,
    REJECTED
}

data class UserProfile(
    val id: String,
    val name: String,
    val telegramHandle: String,
    val role: UserRole,
    val points: Int,
    val streakDays: Int,
    val gradeText: String,
    val schoolName: String = "Ліцей №15 «Лідер»",
    val city: String = "Київ",
    val className: String = "8-А клас",
    val subject: String = "",
    val login: String = "user",
    val personalCode: String = "839214"
)

data class TutorProfile(
    val id: String,
    val name: String,
    val subject: String,
    val phone: String,
    val rating: Double,
    val hourlyRate: String,
    val isOnlineLessonReady: Boolean = true,
    val nextLessonTime: String,
    val meetUrl: String = "https://meet.google.com/tutor-lesson-ukr",
    val avatarEmoji: String = "🧑‍🏫",
    val bio: String = "Викладач з 8-річним досвідом підготовки до НМТ"
)

data class ChildLinkRequest(
    val id: String,
    val childName: String,
    val childSurname: String,
    val requesterName: String,
    val requesterRole: UserRole,
    val requesterSubject: String = "",
    val code: String,
    val timestamp: String = "Щойно",
    val isConfirmed: Boolean = false
)

data class Lesson(
    val id: String,
    val subject: String,
    val teacher: String,
    val room: String,
    val floor: String,
    val textbookPages: String,
    val topic: String,
    val startTime: String,
    val endTime: String,
    val meetUrl: String = "https://meet.google.com/abc-school-ukr",
    val isOnline: Boolean = true,
    val iconEmoji: String = "📐"
)

data class AiHomeworkEvaluation(
    val grade: Int, // Ukrainian 1-12 scale
    val summary: String,
    val stepByStep: List<String>,
    val errorExplanation: String,
    val advice: String,
    val pointsEarned: Int = 10
)

data class Homework(
    val id: String,
    val subject: String,
    val taskTitle: String,
    val description: String,
    val priority: Priority,
    val deadline: String,
    val textbookPages: String,
    val isDone: Boolean = false,
    val photoProofUri: String? = null,
    val aiEvaluation: AiHomeworkEvaluation? = null,
    val pointsReward: Int = 5
)

data class RewardItem(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val costStars: Int,
    val isInstant: Boolean = false,
    val requestedBy: String? = null,
    val requestTime: String? = null,
    val status: RewardStatus = RewardStatus.AVAILABLE,
    val parentComment: String? = null
)

data class BackpackItem(
    val id: String,
    val name: String,
    val subject: String,
    val isPacked: Boolean = false,
    val category: String = "Підручник"
)

data class QuickStatusOption(
    val id: String,
    val title: String,
    val emoji: String,
    val bonusStars: Int,
    val description: String
)

data class NmtQuestion(
    val id: String,
    val subject: String,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class StudentSubmission(
    val id: String,
    val studentName: String,
    val className: String,
    val subject: String,
    val taskTitle: String,
    val submittedAt: String,
    val solutionText: String,
    val teacherGrade: Int? = null,
    val teacherComment: String? = null,
    val aiGradeSuggestion: Int = 11,
    val aiEvaluationSummary: String = "Вірно розв'язано квадратне рівняння через дискримінант. Чисто оформлено."
)

data class ChatMessage(
    val id: String,
    val senderName: String,
    val senderRole: UserRole,
    val text: String,
    val timestamp: String,
    val isMe: Boolean = false,
    val isVoice: Boolean = false
)

data class TextbookItem(
    val id: String,
    val title: String,
    val author: String,
    val grade: String,
    val pagesCount: Int,
    val pdfSize: String
)

data class GradeEntry(
    val id: String,
    val subject: String,
    val value: Int, // 1..12
    val type: String, // "Відповідь", "Самостійна", "Контрольна", "ДЗ", "Тематична"
    val date: String,
    val teacherComment: String = ""
)

data class SubjectReport(
    val subject: String,
    val teacher: String,
    val iconEmoji: String,
    val grades: List<GradeEntry>,
    val targetGrade: Int = 11
) {
    val averageGrade: Double
        get() = if (grades.isNotEmpty()) {
            Math.round(grades.map { it.value }.average() * 10.0) / 10.0
        } else 10.0
}

data class ChildProfile(
    val id: String,
    val name: String,
    val className: String,
    val school: String,
    val avatarEmoji: String,
    val attendanceStatus: String,
    val averageGrade: Double,
    val stars: Int
)

