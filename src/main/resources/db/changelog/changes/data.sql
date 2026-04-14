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
    (1, 1, 'Старт',    1, 1),
    (2, 1, 'Это База', 1, 2);

-- topics
INSERT INTO topic (id, block_id, title, "order") VALUES
    (1, 1, 'Установка JDK',                              1),
    (2, 1, 'Установка IntelliJ IDEA',                    2),
    (3, 1, 'Первый проект: Hello World',                 3),
    (4, 2, 'Примитивы и базовые типы',                   1),
    (5, 2, 'Синтаксис: операторы, условия, циклы, методы', 2),
    (6, 2, 'Naming Conventions',                         3);

-- sections
INSERT INTO section (id, topic_id, title, "order", unlock_condition) VALUES
    (1, 1, 'Теория',  1, 'MANUAL'),
    (2, 2, 'Теория',  1, 'MANUAL'),
    (3, 3, 'Теория',  1, 'MANUAL'),
    (4, 4, 'Теория',  1, 'MANUAL'),
    (5, 4, 'Задание', 2, 'PR_REQUIRED'),
    (6, 5, 'Теория',  1, 'MANUAL'),
    (7, 5, 'Задание', 2, 'PR_REQUIRED'),
    (8, 6, 'Теория',  1, 'MANUAL');

-- materials
INSERT INTO material (id, section_id, type, title, url, "order") VALUES
    (1, 1, 'TEXT', 'Установка JDK',                               'https://github.com/zor07/lastsave-content/blob/main/block1/topic1.md', 1),
    (2, 2, 'TEXT', 'Установка IntelliJ IDEA',                     'https://github.com/zor07/lastsave-content/blob/main/block1/topic2.md', 1),
    (3, 3, 'TEXT', 'Первый проект: Hello World',                  'https://github.com/zor07/lastsave-content/blob/main/block1/topic3.md', 1),
    (4, 4, 'TEXT', 'Примитивы и базовые типы',                    'https://github.com/zor07/lastsave-content/blob/main/block2/topic1.md', 1),
    (5, 6, 'TEXT', 'Синтаксис: операторы, условия, циклы, методы','https://github.com/zor07/lastsave-content/blob/main/block2/topic2.md', 1),
    (6, 8, 'TEXT', 'Naming Conventions',                          'https://github.com/zor07/lastsave-content/blob/main/block2/topic3.md', 1);

-- messages
INSERT INTO message (id, section_id, sender, text, callback_text, wait_for, "order") VALUES
    -- block1 / topic1 / теория
    (1,  1, 'ANYA', 'Привет! Рад видеть тебя на стажировке. Начнём с настройки окружения — установи JDK. Изучи материал и дай знать когда готово.', 'Готово', 'CALLBACK', 1),
    (2,  1, 'ANYA', 'Отлично. Двигаемся дальше.', null, 'NOTHING', 2),
    -- block1 / topic2 / теория
    (3,  2, 'ANYA', 'Теперь установи редактор — IntelliJ IDEA. Изучи материал и дай знать.', 'Готово', 'CALLBACK', 1),
    (4,  2, 'ANYA', 'Хорошо. Продолжаем.', null, 'NOTHING', 2),
    -- block1 / topic3 / теория
    (5,  3, 'ANYA', 'Создай первый проект и запусти Hello World. Изучи материал и дай знать.', 'Готово', 'CALLBACK', 1),
    (6,  3, 'ANYA', 'Окружение готово. Переходим к следующему спринту.', null, 'NOTHING', 2),
    -- block2 / topic1 / теория
    (7,  4, 'ANYA', 'Новый спринт — разберёмся с примитивами и базовыми типами. Это справочник, не нужно заучивать — просто пробегись.', 'Готово', 'CALLBACK', 1),
    (8,  4, 'ANYA', 'Хорошо. Теперь практика.', null, 'NOTHING', 2),
    -- block2 / topic1 / задание
    (9,  5, 'ANYA', 'Открой класс QuestB2T1 в репозитории и реши задания. Когда всё готово — создай Pull Request.', null, 'PR', 1),
    (10, 5, 'BOT',  'PR получен, проверяю...', null, 'NOTHING', 2),
    -- block2 / topic2 / теория
    (11, 6, 'ANYA', 'Переходим к синтаксису. Изучи материал и дай знать.', 'Готово', 'CALLBACK', 1),
    (12, 6, 'ANYA', 'Хорошо. Теперь практика.', null, 'NOTHING', 2),
    -- block2 / topic2 / задание
    (13, 7, 'ANYA', 'Открой класс QuestB2T2 в репозитории и реши задания. Когда всё готово — создай Pull Request.', null, 'PR', 1),
    (14, 7, 'BOT',  'PR получен, проверяю...', null, 'NOTHING', 2),
    -- block2 / topic3 / теория
    (15, 8, 'ANYA', 'Последний материал в этом спринте — соглашения об именовании. Изучи и дай знать.', 'Готово', 'CALLBACK', 1),
    (16, 8, 'ANYA', 'Отлично! Ты прошёл все темы спринта.', null, 'NOTHING', 2);
