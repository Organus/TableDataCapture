# HeidiSQL Dump 
#
# --------------------------------------------------------
# Host:                         *
# Database:                     spread_sheet
# Server version:               5.0.51a-3ubuntu5.4
# Server OS:                    debian-linux-gnu
# Target compatibility:         ANSI SQL
# HeidiSQL version:             4.0
# Date/time:                    6/10/2010 3:39:03 PM
# --------------------------------------------------------

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI,NO_BACKSLASH_ESCAPES';*/
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Dumping data for table 'attachmentTypes'
#

LOCK TABLES "attachmentTypes" WRITE;
/*!40000 ALTER TABLE "attachmentTypes" DISABLE KEYS;*/
INSERT IGNORE INTO "attachmentTypes" ("type_id", "ext") VALUES
	('1','JPG');
INSERT IGNORE INTO "attachmentTypes" ("type_id", "ext") VALUES
	('2','GIF');
INSERT IGNORE INTO "attachmentTypes" ("type_id", "ext") VALUES
	('3','XLS');
INSERT IGNORE INTO "attachmentTypes" ("type_id", "ext") VALUES
	('4','BMP');
INSERT IGNORE INTO "attachmentTypes" ("type_id", "ext") VALUES
	('5','PDF');
/*!40000 ALTER TABLE "attachmentTypes" ENABLE KEYS;*/
UNLOCK TABLES;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
