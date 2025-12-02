-- Пароли должны храниться в BCrypt! Здесь — только пример.
INSERT INTO users (login, password_hash, role)
VALUES
    ('admin', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_ADMIN'),
    ('head1', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_HEAD'),
    ('emp1', '$2a$10$abcdefghijklmnopqrstuv', 'ROLE_EMPLOYEE');

INSERT INTO departments (name)
VALUES
    ('IT'),
    ('HR'),
    ('Finance');

INSERT INTO employees (
    user_id, full_name, gender, birth_date, hire_date, competence_rank, competence_level, department_id
)
VALUES
    (2, 'Иванов Иван', 'М', '1987-03-12', '2017-06-01', 'SENIOR', 3, 1),
    (3, 'Петров Пётр', 'М', '1994-11-05', '2020-02-15', 'MIDDLE', 2, 1);

UPDATE departments SET head_id = 1 WHERE id = 1;

INSERT INTO trainings (employee_id, training_name, start_date, end_date, level_before, level_after)
VALUES
    (2, 'Java Advanced', '2023-02-01', '2023-03-01', 'MIDDLE', 'SENIOR');

INSERT INTO absences (employee_id, start_date, end_date, description, status)
VALUES
    (2, '2023-10-01', '2023-10-10', 'Отпуск', 'GOOD_REASON');
