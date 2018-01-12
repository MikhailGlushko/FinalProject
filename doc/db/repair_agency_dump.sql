-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: repair_agency
-- ------------------------------------------------------
-- Server version	5.7.18-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `clients`
--

use `repair_agency`;

DROP TABLE IF EXISTS `clients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `clients` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repair_shop` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `user` int(11),
  PRIMARY KEY (`id`),
  KEY `c_repair_shop_idx` (`repair_shop`),
  CONSTRAINT `c_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `clients`
--

LOCK TABLES `clients` WRITE;
/*!40000 ALTER TABLE `clients` DISABLE KEYS */;
/*!40000 ALTER TABLE `clients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `device_phisical_states`
--

DROP TABLE IF EXISTS `device_phisical_states`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_phisical_states` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '1 - новий, 2 - потертій, 3 - пошкоджений',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `device_phisical_states`
--

LOCK TABLES `device_phisical_states` WRITE;
/*!40000 ALTER TABLE `device_phisical_states` DISABLE KEYS */;
INSERT INTO `device_phisical_states` VALUES (1,'Новий'),(2,'Потертий'),(3,'Пошкоджений');
/*!40000 ALTER TABLE `device_phisical_states` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repair_shop` int(11) NOT NULL,
  `client` int(11) NOT NULL,
  `device_name` varchar(45) NOT NULL,
  `brand_name` varchar(45) DEFAULT NULL,
  `model_name` varchar(45) DEFAULT NULL,
  `serial_number` varchar(45) DEFAULT NULL,
  `memo` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `d_repair_shop_idx` (`repair_shop`),
  KEY `d_client_idx` (`client`),
  CONSTRAINT `d_client` FOREIGN KEY (`client`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `d_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `devices`
--

LOCK TABLES `devices` WRITE;
/*!40000 ALTER TABLE `devices` DISABLE KEYS */;
/*!40000 ALTER TABLE `devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_history`
--

DROP TABLE IF EXISTS `order_history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repair_shop` int(11) NOT NULL,
  `order` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `executor` int(11) NOT NULL,
  `description` varchar(255) NOT NULL,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `oh_repair_shop_idx` (`repair_shop`),
  KEY `oh_order_idx` (`order`),
  KEY `oh_status_idx` (`status`),
  KEY `oh_executor_idx` (`executor`),
  CONSTRAINT `oh_executor` FOREIGN KEY (`executor`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `oh_order` FOREIGN KEY (`order`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `oh_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `oh_status` FOREIGN KEY (`status`) REFERENCES `order_statuses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_history`
--

LOCK TABLES `order_history` WRITE;
/*!40000 ALTER TABLE `order_history` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_statuses`
--

DROP TABLE IF EXISTS `order_statuses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_statuses` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '1 - НОВИЙ, 2 - В РОБОТІ, ВІДКЛАДЕНИЙ,  ВИКОНАНИЙ, ЗАКРИТИЙ, ВІДХИЛЕНИЙ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_statuses`
--

LOCK TABLES `order_statuses` WRITE;
/*!40000 ALTER TABLE `order_statuses` DISABLE KEYS */;
INSERT INTO `order_statuses` VALUES (2,'В роботі'),(6,'Відхилений'),(4,'Виконаний'),(5,'Закритий'),(1,'Новий'),(3,'Призупинений');
/*!40000 ALTER TABLE `order_statuses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_types`
--

DROP TABLE IF EXISTS `order_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_types` (
  `id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL COMMENT '1 - Платний, 2 - Гарантійний',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_types`
--

LOCK TABLES `order_types` WRITE;
/*!40000 ALTER TABLE `order_types` DISABLE KEYS */;
INSERT INTO `order_types` VALUES (1,'Платний'),(2,'Гарантійний');
/*!40000 ALTER TABLE `order_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_type` int(11) NOT NULL,
  `repair_shop` int(11) NOT NULL,
  `repair_service` int(11) NOT NULL,
  `repair_service_kind` int(11) NOT NULL,
  `repair_device` int(11) NOT NULL,
  `creator` int(11) NOT NULL,
  `executor` int(11) NOT NULL,
  `status` int(11) NOT NULL,
  `client` int(11) NOT NULL,
  `device` int(11) NOT NULL,
  `defect` varchar(255) NOT NULL,
  `device_phisical_state` int(11) NOT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `modify_date` datetime DEFAULT NULL,
  `deadline_date` datetime NOT NULL,
  `urgent` tinyint(4) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `prepaid` decimal(10,0) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `o_order_type_idx` (`order_type`),
  KEY `o_repair_shop_idx` (`repair_shop`),
  KEY `o_repair_service_idx` (`repair_service`),
  KEY `0_repair_device_idx` (`repair_device`),
  KEY `o_manager_idx` (`creator`),
  KEY `o_executor_idx` (`executor`),
  KEY `o_status_idx` (`status`),
  KEY `o_client_idx` (`client`),
  KEY `o_phisical_state_idx` (`device_phisical_state`),
  KEY `o_device_idx` (`device`),
  KEY `o_repair_service_kind_idx` (`repair_service_kind`),
  CONSTRAINT `o_client` FOREIGN KEY (`client`) REFERENCES `clients` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_creator` FOREIGN KEY (`creator`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_device` FOREIGN KEY (`device`) REFERENCES `devices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_device_phisical_state` FOREIGN KEY (`device_phisical_state`) REFERENCES `device_phisical_states` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_executor` FOREIGN KEY (`executor`) REFERENCES `users` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_order_type` FOREIGN KEY (`order_type`) REFERENCES `order_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_repair_device` FOREIGN KEY (`repair_device`) REFERENCES `repair_devices` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_repair_service` FOREIGN KEY (`repair_service`) REFERENCES `repair_services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_repair_service_kind` FOREIGN KEY (`repair_service_kind`) REFERENCES `repair_service_kinds` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `o_status` FOREIGN KEY (`status`) REFERENCES `order_statuses` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_devices`
--

DROP TABLE IF EXISTS `repair_devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repair_devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `repair_service` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `rd_repair_service_idx` (`repair_service`),
  CONSTRAINT `rd_repair_service` FOREIGN KEY (`repair_service`) REFERENCES `repair_services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_devices`
--

LOCK TABLES `repair_devices` WRITE;
/*!40000 ALTER TABLE `repair_devices` DISABLE KEYS */;
INSERT INTO `repair_devices` VALUES (1,'Пилосос',NULL,1),(2,'Музичний центр',NULL,1),(3,'Телевізор',NULL,1),(4,'Відеп-плеєр',NULL,1),(5,'Тюнер',NULL,1);
/*!40000 ALTER TABLE `repair_devices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_service_kinds`
--

DROP TABLE IF EXISTS `repair_service_kinds`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repair_service_kinds` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `repair_service` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `rsk_repair_service_idx` (`repair_service`),
  CONSTRAINT `rsk_repair_service` FOREIGN KEY (`repair_service`) REFERENCES `repair_services` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_service_kinds`
--

LOCK TABLES `repair_service_kinds` WRITE;
/*!40000 ALTER TABLE `repair_service_kinds` DISABLE KEYS */;
INSERT INTO `repair_service_kinds` VALUES (1,'Огляд',1),(2,'Чистка',1),(3,'Перевірка',1),(4,'Профілактика',1),(5,'Ремонт',1);
/*!40000 ALTER TABLE `repair_service_kinds` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_services`
--

DROP TABLE IF EXISTS `repair_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repair_services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `repair_shop` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `rs_repair_shop_idx` (`repair_shop`),
  CONSTRAINT `rs_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_services`
--

LOCK TABLES `repair_services` WRITE;
/*!40000 ALTER TABLE `repair_services` DISABLE KEYS */;
INSERT INTO `repair_services` VALUES (1,'Ремонт побутової техніки',NULL,1);
/*!40000 ALTER TABLE `repair_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `repair_shops`
--

DROP TABLE IF EXISTS `repair_shops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `repair_shops` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `repair_shops`
--

LOCK TABLES `repair_shops` WRITE;
/*!40000 ALTER TABLE `repair_shops` DISABLE KEYS */;
INSERT INTO `repair_shops` VALUES (1,'Ремонтное Агенство','Ремонтное Агенство',NULL,NULL);
/*!40000 ALTER TABLE `repair_shops` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '1 - SYSADMIN, 2 - ADMIN, 3 - MANAGER, 4 - MASTER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'SYSADMIN'),
(2,'ADMIN'),
(3,'MANAGER'),
(4,'MASTER'),
(5,'CLIENT');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `repair_shop` int(11) NOT NULL,
  `userRole` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `u_role_idx` (`userRole`),
  KEY `u_repair_shop_idx` (`repair_shop`),
  CONSTRAINT `u_repair_shop` FOREIGN KEY (`repair_shop`) REFERENCES `repair_shops` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `u_role` FOREIGN KEY (`userRole`) REFERENCES `roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,1,2,'Admin','admin','admin','admin@email.com',NULL,NULL),
(2,1,3,'Manager','manager','manager','manager@email.com',NULL,NULL),
(3,1,4,'Master','master','master','master@email.com',NULL,NULL),
(4,1,5,'Client','user','user','client@email.com',NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-21 19:47:07
