-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: dominos
-- ------------------------------------------------------
-- Server version	8.0.19

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
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CID` varchar(5) NOT NULL,
  `CName` varchar(50) NOT NULL,
  `TPNo` varchar(10) NOT NULL,
  `Address` varchar(99) DEFAULT NULL,
  PRIMARY KEY (`CID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('C001','RAJAT CHANDRA','8320328031','GODREJ GARDEN CITY,AHMEDABAD'),('C002','MARSHALL MATHERS','1800510107','DETROIT,MICHIGAN'),('C003','AMAN','123456789','OK'),('C004','MANJULA CHANDRA','0123456781','HOME'),('C005','SURESH CHANDRA','9428008214','SIVASAGAR,ASSAM'),('C006','KENDRICK LAMAR','1800112345','COMPTON,USA');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `item`
--

DROP TABLE IF EXISTS `item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `item` (
  `ICode` varchar(4) NOT NULL,
  `Description` varchar(50) NOT NULL,
  `UnitPrice` decimal(8,2) NOT NULL,
  `ItemType` varchar(10) NOT NULL,
  `ItemImage` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`ICode`,`Description`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item`
--

LOCK TABLES `item` WRITE;
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` VALUES ('I001','MOJITO',250.00,'Beverage',''),('I002','CLASSIC COKE(100ML)',100.00,'Beverage',''),('I003','CLASSIC GARLIC BREAD',145.00,'GB',''),('I004','BLUE ISLAND',300.00,'Beverage',''),('I005','BOTTLED WATER(1L)',75.00,'Beverage',''),('I007','OLD FASHIONED',350.25,'Beverage',''),('I008','COSMOPOLITAN',450.55,'Beverage',''),('I009','DOUBLE MOSCOW MULE',600.00,'Beverage',''),('I010','BRITISH TEA(240ML)',120.35,'Beverage',''),('I011','COFFEE LATTE(240ML)',150.00,'Beverage',''),('I012','LEMON JUICE(300ML)',100.00,'Beverage',''),('I013','SODA GLASS(250ML)',50.00,'Beverage',''),('I014','ORANGE PUNCH JUICE',80.00,'Beverage',''),('I016','CHEESY MARGARITA',350.00,'Pizza',NULL),('I017','ONION BLOSSOMS',375.25,'Pizza',NULL),('I018','GARDEN DELIGHT',400.00,'Pizza',NULL),('I019','Mexican Garlic Bread',80.00,'GB',NULL),('I020','Extra Chees Garlic Bread',165.00,'GB',NULL);
/*!40000 ALTER TABLE `item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login`
--

DROP TABLE IF EXISTS `login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login` (
  `LUser` varchar(30) NOT NULL,
  `LName` varchar(30) NOT NULL,
  `LPass` varchar(15) NOT NULL,
  `LType` varchar(7) NOT NULL,
  PRIMARY KEY (`LUser`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `login`
--

LOCK TABLES `login` WRITE;
/*!40000 ALTER TABLE `login` DISABLE KEYS */;
INSERT INTO `login` VALUES ('admin','MAIN_ADMIN','admin','Admin'),('rajatc17','RAJAT CHANDRA','Rajatc17!','Admin'),('slim','Slim Shady','shady','Admin'),('test','TEST NAME','test','Cashier');
/*!40000 ALTER TABLE `login` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orderdetails`
--

DROP TABLE IF EXISTS `orderdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orderdetails` (
  `OrderID` varchar(4) NOT NULL,
  `ICode` varchar(4) NOT NULL,
  `Description` varchar(45) NOT NULL,
  `Type` varchar(15) NOT NULL,
  `UnitPrice` decimal(8,2) NOT NULL,
  `QTY` int NOT NULL,
  KEY `OrderID_idx` (`OrderID`),
  KEY `ICode_idx` (`ICode`),
  KEY `Description_idx` (`Description`),
  CONSTRAINT `ICode` FOREIGN KEY (`ICode`) REFERENCES `item` (`ICode`),
  CONSTRAINT `OrderID` FOREIGN KEY (`OrderID`) REFERENCES `orders` (`OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orderdetails`
--

LOCK TABLES `orderdetails` WRITE;
/*!40000 ALTER TABLE `orderdetails` DISABLE KEYS */;
INSERT INTO `orderdetails` VALUES ('O1','I001','MOJITO','Beverage',250.00,1),('O1','I002','CLASSIC COKE(100ML)','Beverage',100.00,2),('O1','I008','COSMOPOLITAN','Beverage',450.55,3),('O2','I003','CLASSIC GARLIC BREAD','GB',145.00,3),('O3','I016','CHEESY MARGARITA','Pizza',350.00,1),('O3','I017','ONION BLOSSOMS','Pizza',375.25,3),('O3','I018','GARDEN DELIGHT','Pizza',400.00,2),('O4','I019','Mexican Garlic Bread','GB',80.00,4),('O4','I020','Extra Chees Garlic Bread','GB',165.00,3);
/*!40000 ALTER TABLE `orderdetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `OrderID` varchar(4) NOT NULL,
  `OrderDate` date NOT NULL,
  `Amount` decimal(8,3) NOT NULL,
  `CID` varchar(5) NOT NULL,
  `Discount` varchar(15) NOT NULL,
  `OrderStatus` varchar(7) NOT NULL,
  PRIMARY KEY (`OrderID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES ('O1','2020-06-27',1711.568,'C001','5%','PAID'),('O2','2020-06-28',435.000,'C002','NO DISCOUNT','PENDING'),('O3','2020-07-01',2048.175,'C006','10%','PAID'),('O4','2020-06-28',652.000,'C006','20%','PENDING');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'dominos'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-06-26 22:53:37
