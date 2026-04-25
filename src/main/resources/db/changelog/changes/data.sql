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
    (4,  2, 'Примитивы и базовые типы',                     1),
    (5,  2, 'Синтаксис: операторы, условия, циклы, методы', 2),
    (6,  2, 'Naming Conventions',                           3),
    -- block3
    (7,  3, 'Класс String — основы',                        1),
    (8,  3, 'Особенности String',                           2),
    -- block4
    (9,  4, 'Одномерные массивы',                           1),
    -- block5
    (10, 5, 'Классы, объекты, поля, методы',                1),
    (11, 5, 'Статика — static поля и методы',               2),
    (12, 5, 'Модификаторы доступа',                         3),
    (13, 5, 'Пакеты и импорты',                             4),
    (14, 5, 'Перегрузка методов',                           5);

-- sections
INSERT INTO section (id, topic_id, title, "order", unlock_condition) VALUES
    -- block1
    (1,  1,  'Теория',   1, 'MANUAL'),
    (2,  2,  'Теория',   1, 'MANUAL'),
    (3,  3,  'Теория',   1, 'MANUAL'),
    -- block2
    (4,  4,  'Теория',   1, 'MANUAL'),
    (5,  4,  'Задание',  2, 'PR_REQUIRED'),
    (6,  5,  'Теория',   1, 'MANUAL'),
    (7,  5,  'Задание',  2, 'PR_REQUIRED'),
    (8,  6,  'Теория',   1, 'MANUAL'),
    -- block3
    (9,  7,  'Теория',   1, 'MANUAL'),
    (10, 7,  'Задание',  2, 'PR_REQUIRED'),
    (11, 8,  'Теория',   1, 'MANUAL'),
    (12, 8,  'Задание',  2, 'PR_REQUIRED'),
    -- block4
    (13, 9,  'Теория',   1, 'MANUAL'),
    (14, 9,  'Задание',  2, 'PR_REQUIRED'),
    -- block5
    (15, 10, 'Теория',   1, 'MANUAL'),
    (16, 10, 'Задание',  2, 'PR_REQUIRED'),
    (17, 11, 'Теория',   1, 'MANUAL'),
    (18, 11, 'Задание',  2, 'PR_REQUIRED'),
    (19, 12, 'Теория',   1, 'MANUAL'),
    (20, 12, 'Задание',  2, 'PR_REQUIRED'),
    (21, 13, 'Теория',   1, 'MANUAL'),
    (22, 13, 'Задание',  2, 'PR_REQUIRED'),
    (23, 14, 'Теория',   1, 'MANUAL'),
    (24, 14, 'Задание',  2, 'PR_REQUIRED');

-- materials
INSERT INTO material (id, section_id, type, title, url, "order") VALUES
    -- block1
    (1,  1,  'TEXT', 'Установка JDK',                                'https://github.com/zor07/lastsave-content/blob/main/block1/topic1/theory.md', 1),
    (2,  2,  'TEXT', 'Установка IntelliJ IDEA',                      'https://github.com/zor07/lastsave-content/blob/main/block1/topic2/theory.md', 1),
    (3,  3,  'TEXT', 'Первый проект: Hello World',                   'https://github.com/zor07/lastsave-content/blob/main/block1/topic3/theory.md', 1),
    -- block2
    (4,  4,  'TEXT', 'Примитивы и базовые типы — теория',            'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/theory.md', 1),
    (5,  5,  'TEXT', 'Примитивы и базовые типы — задание',           'https://github.com/zor07/lastsave-content/blob/main/block2/topic1/tasks.md',  1),
    (6,  6,  'TEXT', 'Синтаксис — теория',                           'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/theory.md', 1),
    (7,  7,  'TEXT', 'Синтаксис — задание',                          'https://github.com/zor07/lastsave-content/blob/main/block2/topic2/tasks.md',  1),
    (8,  8,  'TEXT', 'Naming Conventions',                           'https://github.com/zor07/lastsave-content/blob/main/block2/topic3/theory.md', 1),
    -- block3
    (9,  9,  'TEXT', 'Класс String — теория',                        'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/theory.md', 1),
    (10, 10, 'TEXT', 'Класс String — задание',                       'https://github.com/zor07/lastsave-content/blob/main/block3/topic1/tasks.md',  1),
    (11, 11, 'TEXT', 'Особенности String — теория',                  'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/theory.md', 1),
    (12, 12, 'TEXT', 'Особенности String — задание',                 'https://github.com/zor07/lastsave-content/blob/main/block3/topic2/tasks.md',  1),
    -- block4
    (13, 13, 'TEXT', 'Одномерные массивы — теория',                  'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/theory.md', 1),
    (14, 14, 'TEXT', 'Одномерные массивы — задание',                 'https://github.com/zor07/lastsave-content/blob/main/block4/topic1/tasks.md',  1),
    -- block5
    (15, 15, 'TEXT', 'Классы и объекты — теория',                    'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/theory.md', 1),
    (16, 16, 'TEXT', 'Классы и объекты — задание',                   'https://github.com/zor07/lastsave-content/blob/main/block5/topic1/tasks.md',  1),
    (17, 17, 'TEXT', 'Статика — теория',                             'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/theory.md', 1),
    (18, 18, 'TEXT', 'Статика — задание',                            'https://github.com/zor07/lastsave-content/blob/main/block5/topic2/tasks.md',  1),
    (19, 19, 'TEXT', 'Модификаторы доступа — теория',                'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/theory.md', 1),
    (20, 20, 'TEXT', 'Модификаторы доступа — задание',               'https://github.com/zor07/lastsave-content/blob/main/block5/topic3/tasks.md',  1),
    (21, 21, 'TEXT', 'Пакеты и импорты — теория',                    'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/theory.md', 1),
    (22, 22, 'TEXT', 'Пакеты и импорты — задание',                   'https://github.com/zor07/lastsave-content/blob/main/block5/topic4/tasks.md',  1),
    (23, 23, 'TEXT', 'Перегрузка методов — теория',                  'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/theory.md', 1),
    (24, 24, 'TEXT', 'Перегрузка методов — задание',                 'https://github.com/zor07/lastsave-content/blob/main/block5/topic5/tasks.md',  1);

-- messages
INSERT INTO message (id, section_id, sender, text, callback_text, wait_for, "order") VALUES
    -- block1 / topic1 / Установка JDK
    (1,  1,  'ANYA', 'Первый спринт — настройка окружения. Нужно установить JDK и IntelliJ IDEA, создать первый проект. Ссылка на материал была выше.', NULL, 'NOTHING', 1),
    (2,  1,  'ANYA', 'Установка JDK. Если уже стоит — просто нажми Готово.', 'Готово', 'CALLBACK', 2),

    -- block1 / topic2 / Установка IntelliJ IDEA
    (3,  2,  'ANYA', 'Установка IntelliJ IDEA. Ссылка на материал была выше. Если уже стоит — просто нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block1 / topic3 / Hello World
    (4,  3,  'ANYA', 'Создай первый проект и запусти Hello World. Ссылка на материал была выше. Это необязательное задание — ничего автоматически не проверяется. Просто убедись что всё работает и нажми Готово.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic1 / Примитивы — теория
    (5,  4,  'ANYA', 'Второй спринт — основы языка: примитивы, синтаксис, соглашения об именовании. Начнём с примитивов и базовых типов. Это справочник — не нужно заучивать, просто пробегись. Ссылка на материал была выше.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic1 / Примитивы — задание
    (6,  5,  'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block02/topic01/QuestB2T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB2T1.java. При желании можешь добавить метод main и проверить себя вручную, но не меняй сигнатуры существующих методов и не делай их статическими — это сломает проверки.

Когда готово — запушь ветку и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (7,  5,  'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block2 / topic2 / Синтаксис — теория
    (8,  6,  'ANYA', 'Переходим к синтаксису. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block2 / topic2 / Синтаксис — задание
    (9,  7,  'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block02/topic02/QuestB2T2.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB2T2.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (10, 7,  'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block2 / topic3 / Naming Conventions
    (11, 8,  'ANYA', 'Последнее в этом спринте — соглашения об именовании. Ссылка на материал была выше. Задания нет, просто почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic1 / String основы — теория
    (12, 9,  'ANYA', 'Третий спринт — строки. Начнём с класса String. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic1 / String основы — задание
    (13, 10, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block03/topic01/QuestB3T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB3T1.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (14, 10, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block3 / topic2 / String особенности — теория
    (15, 11, 'ANYA', 'Теперь про особенности строк — иммутабельность, пул строк, StringBuilder. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block3 / topic2 / String особенности — задание
    (16, 12, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block03/topic02/QuestB3T2.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB3T2.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (17, 12, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block4 / topic1 / Массивы — теория
    (18, 13, 'ANYA', 'Четвёртый спринт — массивы. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block4 / topic1 / Массивы — задание
    (19, 14, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, открой файл src/main/java/com/lastsave/block04/topic01/QuestB4T1.java — реализуй методы вместо заглушек // TODO. Пиши свой код только в QuestB4T1.java, сигнатуры не меняй. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (20, 14, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic1 / Классы и объекты — теория
    (21, 15, 'ANYA', 'Пятый спринт — основы ООП. Начнём с классов и объектов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic1 / Классы и объекты — задание
    (22, 16, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic01 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (23, 16, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic2 / Статика — теория
    (24, 17, 'ANYA', 'Теперь статика. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic2 / Статика — задание
    (25, 18, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic02 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (26, 18, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic3 / Модификаторы доступа — теория
    (27, 19, 'ANYA', 'Модификаторы доступа — важная тема. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic3 / Модификаторы доступа — задание
    (28, 20, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic03 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (29, 20, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic4 / Пакеты и импорты — теория
    (30, 21, 'ANYA', 'Пакеты и импорты. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic4 / Пакеты и импорты — задание
    (31, 22, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic04 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (32, 22, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2),

    -- block5 / topic5 / Перегрузка методов — теория
    (33, 23, 'ANYA', 'Последняя тема спринта — перегрузка методов. Ссылка на материал была выше. Почитай и дай знать.', 'Готово', 'CALLBACK', 1),

    -- block5 / topic5 / Перегрузка методов — задание
    (34, 24, 'ANYA', 'Задание. ТЗ было в предыдущем сообщении. Создай новую ветку, реализуй задачи в пакете com/lastsave/block05/topic05 в соответствии с ТЗ. Когда готово — запушь и открой Pull Request в main. После ревью замержи, подтяни main локально, удали ветку.', NULL, 'PR', 1),
    (35, 24, 'BOT',  'PR получен, проверяю...', NULL, 'NOTHING', 2);
