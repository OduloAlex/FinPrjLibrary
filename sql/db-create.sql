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
                                                      `year` YEAR(4) NULL,
                                                      `fine` DECIMAL(19,2) NOT NULL,
                                                      `description` VARCHAR(1024) NULL,
                                                      `autor_id` INT NULL,
                                                      `publishing_id` INT NULL,
                                                      PRIMARY KEY (`id`),
                                                      INDEX `fk_catalog_autor1_idx` (`autor_id` ASC) VISIBLE,
                                                      INDEX `fk_catalog_publishing1_idx` (`publishing_id` ASC) VISIBLE,
                                                      CONSTRAINT `fk_catalog_autor1`
                                                          FOREIGN KEY (`autor_id`)
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
                                                    `state` ENUM('lib', 'room', 'hand') NOT NULL DEFAULT 'lib',
                                                    `catalog_id` INT NOT NULL,
                                                    PRIMARY KEY (`id`, `catalog_id`),
                                                    INDEX `fk_books_catalog1_idx` (`catalog_id` ASC) VISIBLE,
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
                                                    `active` TINYINT NULL DEFAULT 1 COMMENT 'РџРѕР»СЊР·РѕРІР°С‚РµР»СЊ Р°РєС‚РёРІРµРЅ/РЅРµ Р°РєС‚РёРІРµРЅ',
                                                    `description` VARCHAR(1024) NULL,
                                                    `role_id` INT NOT NULL,
                                                    PRIMARY KEY (`id`, `role_id`),
                                                    INDEX `fk_users_role1_idx` (`role_id` ASC) VISIBLE,
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
                                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                                    `return_time` DATETIME NOT NULL,
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


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

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
INSERT INTO `fp2library`.`users` (`id`, `username`, `password`, `active`, `description`, `role_id`) VALUES (1, 'admin', '1234', 1, 'администратор', 1);
INSERT INTO `fp2library`.`users` (`id`, `username`, `password`, `active`, `description`, `role_id`) VALUES (2, 'Alex', '1234', 1, 'читатель', 2);

COMMIT;

