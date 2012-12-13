/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI,NO_BACKSLASH_ESCAPES';*/
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Dumping data for table 'data_format'
#

LOCK TABLES "data_format" WRITE;
/*!40000 ALTER TABLE "data_format" DISABLE KEYS;*/
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('1','MM/dd/yy','03/18/10','Date');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('2','EEE MMM dd HH:mm:ss yyyy','Fri Feb 19 15:06:37 2010','Date');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('3','HH:mm:ss','15:06:37','Date');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('4','MM/dd/yyyy','03/18/2010','Date');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('5','M/d/yy','4/2/10','Date');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('6','7','7 digits of precision','Real');
INSERT IGNORE INTO "data_format" ("data_format_id", "format", "description", "type") VALUES
	('7','2','2 digits of precision','Real');
/*!40000 ALTER TABLE "data_format" ENABLE KEYS;*/
UNLOCK TABLES;


#
# Dumping data for table 'data_type'
#

LOCK TABLES "data_type" WRITE;
/*!40000 ALTER TABLE "data_type" DISABLE KEYS;*/
INSERT IGNORE INTO "data_type" ("data_type_id", "description") VALUES
	('1','LONG');
INSERT IGNORE INTO "data_type" ("data_type_id", "description") VALUES
	('2','REAL');
INSERT IGNORE INTO "data_type" ("data_type_id", "description") VALUES
	('3','DATE');
INSERT IGNORE INTO "data_type" ("data_type_id", "description") VALUES
	('4','STRING');
INSERT IGNORE INTO "data_type" ("data_type_id", "description") VALUES
	('5','BOOLEAN');
/*!40000 ALTER TABLE "data_type" ENABLE KEYS;*/
UNLOCK TABLES;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
