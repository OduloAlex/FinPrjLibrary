-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema fp2library
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `fp2library` ;

-- -----------------------------------------------------
-- Schema fp2library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `fp2library` DEFAULT CHARACTER SET utf8 ;
USE `fp2library` ;

-- -----------------------------------------------------
-- Table `fp2library`.`author`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`author` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`author` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`publishing`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`publishing` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`publishing` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`catalog`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`catalog` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`catalog` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `year` INT NULL,
  `fine` INT NOT NULL,
  `description` VARCHAR(1024) NULL,
  `quantity` INT NOT NULL DEFAULT 0,
  `author_id` INT NULL,
  `publishing_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_catalog_autor1_idx` (`author_id` ASC) VISIBLE,
  INDEX `fk_catalog_publishing1_idx` (`publishing_id` ASC) VISIBLE,
  CONSTRAINT `fk_catalog_autor1`
    FOREIGN KEY (`author_id`)
    REFERENCES `fp2library`.`author` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE,
  CONSTRAINT `fk_catalog_publishing1`
    FOREIGN KEY (`publishing_id`)
    REFERENCES `fp2library`.`publishing` (`id`)
    ON DELETE SET NULL
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`books`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`books` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`books` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `state` INT NOT NULL DEFAULT 0,
  `inv_number` VARCHAR(45) NOT NULL,
  `catalog_id` INT NOT NULL,
  PRIMARY KEY (`id`, `catalog_id`),
  INDEX `fk_books_catalog1_idx` (`catalog_id` ASC) VISIBLE,
  UNIQUE INDEX `inv_number_UNIQUE` (`inv_number` ASC) VISIBLE,
  CONSTRAINT `fk_books_catalog1`
    FOREIGN KEY (`catalog_id`)
    REFERENCES `fp2library`.`catalog` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`role`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`role` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`users` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `active` TINYINT NULL DEFAULT 1 COMMENT 'Пользователь активен/не активен',
  `description` VARCHAR(1024) NULL,
  `locale` VARCHAR(5) NULL DEFAULT 'en',
  `role_id` INT NOT NULL,
  PRIMARY KEY (`id`, `role_id`),
  INDEX `fk_users_role1_idx` (`role_id` ASC) VISIBLE,
  UNIQUE INDEX `username_UNIQUE` (`username` ASC) VISIBLE,
  CONSTRAINT `fk_users_role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `fp2library`.`role` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


-- -----------------------------------------------------
-- Table `fp2library`.`cards`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`cards` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`cards` (
  `create_time` DATE NOT NULL,
  `return_time` DATE NOT NULL,
  `books_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  PRIMARY KEY (`books_id`, `users_id`),
  INDEX `fk_cards_books1_idx` (`books_id` ASC) VISIBLE,
  UNIQUE INDEX `books_id_UNIQUE` (`books_id` ASC) VISIBLE,
  INDEX `fk_cards_users1_idx` (`users_id` ASC) VISIBLE,
  CONSTRAINT `fk_cards_books1`
    FOREIGN KEY (`books_id`)
    REFERENCES `fp2library`.`books` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_cards_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `fp2library`.`users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `fp2library`.`orders`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `fp2library`.`orders` ;

CREATE TABLE IF NOT EXISTS `fp2library`.`orders` (
  `catalog_id` INT NOT NULL,
  `users_id` INT NOT NULL,
  `state` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`catalog_id`, `users_id`),
  INDEX `fk_catalog_has_users_users1_idx` (`users_id` ASC) VISIBLE,
  INDEX `fk_catalog_has_users_catalog1_idx` (`catalog_id` ASC) VISIBLE,
  CONSTRAINT `fk_catalog_has_users_catalog1`
    FOREIGN KEY (`catalog_id`)
    REFERENCES `fp2library`.`catalog` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_catalog_has_users_users1`
    FOREIGN KEY (`users_id`)
    REFERENCES `fp2library`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `fp2library`.`author`
-- -----------------------------------------------------
START TRANSACTION;
USE `fp2library`;
INSERT INTO `fp2library`.`author` (`id`, `name`) VALUES (1, 'Лев Толстой');
INSERT INTO `fp2library`.`author` (`id`, `name`) VALUES (2, 'Народная');
INSERT INTO `fp2library`.`author` (`id`, `name`) VALUES (3, 'Ron Harris');
INSERT INTO `fp2library`.`author` (`id`, `name`) VALUES (4, 'Дональд Эрвин Кнут');

COMMIT;


-- -----------------------------------------------------
-- Data for table `fp2library`.`publishing`
-- -----------------------------------------------------
START TRANSACTION;
USE `fp2library`;
INSERT INTO `fp2library`.`publishing` (`id`, `name`) VALUES (1, 'Пятый Колизей');
INSERT INTO `fp2library`.`publishing` (`id`, `name`) VALUES (2, 'РязаньЛит');
INSERT INTO `fp2library`.`publishing` (`id`, `name`) VALUES (3, 'Самиздат');
INSERT INTO `fp2library`.`publishing` (`id`, `name`) VALUES (4, 'Фолиант');

COMMIT;


-- -----------------------------------------------------
-- Data for table `fp2library`.`catalog`
-- -----------------------------------------------------
START TRANSACTION;
USE `fp2library`;
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (1, 'Война и мир', 1995, 100, 'роман', 0, 1, 1);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (2, 'Колобок', 2005, 50, 'сказка', 0, 2, 1);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (3, 'Delphy 5', 1999, 130, 'тех лит', 0, 3, 2);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (4, 'Искусство программирования', 1967, 200, 'алгоритмы', 0, 4, 2);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (5, 'Воскресение', 1958, 180, 'роман', 0, 1, 4);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (6, 'Как курить Грибы', 2017, 400, 'пособие', 0, 2, 3);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (7, 'Windows 3.11', 1995, 100, 'тех лит', 0, 3, 2);
INSERT INTO `fp2library`.`catalog` (`id`, `name`, `year`, `fine`, `description`, `quantity`, `author_id`, `publishing_id`) VALUES (8, 'Хаджи Мурат', 1912, 1912, 'роман', 0, 1, 4);

COMMIT;


-- -----------------------------------------------------
-- Data for table `fp2library`.`role`
-- -----------------------------------------------------
START TRANSACTION;
USE `fp2library`;
INSERT INTO `fp2library`.`role` (`id`, `name`) VALUES (1, 'admin');
INSERT INTO `fp2library`.`role` (`id`, `name`) VALUES (2, 'librarian');
INSERT INTO `fp2library`.`role` (`id`, `name`) VALUES (3, 'reader');

COMMIT;


-- -----------------------------------------------------
-- Data for table `fp2library`.`users`
-- -----------------------------------------------------
START TRANSACTION;
USE `fp2library`;
INSERT INTO `fp2library`.`users` (`id`, `username`, `password`, `active`, `description`, `locale`, `role_id`) VALUES (1, 'admin', '1234', 1, 'администратор', 'en', 1);
INSERT INTO `fp2library`.`users` (`id`, `username`, `password`, `active`, `description`, `locale`, `role_id`) VALUES (2, 'alex', '2345', 1, 'читатель', 'en', 3);

COMMIT;

