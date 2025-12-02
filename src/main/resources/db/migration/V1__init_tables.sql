
-- ============================================
-- 2. Таблица пользователей (для авторизации)
-- ============================================
CREATE TABLE users (
                       id INT AUTO_INCREMENT PRIMARY KEY,
                       login VARCHAR(50) NOT NULL UNIQUE,
                       password_hash VARCHAR(255) NOT NULL,
                       role ENUM('ADMIN', 'HEAD', 'EMPLOYEE') NOT NULL
);

-- ============================================
-- 3. Таблица отделов
-- ============================================
CREATE TABLE departments (
                             id INT AUTO_INCREMENT PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             head_id INT DEFAULT NULL  -- руководитель отдела (сотрудник)
);

-- ============================================
-- 4. Таблица сотрудников
-- ============================================
CREATE TABLE employees (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           user_id INT UNIQUE, -- связь с таблицей users (для авторизации)
                           full_name VARCHAR(100) NOT NULL,
                           gender ENUM('М', 'Ж') NOT NULL,
                           birth_date DATE,
                           hire_date DATE NOT NULL,
                           fire_date DATE DEFAULT NULL,
                           competence_rank ENUM('JUNIOR', 'MIDDLE', 'SENIOR') NOT NULL,
                           competence_level TINYINT CHECK (competence_level BETWEEN 1 AND 3),
                           department_id INT,
                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                           FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL
);

-- ============================================
-- 5. Добавляем внешний ключ на руководителя отдела
-- (делаем это отдельно, чтобы не было циклов)
-- ============================================
ALTER TABLE departments
    ADD CONSTRAINT fk_departments_head
        FOREIGN KEY (head_id) REFERENCES employees(id)
            ON DELETE SET NULL;

-- ============================================
-- 6. Таблица обучений (1 сотрудник -> много курсов)
-- ============================================
CREATE TABLE trainings (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           employee_id INT NOT NULL,
                           training_name VARCHAR(150) NOT NULL,
                           start_date DATE,
                           end_date DATE,
                           level_before ENUM('JUNIOR', 'MIDDLE', 'SENIOR'),
                           level_after ENUM('JUNIOR', 'MIDDLE', 'SENIOR'),
                           FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- ============================================
-- 7. Таблица пропусков (отпуск, больничный и т.п.)
-- ============================================
CREATE TABLE absences (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          employee_id INT NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE,
                          description VARCHAR(255),
                          status ENUM('GOOD_REASON', 'BAD_REASON') DEFAULT 'GOOD_REASON',
                          FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);