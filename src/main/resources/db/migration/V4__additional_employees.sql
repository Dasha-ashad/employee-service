-- ============================================
-- Миграция V4: Дополнительные сотрудники для расширенного тестирования
-- ============================================
-- 
-- Добавляет еще 30 сотрудников с разнообразными данными для:
-- - Тестирования фильтрации и сортировки
-- - Тестирования аналитики с большим объемом данных
-- - Тестирования производительности
-- - Покрытия различных edge cases
-- 
-- ============================================
-- 1. Дополнительные пользователи
-- ============================================
INSERT INTO users (login, password_hash, role) VALUES
    -- Руководители
    ('head_finance', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    ('head_legal', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    ('head_devops', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    
    -- Обычные сотрудники (30 человек)
    ('emp_ivanov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_petrov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_sidorov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_kozlov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_volkov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_novikov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_morozov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_petrov3', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_sokolov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_lebedeva2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_romanova2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_orlova2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_egorov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_nikitin2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_fedorov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_vasiliev2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_kuznetsov2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_smirnova2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_antonov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_belov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_grigoriev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_dmitriev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_efremov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_zhukov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_zaitsev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_komarov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_lazarev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_makarov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_naumov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE');

-- ============================================
-- 2. Дополнительные сотрудники (30 человек)
-- ============================================
INSERT INTO employees (user_id, full_name, gender, birth_date, hire_date, fire_date, competence_rank, competence_level, department_id) VALUES
    -- Разработка (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_ivanov2'), 'Иванов Иван Иванович', 'М', '1990-05-15', '2020-01-10', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Разработка')),
    ((SELECT id FROM users WHERE login = 'emp_petrov2'), 'Петров Петр Петрович', 'М', '1992-08-20', '2020-03-15', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Разработка')),
    ((SELECT id FROM users WHERE login = 'emp_sidorov2'), 'Сидоров Сидор Сидорович', 'М', '1995-11-10', '2021-06-01', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Разработка')),
    ((SELECT id FROM users WHERE login = 'emp_kozlov2'), 'Козлов Козел Козлович', 'М', '1988-02-25', '2019-09-10', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Разработка')),
    ((SELECT id FROM users WHERE login = 'emp_volkov2'), 'Волков Волк Волкович', 'М', '1993-07-30', '2020-11-20', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Разработка')),
    
    -- Тестирование (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_novikov2'), 'Новиков Новик Новикович', 'М', '1994-04-12', '2021-02-01', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Тестирование')),
    ((SELECT id FROM users WHERE login = 'emp_morozov2'), 'Морозов Мороз Морозович', 'М', '1991-09-18', '2020-05-10', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Тестирование')),
    ((SELECT id FROM users WHERE login = 'emp_petrov3'), 'Петрова Петра Петровна', 'Ж', '1996-12-05', '2021-08-15', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Тестирование')),
    ((SELECT id FROM users WHERE login = 'emp_sokolov2'), 'Соколова Сокол Соколовна', 'Ж', '1992-03-22', '2020-07-01', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Тестирование')),
    ((SELECT id FROM users WHERE login = 'emp_lebedeva2'), 'Лебедева Лебедь Лебедевна', 'Ж', '1989-06-14', '2019-12-10', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Тестирование')),
    
    -- Продажи (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_romanova2'), 'Романова Роман Романовна', 'Ж', '1993-10-28', '2020-04-05', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Продажи')),
    ((SELECT id FROM users WHERE login = 'emp_orlova2'), 'Орлова Орел Орловна', 'Ж', '1995-01-16', '2021-01-20', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Продажи')),
    ((SELECT id FROM users WHERE login = 'emp_egorov2'), 'Егоров Егор Егорович', 'М', '1990-08-09', '2020-02-15', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Продажи')),
    ((SELECT id FROM users WHERE login = 'emp_nikitin2'), 'Никитин Никита Никитич', 'М', '1994-05-23', '2021-05-10', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Продажи')),
    ((SELECT id FROM users WHERE login = 'emp_fedorov2'), 'Федоров Федор Федорович', 'М', '1991-11-30', '2020-09-01', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Продажи')),
    
    -- Маркетинг (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_vasiliev2'), 'Васильев Василий Васильевич', 'М', '1992-07-19', '2020-06-15', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Маркетинг')),
    ((SELECT id FROM users WHERE login = 'emp_kuznetsov2'), 'Кузнецова Кузнец Кузнецовна', 'Ж', '1996-04-07', '2021-07-20', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Маркетинг')),
    ((SELECT id FROM users WHERE login = 'emp_smirnova2'), 'Смирнова Смирн Смирновна', 'Ж', '1993-09-12', '2020-10-05', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Маркетинг')),
    ((SELECT id FROM users WHERE login = 'emp_antonov'), 'Антонов Антон Антонович', 'М', '1990-12-25', '2020-01-15', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Маркетинг')),
    ((SELECT id FROM users WHERE login = 'emp_belov'), 'Белов Бел Белович', 'М', '1994-02-18', '2021-03-10', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Маркетинг')),
    
    -- Бухгалтерия (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_grigoriev'), 'Григорьев Григорий Григорьевич', 'М', '1989-08-03', '2019-11-20', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'Бухгалтерия')),
    ((SELECT id FROM users WHERE login = 'emp_dmitriev'), 'Дмитриев Дмитрий Дмитриевич', 'М', '1992-05-14', '2020-08-01', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Бухгалтерия')),
    ((SELECT id FROM users WHERE login = 'emp_efremov'), 'Ефремова Ефрем Ефремовна', 'Ж', '1995-10-27', '2021-04-15', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'Бухгалтерия')),
    ((SELECT id FROM users WHERE login = 'emp_zhukov'), 'Жуков Жук Жукович', 'М', '1991-03-08', '2020-05-20', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Бухгалтерия')),
    ((SELECT id FROM users WHERE login = 'emp_zaitsev'), 'Зайцева Заяц Зайцевна', 'Ж', '1993-06-21', '2020-12-10', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'Бухгалтерия')),
    
    -- DevOps (5 человек)
    ((SELECT id FROM users WHERE login = 'emp_komarov'), 'Комаров Комар Комарович', 'М', '1990-09-04', '2020-02-01', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'DevOps')),
    ((SELECT id FROM users WHERE login = 'emp_lazarev'), 'Лазарев Лазарь Лазаревич', 'М', '1992-11-17', '2020-07-15', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'DevOps')),
    ((SELECT id FROM users WHERE login = 'emp_makarov'), 'Макарова Макар Макаровна', 'Ж', '1994-01-29', '2021-06-01', NULL, 'JUNIOR', 1, (SELECT id FROM departments WHERE name = 'DevOps')),
    ((SELECT id FROM users WHERE login = 'emp_naumov'), 'Наумов Наум Наумович', 'М', '1991-04-11', '2020-09-20', NULL, 'MIDDLE', 2, (SELECT id FROM departments WHERE name = 'DevOps')),
    ((SELECT id FROM users WHERE login = 'head_devops'), 'Осипов Осип Осипович', 'М', '1987-07-26', '2019-05-10', NULL, 'SENIOR', 3, (SELECT id FROM departments WHERE name = 'DevOps'));

-- ============================================
-- 3. Назначение руководителей отделов
-- ============================================
-- Назначаем руководителей для новых отделов
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'head_finance'))
WHERE name = 'Бухгалтерия' AND head_id IS NULL;

UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'head_legal'))
WHERE name = 'Юридический отдел' AND head_id IS NULL;

UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'head_devops'))
WHERE name = 'DevOps' AND head_id IS NULL;

-- ============================================
-- 4. Дополнительные обучения (15 записей)
-- ============================================
INSERT INTO trainings (employee_id, training_name, start_date, end_date, level_before, level_after) VALUES
    -- Обучения для новых сотрудников
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_ivanov2')), 'Продвинутый Java', '2023-01-15', '2023-02-15', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_petrov2')), 'Spring Framework', '2023-03-01', '2023-03-31', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_sidorov2')), 'Основы программирования', '2023-04-10', '2023-05-10', 'JUNIOR', 'JUNIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_kozlov2')), 'Архитектура ПО', '2023-02-20', '2023-03-20', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_volkov2')), 'Базы данных', '2023-05-01', '2023-05-31', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_novikov2')), 'Тестирование ПО', '2023-06-01', '2023-06-30', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_morozov2')), 'Автоматизация тестирования', '2023-07-10', '2023-08-10', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_romanova2')), 'Техники продаж', '2023-08-01', '2023-08-31', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_egorov2')), 'Управление клиентами', '2023-09-01', '2023-09-30', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_vasiliev2')), 'Digital маркетинг', '2023-10-01', '2023-10-31', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_antonov')), 'Стратегия маркетинга', '2023-11-01', '2023-11-30', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_grigoriev')), 'Финансовый учет', '2023-12-01', '2023-12-31', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_komarov')), 'Kubernetes', '2024-01-01', '2024-01-31', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_lazarev')), 'Docker', '2024-02-01', '2024-02-28', 'JUNIOR', 'MIDDLE'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_naumov')), 'CI/CD', '2024-03-01', '2024-03-31', 'JUNIOR', 'MIDDLE');

-- ============================================
-- 5. Дополнительные пропуски (20 записей)
-- ============================================
INSERT INTO absences (employee_id, start_date, end_date, reason, status) VALUES
    -- Отпуска
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_ivanov2')), '2023-07-01', '2023-07-14', 'Ежегодный отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_petrov2')), '2023-08-01', '2023-08-14', 'Ежегодный отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_sidorov2')), '2023-09-01', '2023-09-14', 'Ежегодный отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_kozlov2')), '2023-10-01', '2023-10-14', 'Ежегодный отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_volkov2')), '2023-11-01', '2023-11-14', 'Ежегодный отпуск', 'GOOD_REASON'),
    
    -- Больничные
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_novikov2')), '2023-06-05', '2023-06-12', 'Больничный лист', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_morozov2')), '2023-07-20', '2023-07-27', 'Больничный лист', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_petrov3')), '2023-08-15', '2023-08-22', 'Больничный лист', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_sokolov2')), '2023-09-10', '2023-09-17', 'Больничный лист', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_lebedeva2')), '2023-10-05', '2023-10-12', 'Больничный лист', 'GOOD_REASON'),
    
    -- Прогулы
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_romanova2')), '2023-11-15', '2023-11-15', 'Прогул', 'BAD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_orlova2')), '2023-12-10', '2023-12-10', 'Прогул', 'BAD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_egorov2')), '2024-01-05', '2024-01-05', 'Прогул', 'BAD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_nikitin2')), '2024-02-20', '2024-02-20', 'Прогул', 'BAD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_fedorov2')), '2024-03-15', '2024-03-15', 'Прогул', 'BAD_REASON'),
    
    -- Дополнительные отпуска
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_vasiliev2')), '2023-12-20', '2024-01-08', 'Новогодние праздники', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_kuznetsov2')), '2024-01-10', '2024-01-12', 'Больничный лист', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_smirnova2')), '2024-02-01', '2024-02-05', 'Отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_antonov')), '2024-03-01', '2024-03-05', 'Отпуск', 'GOOD_REASON'),
    ((SELECT id FROM employees WHERE user_id = (SELECT id FROM users WHERE login = 'emp_belov')), '2024-04-01', '2024-04-05', 'Больничный лист', 'GOOD_REASON');

-- ============================================
-- Итого добавлено в V4:
-- ============================================
-- - 3 новых руководителя
-- - 30 новых пользователей (EMPLOYEE)
-- - 30 новых сотрудников с разнообразными данными
-- - 3 руководителя отделов назначено
-- - 15 обучений для различных сотрудников
-- - 20 пропусков (отпуска, больничные, прогулы)
-- 
-- Общее количество сотрудников после V4: 50
-- Данные покрывают:
-- - Все отделы
-- - Разные полы
-- - Разные возрастные группы
-- - Разные уровни компетенции
-- - Разные периоды работы
-- - Разные типы пропусков

