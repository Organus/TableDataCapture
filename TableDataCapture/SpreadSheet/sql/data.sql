# HeidiSQL Dump 
#
# --------------------------------------------------------
# Host:                         *
# Database:                     spread_sheet
# Server version:               5.0.51a-3ubuntu5.4
# Server OS:                    debian-linux-gnu
# Target compatibility:         ANSI SQL
# HeidiSQL version:             4.0
# Date/time:                    6/10/2010 3:35:29 PM
# --------------------------------------------------------

/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ANSI,NO_BACKSLASH_ESCAPES';*/
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;*/


#
# Table structure for table 'data_format'
#

CREATE TABLE /*!32312 IF NOT EXISTS*/ "data_format" (
  "data_format_id" bigint(20) NOT NULL,
  "format" varchar(255) NOT NULL,
  "description" varchar(255) default NULL,
  "type" varchar(50) default NULL,
  PRIMARY KEY  ("data_format_id")
);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE;*/
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;*/
