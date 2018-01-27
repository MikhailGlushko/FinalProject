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

INSERT INTO `users` VALUES
  (1,'ADMIN','Administrator','admin','e9ef7606634776f071dda30ae9c2c00c','mikhail.glushko@gmail.com','066-3864046','ACTIVE','INTERNAL',NULL);

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

(12,'services','services','ADMIN','MCRUD','ALL'),

(13, 'guestbook','guestbook', 'ADMIN',   'MCRUD','ALL'),
(14, 'guestbook','guestbook', 'MANAGER', 'MR',   'ALL'),
(15, 'guestbook','guestbook', 'MASTER',  'MR',   'ALL'),
(16, 'guestbook','guestbook', 'CUSTOMER','MRC',  'ALL'),
(17, 'guestbook','guestbook', 'GUEST',   'MRC',  'ALL')
;

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
  `memo` mediumtext,
  `status` ENUM('NEW', 'VERIFICATION', 'ESTIMATE', 'CONFIRMATION', 'PROGRESS', 'COMPLETE', 'SUSPEND', 'CLOSE', 'REJECT', 'PAYMENT', 'INWORK') DEFAULT 'NEW',
  `employee_id` int(11) DEFAULT NULL,
  `manager_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `orders_history`;
CREATE TABLE `orders_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `action` enum('CHANGE_EMPLOYEE','CHANGE_STATUS','CHANGE_DATE','CHANGE_PRICE','ADD_COMMENT','GUESTBOOK_COMMENT') NOT NULL,
  `description` varchar(255) NOT NULL,
  `action_date` datetime NOT NULL,
  `old_value` varchar(45) DEFAULT NULL,
  `new_value` varchar(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `guest_book`;
CREATE TABLE `guest_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `user_name` varchar(45) NOT NULL,
  `description` varchar(45) NOT NULL,
  `action_date` datetime NOT NULL,
  `memo` varchar(1024) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `news`;
CREATE TABLE `news` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(45) NOT NULL,
  `action_date` datetime NOT NULL,
  `memo` varchar(1024) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `news` VALUES (1,'News 1',now(),'Многие люди, учитывая нынешнюю обстановку в Украине, стараются экономить.<br>И вместо того, чтобы выбросить сломавшийся бытовой прибор, отправляются в мастерскую,<br>чтобы профессионал его отремонтировал. В большинстве случаев ремонт обходится дешевле,<br>чем покупка новой бытовой техники.<br>Наше агентство предлагает «вдохнуть вторую жизнь» в любой прибор, облегчающий быт людей.');

DROP TABLE IF EXISTS `order_que`;
CREATE TABLE `order_que` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL,
  `role` enum('ADMIN','MANAGER','MASTER','CUSTOMER') NOT NULL,
  `employee_id` int(11) DEFAULT NULL,
  `create_date` datetime NOT NULL,
  `close_date` datetime DEFAULT NULL,
  `message` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;