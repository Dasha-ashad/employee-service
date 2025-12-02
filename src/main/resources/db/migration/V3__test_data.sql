-- ============================================
-- Миграция V3: Тестовые данные для полноценного тестирования
-- ============================================
-- 
-- Добавляет:
-- - 20 различных сотрудников с разнообразными данными
-- - Дополнительные отделы
-- - Пользователей для авторизации
-- - Обучения для сотрудников
-- - Пропуски (отпуска, больничные)
-- 
-- ВАЖНО: Пароли в BCrypt. Для тестирования используйте:
-- - admin/admin - администратор
-- - head/head - руководитель
-- - emp/emp - сотрудник
-- Или создайте реальные BCrypt хеши через AuthService

-- ============================================
-- 1. Дополнительные отделы
-- ============================================
INSERT INTO departments (name) VALUES
    ('Продажи'),
    ('Маркетинг'),
    ('Логистика'),
    ('Бухгалтерия'),
    ('Юридический отдел'),
    ('Разработка'),
    ('Тестирование'),
    ('DevOps');

-- ============================================
-- 2. Пользователи для авторизации
-- ============================================
-- ВАЖНО: Пароли должны быть захешированы через BCrypt!
-- Для тестирования можно использовать простые пароли, но в production обязательно BCrypt
-- Пример BCrypt хеша для пароля "password": $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy
INSERT INTO users (login, password_hash, role) VALUES
    -- Администраторы
    ('admin2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN'),
    
    -- Руководители отделов
    ('head_it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    ('head_sales', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    ('head_hr', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    ('head_marketing', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'HEAD'),
    
    -- Обычные сотрудники
    ('emp_sidorov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_kozlov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_volkov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_novikov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_morozov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_petrov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_sokolov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_lebedeva', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_romanova', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_orlova', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_volkova', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_ivanova', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_smirnova', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_kuznetsov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_fedorov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_vasiliev', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_egorov', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE'),
    ('emp_nikitin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'EMPLOYEE');

-- ============================================
-- 3. Сотрудники (20 человек)
-- ============================================
-- Разнообразные данные: разные отделы, полы, возрасты, уровни компетенции
-- Некоторые уволенные для тестирования фильтрации
INSERT INTO employees (
    user_id, full_name, gender, birth_date, hire_date, fire_date, 
    competence_rank, competence_level, department_id
) VALUES
    -- IT отдел (отдел id=1)
    (4, 'Сидоров Сергей Сергеевич', 'М', '1990-05-15', '2018-03-10', NULL, 'SENIOR', 3, 1),
    (5, 'Козлов Дмитрий Александрович', 'М', '1988-11-22', '2019-07-20', NULL, 'MIDDLE', 2, 1),
    (6, 'Волков Андрей Викторович', 'М', '1995-02-28', '2020-01-15', NULL, 'JUNIOR', 1, 1),
    (7, 'Новиков Максим Игоревич', 'М', '1992-08-10', '2017-09-05', '2023-12-31', 'MIDDLE', 2, 1),
    
    -- HR отдел (отдел id=2)
    (8, 'Морозов Иван Петрович', 'М', '1985-04-18', '2016-05-12', NULL, 'SENIOR', 3, 2),
    (9, 'Петрова Анна Сергеевна', 'Ж', '1991-09-25', '2018-11-08', NULL, 'MIDDLE', 2, 2),
    (10, 'Соколова Елена Дмитриевна', 'Ж', '1993-12-03', '2021-02-20', NULL, 'JUNIOR', 1, 2),
    
    -- Продажи (новый отдел)
    (11, 'Лебедева Мария Александровна', 'Ж', '1989-06-14', '2017-08-30', NULL, 'SENIOR', 3, 4),
    (12, 'Романова Ольга Викторовна', 'Ж', '1994-03-07', '2019-04-15', NULL, 'MIDDLE', 2, 4),
    (13, 'Орлова Татьяна Игоревна', 'Ж', '1996-10-19', '2022-06-01', NULL, 'JUNIOR', 1, 4),
    (14, 'Волкова Светлана Петровна', 'Ж', '1987-01-30', '2015-12-10', NULL, 'SENIOR', 3, 4),
    
    -- Маркетинг
    (15, 'Иванова Наталья Сергеевна', 'Ж', '1990-07-22', '2018-09-18', NULL, 'MIDDLE', 2, 5),
    (16, 'Смирнова Юлия Дмитриевна', 'Ж', '1992-11-05', '2020-03-25', NULL, 'MIDDLE', 2, 5),
    
    -- Финансы (отдел id=3)
    (17, 'Кузнецов Павел Александрович', 'М', '1986-05-08', '2016-07-22', NULL, 'SENIOR', 3, 3),
    (18, 'Федоров Алексей Викторович', 'М', '1991-08-16', '2019-10-05', NULL, 'MIDDLE', 2, 3),
    
    -- Разработка
    (19, 'Васильев Роман Игоревич', 'М', '1993-04-12', '2020-05-14', NULL, 'MIDDLE', 2, 6),
    (20, 'Егоров Виктор Сергеевич', 'М', '1995-09-28', '2021-08-30', NULL, 'JUNIOR', 1, 6),
    
    -- Тестирование
    (21, 'Никитин Артем Дмитриевич', 'М', '1994-12-20', '2022-01-10', NULL, 'JUNIOR', 1, 7),
    
    -- Логистика
    (22, 'Семенов Игорь Петрович', 'М', '1988-02-14', '2017-11-20', NULL, 'MIDDLE', 2, 3),
    
    -- Без отдела (для тестирования)
    (23, 'Павлов Константин Александрович', 'М', '1990-06-25', '2023-03-15', NULL, 'JUNIOR', 1, NULL);

-- ============================================
-- 4. Назначение руководителей отделов
-- ============================================
-- Назначаем руководителей отделов (head_id указывает на employee.id)
-- Используем подзапросы для получения employee_id по user_id для надежности
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = 4) WHERE id = 1;  -- IT: Сидоров (user_id=4)
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = 8) WHERE id = 2;  -- HR: Морозов (user_id=8)
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = 11) WHERE id = 4; -- Продажи: Лебедева (user_id=11)
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = 15) WHERE id = 5; -- Маркетинг: Иванова (user_id=15)
UPDATE departments SET head_id = (SELECT id FROM employees WHERE user_id = 17) WHERE id = 3; -- Финансы: Кузнецов (user_id=17)

-- ============================================
-- 5. Обучения сотрудников
-- ============================================
-- Разнообразные обучения для тестирования функционала
-- ВАЖНО: employee_id учитывает что в V2 уже есть employees с id=1 и id=2
-- Новые employees начинаются с id=3
-- Используем подзапросы для получения employee_id по user_id для надежности
INSERT INTO trainings (employee_id, training_name, start_date, end_date, level_before, level_after) VALUES
    -- Обучения для IT отдела (employee.id начинается с 3)
    -- Сидоров (user_id=4) -> employee.id=3
    ((SELECT id FROM employees WHERE user_id = 4), 'Java Spring Boot Advanced', '2023-01-15', '2023-03-15', 'MIDDLE', 'SENIOR'),
    ((SELECT id FROM employees WHERE user_id = 4), 'Microservices Architecture', '2023-06-01', '2023-07-15', 'SENIOR', 'SENIOR'),
    -- Козлов (user_id=5) -> employee.id=4
    ((SELECT id FROM employees WHERE user_id = 5), 'React и TypeScript', '2023-02-10', '2023-04-10', 'JUNIOR', 'MIDDLE'),
    -- Волков (user_id=6) -> employee.id=5
    ((SELECT id FROM employees WHERE user_id = 6), 'Docker и Kubernetes', '2023-05-20', '2023-06-20', 'MIDDLE', 'MIDDLE'),
    -- Новиков (user_id=7) -> employee.id=6
    ((SELECT id FROM employees WHERE user_id = 7), 'Основы программирования', '2022-11-01', '2023-01-01', 'JUNIOR', 'JUNIOR'),
    
    -- Обучения для HR
    -- Морозов (user_id=8) -> employee.id=7
    ((SELECT id FROM employees WHERE user_id = 8), 'Управление персоналом', '2023-03-01', '2023-04-15', 'MIDDLE', 'SENIOR'),
    -- Петрова (user_id=9) -> employee.id=8
    ((SELECT id FROM employees WHERE user_id = 9), 'Трудовое право', '2023-01-10', '2023-02-28', 'JUNIOR', 'MIDDLE'),
    -- Соколова (user_id=10) -> employee.id=9
    ((SELECT id FROM employees WHERE user_id = 10), 'HR аналитика', '2023-08-01', '2023-09-15', 'MIDDLE', 'MIDDLE'),
    
    -- Обучения для Продаж
    -- Лебедева (user_id=11) -> employee.id=11
    ((SELECT id FROM employees WHERE user_id = 11), 'Техники продаж', '2023-02-01', '2023-03-15', 'MIDDLE', 'SENIOR'),
    -- Романова (user_id=12) -> employee.id=12
    ((SELECT id FROM employees WHERE user_id = 12), 'Работа с клиентами', '2023-04-01', '2023-05-01', 'JUNIOR', 'MIDDLE'),
    -- Орлова (user_id=13) -> employee.id=13
    ((SELECT id FROM employees WHERE user_id = 13), 'CRM системы', '2023-07-01', '2023-08-15', 'JUNIOR', 'JUNIOR'),
    -- Волкова (user_id=14) -> employee.id=14
    ((SELECT id FROM employees WHERE user_id = 14), 'Управление продажами', '2022-09-01', '2022-11-01', 'MIDDLE', 'SENIOR'),
    
    -- Обучения для Маркетинга
    -- Иванова (user_id=15) -> employee.id=15
    ((SELECT id FROM employees WHERE user_id = 15), 'Digital Marketing', '2023-03-15', '2023-05-15', 'MIDDLE', 'SENIOR'),
    -- Смирнова (user_id=16) -> employee.id=16
    ((SELECT id FROM employees WHERE user_id = 16), 'SMM стратегии', '2023-06-01', '2023-07-15', 'JUNIOR', 'MIDDLE'),
    
    -- Обучения для Финансов
    -- Кузнецов (user_id=17) -> employee.id=17
    ((SELECT id FROM employees WHERE user_id = 17), 'Финансовый анализ', '2023-01-20', '2023-03-20', 'MIDDLE', 'SENIOR'),
    -- Федоров (user_id=18) -> employee.id=18
    ((SELECT id FROM employees WHERE user_id = 18), 'Бухгалтерский учет', '2023-05-01', '2023-06-30', 'JUNIOR', 'MIDDLE'),
    
    -- Обучения для Разработки
    -- Васильев (user_id=19) -> employee.id=19
    ((SELECT id FROM employees WHERE user_id = 19), 'Python Advanced', '2023-04-01', '2023-06-01', 'MIDDLE', 'MIDDLE'),
    -- Егоров (user_id=20) -> employee.id=20
    ((SELECT id FROM employees WHERE user_id = 20), 'Frontend разработка', '2023-02-15', '2023-04-15', 'JUNIOR', 'MIDDLE'),
    
    -- Обучения для Тестирования
    -- Никитин (user_id=21) -> employee.id=21
    ((SELECT id FROM employees WHERE user_id = 21), 'Автоматизация тестирования', '2023-08-01', '2023-09-30', 'JUNIOR', 'JUNIOR');

-- ============================================
-- 6. Пропуски сотрудников
-- ============================================
-- Разнообразные пропуски: отпуска, больничные, уважительные и неуважительные
-- Используем подзапросы для получения employee_id по user_id
INSERT INTO absences (employee_id, start_date, end_date, description, status) VALUES
    -- Отпуска (уважительные)
    -- Сидоров (user_id=4) -> employee.id=3
    ((SELECT id FROM employees WHERE user_id = 4), '2023-07-01', '2023-07-14', 'Ежегодный оплачиваемый отпуск', 'GOOD_REASON'),
    -- Козлов (user_id=5) -> employee.id=4
    ((SELECT id FROM employees WHERE user_id = 5), '2023-08-15', '2023-08-28', 'Ежегодный оплачиваемый отпуск', 'GOOD_REASON'),
    -- Морозов (user_id=8) -> employee.id=7
    ((SELECT id FROM employees WHERE user_id = 8), '2023-06-10', '2023-06-23', 'Ежегодный оплачиваемый отпуск', 'GOOD_REASON'),
    -- Лебедева (user_id=11) -> employee.id=11
    ((SELECT id FROM employees WHERE user_id = 11), '2023-09-01', '2023-09-14', 'Ежегодный оплачиваемый отпуск', 'GOOD_REASON'),
    -- Иванова (user_id=15) -> employee.id=15
    ((SELECT id FROM employees WHERE user_id = 15), '2023-10-01', '2023-10-14', 'Ежегодный оплачиваемый отпуск', 'GOOD_REASON'),
    
    -- Больничные (уважительные)
    -- Волков (user_id=6) -> employee.id=5
    ((SELECT id FROM employees WHERE user_id = 6), '2023-03-05', '2023-03-12', 'Больничный лист', 'GOOD_REASON'),
    -- Морозов (user_id=8) -> employee.id=7
    ((SELECT id FROM employees WHERE user_id = 8), '2023-04-20', '2023-04-27', 'Больничный лист', 'GOOD_REASON'),
    -- Романова (user_id=12) -> employee.id=12
    ((SELECT id FROM employees WHERE user_id = 12), '2023-05-10', '2023-05-17', 'Больничный лист', 'GOOD_REASON'),
    -- Кузнецов (user_id=17) -> employee.id=17
    ((SELECT id FROM employees WHERE user_id = 17), '2023-06-15', '2023-06-22', 'Больничный лист', 'GOOD_REASON'),
    
    -- Длительные больничные
    -- Петрова (user_id=9) -> employee.id=8
    ((SELECT id FROM employees WHERE user_id = 9), '2023-02-01', '2023-02-20', 'Больничный лист (операция)', 'GOOD_REASON'),
    -- Смирнова (user_id=16) -> employee.id=16
    ((SELECT id FROM employees WHERE user_id = 16), '2023-11-01', '2023-11-15', 'Больничный лист', 'GOOD_REASON'),
    
    -- Неуважительные пропуски
    -- Новиков (user_id=7) -> employee.id=6
    ((SELECT id FROM employees WHERE user_id = 7), '2023-01-15', '2023-01-15', 'Прогул', 'BAD_REASON'),
    -- Орлова (user_id=13) -> employee.id=13
    ((SELECT id FROM employees WHERE user_id = 13), '2023-03-20', '2023-03-20', 'Прогул', 'BAD_REASON'),
    -- Егоров (user_id=20) -> employee.id=20
    ((SELECT id FROM employees WHERE user_id = 20), '2023-04-10', '2023-04-10', 'Прогул', 'BAD_REASON'),
    -- Никитин (user_id=21) -> employee.id=21
    ((SELECT id FROM employees WHERE user_id = 21), '2023-05-05', '2023-05-05', 'Прогул', 'BAD_REASON'),
    
    -- Отпуск по уходу за ребенком
    -- Соколова (user_id=10) -> employee.id=9
    ((SELECT id FROM employees WHERE user_id = 10), '2023-01-01', '2023-06-30', 'Отпуск по уходу за ребенком', 'GOOD_REASON'),
    
    -- Несколько пропусков у одного сотрудника (для тестирования)
    -- Сидоров (user_id=4) -> employee.id=3
    ((SELECT id FROM employees WHERE user_id = 4), '2023-12-20', '2023-12-31', 'Новогодние праздники', 'GOOD_REASON'),
    -- Лебедева (user_id=11) -> employee.id=11
    ((SELECT id FROM employees WHERE user_id = 11), '2023-12-25', '2024-01-08', 'Новогодние праздники', 'GOOD_REASON'),
    
    -- Недавние пропуски (для тестирования фильтров по датам)
    -- Васильев (user_id=19) -> employee.id=19
    ((SELECT id FROM employees WHERE user_id = 19), '2024-01-10', '2024-01-12', 'Больничный лист', 'GOOD_REASON'),
    -- Федоров (user_id=18) -> employee.id=18
    ((SELECT id FROM employees WHERE user_id = 18), '2024-02-01', '2024-02-05', 'Отпуск', 'GOOD_REASON');

-- ============================================
-- Итого добавлено:
-- ============================================
-- - 8 новых отделов
-- - 20 новых пользователей (1 ADMIN, 4 HEAD, 15 EMPLOYEE)
-- - 20 новых сотрудников с разнообразными данными
-- - 5 руководителей отделов назначено
-- - 20 обучений для различных сотрудников
-- - 20 пропусков (отпуска, больничные, прогулы)
-- 
-- Данные покрывают различные сценарии:
-- - Разные отделы
-- - Разные полы (мужчины и женщины)
-- - Разные возрастные группы
-- - Разные уровни компетенции (JUNIOR, MIDDLE, SENIOR)
-- - Уволенные сотрудники (для тестирования фильтров)
-- - Сотрудники без отдела
-- - Разные типы пропусков (уважительные/неуважительные)
-- - Разные периоды обучения
-- - Разные статусы обучения (повышение уровня/без изменения)

