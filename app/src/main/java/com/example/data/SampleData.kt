package com.example.data

import com.example.model.*

object SampleData {

    val studentProfile = UserProfile(
        id = "std_1",
        name = "Андрій Коваленко",
        telegramHandle = "@andriy_koval",
        role = UserRole.STUDENT,
        points = 128,
        streakDays = 12,
        gradeText = "8-А клас",
        schoolName = "Ліцей №15 «Лідер»",
        city = "Київ",
        className = "8-А клас",
        subject = "",
        login = "andriy_koval",
        personalCode = "839214"
    )

    val parentProfile = UserProfile(
        id = "par_1",
        name = "Олена Коваленко",
        telegramHandle = "@olena_kovalenko",
        role = UserRole.PARENT,
        points = 894,
        streakDays = 30,
        gradeText = "Мама (Андрій, Оля)",
        schoolName = "Сім'я Коваленків",
        city = "Київ",
        className = "8-А клас",
        subject = "",
        login = "olena_mama",
        personalCode = "192834"
    )

    val teacherProfile = UserProfile(
        id = "tch_1",
        name = "Олена Петренко",
        telegramHandle = "@petrenko_math",
        role = UserRole.TEACHER,
        points = 450,
        streakDays = 45,
        gradeText = "Вчитель алгебри та геометрії",
        schoolName = "Ліцей №15 «Лідер»",
        city = "Київ",
        className = "7-А, 8-Б класи",
        subject = "Математика / Алгебра",
        login = "petrenko_math",
        personalCode = "582194"
    )

    val tutorProfile = UserProfile(
        id = "tut_1",
        name = "Олег Петренко",
        telegramHandle = "@oleg_tutor",
        role = UserRole.TUTOR,
        points = 320,
        streakDays = 20,
        gradeText = "Репетитор НМТ (Математика)",
        schoolName = "Освітній хаб «Знання»",
        city = "Київ",
        className = "Індивідуальні учні",
        subject = "Математика / Алгебра",
        login = "oleg_math",
        personalCode = "391058"
    )

    val englishTutorProfile = UserProfile(
        id = "tut_2",
        name = "Сергій Бондар",
        telegramHandle = "@bondar_english",
        role = UserRole.TUTOR,
        points = 290,
        streakDays = 15,
        gradeText = "Репетитор (Англійська мова)",
        schoolName = "Language Excellence Club",
        city = "Київ",
        className = "Групи та індивідуальні",
        subject = "Англійська мова",
        login = "bondar_english",
        personalCode = "472910"
    )

    val initialTutors = listOf(
        TutorProfile(
            id = "tut_math",
            name = "Олег Петренко",
            subject = "Математика / Алгебра",
            phone = "+380 67 111 2233",
            rating = 4.95,
            hourlyRate = "350 грн/год",
            isOnlineLessonReady = true,
            nextLessonTime = "Сьогодні, 16:30",
            meetUrl = "https://meet.google.com/math-tutor-ukr",
            avatarEmoji = "📐",
            bio = "Експерт з підготовки до НМТ та ДПА з математики. 120+ учнів вступили на бюджет."
        ),
        TutorProfile(
            id = "tut_eng",
            name = "Сергій Бондар",
            subject = "Англійська мова",
            phone = "+380 50 444 5566",
            rating = 4.98,
            hourlyRate = "400 грн/год",
            isOnlineLessonReady = true,
            nextLessonTime = "Завтра, 15:00",
            meetUrl = "https://meet.google.com/english-tutor-ukr",
            avatarEmoji = "🇬🇧",
            bio = "Сертифікат Cambridge CELTA, C2 Mastery. Розмовна практика, граматика та НМТ."
        ),
        TutorProfile(
            id = "tut_phys",
            name = "Наталія Шевчук",
            subject = "Фізика",
            phone = "+380 93 777 8899",
            rating = 4.90,
            hourlyRate = "320 грн/год",
            isOnlineLessonReady = true,
            nextLessonTime = "Чт, 17:00",
            meetUrl = "https://meet.google.com/physics-tutor-ukr",
            avatarEmoji = "⚛️",
            bio = "Кандидат фіз.-мат. наук. Просте пояснення законів механіки, електрики та оптики."
        ),
        TutorProfile(
            id = "tut_ukr",
            name = "Ірина Коваленко",
            subject = "Українська мова та література",
            phone = "+380 97 222 3344",
            rating = 5.0,
            hourlyRate = "350 грн/год",
            isOnlineLessonReady = true,
            nextLessonTime = "Пт, 16:00",
            meetUrl = "https://meet.google.com/ukrmova-tutor-ukr",
            avatarEmoji = "📖",
            bio = "Вчитель вищої категорії. Автор методики легкого запам'ятовування наголосів і винятків."
        ),
        TutorProfile(
            id = "tut_hist",
            name = "Тарас Мельник",
            subject = "Історія України",
            phone = "+380 63 999 0011",
            rating = 4.92,
            hourlyRate = "300 грн/год",
            isOnlineLessonReady = false,
            nextLessonTime = "Сб, 11:00",
            meetUrl = "https://meet.google.com/history-tutor-ukr",
            avatarEmoji = "🏛️",
            bio = "Хронологічні таблиці, асоціативні карти та повне охоплення всіх періодів програми МОН."
        )
    )

    val initialLinkRequests = listOf(
        ChildLinkRequest(
            id = "req_1",
            childName = "Андрій",
            childSurname = "Коваленко",
            requesterName = "Олена Коваленко",
            requesterRole = UserRole.PARENT,
            requesterSubject = "",
            code = "742918",
            timestamp = "5 хв тому",
            isConfirmed = false
        ),
        ChildLinkRequest(
            id = "req_2",
            childName = "Андрій",
            childSurname = "Коваленко",
            requesterName = "Сергій Бондар",
            requesterRole = UserRole.TUTOR,
            requesterSubject = "Англійська мова",
            code = "839214",
            timestamp = "12 хв тому",
            isConfirmed = false
        )
    )

    val weekDays = listOf(
        Pair("Пн", "24 січ"),
        Pair("Вт", "25 січ"),
        Pair("Ср", "26 січ"),
        Pair("Чт", "27 січ"),
        Pair("Пт", "28 січ"),
        Pair("Сб", "29 січ"),
        Pair("Нд", "30 січ")
    )

    val currentLesson = Lesson(
        id = "les_current",
        subject = "Алгебра",
        teacher = "Олег Петренко",
        room = "Кабінет 304",
        floor = "3 поверх",
        textbookPages = "стор. 112–115",
        topic = "Квадратні рівняння та теорема Вієта",
        startTime = "09:00",
        endTime = "09:45",
        meetUrl = "https://meet.google.com/xyz-algebra-ukr",
        isOnline = true,
        iconEmoji = "√x"
    )

    val nextLesson = Lesson(
        id = "les_next",
        subject = "Історія України",
        teacher = "Василь Мельник",
        room = "Кабінет 106",
        floor = "1 поверх",
        textbookPages = "стор. 55–62",
        topic = "Гетьманщина у другій половині XVII ст.",
        startTime = "10:00",
        endTime = "10:45",
        meetUrl = "https://meet.google.com/history-ukr-106",
        isOnline = false,
        iconEmoji = "🏛️"
    )

    val scheduleForToday = listOf(
        currentLesson,
        nextLesson,
        Lesson(
            id = "les_3",
            subject = "Українська мова",
            teacher = "Ірина Василівна",
            room = "Кабінет 214",
            floor = "2 поверх",
            textbookPages = "стор. 84–89",
            topic = "Однорідні члени речення та розділові знаки",
            startTime = "11:00",
            endTime = "11:45",
            isOnline = false,
            iconEmoji = "📖"
        ),
        Lesson(
            id = "les_4",
            subject = "Фізика",
            teacher = "Микола Іванович",
            room = "Кабінет 302",
            floor = "3 поверх",
            textbookPages = "стор. 40–45",
            topic = "Закон Архімеда. Плавання тіл",
            startTime = "12:00",
            endTime = "12:45",
            isOnline = true,
            iconEmoji = "🔬"
        ),
        Lesson(
            id = "les_5",
            subject = "Англійська мова",
            teacher = "Sarah Jenkins",
            room = "Кабінет 112",
            floor = "1 поверх",
            textbookPages = "стор. 72–75",
            topic = "Present Perfect Continuous vs Past Simple",
            startTime = "13:00",
            endTime = "13:45",
            isOnline = true,
            iconEmoji = "🌍"
        )
    )

    val quickStatuses = listOf(
        QuickStatusOption(
            id = "qs_1",
            title = "Я в школі",
            emoji = "🏫",
            bonusStars = 2,
            description = "Сповістити батьків про безпечне прибуття"
        ),
        QuickStatusOption(
            id = "qs_2",
            title = "Їду додому",
            emoji = "🏠",
            bonusStars = 2,
            description = "Уроки закінчилися, прямую додому"
        ),
        QuickStatusOption(
            id = "qs_3",
            title = "Роблю ДЗ",
            emoji = "📚",
            bonusStars = 3,
            description = "Сфокусований на навчанні та завданнях"
        ),
        QuickStatusOption(
            id = "qs_4",
            title = "ДЗ готово",
            emoji = "✅",
            bonusStars = 5,
            description = "Всі уроки виконані, час для відпочинку"
        )
    )

    val initialHomework = listOf(
        Homework(
            id = "hw_1",
            subject = "Алгебра",
            taskTitle = "№ 542 (1-4), № 546",
            description = "Розв'язати повні квадратні рівняння через формулу дискримінанта. Перевірити корені за теоремою Вієта.",
            priority = Priority.URGENT,
            deadline = "Сьогодні, до 18:00",
            textbookPages = "стор. 114",
            isDone = false,
            pointsReward = 5
        ),
        Homework(
            id = "hw_2",
            subject = "Історія України",
            taskTitle = "Параграф 14, конспект",
            description = "Скласти хронологічну таблицю походів та реформ Петра Дорошенка.",
            priority = Priority.IMPORTANT,
            deadline = "Завтра, 08:30",
            textbookPages = "стор. 58–61",
            isDone = true,
            pointsReward = 5,
            aiEvaluation = AiHomeworkEvaluation(
                grade = 11,
                summary = "Відмінний конспект! Хронологія точна, виділено головні наслідки.",
                stepByStep = listOf(
                    "Вступ та передумови гетьманування — висвітлено чітко",
                    "Дати 1665–1676 рр. зазначені без помилок",
                    "Висновок про об'єднання Правобережжя та Лівобережжя грамотно сформульовано"
                ),
                errorExplanation = "Невелике зауваження: уточнити дату Корсунської ради 1669 р.",
                advice = "Повторити карту розподілу впливу Речі Посполитої та Османської імперії."
            )
        ),
        Homework(
            id = "hw_3",
            subject = "Англійська мова",
            taskTitle = "Unit 6, Exercise 4 (Essay)",
            description = "Write 80-100 words about the environmental protection in Ukraine using Present Perfect.",
            priority = Priority.NORMAL,
            deadline = "П'ятниця, 09:00",
            textbookPages = "стор. 74",
            isDone = false,
            pointsReward = 5
        ),
        Homework(
            id = "hw_4",
            subject = "Фізика",
            taskTitle = "Вправа 21 (задачі 2, 3)",
            description = "Обчислити виштовхувальну силу, що діє на занурене тіло в прісну та солону воду.",
            priority = Priority.IMPORTANT,
            deadline = "Завтра, 11:30",
            textbookPages = "стор. 43",
            isDone = false,
            pointsReward = 5
        )
    )

    val initialRewards = listOf(
        RewardItem(
            id = "rew_1",
            title = "Кіно з друзями",
            iconEmoji = "🍿",
            costStars = 120,
            isInstant = false,
            requestedBy = "Андрій",
            requestTime = "17 січ, 14:20",
            status = RewardStatus.PENDING
        ),
        RewardItem(
            id = "rew_2",
            title = "Нова комп'ютерна гра",
            iconEmoji = "🎮",
            costStars = 200,
            isInstant = false,
            requestedBy = "Андрій",
            requestTime = "16 січ, 19:10",
            status = RewardStatus.PENDING
        ),
        RewardItem(
            id = "rew_3",
            title = "Книга «Гаррі Поттер»",
            iconEmoji = "📖",
            costStars = 150,
            isInstant = false,
            requestedBy = "Андрій",
            requestTime = "14 січ, 12:00",
            status = RewardStatus.PENDING
        ),
        RewardItem(
            id = "rew_4",
            title = "Піца з батьками у вихідний",
            iconEmoji = "🍕",
            costStars = 100,
            isInstant = false,
            status = RewardStatus.AVAILABLE
        ),
        RewardItem(
            id = "rew_5",
            title = "+1 година за комп'ютером",
            iconEmoji = "⚡",
            costStars = 50,
            isInstant = true,
            status = RewardStatus.AVAILABLE
        ),
        RewardItem(
            id = "rew_6",
            title = "Смачний десерт у кав'ярні",
            iconEmoji = "🍦",
            costStars = 40,
            isInstant = true,
            status = RewardStatus.AVAILABLE
        )
    )

    val initialBackpack = listOf(
        BackpackItem("bp_1", "Підручник «Алгебра 8 клас» (Істер)", "Алгебра", true, "Підручник"),
        BackpackItem("bp_2", "Зошит у клітинку (24 арк.)", "Алгебра", true, "Зошит"),
        BackpackItem("bp_3", "Підручник «Історія України 8 клас» (Гісем)", "Історія", true, "Підручник"),
        BackpackItem("bp_4", "Атлас та контурні карти", "Історія", false, "Приладдя"),
        BackpackItem("bp_5", "Підручник «Українська мова 8 клас» (Заболотний)", "Укр. мова", false, "Підручник"),
        BackpackItem("bp_6", "Пенал (сині ручки, олівець, гумка)", "Загальне", true, "Приладдя"),
        BackpackItem("bp_7", "Лінійка, трикутник та циркуль", "Геометрія", false, "Приладдя"),
        BackpackItem("bp_8", "Спортивна форма та змінне взуття", "Фізкультура", false, "Екіпірування")
    )

    val nmtQuestions = listOf(
        NmtQuestion(
            id = "nmt_1",
            subject = "Математика",
            question = "Знайдіть корені рівняння: 2x² - 5x + 2 = 0",
            options = listOf("x₁ = 2, x₂ = 0.5", "x₁ = -2, x₂ = -0.5", "x₁ = 1, x₂ = 4", "x₁ = -1, x₂ = 2.5"),
            correctIndex = 0,
            explanation = "D = (-5)² - 4·2·2 = 25 - 16 = 9. √D = 3. x₁ = (5 + 3)/4 = 2; x₂ = (5 - 3)/4 = 0.5."
        ),
        NmtQuestion(
            id = "nmt_2",
            subject = "Українська мова",
            question = "Усі слова пишуться з апострофом у рядку:",
            options = listOf(
                "під..їзд, зв..язок, торф..яний",
                "моркв..яний, свято, прем..єра",
                "духм..яний, рутв..яний, в..юн",
                "мавп..ячий, різдв..яний, м..ята"
            ),
            correctIndex = 0,
            explanation = "Після губних (б, п, в, м, ф) перед я, ю, є, ї апостроф ставиться, якщо перед ними немає іншого кореневого приголосного: під'їзд (після префікса), зв'язок (з- префікс), торф'яний (після рф). У словах морквяний, свято, духмяний апостроф не пишеться."
        ),
        NmtQuestion(
            id = "nmt_3",
            subject = "Історія України",
            question = "У якому році було укладено Люблінську унію?",
            options = listOf("1569 р.", "1596 р.", "1648 р.", "1556 р."),
            correctIndex = 0,
            explanation = "Люблінська унія — угода про об'єднання Королівства Польського та Великого князівства Литовського в єдину державу Річ Посполиту — була укладена 1 липня 1569 року."
        ),
        NmtQuestion(
            id = "nmt_4",
            subject = "Англійська мова",
            question = "Choose the correct sentence in Present Perfect Continuous:",
            options = listOf(
                "She has been studying Ukrainian history for three years.",
                "She had studied Ukrainian history yesterday.",
                "She is studying history since morning.",
                "She studied Ukrainian history already."
            ),
            correctIndex = 0,
            explanation = "'has been studying' demonstrates an action that started in the past and is still ongoing."
        )
    )

    val teacherSubmissions = listOf(
        StudentSubmission(
            id = "sub_1",
            studentName = "Олег Петренко",
            className = "7-А клас",
            subject = "Алгебра",
            taskTitle = "№ 542 — Розв'язання через D",
            submittedAt = "Вчора, 18:40",
            solutionText = "2x^2 - 8x + 6 = 0; D = 64 - 48 = 16; x1 = (8+4)/4 = 3; x2 = (8-4)/4 = 1. Перевірка: 3*1 = 3 = c/a.",
            teacherGrade = null,
            aiGradeSuggestion = 12,
            aiEvaluationSummary = "Ідеальне виконання: дискримінант знайдено правильно, корені вірні, перевірку за Вієтом наведено."
        ),
        StudentSubmission(
            id = "sub_2",
            studentName = "Марія Ткаченко",
            className = "8-Б клас",
            subject = "Геометрія",
            taskTitle = "Властивості вписаного кута",
            submittedAt = "Сьогодні, 08:15",
            solutionText = "Кут BAC = 1/2 дуги BC. Дуга BC = 80 градусів, тому кут BAC = 40 градусів.",
            teacherGrade = 11,
            teacherComment = "Чудово, але не забудь накреслити малюнок у зошиті!",
            aiGradeSuggestion = 11,
            aiEvaluationSummary = "Теорему застосовано коректно, результат правильний."
        ),
        StudentSubmission(
            id = "sub_3",
            studentName = "Дмитро Коваль",
            className = "7-А клас",
            subject = "Алгебра",
            taskTitle = "№ 546 — Рівняння",
            submittedAt = "Вчора, 20:10",
            solutionText = "x^2 - 4x - 5 = 0; D = 16 + 20 = 36; x1 = (4+6)/2 = 5; x2 = (4-6)/2 = -1.",
            teacherGrade = null,
            aiGradeSuggestion = 12,
            aiEvaluationSummary = "Правильно розраховано корені з урахуванням знаків коефіцієнтів."
        ),
        StudentSubmission(
            id = "sub_4",
            studentName = "Анна Васильченко",
            className = "9-А клас",
            subject = "Історія України",
            taskTitle = "Есе: Українська революція",
            submittedAt = "Сьогодні, 09:30",
            solutionText = "Перший Універсал Центральної Ради проголосив автономію України у складі федеративної республіки...",
            teacherGrade = null,
            aiGradeSuggestion = 12,
            aiEvaluationSummary = "Глибоке розуміння історичного контексту та точні терміни."
        )
    )

    val textbooks = listOf(
        TextbookItem("tb_1", "Алгебра 8 клас", "О.С. Істер", "8 клас", 240, "18.4 МБ"),
        TextbookItem("tb_2", "Геометрія 8 клас", "А.Г. Мерзляк", "8 клас", 208, "16.1 МБ"),
        TextbookItem("tb_3", "Українська мова 8 клас", "О.В. Заболотний", "8 клас", 224, "15.8 МБ"),
        TextbookItem("tb_4", "Історія України 8 клас", "О.В. Гісем", "8 клас", 256, "22.5 МБ"),
        TextbookItem("tb_5", "Фізика 8 клас", "В.Г. Бар'яхтар", "8 клас", 240, "19.0 МБ")
    )

    val initialChatMessages = listOf(
        ChatMessage("msg_1", "Олена Коваленко", UserRole.PARENT, "Привіт, синку! Як перший урок пройшов?", "09:50", false),
        ChatMessage("msg_2", "Андрій Коваленко", UserRole.STUDENT, "Привіт, мамо! Чудово, на алгебрі отримав 11 балів за тему рівнянь!", "09:52", true),
        ChatMessage("msg_3", "Олена Коваленко", UserRole.PARENT, "Пишаюся тобою! Я бачила твій запит на кіно у додатку, сьогодні обов'язково схвалю 🍿", "09:55", false),
        ChatMessage("msg_4", "Андрій Коваленко", UserRole.STUDENT, "Дякую! Вже збираю рюкзак на завтра і зроблю ДЗ вчасно!", "09:56", true)
    )

    val childrenList = listOf(
        ChildProfile(
            id = "child_1",
            name = "Андрій",
            className = "8-А клас",
            school = "Ліцей №15 «Лідер»",
            avatarEmoji = "👦",
            attendanceStatus = "🏫 В школі (Каб. 304)",
            averageGrade = 10.6,
            stars = 128
        ),
        ChildProfile(
            id = "child_2",
            name = "Оля",
            className = "4-Б клас",
            school = "Гімназія №31",
            avatarEmoji = "👧",
            attendanceStatus = "🏠 Дома (ДЗ виконано)",
            averageGrade = 11.2,
            stars = 240
        )
    )

    val initialSubjectReports = listOf(
        SubjectReport(
            subject = "Алгебра",
            teacher = "Оксана Петренко",
            iconEmoji = "📐",
            targetGrade = 12,
            grades = listOf(
                GradeEntry("g_1", "Алгебра", 11, "Самостійна", "18 січ", "Відмінний розрахунок дискримінанта"),
                GradeEntry("g_2", "Алгебра", 10, "ДЗ", "16 січ", "Акуратне оформлення"),
                GradeEntry("g_3", "Алгебра", 12, "Контрольна", "12 січ", "Бездоганне розв'язання"),
                GradeEntry("g_4", "Алгебра", 11, "Відповідь", "10 січ", "Усна робота біля дошки")
            )
        ),
        SubjectReport(
            subject = "Геометрія",
            teacher = "Оксана Петренко",
            iconEmoji = "📏",
            targetGrade = 11,
            grades = listOf(
                GradeEntry("g_5", "Геометрія", 10, "Самостійна", "17 січ", "Теорема про вписаний кут"),
                GradeEntry("g_6", "Геометрія", 11, "Контрольна", "11 січ", "Властивості прямокутного трикутника")
            )
        ),
        SubjectReport(
            subject = "Українська мова",
            teacher = "Ірина Василівна",
            iconEmoji = "📖",
            targetGrade = 12,
            grades = listOf(
                GradeEntry("g_7", "Українська мова", 12, "Диктант", "19 січ", "Жодної орфографічної помилки!"),
                GradeEntry("g_8", "Українська мова", 11, "Відповідь", "15 січ", "Однорідні члени речення"),
                GradeEntry("g_9", "Українська мова", 12, "Твір", "09 січ", "Багата лексика")
            )
        ),
        SubjectReport(
            subject = "Українська література",
            teacher = "Ірина Василівна",
            iconEmoji = "📚",
            targetGrade = 11,
            grades = listOf(
                GradeEntry("g_10", "Українська література", 11, "Аналіз твору", "17 січ", "Іван Франко «Захар Беркут»"),
                GradeEntry("g_11", "Українська література", 10, "Тест", "13 січ", "Теорія літератури")
            )
        ),
        SubjectReport(
            subject = "Історія України",
            teacher = "Василь Мельник",
            iconEmoji = "🏛️",
            targetGrade = 11,
            grades = listOf(
                GradeEntry("g_12", "Історія України", 11, "Конспект", "18 січ", "Хронологія доби Руїни"),
                GradeEntry("g_13", "Історія України", 12, "Тест", "14 січ", "Гетьман Петро Дорошенко")
            )
        ),
        SubjectReport(
            subject = "Фізика",
            teacher = "Микола Іванович",
            iconEmoji = "🔬",
            targetGrade = 10,
            grades = listOf(
                GradeEntry("g_14", "Фізика", 10, "Лабораторна", "16 січ", "Визначення виштовхувальної сили"),
                GradeEntry("g_15", "Фізика", 9, "Самостійна", "10 січ", "Закон Архімеда")
            )
        ),
        SubjectReport(
            subject = "Англійська мова",
            teacher = "Sarah Jenkins",
            iconEmoji = "🌍",
            targetGrade = 12,
            grades = listOf(
                GradeEntry("g_16", "Англійська мова", 12, "Speaking", "19 січ", "Fluent dialogue, great vocabulary"),
                GradeEntry("g_17", "Англійська мова", 11, "Essay", "14 січ", "Present Perfect Continuous")
            )
        ),
        SubjectReport(
            subject = "Хімія",
            teacher = "Світлана Анатоліївна",
            iconEmoji = "🧪",
            targetGrade = 11,
            grades = listOf(
                GradeEntry("g_18", "Хімія", 11, "Практична", "15 січ", "Класифікація неорганічних сполук"),
                GradeEntry("g_19", "Хімія", 10, "ДЗ", "08 січ", "Рівняння реакцій")
            )
        ),
        SubjectReport(
            subject = "Біологія",
            teacher = "Ольга Степанівна",
            iconEmoji = "🌿",
            targetGrade = 12,
            grades = listOf(
                GradeEntry("g_20", "Біологія", 12, "Проєкт", "17 січ", "Будова клітини рослин та тварин")
            )
        )
    )

    fun getScheduleForDay(dayIndex: Int): List<Lesson> {
        return when (dayIndex) {
            0 -> scheduleForToday // Monday
            1 -> listOf( // Tuesday
                Lesson("tue_1", "Геометрія", "Оксана Петренко", "Каб. 304", "3 поверх", "стор. 44–48", "Вписані та описані чотирикутники", "08:30", "09:15", "https://meet.google.com/xyz-geom-ukr", true, "📏"),
                Lesson("tue_2", "Українська мова", "Ірина Василівна", "Каб. 214", "2 поверх", "стор. 90–94", "Звертання та вставні слова", "09:25", "10:10", "https://meet.google.com/xyz-ukrmova-ukr", false, "📖"),
                Lesson("tue_3", "Фізика", "Микола Іванович", "Каб. 302", "3 поверх", "стор. 48–52", "Умови плавання тіл", "10:25", "11:10", "https://meet.google.com/xyz-physics-ukr", true, "🔬"),
                Lesson("tue_4", "Історія України", "Василь Мельник", "Каб. 106", "1 поверх", "стор. 64–70", "Чигиринські походи", "11:30", "12:15", "https://meet.google.com/xyz-history-ukr", false, "🏛️"),
                Lesson("tue_5", "Фізкультура", "Віктор Павлович", "Спортзал", "1 поверх", "-", "Волейбол: подача та прийом м'яча", "12:35", "13:20", "", false, "⚽")
            )
            2 -> listOf( // Wednesday
                Lesson("wed_1", "Алгебра", "Оксана Петренко", "Каб. 304", "3 поверх", "стор. 118–122", "Розкладання тричлена на множники", "08:30", "09:15", "https://meet.google.com/xyz-algebra-ukr", true, "📐"),
                Lesson("wed_2", "Англійська мова", "Sarah Jenkins", "Каб. 112", "1 поверх", "стор. 78–82", "Reading & Discussion: Green Planet", "09:25", "10:10", "https://meet.google.com/xyz-english-ukr", true, "🌍"),
                Lesson("wed_3", "Хімія", "Світлана Анатоліївна", "Каб. 208", "2 поверх", "стор. 55–60", "Оксиди, основи та кислоти", "10:25", "11:10", "https://meet.google.com/xyz-chemistry-ukr", false, "🧪"),
                Lesson("wed_4", "Українська література", "Ірина Василівна", "Каб. 214", "2 поверх", "стор. 102–108", "Михайло Коцюбинський «Дорогою ціною»", "11:30", "12:15", "https://meet.google.com/xyz-ukrlit-ukr", false, "📚"),
                Lesson("wed_5", "Географія", "Людмила Тарасівна", "Каб. 310", "3 поверх", "стор. 70–75", "Рельєф та корисні копалини України", "12:35", "13:20", "https://meet.google.com/xyz-geography-ukr", true, "🗺️")
            )
            3 -> listOf( // Thursday
                Lesson("thu_1", "Біологія", "Ольга Степанівна", "Каб. 202", "2 поверх", "стор. 85–90", "Опорно-рухова система людини", "08:30", "09:15", "https://meet.google.com/xyz-biology-ukr", false, "🌿"),
                Lesson("thu_2", "Геометрія", "Оксана Петренко", "Каб. 304", "3 поверх", "стор. 50–55", "Розв'язування задач підвищеної складності", "09:25", "10:10", "https://meet.google.com/xyz-geom-ukr", true, "📏"),
                Lesson("thu_3", "Зарубіжна література", "Наталія Олександрівна", "Каб. 210", "2 поверх", "стор. 60–65", "Вільям Шекспір «Ромео і Джульєтта»", "10:25", "11:10", "https://meet.google.com/xyz-lit-ukr", false, "🎭"),
                Lesson("thu_4", "Інформатика", "Андрій Вікторович", "Комп. клас 2", "2 поверх", "стор. 95–102", "Основи алгоритмізації на Python", "11:30", "12:15", "https://meet.google.com/xyz-cs-ukr", true, "💻"),
                Lesson("thu_5", "Англійська мова", "Sarah Jenkins", "Каб. 112", "1 поверх", "стор. 84–88", "Grammar Workshop: Conditionals", "12:35", "13:20", "https://meet.google.com/xyz-english-ukr", true, "🌍")
            )
            4 -> listOf( // Friday
                Lesson("fri_1", "Алгебра", "Оксана Петренко", "Каб. 304", "3 поверх", "стор. 125–129", "Підсумкова робота з теми «Квадратні рівняння»", "08:30", "09:15", "https://meet.google.com/xyz-algebra-ukr", true, "📐"),
                Lesson("fri_2", "Українська мова", "Ірина Василівна", "Каб. 214", "2 поверх", "стор. 96–100", "Контрольний диктант", "09:25", "10:10", "https://meet.google.com/xyz-ukrmova-ukr", false, "📖"),
                Lesson("fri_3", "Історія України", "Василь Мельник", "Каб. 106", "1 поверх", "стор. 72–78", "Культура України другої половини XVII ст.", "10:25", "11:10", "https://meet.google.com/xyz-history-ukr", false, "🏛️"),
                Lesson("fri_4", "Мистецтво", "Тетяна Юріївна", "Актова зала", "1 поверх", "стор. 30–35", "Українське бароко в архітектурі та живописі", "11:30", "12:15", "", false, "🎨")
            )
            else -> listOf( // Weekend / Extra
                Lesson("sat_1", "НМТ Підготовка (Математика)", "Олег Петренко", "Онлайн", "-", "Тест № 4", "Тренінг завдань закритої форми", "10:00", "11:30", "https://meet.google.com/xyz-nmt-prep", true, "🎓"),
                Lesson("sat_2", "Розмовний клуб (English)", "Sarah Jenkins", "Онлайн", "-", "Speaking Club", "Interactive debate on Science & AI", "12:00", "13:00", "https://meet.google.com/xyz-speaking-ukr", true, "🗣️")
            )
        }
    }
}

