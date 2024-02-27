-- MySQL dump 10.13  Distrib 8.0.34, for macos13 (arm64)
--
-- Host: stusql.dcs.shef.ac.uk    Database: team035
-- ------------------------------------------------------
-- Server version	8.0.32

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
-- Table structure for table `Address`
--

DROP TABLE IF EXISTS `Address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Address` (
  `houseNumber` varchar(20) NOT NULL,
  `postcode` varchar(10) NOT NULL,
  `roadName` varchar(45) DEFAULT NULL,
  `cityName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`houseNumber`,`postcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Address`
--

LOCK TABLES `Address` WRITE;
/*!40000 ALTER TABLE `Address` DISABLE KEYS */;
INSERT INTO `Address` VALUES ('1','S10 2TN','Western Bank','Sheffield');
/*!40000 ALTER TABLE `Address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `BankingDetails`
--

DROP TABLE IF EXISTS `BankingDetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `BankingDetails` (
  `cardNumber` varchar(255) NOT NULL,
  `holderName` varchar(20) DEFAULT NULL,
  `expiryDate` date DEFAULT NULL,
  `cvv` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`cardNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `BankingDetails`
--

LOCK TABLES `BankingDetails` WRITE;
/*!40000 ALTER TABLE `BankingDetails` DISABLE KEYS */;
/*!40000 ALTER TABLE `BankingDetails` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Controllers`
--

DROP TABLE IF EXISTS `Controllers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Controllers` (
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`productCode`),
  CONSTRAINT `fk_controllers_productCode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Controllers`
--

LOCK TABLES `Controllers` WRITE;
/*!40000 ALTER TABLE `Controllers` DISABLE KEYS */;
/*!40000 ALTER TABLE `Controllers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Locomotives`
--

DROP TABLE IF EXISTS `Locomotives`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Locomotives` (
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`productCode`),
  CONSTRAINT `fk_locomotive_productCode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Locomotives`
--

LOCK TABLES `Locomotives` WRITE;
/*!40000 ALTER TABLE `Locomotives` DISABLE KEYS */;
INSERT INTO `Locomotives` VALUES ('L2137');
/*!40000 ALTER TABLE `Locomotives` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OrderLines`
--

DROP TABLE IF EXISTS `OrderLines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `OrderLines` (
  `orderNumber` int NOT NULL,
  `quantity` int DEFAULT NULL,
  `orderPrice` float DEFAULT NULL,
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`orderNumber`,`productCode`),
  KEY `OrderLines_ibfk_2` (`productCode`),
  CONSTRAINT `OrderLines_ibfk_1` FOREIGN KEY (`orderNumber`) REFERENCES `Orders` (`orderNumber`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `OrderLines_ibfk_2` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OrderLines`
--

LOCK TABLES `OrderLines` WRITE;
/*!40000 ALTER TABLE `OrderLines` DISABLE KEYS */;
INSERT INTO `OrderLines` VALUES (37,4,574.4,'C3405'),(37,1,270,'L3212'),(37,1,150,'M112'),(37,1,180,'M3782'),(37,3,103.5,'R640'),(37,4,159.96,'S27920');
/*!40000 ALTER TABLE `OrderLines` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Orders`
--

DROP TABLE IF EXISTS `Orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Orders` (
  `orderNumber` int NOT NULL AUTO_INCREMENT,
  `dateOrdered` date DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `totalPrice` decimal(8,2) DEFAULT NULL,
  `userUUID` varchar(36) NOT NULL,
  PRIMARY KEY (`orderNumber`),
  KEY `Orders_ibfk_1_idx` (`userUUID`),
  CONSTRAINT `Orders_ibfk_1` FOREIGN KEY (`userUUID`) REFERENCES `Users` (`uuid`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Orders`
--

LOCK TABLES `Orders` WRITE;
/*!40000 ALTER TABLE `Orders` DISABLE KEYS */;
INSERT INTO `Orders` VALUES (37,NULL,'PENDING',1437.86,'38fffaf3-1710-4e8a-857f-0af2a9535231');
/*!40000 ALTER TABLE `Orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProductSetItems`
--

DROP TABLE IF EXISTS `ProductSetItems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProductSetItems` (
  `setId` int NOT NULL,
  `productCode` varchar(30) NOT NULL,
  `quantity` int DEFAULT NULL,
  PRIMARY KEY (`setId`,`productCode`),
  KEY `fk_productsetitem_productCode` (`productCode`),
  CONSTRAINT `fk_productsetitem_productCode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`),
  CONSTRAINT `fk_productsetitem_setid` FOREIGN KEY (`setId`) REFERENCES `ProductSets` (`setId`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProductSetItems`
--

LOCK TABLES `ProductSetItems` WRITE;
/*!40000 ALTER TABLE `ProductSetItems` DISABLE KEYS */;
INSERT INTO `ProductSetItems` VALUES (1,'R2309',7),(1,'R600',9),(1,'R702',10),(2,'R600',4),(12,'R600',8),(12,'R870',2),(20,'C5680',1),(20,'L4468',1),(20,'P09',1),(20,'S318',2),(20,'S4674',1),(21,'C7432',1),(21,'L3212',1),(21,'P09',1),(21,'S2639',2),(21,'S42879',1),(22,'C8214',1),(22,'L12932',1),(22,'P18',1),(22,'S6292',4),(23,'C5680',1),(23,'L2719',1),(23,'P818',1),(23,'S318',2),(23,'S4674',1);
/*!40000 ALTER TABLE `ProductSetItems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ProductSets`
--

DROP TABLE IF EXISTS `ProductSets`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ProductSets` (
  `setId` int NOT NULL AUTO_INCREMENT,
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`setId`,`productCode`),
  KEY `fk_productset_productcode_idx` (`productCode`),
  CONSTRAINT `fk_productset_productcode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ProductSets`
--

LOCK TABLES `ProductSets` WRITE;
/*!40000 ALTER TABLE `ProductSets` DISABLE KEYS */;
INSERT INTO `ProductSets` VALUES (22,'M112'),(20,'M3276'),(21,'M3782'),(23,'M9370'),(2,'P09'),(1,'P18'),(12,'P818');
/*!40000 ALTER TABLE `ProductSets` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Products`
--

DROP TABLE IF EXISTS `Products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Products` (
  `productCode` varchar(30) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` float NOT NULL,
  `gauge` varchar(30) NOT NULL,
  `brand` varchar(30) DEFAULT NULL,
  `isSet` int NOT NULL DEFAULT '0',
  `stock` int NOT NULL DEFAULT '1',
  `isDiscontinued` int DEFAULT '0',
  PRIMARY KEY (`productCode`,`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Products`
--

LOCK TABLES `Products` WRITE;
/*!40000 ALTER TABLE `Products` DISABLE KEYS */;
INSERT INTO `Products` VALUES ('C3405','Standard Controller DCC,DIGITAL',143.6,'OO','Hornby',0,19,0),('C5680','Standard Controller,ANALOGUE',326.75,'OO','HoRNBY',0,50,0),('C5873','DCC Standard Controller,DIGITAL',58.87,'OO','Hornby',0,97,0),('C7432','Standard Controller Analogue,ANALOGUE',67.85,'OO','Graham Farish',0,50,0),('C8214','DCC Elite Controller,DIGITAL',259.87,'OO','Bachmann',0,50,0),('C8375','DCC Elite Controller ,ANALOGUE',130.56,'OO','Peco',0,35,0),('C8706','DCC Elite Controller,DIGITAL',89.75,'OO','Dapol',0,45,0),('L12932','7P,Castle,3,DCCFITTED',76.45,'TT','Hornby',0,24,0),('L132','BR Mark1,,3,DCCREADY',30,'TT','Hornby',0,1,0),('L2137','A3,\'The A-Train\',11,DCCFITTED',69.99,'OO','Hornby',0,17,0),('L2719','A3,Flying Scotsman,5,ANALOGUE',94.99,'OO','Hornby',0,26,0),('L2731','07,Austerity,7,DCCSOUND',86.99,'TT','Graham Farish',0,1,0),('L3212','373,Eurostar EMU power car,11,DCCFITTED',270,'N','Bachmann',0,8,0),('L4468','A4,Mallard,6,ANALOGUE',125.99,'OO','LNER',0,16,0),('L45923','8P,Coronation,1,ANALOGUE',129,'TT','Dapol',0,10,0),('L6539','5MT,Black Five,11,DCCREADY',113,'TT',' Bachmann',0,50,0),('L72902','7P,Brittania,6,DCCFITTED',83.99,'TT','Peco',0,13,0),('L909','BR Mk1,Inserted Train 2,8,DCCFITTED',900,'OO','Hornby',0,6,1),('L9372','8P,Merchant Navy,6,ANALOGUE',87.99,'TT','Bachmann',0,120,0),('L999','JKUB,Inserted Train,9,DCCFITTED',89,'OO','Hornby',0,1,1),('M112','Custom Hornby Set',150,'N','Hornby',1,1,0),('M3276','Mallard Record  Breaker Train Set',250.99,'TT','Hornby',1,1,0),('M3782','Eurostar Train Set',180,'OO','Bachmann',1,1,0),('M9370','Flying Scotsman Train Set',180,'N','Graham Farish',1,1,0),('P09','2nd Radius Curve MiniPack,EXTENSION',12.99,'TT','Bachmann',1,7,0),('P18','Mega Track Booster Pack,EXTENSION',79.99,'TT','Bachmann',1,21,0),('P4560',' 3rd Radius Starter Oval ,STARTER',15.8,'OO','Hornby',1,1,0),('P818','2nd Radius Starter Pack,STARTER',99,'OO','SSS',1,1,0),('R2309','Double Straight,STRAIGHT',3.56,'TT','Bachmann',0,15,0),('R430','2nd Radius Single Curve,CURVE',2.99,'OO','Bachmann',0,40,0),('R600','2nd Radius Double Curve,CURVE',3.99,'TT','Bachmann',0,43,0),('R640','3rd Radius Double Curve,CURVE',34.5,'OO','Hornby',0,35,0),('R702','Single Straight,STRAIGHT',3.99,'TT','Bachmann',0,11,0),('R870','Single Straight,STRAIGHT',3.49,'TT','Graham Farish',0,40,0),('S1720','GWR,Composite Coach,SECOND,6',39.99,'OO','Dapol',0,1,0),('S1730','Blue/Grey,Pullman,STANDARD,5',29.45,'OO','Graham Farish',0,1,0),('S1819','LNER,Shisha Lounge Cabin,FIRST,8',13.99,'OO','Hornby',0,21,0),('S2639','SR2,Passenger Saloons,SECOND,4',57.99,'OO','Bachmann',0,15,0),('S27920','Maroon,Open,SECOND,11',39.99,'N','Bachmann',0,31,0),('S318','Gresley,Composite Coach,NULL,4',49.5,'OO','Hornby',0,31,0),('S42879','LNER,Unpowered Trailer Car,STANDARD,6',72,'TT','Bachmann',0,9,0),('S4674','Gresley,Brake Coach,NULL,7',32.99,'TT','Hornby',0,9,0),('S5293','LMS,Corridor,FIRST,9',42,'TT','Hornby',0,15,0),('S6292','LNER,21t Clam Ballast Wagon,THIRD,4',45,'TT','Bachmann',0,23,0),('S6932','InterCity 225,Cattle Wagon,NULL,8',87.95,'N','Hornby',0,31,0),('S7192','InterCity 225,Sleeper Car,NULL,7',35.99,'OO','Peco',0,21,0),('S8211','SR2,Buffet CAR,NULL,7',29.59,'OO','Bachmann',0,5,0),('S9162','Blue/Grey, 6-Plank Coal Wagon,NULL,6',32.7,'N','Hornby',0,4,0),('S999','GWR,Inserted Wagon,THIRD,8',18,'OO','Hornby',0,1,1);
/*!40000 ALTER TABLE `Products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `RollingStocks`
--

DROP TABLE IF EXISTS `RollingStocks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `RollingStocks` (
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`productCode`),
  CONSTRAINT `fk_rollingstock_productcode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `RollingStocks`
--

LOCK TABLES `RollingStocks` WRITE;
/*!40000 ALTER TABLE `RollingStocks` DISABLE KEYS */;
INSERT INTO `RollingStocks` VALUES ('R600');
/*!40000 ALTER TABLE `RollingStocks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Tracks`
--

DROP TABLE IF EXISTS `Tracks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Tracks` (
  `productCode` varchar(30) NOT NULL,
  PRIMARY KEY (`productCode`),
  CONSTRAINT `fk_tracks_productcode` FOREIGN KEY (`productCode`) REFERENCES `Products` (`productCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tracks`
--

LOCK TABLES `Tracks` WRITE;
/*!40000 ALTER TABLE `Tracks` DISABLE KEYS */;
INSERT INTO `Tracks` VALUES ('S1819');
/*!40000 ALTER TABLE `Tracks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `uuid` varchar(36) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `salt` varchar(255) NOT NULL,
  `forename` varchar(30) DEFAULT NULL,
  `surname` varchar(45) DEFAULT NULL,
  `roles` varchar(22) DEFAULT NULL,
  `cardNumber` varchar(255) DEFAULT NULL,
  `houseNumber` varchar(20) DEFAULT NULL,
  `postcode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`uuid`),
  KEY `Users_ibfk_1_idx` (`cardNumber`),
  KEY `Users_ibfk_2_idx` (`houseNumber`,`postcode`),
  CONSTRAINT `Users_ibfk_1` FOREIGN KEY (`cardNumber`) REFERENCES `BankingDetails` (`cardNumber`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `Users_ibfk_2` FOREIGN KEY (`houseNumber`, `postcode`) REFERENCES `Address` (`houseNumber`, `postcode`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES ('38fffaf3-1710-4e8a-857f-0af2a9535231','manager@trains.com','bcf2d0208d7da87831554b9b2532895a4bee00f6c6df61b403fdf1c5f7bdfcf4','nt2t5PMjbYE2TLu6M9DgNQ==','Mr','Manager','STAFF;MANAGER',NULL,'1','S10 2TN');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-11-30 21:51:54
