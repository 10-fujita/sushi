CREATE DATABASE  IF NOT EXISTS `suki` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `suki`;
-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: suki
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `product_id` bigint unsigned NOT NULL,
  `quantity` int unsigned NOT NULL,
  `unit_price` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_cart_user` (`user_id`),
  KEY `fk_cart_product` (`product_id`),
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
INSERT INTO `cart_items` VALUES (14,2,32,1,450,'2026-01-07 12:24:54','2026-01-07 12:24:54'),(15,3,12,5,200,'2026-01-07 16:18:24','2026-01-07 16:18:24'),(18,1,34,2,400,'2026-01-07 16:58:11','2026-01-07 16:58:11'),(20,1,5,4,180,'2026-01-07 17:04:25','2026-01-07 17:04:25'),(21,1,22,4,260,'2026-01-08 11:43:29','2026-01-08 11:43:29'),(22,1,26,2,180,'2026-01-08 11:43:29','2026-01-08 11:43:29'),(23,1,28,2,200,'2026-01-08 11:43:29','2026-01-08 11:43:29'),(24,1,30,2,300,'2026-01-08 11:43:29','2026-01-08 11:43:29'),(25,1,3,2,480,'2026-01-08 11:43:29','2026-01-08 11:43:29'),(26,1,18,2,520,'2026-01-08 11:43:29','2026-01-08 11:43:29');
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `sort_order` int unsigned NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (1,'にぎり',1,'2026-01-05 15:41:47','2026-01-05 15:41:47'),(2,'軍艦',2,'2026-01-05 15:41:47','2026-01-05 15:41:47'),(3,'その他',3,'2026-01-05 15:41:47','2026-01-05 15:41:47');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `order_id` bigint unsigned NOT NULL,
  `product_id` bigint unsigned NOT NULL,
  `product_name` varchar(100) NOT NULL,
  `unit_price` int unsigned NOT NULL,
  `quantity` int unsigned NOT NULL,
  `subtotal_amount` int unsigned NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_order_items_order` (`order_id`),
  KEY `fk_order_items_product` (`product_id`),
  CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_order_items_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (64,2,31,'ヒラメの縁側',400,2,800,'2026-01-07 15:49:15','2026-01-07 15:49:15'),(65,3,12,'ひらめ',200,5,1000,'2026-01-07 16:18:36','2026-01-07 16:18:36'),(66,4,34,'肉寿司',400,2,800,'2026-01-08 11:30:49','2026-01-08 11:30:49'),(67,4,5,'いか',180,4,720,'2026-01-08 11:30:49','2026-01-08 11:30:49'),(68,5,34,'肉寿司',400,2,800,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(69,5,5,'いか',180,4,720,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(70,5,22,'つぶ貝',260,4,1040,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(71,5,26,'納豆巻',180,2,360,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(72,5,28,'しらす',200,2,400,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(73,5,30,'炙りえんがわ',300,2,600,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(74,5,3,'大とろ',480,2,960,'2026-01-08 11:47:30','2026-01-08 11:47:30'),(75,5,18,'うに',520,2,1040,'2026-01-08 11:47:30','2026-01-08 11:47:30');
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `order_number` varchar(50) NOT NULL,
  `order_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) NOT NULL,
  `total_amount` int unsigned NOT NULL,
  `shipping_recipient_name` varchar(100) NOT NULL,
  `shipping_postal_code` varchar(10) NOT NULL,
  `shipping_prefecture` varchar(50) NOT NULL,
  `shipping_city` varchar(100) NOT NULL,
  `shipping_address_line1` varchar(255) NOT NULL,
  `shipping_address_line2` varchar(255) DEFAULT NULL,
  `shipping_phone_number` varchar(20) DEFAULT NULL,
  `delivery_date` date NOT NULL,
  `delivery_time_slot` varchar(20) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_number` (`order_number`),
  KEY `fk_orders_user` (`user_id`),
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (2,1,'a10a63f1-5e32-4f86-9880-ccb4d54ee3e6','2026-01-07 15:49:15','COMPLETED',800,'a','100-0001','東京都','千代田区','1-1-1','','','2026-01-21','18:00','2026-01-07 15:49:15','2026-01-08 12:50:19'),(3,3,'f0fa2525-abdf-44f4-93a8-5aa037c6a464','2026-01-07 16:18:37','ORDERED',1000,'じいさん','7330023','広島県','広島市西区都町','2','','','2026-01-21','17:00','2026-01-07 16:18:36','2026-01-07 16:18:36'),(4,1,'3c364d25-7af5-4a93-beef-4f9adcb0dab3','2026-01-08 11:30:50','ORDERED',1520,'聖徳太子','100-0001','東京都','千代田区','1-1-1','','099999999','2026-01-09','12:00','2026-01-08 11:30:49','2026-01-08 11:30:49'),(5,1,'ff2f099e-4051-4950-957c-685405b995e1','2026-01-08 11:47:31','ORDERED',5920,'さわら','100-0001','東京都','千代田区','1-1-1','','1111111','2026-01-10','00:00','2026-01-08 11:47:30','2026-01-08 11:47:30');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `category_id` bigint unsigned NOT NULL,
  `name` varchar(160) NOT NULL,
  `description` text,
  `price` int unsigned NOT NULL,
  `calories` int unsigned DEFAULT NULL,
  `image_url` varchar(255) DEFAULT NULL,
  `allergy_info` varchar(255) DEFAULT NULL,
  `ingredient_info` text,
  `is_active` tinyint(1) NOT NULL DEFAULT '1',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_products_category` (`category_id`),
  CONSTRAINT `fk_products_category` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,1,'まぐろ','定番の赤身まぐろ。さっぱりとした味わい。',180,100,'/images/products/maguro.png','なし','まぐろ、米',1,'2026-01-05 15:46:03','2026-01-06 15:21:26'),(2,1,'中とろ','ほどよい脂のりの中とろ。',280,130,'/images/products/chutoro.png','なし','まぐろ、米',1,'2026-01-05 15:46:03','2026-01-06 15:21:26'),(3,1,'大とろ','とろける食感の大とろ。',480,180,'/images/products/otoro.png','なし','まぐろ、米',1,'2026-01-05 15:46:03','2026-01-06 15:21:26'),(4,1,'サーモン','脂ののった人気のサーモン。',220,140,'/images/products/salmon.png','さけ','サーモン、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(5,1,'いか','歯ごたえのあるいか。',180,90,'/images/products/ika.png','いか','いか、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(6,1,'たこ','旨みのある蒸したこ。',200,95,'/images/products/tako.png','なし','たこ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(7,1,'えび','甘みのあるボイルえび。',220,110,'/images/products/ebi.png','えび','えび、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(8,1,'甘えび','とろりとした甘えび。',260,120,'/images/products/amaebi.png','えび','甘えび、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(9,1,'あじ','さっぱりした青魚の定番。',180,105,'/images/products/aji.png','なし','あじ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(10,1,'さば','脂ののったしめさば。',200,140,'/images/products/saba.png','なし','さば、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(11,1,'たい','上品な味わいの白身。',180,85,'/images/products/tai.png','なし','たい、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(12,1,'ひらめ','淡白で旨みのある白身魚。',200,80,'/images/products/hirame.png','なし','ひらめ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(13,1,'かんぱち','コクのある味わい。',220,120,'/images/products/kanpachi.png','なし','かんぱち、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(14,1,'ぶり','脂がのった旬の味。',240,150,'/images/products/buri.png','なし','ぶり、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(15,1,'こはだ','江戸前寿司の定番。',180,100,'/images/products/kohada.png','なし','こはだ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(16,1,'玉子','甘めの厚焼き玉子。',160,110,'/images/products/tamago.png','卵','卵、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(17,2,'いくら','ぷちぷち食感のいくら。',380,160,'/images/products/ikura.png','いくら','いくら、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(18,2,'うに','濃厚な甘みのうに。',520,190,'/images/products/uni.png','なし','うに、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(19,1,'穴子','ふっくら煮上げた穴子。',420,200,'/images/products/anago.png','なし','穴子、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(20,1,'ほたて','甘みのある貝柱。',260,120,'/images/products/hotate.png','なし','ほたて、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(21,1,'赤貝','コリコリとした食感。',300,110,'/images/products/akagai.png','なし','赤貝、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(22,1,'つぶ貝','歯ごたえの良い貝。',260,105,'/images/products/tsubugai.png','なし','つぶ貝、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(23,2,'ねぎとろ','まぐろとねぎの相性抜群。',220,150,'/images/products/negitoro.png','なし','まぐろ、ねぎ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(24,2,'鉄火巻','まぐろを使った定番巻物。',300,180,'/images/products/tekkamaki.png','なし','まぐろ、海苔、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(25,2,'かっぱ巻','きゅうりのさっぱり巻。',160,90,'/images/products/kappamaki.png','なし','きゅうり、海苔、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(26,2,'納豆巻','納豆の旨みが楽しめる巻物。',180,140,'/images/products/nattomaki.png','大豆','納豆、海苔、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(27,2,'とびっこ','プチプチ食感の魚卵。',240,130,'/images/products/tobikko.png','なし','とびっこ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(28,3,'しらす','釜揚げしらすの優しい味。',200,110,'/images/products/shirasu.png','なし','しらす、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(29,3,'炙りサーモン','香ばしく炙ったサーモン。',260,160,'/images/products/aburi_salmon.png','さけ','サーモン、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(30,3,'炙りえんがわ','脂の旨みが際立つえんがわ。',300,170,'/images/products/aburi_engawa.png','なし','えんがわ、米',1,'2026-01-05 15:46:03','2026-01-05 16:08:57'),(31,1,'ヒラメの縁側','メッチャ美味しい',400,NULL,'/images/products/31.jpg','なし','活魚',1,'2026-01-06 14:35:46','2026-01-06 14:49:19'),(32,3,'ラーメン','アッサリトシテルヨ',450,NULL,'/images/products/32.jpg','小麦','色々あるね',1,'2026-01-06 14:40:15','2026-01-06 15:21:26'),(33,1,'しめ鯖','大人の味♡',200,NULL,'/images/products/33.png','なし','鯖米米',1,'2026-01-06 16:09:22','2026-01-07 12:24:05'),(34,1,'肉寿司','みんな大好きなお肉だよ！',400,NULL,'/images/products/34.png','牛','色々',1,'2026-01-07 16:50:16','2026-01-07 16:50:16');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `reviews`
--

DROP TABLE IF EXISTS `reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `reviews` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `product_id` bigint unsigned NOT NULL,
  `rating` tinyint unsigned NOT NULL,
  `comment` text,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_reviews_user` (`user_id`),
  KEY `fk_reviews_product` (`product_id`),
  CONSTRAINT `fk_reviews_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `fk_reviews_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `reviews`
--

LOCK TABLES `reviews` WRITE;
/*!40000 ALTER TABLE `reviews` DISABLE KEYS */;
INSERT INTO `reviews` VALUES (1,1,15,3,'あ',0,'2026-01-07 11:35:42','2026-01-07 11:35:42'),(2,3,12,3,'絶品でした！！！！！',0,'2026-01-07 16:19:13','2026-01-07 16:19:13');
/*!40000 ALTER TABLE `reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_USER','一般ユーザー','2026-01-05 14:12:21','2026-01-07 16:13:52'),(2,'ROLE_ADMIN','管理者','2026-01-05 14:12:21','2026-01-07 16:13:52');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_addresses`
--

DROP TABLE IF EXISTS `user_addresses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_addresses` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `user_id` bigint unsigned NOT NULL,
  `recipient_name` varchar(100) NOT NULL,
  `postal_code` varchar(10) NOT NULL,
  `prefecture` varchar(50) NOT NULL,
  `city` varchar(100) NOT NULL,
  `address_line1` varchar(255) NOT NULL,
  `address_Line2` varchar(255) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `is_default` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_addresses_user` (`user_id`),
  CONSTRAINT `fk_user_addresses_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_addresses`
--

LOCK TABLES `user_addresses` WRITE;
/*!40000 ALTER TABLE `user_addresses` DISABLE KEYS */;
INSERT INTO `user_addresses` VALUES (1,1,'a','7310523','a','a','a','',NULL,1,'2026-01-05 14:15:00','2026-01-05 14:15:00'),(2,3,'じいさん','7330023','広島県','広島市西区都町','2','',NULL,1,'2026-01-07 16:14:37','2026-01-07 16:14:37');
/*!40000 ALTER TABLE `user_addresses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `name` varchar(100) NOT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `dm_opt_in` tinyint(1) NOT NULL DEFAULT '0',
  `withdraw_flag` tinyint(1) NOT NULL DEFAULT '0',
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `role_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `fk_users_role` (`role_id`),
  CONSTRAINT `fk_users_role` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'fujitahironari1019@gmail.com','$2a$10$uHlQ654rMq3uSEAKe2oiM.l9S7N5MHKCBO8uhVSQSuYBCpOhXBzOC','a','',0,0,'2026-01-05 14:15:00','2026-01-05 14:15:00','2026-01-05 14:15:00',1),(2,'motoyasu0212@gmail.com','$2a$10$uHlQ654rMq3uSEAKe2oiM.l9S7N5MHKCBO8uhVSQSuYBCpOhXBzOC','motoyasu','090-0000-0000',0,0,'2026-01-06 13:16:50','2026-01-06 13:16:50','2026-01-06 13:16:50',2),(3,'tamudoku22@i.softbank.jp','$2a$10$GG0U7tDWfi2yMvynUc0BTehdKBCCBulUfa7GjT5Wpu6uIRxsd0pOe','じいさん','',0,0,'2026-01-07 16:14:38','2026-01-07 16:14:37','2026-01-07 16:14:37',1);
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

-- Dump completed on 2026-01-08 12:54:02
