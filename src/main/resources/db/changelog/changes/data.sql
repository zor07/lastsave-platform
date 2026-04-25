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
INSERT INTO block (id, course_id, title, git_repository_id, "order") VALUES
    (1, 1, 'Старт',        NULL, 1),
    (2, 1, 'Это База',     1,    2),
    (3, 1, 'Строки',       NULL, 3),
    (4, 1, 'Массивы',      NULL, 4),
    (5, 1, 'ООП базово',   NULL, 5);

-- topics
INSERT INTO topic (id, block_id, title, "order") VALUES
    -- block1
    (1,  1, 'Установка JDK',                                1),
    (2,  1, 'Установка IntelliJ IDEA',                      2),
    (3,  1, 'Первый проект: Hello World',                   3),
    -- block2
    (4,  2, 'Старт спринта',                                1),
    (5,  2, 'Примитивы и базовые типы',                     2),
    (6,  2, 'Синтаксис: операторы, условия, циклы, методы', 3),
    (7,  2, 'Naming Conventions',                           4),
    -- block3
    (8,  3, 'Класс String — основы',                        1),
    (9,  3, 'Особенности String',                           2),
    -- block4
    (10, 4, 'Одномерные массивы',                           1),
    -- block5
    (11, 5, 'Классы, объекты, поля, методы',                1),
    (12, 5, 'Статика — static поля и методы',               2),
    (13, 5, 'Модификаторы доступа',                         3),
    (14, 5, 'Пакеты и импорты',                             4),
    (15, 5, 'Перегрузка методов',                           5);

-- sections
INSERT INTO section (id, topic_id, title, "order", unlock_condition) VALUES
    -- block1
    (1,  1,  'Теория',        1, 'MANUAL'),
    (2,  2,  'Теория',        1, 'MANUAL'),
    (3,  3,  'Теория',        1, 'MANUAL'),
    -- block2
    (4,  4,  'Старт спринта', 1, 'MANUAL'),
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
    (1,  1,  'TEXT', 'Установка JDK',                      'https://github.com/zor07/lastsave-content/blob/main/block1/topic1/theory.md', 1),
    (2,  2,  'TEXT', 'Установка IntelliJ IDEA',             'https://github.com/zor07/lastsave-content/blob/main/block1/topic2/theory.md', 1),
    (3,  3,  'TEXT', 'Первый проект: Hello World',          'https://github.com/zor07/lastsave-content/blob/main/block1/topic3/theory.md', 1),
    -- block2
    (4,  5,  'TEXT', 'Примитивы и базовые типы — теория',   'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/theory.md', 1),
    (5,  6,  'TEXT', 'Примитивы и базовые типы — задание',  'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/tasks.md',  1),
    (6,  7,  'TEXT', 'Синтаксис — теория',                  'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/theory.md', 1),
    (7,  8,  'TEXT', 'Синтаксис — задание',                 'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/tasks.md',  1),
    (8,  9,  'TEXT', 'Naming Conventions',                  'https://github.com/zor07/lastsave-content/blob/main/block2/topic3/theory.md', 1),
    -- block3
    (9,  10, 'TEXT', 'Класс String — теория',               'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/theory.md', 1),
    (10, 11, 'TEXT', 'Класс String — задание',              'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/tasks.md',  1),
    (11, 12, 'TEXT', 'Особенности String — теория',         'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/theory.md', 1),
    (12, 13, 'TEXT', 'Особенности String — задание',        'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/tasks.md',  1),
    -- block4
    (13, 14, 'TEXT', 'Одномерные массивы — теория',         'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/theory.md', 1),
    (14, 15, 'TEXT', 'Одномерные массивы — задание',        'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/tasks.md',  1),
    -- block5
    (15, 16, 'TEXT', 'Классы и объекты — теория',           'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/theory.md', 1),
    (16, 17, 'TEXT', 'Классы и объекты — задание',          'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/tasks.md',  1),
    (17, 18, 'TEXT', 'Статика — теория',                    'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/theory.md', 1),
    (18, 19, 'TEXT', 'Статика — задание',                   'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/tasks.md',  1),
    (19, 20, 'TEXT', 'Модификаторы доступа — теория',       'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/theory.md', 1),
    (20, 21, 'TEXT', 'Модификаторы доступа — задание',      'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/tasks.md',  1),
    (21, 22, 'TEXT', 'Пакеты и импорты — теория',           'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/theory.md', 1),
    (22, 23, 'TEXT', 'Пакеты и импорты — задание',          'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/tasks.md',  1),
    (23, 24, 'TEXT', 'Перегрузка методов — теория',         'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/theory.md', 1),
    (24, 25, 'TEXT', 'Перегрузка методов — задание',        'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/tasks.md',  1);

-- messages
INSERT INTO message (id, section_id, sender, text, callback_text, wait_for, "order") VALUES
    -- block1 / topic1 / Установка JDK
    (1,  1,  'ANYA', 'Первый спринт — настройка окружения. Нужно установить JDK и IntelliJ IDEA, создать первый проект. Ссылка на материал была выше.', NULL, 'NOTHING', 1),
    (2,  1,  'ANYA', 'Установка JDK. Если уже стоит — просто нажми Готово.', 'Готово', 'CALLBACK', 2),

    -- block1 / topic2 / Установка IntelliJ IDEA
    (3,  2,  'ANYA', 'Установка IntelliJ IDEA. Ссылка на материал была выше. Если уже стоит — просто нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block1 / topic3 / Hello World
    (4,  3,  'ANYA', 'Создай первый проект и запусти Hello World. Ссылка на материал была выше. Это необязательное задание — ничего автоматически не проверяется. Просто убедись что всё работает и нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic4 / Старт спринта (ждём создания репо)
    (5,  4,  'ANYA', 'Второй спринт — основы языка. Сейчас создадим для тебя репозиторий для работы, ссылка придёт следующим сообщением.', NULL, 'NOTHING', 1),

    -- block2 / topic5 / Примитивы — теория
    (6,  5,  'ANYA', 'Начнём с примитивов и базовых типов. Это справочник — не нужно заучивать, просто пробегись. Ссылка на материал была выше.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic5 / Примитивы — задание
    (7,  6,  'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block02/topic01/QuestB2T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB2T1.java. При желании можешь добавить метод main и проверить себя вручную, но не меняй сигнатуры существующих методов и не делай их статическими — это сломает проверки.

Когда готово — запушь ветку и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (8,  6,  'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block2 / topic6 / Синтаксис — теория
    (9,  7,  'ANYA', 'Переходим к синтаксису. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic6 / Синтаксис — задание
    (10, 8,  'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block02/topic02/QuestB2T2.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB2T2.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (11, 8,  'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block2 / topic7 / Naming Conventions
    (12, 9,  'ANYA', 'Последнее в этом спринте — соглашения об именовании. Ссылка на материал была выше. Задания нет, просто почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic8 / String основы — теория
    (13, 10, 'ANYA', 'Третий спринт — строки. Начнём с класса String. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic8 / String основы — задание
    (14, 11, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block03/topic01/QuestB3T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB3T1.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (15, 11, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block3 / topic9 / String особенности — теория
    (16, 12, 'ANYA', 'Теперь про особенности строк — иммутабельность, пул строк, StringBuilder. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic9 / String особенности — задание
    (17, 13, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block03/topic02/QuestB3T2.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB3T2.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (18, 13, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block4 / topic10 / Массивы — теория
    (19, 14, 'ANYA', 'Четвёртый спринт — массивы. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block4 / topic10 / Массивы — задание
    (20, 15, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block04/topic01/QuestB4T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB4T1.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (21, 15, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic11 / Классы и объекты — теория
    (22, 16, 'ANYA', 'Пятый спринт — основы ООП. Начнём с классов и объектов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic11 / Классы и объекты — задание
    (23, 17, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic01 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (24, 17, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic12 / Статика — теория
    (25, 18, 'ANYA', 'Теперь статика. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic12 / Статика — задание
    (26, 19, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic02 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (27, 19, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic13 / Модификаторы доступа — теория
    (28, 20, 'ANYA', 'Модификаторы доступа — важная тема. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic13 / Модификаторы доступа — задание
    (29, 21, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic03 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (30, 21, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic14 / Пакеты и импорты — теория
    (31, 22, 'ANYA', 'Пакеты и импорты. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic14 / Пакеты и импорты — задание
    (32, 23, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic04 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (33, 23, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic15 / Перегрузка методов — теория
    (34, 24, 'ANYA', 'Последняя тема спринта — перегрузка методов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic15 / Перегрузка методов — задание
    (35, 25, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic05 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (36, 25, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2);
