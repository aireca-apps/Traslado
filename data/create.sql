-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         5.6.17 - MySQL Community Server (GPL)
-- SO del servidor:              Win64
-- HeidiSQL Versión:             9.3.0.5033
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- Volcando estructura de base de datos para iparsex
CREATE DATABASE IF NOT EXISTS `iparsex` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `iparsex`;


-- Volcando estructura para tabla iparsex.persona
CREATE TABLE IF NOT EXISTS `persona` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `dni` varchar(9) DEFAULT NULL COMMENT 'DNI persona, no es un NIF',
  `pass` varchar(250) DEFAULT NULL COMMENT 'password de usuario',
  `fecha_nacimiento` date DEFAULT NULL COMMENT 'Fecha nacimiento',
  `observaciones` text,
  `email` varchar(250) DEFAULT NULL COMMENT 'email de contacto',
  `nombre` varchar(200) DEFAULT 'ANONIMO' COMMENT 'nombre persona',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='persona fisica';

-- La exportación de datos fue deseleccionada.
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
