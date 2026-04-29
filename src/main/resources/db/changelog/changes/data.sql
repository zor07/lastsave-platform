-- cleanup (FK order)
DELETE FROM student_repository;
DELETE FROM student_progress;
DELETE FROM message_log;
DELETE FROM students;
DELETE FROM message;
DELETE FROM material;
DELETE FROM section;
DELETE FROM topic;
DELETE FROM block;
DELETE FROM course;

-- course
INSERT INTO course (id, title) VALUES (1, 'Java Core');

-- blocks
INSERT INTO block (id, course_id, title, git_repository_id, "order", code) VALUES
    (1, 1, 'Старт',        NULL, 1, 'block01'),
    (2, 1, 'Это База',     1,    2, 'block02'),
    (3, 1, 'Строки',       NULL, 3, 'block03'),
    (4, 1, 'Массивы',      NULL, 4, 'block04'),
    (5, 1, 'ООП базово',   NULL, 5, 'block05');

-- topics
INSERT INTO topic (id, block_id, title, "order", code) VALUES
    -- block1
    (1,  1, 'Установка JDK',                                1, 'topic01'),
    (2,  1, 'Установка IntelliJ IDEA',                      2, 'topic02'),
    (3,  1, 'Первый проект: Hello World',                   3, 'topic03'),
    -- block2
    (5,  2, 'Примитивы и базовые типы',                     1, 'topic01'),
    (6,  2, 'Синтаксис: операторы, условия, циклы, методы', 2, 'topic02'),
    (7,  2, 'Naming Conventions',                           3, 'topic03'),
    -- block3
    (8,  3, 'Класс String — основы',                        1, 'topic01'),
    (9,  3, 'Особенности String',                           2, 'topic02'),
    -- block4
    (10, 4, 'Одномерные массивы',                           1, 'topic01'),
    -- block5
    (11, 5, 'Классы, объекты, поля, методы',                1, 'topic01'),
    (12, 5, 'Статика — static поля и методы',               2, 'topic02'),
    (13, 5, 'Модификаторы доступа',                         3, 'topic03'),
    (14, 5, 'Пакеты и импорты',                             4, 'topic04'),
    (15, 5, 'Перегрузка методов',                           5, 'topic05');

-- sections
INSERT INTO section (id, topic_id, title, "order", unlock_condition) VALUES
    -- block1
    (1,  1,  'Теория',        1, 'MANUAL'),
    (2,  2,  'Теория',        1, 'MANUAL'),
    (3,  3,  'Теория',        1, 'MANUAL'),
    -- block2
    (5,  5,  'Теория',        1, 'MANUAL'),
    (6,  5,  'Задание',       2, 'PR_REQUIRED'),
    (7,  6,  'Теория',        1, 'MANUAL'),
    (8,  6,  'Задание',       2, 'PR_REQUIRED'),
    (9,  7,  'Теория',        1, 'MANUAL'),
    -- block3
    (10, 8,  'Теория',        1, 'MANUAL'),
    (11, 8,  'Задание',       2, 'PR_REQUIRED'),
    (12, 9,  'Теория',        1, 'MANUAL'),
    (13, 9,  'Задание',       2, 'PR_REQUIRED'),
    -- block4
    (14, 10, 'Теория',        1, 'MANUAL'),
    (15, 10, 'Задание',       2, 'PR_REQUIRED'),
    -- block5
    (16, 11, 'Теория',        1, 'MANUAL'),
    (17, 11, 'Задание',       2, 'PR_REQUIRED'),
    (18, 12, 'Теория',        1, 'MANUAL'),
    (19, 12, 'Задание',       2, 'PR_REQUIRED'),
    (20, 13, 'Теория',        1, 'MANUAL'),
    (21, 13, 'Задание',       2, 'PR_REQUIRED'),
    (22, 14, 'Теория',        1, 'MANUAL'),
    (23, 14, 'Задание',       2, 'PR_REQUIRED'),
    (24, 15, 'Теория',        1, 'MANUAL'),
    (25, 15, 'Задание',       2, 'PR_REQUIRED');

-- materials
INSERT INTO material (id, section_id, type, title, url, "order") VALUES
    -- block1
    (1,  1,  'THEORY',   'Установка JDK',                      'https://github.com/zor07/lastsave-content/blob/main/block1/topic1/theory.md', 1),
    (2,  2,  'THEORY',   'Установка IntelliJ IDEA',             'https://github.com/zor07/lastsave-content/blob/main/block1/topic2/theory.md', 1),
    (3,  3,  'THEORY',   'Первый проект: Hello World',          'https://github.com/zor07/lastsave-content/blob/main/block1/topic3/theory.md', 1),
    -- block2
    (4,  5,  'THEORY',   'Примитивы и базовые типы',            'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/theory.md', 1),
    (5,  6,  'PRACTICE', 'Примитивы и базовые типы — задание',  'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/tasks.md',  1),
    (6,  7,  'THEORY',   'Синтаксис',                           'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/theory.md', 1),
    (7,  8,  'PRACTICE', 'Синтаксис — задание',                 'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/tasks.md',  1),
    (8,  9,  'THEORY',   'Naming Conventions',                  'https://github.com/zor07/lastsave-content/blob/main/block2/topic3/theory.md', 1),
    -- block3
    (9,  10, 'THEORY',   'Класс String',                        'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/theory.md', 1),
    (10, 11, 'PRACTICE', 'Класс String — задание',              'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/tasks.md',  1),
    (11, 12, 'THEORY',   'Особенности String',                  'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/theory.md', 1),
    (12, 13, 'PRACTICE', 'Особенности String — задание',        'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/tasks.md',  1),
    -- block4
    (13, 14, 'THEORY',   'Одномерные массивы',                  'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/theory.md', 1),
    (14, 15, 'PRACTICE', 'Одномерные массивы — задание',        'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/tasks.md',  1),
    -- block5
    (15, 16, 'THEORY',   'Классы и объекты',                    'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/theory.md', 1),
    (16, 17, 'PRACTICE', 'Классы и объекты — задание',          'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/tasks.md',  1),
    (17, 18, 'THEORY',   'Статика',                             'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/theory.md', 1),
    (18, 19, 'PRACTICE', 'Статика — задание',                   'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/tasks.md',  1),
    (19, 20, 'THEORY',   'Модификаторы доступа',                'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/theory.md', 1),
    (20, 21, 'PRACTICE', 'Модификаторы доступа — задание',      'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/tasks.md',  1),
    (21, 22, 'THEORY',   'Пакеты и импорты',                    'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/theory.md', 1),
    (22, 23, 'PRACTICE', 'Пакеты и импорты — задание',          'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/tasks.md',  1),
    (23, 24, 'THEORY',   'Перегрузка методов',                  'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/theory.md', 1),
    (24, 25, 'PRACTICE', 'Перегрузка методов — задание',        'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/tasks.md',  1);

-- messages
INSERT INTO message (id, section_id, sender, text, callback_text, wait_for, "order") VALUES
    -- block1 / topic1 / Установка JDK
    (1,  1,  'ANYA', 'Первый спринт — настройка окружения. Нужно установить JDK и IntelliJ IDEA, создать первый проект. Ссылка на материалы выше.

Когда всё готово — нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block1 / topic2 / Установка IntelliJ IDEA
    (3,  2,  'ANYA', 'Установка IntelliJ IDEA. Ссылка на материал была выше. Если уже стоит — просто нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block1 / topic3 / Hello World
    (4,  3,  'ANYA', 'Создай первый проект и запусти Hello World. Ссылка на материал была выше. Это необязательное задание — ничего автоматически не проверяется. Просто убедись что всё работает и нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic5 / Примитивы — теория
    (5,  5,  'ANYA', 'Второй спринт — основы языка. Сейчас создадим для тебя репозиторий для работы, ссылка придёт следующим сообщением.', NULL, 'NOTHING', 1),
    (6,  5,  'ANYA', 'Начнём с примитивов и базовых типов. Это справочник — не нужно заучивать, просто пробегись. Ссылка на материал была выше.', 'Готово', 'CALLBACK', 2),

    -- block2 / topic5 / Примитивы — задание
    (7,  6,  'ANYA', 'Практика

Открой QuestB2T1.java в пакете block02/topic01, реализуй методы вместо заглушек // TODO. Сигнатуры не меняй и не делай их статическими — это сломает проверки.

Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (8,  6,  'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block2 / topic6 / Синтаксис — теория
    (9,  7,  'ANYA', 'Переходим к синтаксису. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic6 / Синтаксис — задание
    (10, 8,  'ANYA', 'Задача

Открой QuestB2T2.java в пакете block02/topic02, реализуй методы вместо заглушек // TODO. Сигнатуры не меняй.

Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (11, 8,  'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block2 / topic7 / Naming Conventions
    (12, 9,  'ANYA', 'Последнее в этом спринте — соглашения об именовании. Ссылка на материал была выше. Задания нет, просто почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic8 / String основы — теория
    (13, 10, 'ANYA', 'Третий спринт — строки. Начнём с класса String. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic8 / String основы — задание
    (14, 11, 'ANYA', 'Упражнение

Открой QuestB3T1.java в пакете block03/topic01, реализуй методы вместо заглушек // TODO. Сигнатуры не меняй.

Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (15, 11, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block3 / topic9 / String особенности — теория
    (16, 12, 'ANYA', 'Теперь про особенности строк — иммутабельность, пул строк, StringBuilder. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic9 / String особенности — задание
    (17, 13, 'ANYA', 'Практическое задание

Открой QuestB3T2.java в пакете block03/topic02, реализуй методы вместо заглушек // TODO. Сигнатуры не меняй.

Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (18, 13, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block4 / topic10 / Массивы — теория
    (19, 14, 'ANYA', 'Четвёртый спринт — массивы. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block4 / topic10 / Массивы — задание
    (20, 15, 'ANYA', 'Самостоятельная работа

Открой QuestB4T1.java в пакете block04/topic01, реализуй методы вместо заглушек // TODO. Сигнатуры не меняй.

Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (21, 15, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block5 / topic11 / Классы и объекты — теория
    (22, 16, 'ANYA', 'Пятый спринт — основы ООП. Начнём с классов и объектов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic11 / Классы и объекты — задание
    (23, 17, 'ANYA', 'Теперь практика

Реализуй задачи в пакете block05/topic01. Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (24, 17, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block5 / topic12 / Статика — теория
    (25, 18, 'ANYA', 'Теперь статика. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic12 / Статика — задание
    (26, 19, 'ANYA', 'Задание на практику

Реализуй задачи в пакете block05/topic02. Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (27, 19, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block5 / topic13 / Модификаторы доступа — теория
    (28, 20, 'ANYA', 'Модификаторы доступа — важная тема. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic13 / Модификаторы доступа — задание
    (29, 21, 'ANYA', 'Закрепление

Реализуй задачи в пакете block05/topic03. Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (30, 21, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block5 / topic14 / Пакеты и импорты — теория
    (31, 22, 'ANYA', 'Пакеты и импорты. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic14 / Пакеты и импорты — задание
    (32, 23, 'ANYA', 'Пора покодить

Реализуй задачи в пакете block05/topic04. Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (33, 23, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2),

    -- block5 / topic15 / Перегрузка методов — теория
    (34, 24, 'ANYA', 'Последняя тема спринта — перегрузка методов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic15 / Перегрузка методов — задание
    (35, 25, 'ANYA', 'Задача на код

Реализуй задачи в пакете block05/topic05. Создай ветку, запушь, открой PR в main. В бот придут результаты тестов — если всё ок, следом придёт код ревью.', NULL, 'PR', 1),
    (36, 25, 'ANYA', 'Всё проверено. Замержи код, подтяни main локально, удали ветку.', 'Готово', 'CALLBACK', 2);
