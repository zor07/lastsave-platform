-- course
INSERT INTO course (id, title) VALUES (1, 'Java Core');

-- block
INSERT INTO block (id, course_id, title, template_repo_name, "order")
VALUES (1, 1, 'Входной билет', 'java-core-prototype', 1);

-- topics
INSERT INTO topic (id, block_id, title, "order") VALUES
                                                     (1, 1, 'Настройка окружения', 1),
                                                     (2, 1, 'Синтаксис', 2);

-- sections
INSERT INTO section (id, topic_id, title, "order", unlock_condition) VALUES
                                                                         (1, 1, 'Теория', 1, 'MANUAL'),
                                                                         (2, 1, 'Задание', 2, 'PR_REQUIRED'),
                                                                         (3, 2, 'Теория', 1, 'MANUAL'),
                                                                         (4, 2, 'Задание', 2, 'PR_REQUIRED');

-- materials
INSERT INTO material (id, section_id, type, title, url, "order") VALUES
                                                                     (1, 1, 'THEORY', 'Настройка окружения', 'https://zor07.yonote.ru/share/c5c683fd-ba94-4d97-991f-1d231757b989/doc/1-nastrojka-okruzheniya-jdk-idea-pervyj-proekt-IxNenKR0Tn', 1),
                                                                     (2, 3, 'THEORY', 'Справочник по синтаксису', 'https://zor07.yonote.ru/share/697a6378-254d-4687-a55f-93b04be57c10/doc/spravochnik-po-sintaksisu-java-kS1YqWlhj0', 1);

-- messages
INSERT INTO message (id, section_id, sender, text, callback_text, wait_for, "order") VALUES
                                                                                         (1,  1, 'ANYA', 'Привет! Сначала настроим окружение. Вот материал — изучи и дай знать когда готов.', 'Готово', 'CALLBACK', 1),
                                                                                         (2,  1, 'ANYA', 'Отлично. Двигаемся дальше.', null, 'NOTHING', 2),
                                                                                         (3,  2, 'ANYA', 'Теперь практика — сделай PR в шаблонную репу.', null, 'PR', 1),
                                                                                         (4,  2, 'BOT',  'PR получен, проверяю...', null, 'NOTHING', 2),
                                                                                         (5,  3, 'ANYA', 'Переходим к синтаксису. Вот справочник — посмотри и скажи когда готов.', 'Готово', 'CALLBACK', 1),
                                                                                         (6,  3, 'ANYA', 'Хорошо. Теперь задание.', null, 'NOTHING', 2),
                                                                                         (7,  4, 'ANYA', 'Сделай PR с решением.', null, 'PR', 1),
                                                                                         (8,  4, 'BOT',  'PR получен, проверяю...', null, 'NOTHING', 2);


INSERT INTO students (id, telegram_chat_id, github_username, created_at) VALUES (1, 395712833, 'yourjm', '2026-03-20 16:00:19.004054');
INSERT INTO students (id, telegram_chat_id, github_username, created_at) VALUES (2, 1012303963, 'tigra07', '2026-03-20 19:52:48.865200');
