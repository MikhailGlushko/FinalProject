CREATE SCHEMA IF NOT EXISTS REPAIR_AGENCY;

SET SCHEMA REPAIR_AGENCY;

DROP TABLE IF EXISTS `roles`;
CREATE TABLE `roles`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL COMMENT '1 - SYSADMIN, 2 - ADMIN, 3 - MANAGER, 4 - MASTER',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO roles VALUES (1,'SYSADMIN'),
(2,'ADMIN'),
(3,'MANAGER'),
(4,'MASTER'),
(5,'CLIENT');

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` enum('ADMIN','MANAGER','MASTER','CUSTOMER') NOT NULL DEFAULT 'CUSTOMER',
  `name` varchar(45) NOT NULL,
  `login` varchar(45) NOT NULL,
  `password` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `status` enum('ACTIVE','CLOSE','BLOCKED') NOT NULL DEFAULT 'ACTIVE',
  `type` enum('INTERNAL','EXTERNAL') NOT NULL DEFAULT 'EXTERNAL',
  `last_login` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_login_UNIQUE` (`login`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

INSERT INTO `users` VALUES (1,'ADMIN','Administrator','admin','admin','mikhail.glushko@gmail.com','066-3864046','ACTIVE','INTERNAL',NULL),
  (2,'MANAGER','Manager','manager','manager','manager@mail.com','01','ACTIVE','INTERNAL',NULL),
  (3,'MASTER','Master','master','master','master@mail.com','02','ACTIVE','INTERNAL',NULL),
  (4,'CUSTOMER','Customer','customer','customer','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (5,'CUSTOMER','Customer5','customer5','customer5','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (6,'CUSTOMER','Customer6','customer6','customer6','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (7,'CUSTOMER','Customer7','customer7','customer7','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (8,'CUSTOMER','Customer8','customer8','customer8','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (9,'CUSTOMER','Customer9','customer9','customer9','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (10,'CUSTOMER','Customer10','customer10','customer10','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (11,'CUSTOMER','Customer11','customer11','customer11','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (12,'CUSTOMER','Customer12','customer12','customer12','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (13,'CUSTOMER','Customer13','customer13','customer13','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (14,'CUSTOMER','Customer14','customer14','customer14','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (15,'CUSTOMER','Customer15','customer15','customer15','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (16,'CUSTOMER','Customer16','customer16','customer16','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (17,'CUSTOMER','Customer17','customer17','customer17','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (18,'CUSTOMER','Customer18','customer18','customer18','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (19,'CUSTOMER','Customer19','customer19','customer19','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (20,'CUSTOMER','Customer20','customer20','customer20','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (21,'CUSTOMER','Customer21','customer21','customer21','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (22,'CUSTOMER','Customer22','customer22','customer22','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (23,'CUSTOMER','Customer23','customer23','customer23','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (24,'CUSTOMER','Customer24','customer24','customer24','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (25,'CUSTOMER','Customer25','customer25','customer25','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (26,'CUSTOMER','Customer26','customer26','customer26','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (27,'CUSTOMER','Customer27','customer27','customer27','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (28,'CUSTOMER','Customer28','customer28','customer28','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (29,'CUSTOMER','Customer29','customer29','customer29','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (30,'CUSTOMER','Customer30','customer30','customer30','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (31,'CUSTOMER','Customer31','customer31','customer31','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (32,'CUSTOMER','Customer32','customer32','customer32','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (33,'CUSTOMER','Customer33','customer33','customer33','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (34,'CUSTOMER','Customer34','customer34','customer34','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (35,'CUSTOMER','Customer35','customer35','customer35','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (36,'CUSTOMER','Customer36','customer36','customer36','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (37,'CUSTOMER','Customer37','customer37','customer37','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (38,'CUSTOMER','Customer38','customer38','customer38','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (39,'CUSTOMER','Customer39','customer39','customer39','customer@mail.com','03','ACTIVE','EXTERNAL',NULL),
  (40,'CUSTOMER','Customer40','customer40','customer40','customer@mail.com','03','ACTIVE','EXTERNAL',NULL)
  ;

DROP TABLE IF EXISTS `grants`;
CREATE TABLE `grants` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `command` varchar(45) NOT NULL,
  `menu` varchar(45) NOT NULL,
  `role` enum('ALL','ADMIN','MANAGER','MASTER','CUSTOMER','GUEST') NOT NULL,
  `action` varchar(5) NOT NULL DEFAULT 'M',
  `scope` enum('ALL','OWNER','NONE') NOT NULL DEFAULT 'NONE',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

INSERT INTO `grants` VALUES
(1,'users','users','ADMIN','MCRUD','ALL'),
(2,'users','users','MANAGER','MR','ALL'),
(3,'users','users','MASTER','MR','OWNER'),

(4,'orders','orders','ADMIN','MRU','ALL'),
(5,'orders','orders','MANAGER','MCRU','ALL'),
(6,'orders','orders','MASTER','MRU','OWNER'),
(7,'orders','orders','CUSTOMER','MCRU','OWNER'),

(8,'setup','setup','ADMIN','MCRUD','ALL'),
(9,'setup','setup','MANAGER','MRU','OWNER'),
(10,'setup','setup','MASTER','MRU','OWNER'),
(11,'setup','setup','CUSTOMER','MRU','OWNER'),

(12,'services','services','ADMIN','MCRUD','ALL');

DROP TABLE IF EXISTS `repair_services`;
CREATE TABLE `repair_services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `name_ru` varchar(45) NOT NULL,
  `parent` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=94 DEFAULT CHARSET=utf8;

INSERT INTO `repair_services` VALUES
  (1,'Repair of large household appliances','Ремонт крупной бытовой техники','0'),
  (2,'Repair of small household appliances','Ремонт мелкой бытовой техники','0'),
  (3,'Repair of digital equipment','Ремонт цыфровой техники','0'),
  (4,'Repair of office equipment','Ремонт оргтехники','0'),
  (5,'Repair of computer equipment','Ремонт компъютерной техники','0'),
  (6,'Repair of power tools','Ремонт электроинструмента','0'),
  (7,'Other repairs','Другой ремонт','0'),
  (8,'Repair of washing machines','Ремонт стиральных машин','1'),
  (9,'Repair of refrigerators','Ремонт холодильников','1'),
  (10,'Boiler repair','Ремонт бойлеров','1'),
  (11,'Repair of dishwashers','Ремонт посудомоечных машин','1'),
  (12,'Repair of air conditioners','Ремонт кондиционеров','1'),
  (13,'Vacuum cleaner repair','Ремрнт пылесосов','1'),
  (14,'Repair of hoods','Ремонт вытяжек','1'),
  (15,'Repair of gas columns','Ремонт газовых колонок','1'),
  (16,'Repair of gas boilers','Ремонт газовых котлов','1'),
  (19,'Repair of microwave ovens','Ремонт микроволновок','1'),
  (20,'Repair of gas stoves','Ремонт газовых плит','1'),
  (21,'Repair of electric stoves','Ремонт электроплит','1'),
  (22,'Repair of electric oven','Ремонт электродуховок','1'),
  (23,'Repair of freezers','Ремонт морозильных камер','1'),
  (24,'Repair of hobs','Ремонт варочных панелей','1'),
  (25,'Repair of steam generators','Ремонт парогенераторов','1'),
  (26,'Repair of waste shredders','Ремонт измельчителей отходов','1'),
  (27,'Repair of drying machines','Ремонт сушильных машин','1'),
  (28,'Repair of other equipment','Ремонт другой техники','1'),
  (29,'Sewing machine repair','Ремонт швейных машин','2'),
  (30,'Repair of irons','Ремонт утюгов','2'),
  (31,'Repair of electric kettles','Ремонт электрочайников','2'),
  (32,'Repair of multivarches','Ремонт мультиварок','2'),
  (33,'Repair of food processors','Ремонт кухонных комбайнов','2'),
  (34,'Repair of electric furnaces','Ремонт электропечей','2'),
  (35,'Repair of blenders and mixers','Ремонт блендеров и миксеров','2'),
  (36,'Repair of deep fryers','Ремонт фритюрниц','2'),
  (37,'Repair of scales','Ремонт весов','2'),
  (38,'Repair of toasters','Ремонт тостеров','2'),
  (39,'Repair of razors','Ремонт бритв','2'),
  (40,'Repair of air humidifiers','Ремонт увлажнителей воздуха','2'),
  (41,'Repair of meat grinders','Ремонт мясорубок','2'),
  (42,'Repair of hairdryers and clothes','Ремонт фенов и плоек','2'),
  (43,'Repair of juicers','Ремонт соковыжималок','2'),
  (44,'Repair of fans','Ремонт вентиляторов','2'),
  (45,'Repair of heaters','Ремонт обогревателей','2'),
  (46,'Repair of other equipment','Ремонт другой техники','2'),
  (47,'Repair of TVs','Ремонт телевизоров','3'),
  (48,'Car radio repair','Ремонт автомагнитол','3'),
  (49,'Repair of mobile phones','Ремонт мобильных телефонов','3'),
  (50,'Repair of cameras','Ремонт фотоаппаратов','3'),
  (51,'Headphone repair','Ремонт наушников','3'),
  (52,'Repair of tablets','Ремонт планшетов','3'),
  (53,'Repair of amplifiers','Ремонт усилителей','3'),
  (54,'UPS Repair','Ремонт UPS','3'),
  (55,'Repair of DVRs','Ремонт видеорегистраторов','3'),
  (56,'Repair MP3','Ремонт MP3','3'),
  (64,'Repair of GPS navigators','Ремонт GPS навигаторов','3'),
  (65,'Repair of electronic books','Ремонт электронных книг','3'),
  (66,'Repair of game consoles','Ремонт игровых приставок','3'),
  (67,'Repair of audiosystems','Ремонт аудиосистем','3'),
  (68,'Repair of video cameras','Ремонт видеокамер','3'),
  (69,'DVD Repair','Ремонт DVD','3'),
  (70,'Satellite antennas','Спутниковых антенн','3'),
  (71,'Repair of other equipment','Ремонт другой техники','3'),
  (72,'Repair of printers','Ремонт принтеров','4'),
  (73,'Scanner repair','Ремонт сканеров','4'),
  (74,'Repair of MFPs','Ремонт МФУ','4'),
  (75,'Repair of projectors','Ремонт проекторов','4'),
  (76,'Repair of shredders','Ремонт шредеров','4'),
  (77,'Fax Repair','Ремонт факсов','4'),
  (78,'Repair of other equipment','Ремонт другой техники','4'),
  (79,'Laptop Repair','Ремонт ноутбуков','5'),
  (80,'PC Repair','Ремонт ПК','5'),
  (81,'Repair of monitors','Ремонт мониторов','5'),
  (82,'Repair of other equipment','Ремонт другой техники','5'),
  (83,'Repair of electrodes','Ремонт электропил','6'),
  (84,'Repair of chainsaws','Ремонт бензопил','6'),
  (85,'-','Ремонт болгарок','6'),
  (86,'Repair of milling machines','Ремонт фрезеров','6'),
  (87,'Repair of paint spray guns','Ремонт краскопультов','6'),
  (88,'Repair of electric jigsaws','Ремонт электролобзиков','6'),
  (89,'Repair of punchers','Ремонт перфораторов','6'),
  (90,'Repair of electric drills','Ремонт электродрелей','6'),
  (91,'Screwdriver repair','Ремонт шуруповертов','6'),
  (92,'Repair of other equipment','Ремонт другой техники','6'),
  (93,'Repair of other equipment','Ремонт другой техники','7');

DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description_short` varchar(45) NOT NULL,
  `description_detail` varchar(255) DEFAULT NULL,
  `repair_service` int(11) NOT NULL,
  `city` varchar(45) NOT NULL,
  `street` varchar(45) NOT NULL,
  `order_date` datetime DEFAULT NULL,
  `expected_date` datetime DEFAULT NULL,
  `appliance` varchar(45) NOT NULL,
  `price` decimal(10,2) DEFAULT '0.00',
  `user_id` int(11) NOT NULL,
  `memo` varchar(255) DEFAULT NULL,
  `status` enum('NEW','CLOSE','COMPLETE','SUSPEND','INWORK','REJECT') DEFAULT 'NEW',
  `employee_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

INSERT INTO `orders` VALUES (1,'Сломался холодилник1','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,4,NULL,'NEW',2 );
INSERT INTO `orders` VALUES (2,'Сломался холодилник2','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,4,NULL,'NEW',NULL );
INSERT INTO `orders` VALUES (3,'Сломался холодилни3','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,4,NULL,'NEW',NULL );

INSERT INTO `orders` VALUES (4,'Сломался холодилник4','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',NULL );
INSERT INTO `orders` VALUES (5,'Сломался холодилник5','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',2 );
INSERT INTO `orders` VALUES (6,'Сломался холодилни6','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',3 );

INSERT INTO `orders` VALUES (7,'Сломался холодилник7','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',NULL );
INSERT INTO `orders` VALUES (8,'Сломался холодилник8','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',2 );
INSERT INTO `orders` VALUES (9,'Сломался холодилни9','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',NULL,NULL,'Холодильник',0.00,5,NULL,'NEW',3 );

DROP TABLE IF EXISTS `orders_history`;
CREATE TABLE `orders_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `action` enum('CHANGE_EMPLOYEE','CHANGE_STATUS','CHANGE_DATE','CHANGE_PRICE','ADD_COMMENT') NOT NULL,
  `description` varchar(255) NOT NULL,
  `action_date` datetime NOT NULL,
  `old_value` varchar(45) NOT NULL,
  `new_value` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `guest_book`;
CREATE TABLE `guest_book` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `order_id` varchar(45) DEFAULT NULL,
  `date` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

