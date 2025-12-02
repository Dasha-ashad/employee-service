-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema hr_monitoring
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema hr_monitoring_system
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hr_monitoring_system
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hr_monitoring_system` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
-- -----------------------------------------------------
-- Schema hr_management
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema hr_management
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `hr_management` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
-- -----------------------------------------------------
-- Schema monitoring_personnel
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema monitoring_personnel
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `monitoring_personnel` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
-- -----------------------------------------------------
-- Schema personnel_monitoring
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema personnel_monitoring
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `personnel_monitoring` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci ;
USE `hr_monitoring_system` ;

-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`departments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name` (`name` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `full_name` VARCHAR(150) NOT NULL,
  `birth_date` DATE NULL DEFAULT NULL,
  `gender` ENUM('м', 'ж') NOT NULL,
  `hire_date` DATE NOT NULL,
  `fire_date` DATE NULL DEFAULT NULL,
  `position` VARCHAR(100) NULL DEFAULT NULL,
  `department_id` INT NULL DEFAULT NULL,
  `role_id` INT NULL DEFAULT NULL,
  `competence_level` FLOAT NULL DEFAULT '1',
  `status` ENUM('активен', 'уволен') NULL DEFAULT 'активен',
  PRIMARY KEY (`id`),
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  INDEX `role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `employees_ibfk_1`
    FOREIGN KEY (`department_id`)
    REFERENCES `hr_monitoring_system`.`departments` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `employees_ibfk_2`
    FOREIGN KEY (`role_id`)
    REFERENCES `hr_monitoring_system`.`roles` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`disciplinary_actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`disciplinary_actions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `action_date` DATE NULL DEFAULT curdate(),
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `disciplinary_actions_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_monitoring_system`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(150) NOT NULL,
  `provider` VARCHAR(100) NULL DEFAULT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `skill` VARCHAR(100) NULL DEFAULT NULL,
  `skill_increase` FLOAT NULL DEFAULT '0.5',
  `cost` DECIMAL(10,2) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`employee_trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`employee_trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `training_id` INT NOT NULL,
  `completion_status` ENUM('в процессе', 'завершено') NULL DEFAULT 'в процессе',
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  INDEX `training_id` (`training_id` ASC) VISIBLE,
  CONSTRAINT `employee_trainings_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_monitoring_system`.`employees` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `employee_trainings_ibfk_2`
    FOREIGN KEY (`training_id`)
    REFERENCES `hr_monitoring_system`.`trainings` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`hr_analytics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`hr_analytics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `period_start` DATE NULL DEFAULT NULL,
  `period_end` DATE NULL DEFAULT NULL,
  `total_employees` INT NULL DEFAULT NULL,
  `fired_employees` INT NULL DEFAULT NULL,
  `turnover_rate` FLOAT NULL DEFAULT NULL,
  `avg_competence` FLOAT NULL DEFAULT NULL,
  `report_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`projects`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`projects` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `department_id` INT NULL DEFAULT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `projects_ibfk_1`
    FOREIGN KEY (`department_id`)
    REFERENCES `hr_monitoring_system`.`departments` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`project_team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`project_team` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `project_id` INT NOT NULL,
  `employee_id` INT NOT NULL,
  `role_in_project` VARCHAR(100) NULL DEFAULT NULL,
  `workload_percent` FLOAT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `project_id` (`project_id` ASC) VISIBLE,
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `project_team_ibfk_1`
    FOREIGN KEY (`project_id`)
    REFERENCES `hr_monitoring_system`.`projects` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `project_team_ibfk_2`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_monitoring_system`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`reports`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`reports` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` ENUM('по возрасту', 'по полу', 'по отделам', 'по текучести', 'по обучению') NULL DEFAULT NULL,
  `generated_by` INT NULL DEFAULT NULL,
  `generated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `data` JSON NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `generated_by` (`generated_by` ASC) VISIBLE,
  CONSTRAINT `reports_ibfk_1`
    FOREIGN KEY (`generated_by`)
    REFERENCES `hr_monitoring_system`.`employees` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `hr_monitoring_system`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_monitoring_system`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NULL DEFAULT NULL,
  `username` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  UNIQUE INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `users_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_monitoring_system`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `hr_management` ;

-- -----------------------------------------------------
-- Table `hr_management`.`departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`departments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `head_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_head` (`head_id` ASC) VISIBLE,
  CONSTRAINT `fk_head`
    FOREIGN KEY (`head_id`)
    REFERENCES `hr_management`.`employees` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `birth_date` DATE NULL DEFAULT NULL,
  `gender` ENUM('М', 'Ж') NOT NULL,
  `department_id` INT NULL DEFAULT NULL,
  `position` VARCHAR(100) NULL DEFAULT NULL,
  `hire_date` DATE NULL DEFAULT NULL,
  `termination_date` DATE NULL DEFAULT NULL,
  `competence_level` FLOAT NULL DEFAULT '0',
  `status` ENUM('active', 'terminated') NULL DEFAULT 'active',
  PRIMARY KEY (`id`),
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `employees_ibfk_1`
    FOREIGN KEY (`department_id`)
    REFERENCES `hr_management`.`departments` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`absence`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`absence` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `type` ENUM('vacation', 'sick') NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `absence_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_management`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`analytics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`analytics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NULL DEFAULT NULL,
  `department_id` INT NULL DEFAULT NULL,
  `metric_type` VARCHAR(100) NOT NULL,
  `metric_value` FLOAT NOT NULL,
  `calculated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `analytics_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_management`.`employees` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `analytics_ibfk_2`
    FOREIGN KEY (`department_id`)
    REFERENCES `hr_management`.`departments` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`disciplinary_actions`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`disciplinary_actions` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `date` DATE NOT NULL,
  `description` TEXT NULL DEFAULT NULL,
  `action_taken` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `disciplinary_actions_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_management`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `duration_days` INT NULL DEFAULT '1',
  `skill_level_increase` FLOAT NULL DEFAULT '0',
  `source` VARCHAR(100) NULL DEFAULT NULL,
  `cost` DECIMAL(10,2) NULL DEFAULT '0.00',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `hr_management`.`employee_trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `hr_management`.`employee_trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `training_id` INT NOT NULL,
  `status` ENUM('assigned', 'completed') NULL DEFAULT 'assigned',
  `completed_at` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  INDEX `training_id` (`training_id` ASC) VISIBLE,
  CONSTRAINT `employee_trainings_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `hr_management`.`employees` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `employee_trainings_ibfk_2`
    FOREIGN KEY (`training_id`)
    REFERENCES `hr_management`.`trainings` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;

USE `monitoring_personnel` ;

-- -----------------------------------------------------
-- Table `monitoring_personnel`.`departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`departments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `head_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_department_head` (`head_id` ASC) VISIBLE,
  CONSTRAINT `fk_department_head`
    FOREIGN KEY (`head_id`)
    REFERENCES `monitoring_personnel`.`employees` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name` (`name` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username` (`username` ASC) VISIBLE,
  INDEX `role_id` (`role_id` ASC) VISIBLE,
  CONSTRAINT `users_ibfk_1`
    FOREIGN KEY (`role_id`)
    REFERENCES `monitoring_personnel`.`roles` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NULL DEFAULT NULL,
  `first_name` VARCHAR(50) NOT NULL,
  `last_name` VARCHAR(50) NOT NULL,
  `birth_date` DATE NULL DEFAULT NULL,
  `gender` ENUM('М', 'Ж') NOT NULL,
  `department_id` INT NULL DEFAULT NULL,
  `position` VARCHAR(100) NULL DEFAULT NULL,
  `hire_date` DATE NOT NULL,
  `termination_date` DATE NULL DEFAULT NULL,
  `competence_level` FLOAT NULL DEFAULT '0',
  `disciplinary_actions` INT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `employees_ibfk_1`
    FOREIGN KEY (`department_id`)
    REFERENCES `monitoring_personnel`.`departments` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `employees_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `monitoring_personnel`.`users` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`absences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`absences` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `reason` VARCHAR(255) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `absences_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `monitoring_personnel`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`analytics`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`analytics` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `department_id` INT NULL DEFAULT NULL,
  `metric_type` VARCHAR(100) NOT NULL,
  `metric_value` FLOAT NOT NULL,
  `calculated_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `analytics_ibfk_1`
    FOREIGN KEY (`department_id`)
    REFERENCES `monitoring_personnel`.`departments` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `monitoring_personnel`.`trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `monitoring_personnel`.`trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `program_name` VARCHAR(150) NOT NULL,
  `provider` VARCHAR(100) NULL DEFAULT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `result` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `trainings_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `monitoring_personnel`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

USE `personnel_monitoring` ;

-- -----------------------------------------------------
-- Table `personnel_monitoring`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personnel_monitoring`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `password_hash` VARCHAR(60) NOT NULL,
  `role` ENUM('admin_ROLE', 'employee_ROLE', 'head_ROLE') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `login` (`login` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `personnel_monitoring`.`departments`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personnel_monitoring`.`departments` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `head_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_departments_head` (`head_id` ASC) VISIBLE,
  CONSTRAINT `fk_departments_head`
    FOREIGN KEY (`head_id`)
    REFERENCES `personnel_monitoring`.`employees` (`id`)
    ON DELETE SET NULL)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `personnel_monitoring`.`employees`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personnel_monitoring`.`employees` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `full_name` VARCHAR(100) NOT NULL,
  `gender` ENUM('М', 'Ж') NOT NULL,
  `birth_date` DATE NULL DEFAULT NULL,
  `hire_date` DATE NOT NULL,
  `fire_date` DATE NULL DEFAULT NULL,
  `competence_rank` ENUM('junior', 'middle', 'senior') NOT NULL,
  `competence_level` TINYINT NULL DEFAULT NULL,
  `department_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `user_id` (`user_id` ASC) VISIBLE,
  INDEX `department_id` (`department_id` ASC) VISIBLE,
  CONSTRAINT `employees_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `personnel_monitoring`.`users` (`id`)
    ON DELETE CASCADE,
  CONSTRAINT `employees_ibfk_2`
    FOREIGN KEY (`department_id`)
    REFERENCES `personnel_monitoring`.`departments` (`id`)
    ON DELETE SET NULL,
  CONSTRAINT `id`
    FOREIGN KEY (`user_id`)
    REFERENCES `hr_monitoring_system`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `personnel_monitoring`.`absences`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personnel_monitoring`.`absences` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL DEFAULT NULL,
  `status` ENUM('уважительная', 'неуважительная') NULL DEFAULT 'уважительная',
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `absences_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `personnel_monitoring`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


-- -----------------------------------------------------
-- Table `personnel_monitoring`.`trainings`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `personnel_monitoring`.`trainings` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `employee_id` INT NOT NULL,
  `training_name` VARCHAR(150) NOT NULL,
  `start_date` DATE NULL DEFAULT NULL,
  `end_date` DATE NULL DEFAULT NULL,
  `level_before` ENUM('junior', 'middle', 'senior') NULL DEFAULT NULL,
  `level_after` ENUM('junior', 'middle', 'senior') NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `employee_id` (`employee_id` ASC) VISIBLE,
  CONSTRAINT `trainings_ibfk_1`
    FOREIGN KEY (`employee_id`)
    REFERENCES `personnel_monitoring`.`employees` (`id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_unicode_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
