ALTER TABLE `repair_agency`.`orders` 
ADD COLUMN `change_date` DATETIME NULL AFTER `manager_id`;

INSERT INTO `orders` VALUES (15,'Сломался холодилник9','Сломался холодильник. Не морозит ',9,'Киев','Крещатик 44',now(),NULL,'Холодильник',0.00,5,NULL,'NEW',NULL,NULL , now());