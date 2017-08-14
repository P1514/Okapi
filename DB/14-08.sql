CREATE DATABASE  IF NOT EXISTS `Okapi` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `Okapi`;
-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: Okapi
-- ------------------------------------------------------
-- Server version	5.7.17

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
-- Table structure for table `GRANULARITY`
--

DROP TABLE IF EXISTS `GRANULARITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `GRANULARITY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `GRANULARITY`
--

LOCK TABLES `GRANULARITY` WRITE;
/*!40000 ALTER TABLE `GRANULARITY` DISABLE KEYS */;
INSERT INTO `GRANULARITY` VALUES (1,'month'),(2,'week'),(3,'day'),(4,'hour'),(5,'minute'),(6,'second');
/*!40000 ALTER TABLE `GRANULARITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI`
--

DROP TABLE IF EXISTS `KPI`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) DEFAULT NULL,
  `NAME` varchar(45) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `SAMPLING_RATE` int(11) DEFAULT '1',
  `SAMPLING_INTERVAL` varchar(45) DEFAULT 'hour',
  `CONTEXT_PRODUCT` bit(1) DEFAULT b'0',
  `CONTEXT_MACHINE` bit(1) DEFAULT b'0',
  `CONTEXT_MOULD` bit(1) DEFAULT b'0',
  `CONTEXT_SHIFT` bit(1) DEFAULT b'0',
  `CALCULATION_TYPE` varchar(45) NOT NULL,
  `AGGREGATION` int(11) DEFAULT NULL,
  `NUMBER_SUPPORT` varchar(50) DEFAULT NULL,
  `NUMBER_SUPPORT_FORMAT` varchar(50) DEFAULT 'DECIMAL',
  `ACTIVE` tinyint(1) DEFAULT '1',
  `COMPANY_CONTEXT` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `PARENT_KPI` (`PARENT_ID`),
  KEY `KPI_KPI_AGG_TYPE_FK` (`AGGREGATION`),
  CONSTRAINT `KPI_KPI_AGG_TYPE_FK` FOREIGN KEY (`AGGREGATION`) REFERENCES `KPI_AGG_TYPE` (`ID`),
  CONSTRAINT `PARENT_KPI` FOREIGN KEY (`PARENT_ID`) REFERENCES `KPI` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI`
--

LOCK TABLES `KPI` WRITE;
/*!40000 ALTER TABLE `KPI` DISABLE KEYS */;
INSERT INTO `KPI` VALUES (1,NULL,'Count','Amount of product created. This can be defined per machine employee shift or the whole plant.',1,'hour','','','','','simple',6,'NUMERIC','DECIMAL',1,NULL),(2,1,'Good parts','Amount of products produced with acceptable quality.',1,'hour','','','','','simple',6,'NUMERIC','DECIMAL',1,NULL),(3,1,'Scrapped parts','Amount of products produced that are rejected.',1,'hour','','','','','simple',6,'NUMERIC','DECIMAL',1,NULL),(4,NULL,'Scrap rate','Relation of product inappropriate to sell.',1,'hour','','','','','composed',1,'NUMERIC','PERCENTAGE',1,NULL),(5,NULL,'Availability','Percentage of scheduled time that the operation is available to operate.',1,'hour','\0','','\0','','composed',1,'NUMERIC','PERCENTAGE',0,NULL),(6,NULL,'Performance','Speed at which the operation runs as a percentage of its designed speed.',1,'hour','\0','','\0','','composed',1,'NUMERIC','PERCENTAGE',0,NULL),(7,NULL,'Quality','Good units produced as a percentage of the total units started.',1,'hour','','','','','composed',1,'NUMERIC','PERCENTAGE',1,NULL),(8,NULL,'Overall Equipment Efficiency (OEE)','Evaluates how effectively an operation is used. Quantifies how well a manufacturing unit performs relative to its designed capacity, during the periods when it is scheduled to run.',1,'hour','','','','','composed',1,'NUMERIC','PERCENTAGE',0,NULL);
/*!40000 ALTER TABLE `KPI` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI_AGG_TYPE`
--

DROP TABLE IF EXISTS `KPI_AGG_TYPE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI_AGG_TYPE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `AGGREGATION` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI_AGG_TYPE`
--

LOCK TABLES `KPI_AGG_TYPE` WRITE;
/*!40000 ALTER TABLE `KPI_AGG_TYPE` DISABLE KEYS */;
INSERT INTO `KPI_AGG_TYPE` VALUES (1,'NONE'),(2,'MIN'),(3,'MAX'),(4,'AVG'),(5,'SUM'),(6,'COUNT');
/*!40000 ALTER TABLE `KPI_AGG_TYPE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI_FORMULA`
--

DROP TABLE IF EXISTS `KPI_FORMULA`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI_FORMULA` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KPI_ID` int(11) NOT NULL,
  `TERM1_KPI_ID` int(11) DEFAULT NULL,
  `TERM1_SENSOR_ID` int(11) DEFAULT NULL,
  `OPERATOR_1` varchar(45) DEFAULT NULL,
  `TERM2_KPI_ID` int(11) DEFAULT NULL,
  `TERM2_SENSOR_ID` int(11) DEFAULT NULL,
  `OPERATOR_2` varchar(45) DEFAULT NULL,
  `TERM3_KPI_ID` int(11) DEFAULT NULL,
  `TERM3_SENSOR_ID` int(11) DEFAULT NULL,
  `CRITERIA` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FORMULA_KPI_ID` (`KPI_ID`),
  KEY `TERM1_KPI` (`TERM1_KPI_ID`),
  KEY `TERM2_KPI` (`TERM2_KPI_ID`),
  KEY `TERM3_KPI` (`TERM3_KPI_ID`),
  KEY `TERM1_SENSOR` (`TERM1_SENSOR_ID`),
  KEY `TERM2_SENSOR` (`TERM2_SENSOR_ID`),
  KEY `TERM3_SENSOR` (`TERM3_SENSOR_ID`),
  CONSTRAINT `FORMULA_KPI_ID` FOREIGN KEY (`KPI_ID`) REFERENCES `KPI` (`ID`),
  CONSTRAINT `TERM1_KPI` FOREIGN KEY (`TERM1_KPI_ID`) REFERENCES `KPI` (`ID`),
  CONSTRAINT `TERM1_SENSOR` FOREIGN KEY (`TERM1_SENSOR_ID`) REFERENCES `SENSOREVENT` (`ID`),
  CONSTRAINT `TERM2_KPI` FOREIGN KEY (`TERM2_KPI_ID`) REFERENCES `KPI` (`ID`),
  CONSTRAINT `TERM2_SENSOR` FOREIGN KEY (`TERM2_SENSOR_ID`) REFERENCES `SENSOREVENT` (`ID`),
  CONSTRAINT `TERM3_KPI` FOREIGN KEY (`TERM3_KPI_ID`) REFERENCES `KPI` (`ID`),
  CONSTRAINT `TERM3_SENSOR` FOREIGN KEY (`TERM3_SENSOR_ID`) REFERENCES `SENSOREVENT` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI_FORMULA`
--

LOCK TABLES `KPI_FORMULA` WRITE;
/*!40000 ALTER TABLE `KPI_FORMULA` DISABLE KEYS */;
INSERT INTO `KPI_FORMULA` VALUES (1,4,3,NULL,'/',1,NULL,NULL,NULL,NULL,NULL),(2,5,NULL,4,'/',NULL,5,NULL,NULL,NULL,NULL),(3,6,1,NULL,'*',NULL,6,'/',NULL,7,NULL),(4,7,2,NULL,'/',1,NULL,NULL,NULL,NULL,NULL),(5,8,5,NULL,'*',6,NULL,'*',7,NULL,NULL),(6,1,NULL,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,2,NULL,2,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,3,NULL,3,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
/*!40000 ALTER TABLE `KPI_FORMULA` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI_TARGET`
--

DROP TABLE IF EXISTS `KPI_TARGET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI_TARGET` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KPI_ID` int(11) DEFAULT NULL,
  `PRODUCT_ID` int(11) DEFAULT NULL,
  `MOULD_ID` int(11) DEFAULT NULL,
  `MACHINE_ID` int(11) DEFAULT NULL,
  `SHIFT_ID` int(11) DEFAULT NULL,
  `LOWER_BOUND` double DEFAULT NULL,
  `UPPER_BOUND` double DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `TARGET_KPI` (`KPI_ID`),
  KEY `TARGET_MACHINE` (`MACHINE_ID`),
  KEY `TARGET_MOULD` (`MOULD_ID`),
  KEY `TARGET_PRODUCT` (`PRODUCT_ID`),
  KEY `TARGET_SHIFT` (`SHIFT_ID`),
  CONSTRAINT `TARGET_KPI` FOREIGN KEY (`KPI_ID`) REFERENCES `KPI` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `TARGET_MACHINE` FOREIGN KEY (`MACHINE_ID`) REFERENCES `MACHINE` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `TARGET_MOULD` FOREIGN KEY (`MOULD_ID`) REFERENCES `MOULD` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `TARGET_PRODUCT` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `TARGET_SHIFT` FOREIGN KEY (`SHIFT_ID`) REFERENCES `SHIFT` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI_TARGET`
--

LOCK TABLES `KPI_TARGET` WRITE;
/*!40000 ALTER TABLE `KPI_TARGET` DISABLE KEYS */;
INSERT INTO `KPI_TARGET` VALUES (3,3,NULL,NULL,NULL,NULL,0,1);
/*!40000 ALTER TABLE `KPI_TARGET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI_VALUES`
--

DROP TABLE IF EXISTS `KPI_VALUES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI_VALUES` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KPI_TIMESTMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `KPI_ID` int(11) NOT NULL,
  `MACHINE_ID` int(11) DEFAULT NULL,
  `KPI_VALUE` decimal(13,4) DEFAULT NULL,
  `PRODUCT_ID` int(11) DEFAULT NULL,
  `MOULD_ID` int(11) DEFAULT NULL,
  `SHIFT_ID` int(11) DEFAULT NULL,
  `GRANULARITY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI_VALUES`
--

LOCK TABLES `KPI_VALUES` WRITE;
/*!40000 ALTER TABLE `KPI_VALUES` DISABLE KEYS */;
/*!40000 ALTER TABLE `KPI_VALUES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KPI_VALUESOLD`
--

DROP TABLE IF EXISTS `KPI_VALUESOLD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KPI_VALUESOLD` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `KPI_TIMESTMP` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `KPI_ID` int(11) NOT NULL,
  `MACHINE_ID` int(11) DEFAULT NULL,
  `KPI_VALUE` decimal(13,4) DEFAULT NULL,
  `PRODUCT_ID` int(11) DEFAULT NULL,
  `MOULD_ID` int(11) DEFAULT NULL,
  `SHIFT_ID` int(11) DEFAULT NULL,
  `GRANULARITY_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KPI_VALUESOLD`
--

LOCK TABLES `KPI_VALUESOLD` WRITE;
/*!40000 ALTER TABLE `KPI_VALUESOLD` DISABLE KEYS */;
/*!40000 ALTER TABLE `KPI_VALUESOLD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `LOGS`
--

DROP TABLE IF EXISTS `LOGS`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LOGS` (
  `LOGS_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `TIMESTAMP` timestamp(5) NOT NULL DEFAULT CURRENT_TIMESTAMP(5),
  `MESSAGE` text NOT NULL,
  PRIMARY KEY (`LOGS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `LOGS`
--

LOCK TABLES `LOGS` WRITE;
/*!40000 ALTER TABLE `LOGS` DISABLE KEYS */;
/*!40000 ALTER TABLE `LOGS` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MACHINE`
--

DROP TABLE IF EXISTS `MACHINE`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MACHINE` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MACHINE`
--

LOCK TABLES `MACHINE` WRITE;
/*!40000 ALTER TABLE `MACHINE` DISABLE KEYS */;
/*!40000 ALTER TABLE `MACHINE` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MOULD`
--

DROP TABLE IF EXISTS `MOULD`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MOULD` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PRODUCT_ID` int(11) NOT NULL,
  `NAME` varchar(60) DEFAULT NULL,
  `CODE` varchar(45) DEFAULT NULL,
  `CYCLE` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `KPI_VALUES_MOULD_FK` (`PRODUCT_ID`),
  CONSTRAINT `KPI_VALUES_MOULD_FK` FOREIGN KEY (`PRODUCT_ID`) REFERENCES `PRODUCT` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MOULD`
--

LOCK TABLES `MOULD` WRITE;
/*!40000 ALTER TABLE `MOULD` DISABLE KEYS */;
/*!40000 ALTER TABLE `MOULD` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `PRODUCT`
--

DROP TABLE IF EXISTS `PRODUCT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `PRODUCT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(60) DEFAULT NULL,
  `CODE` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `PRODUCT`
--

LOCK TABLES `PRODUCT` WRITE;
/*!40000 ALTER TABLE `PRODUCT` DISABLE KEYS */;
/*!40000 ALTER TABLE `PRODUCT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SENSOREVENT`
--

DROP TABLE IF EXISTS `SENSOREVENT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SENSOREVENT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `SAMPLING_RATE` int(11) NOT NULL,
  `SAMPLING_INTERVAL` varchar(45) NOT NULL,
  `SENSORID` varchar(100) DEFAULT NULL,
  `EVENTNAME` varchar(100) DEFAULT NULL,
  `EVENTTYPE` varchar(100) DEFAULT NULL,
  `EVENTPARTITION` varchar(100) DEFAULT NULL,
  `PARTITIONID` varchar(100) DEFAULT NULL,
  `EVENTTYPEVALUE` varchar(100) DEFAULT NULL,
  `SENSORNAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SENSOREVENT`
--

LOCK TABLES `SENSOREVENT` WRITE;
/*!40000 ALTER TABLE `SENSOREVENT` DISABLE KEYS */;
INSERT INTO `SENSOREVENT` VALUES (1,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,1,'hour',NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,6,'hour','sensor1id','Good','boolean','true',NULL,NULL,'Quality'),(25,6,'hour','sensor2id','Finished','string','false','none',NULL,'Count');
/*!40000 ALTER TABLE `SENSOREVENT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SHIFT`
--

DROP TABLE IF EXISTS `SHIFT`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SHIFT` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SHIFT`
--

LOCK TABLES `SHIFT` WRITE;
/*!40000 ALTER TABLE `SHIFT` DISABLE KEYS */;
/*!40000 ALTER TABLE `SHIFT` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'Okapi'
--

--
-- Dumping routines for database 'Okapi'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-08-14  1:06:49
