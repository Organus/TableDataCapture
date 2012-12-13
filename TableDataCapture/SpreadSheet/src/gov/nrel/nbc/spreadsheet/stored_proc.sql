DROP PROCEDURE IF EXISTS writecountpivot;
DELIMITER |
CREATE PROCEDURE writecountpivot( tbl CHAR(64), col CHAR(64) )
BEGIN
  DECLARE datadelim CHAR(1) DEFAULT '"';
  DECLARE singlequote CHAR(1) DEFAULT CHAR(39);
  DECLARE comma CHAR(1) DEFAULT ',';
  SET @sqlmode = (SELECT @@sql_mode);
  SET @@sql_mode='';
  SET @sql = CONCAT( 'SELECT DISTINCT CONCAT(', singlequote,
                     ',IF(', col, ' = ', datadelim, singlequote, comma,
                     col, comma, singlequote, datadelim, comma, '1,0) AS `', 
                     singlequote, comma, col, comma, singlequote, '`', singlequote, 
                     ') AS countpivotarg FROM ', tbl,
                     ' WHERE ', col, ' IS NOT NULL' );
  -- UNCOMMENT TO SEE THE MIDLEVEL CODE:
  -- SELECT @sql; 
  PREPARE stmt FROM @sql;
  EXECUTE stmt;
  DROP PREPARE stmt;
  SET @@sql_mode=@sqlmode;
END;
|
DELIMITER ;
CALL writecountpivot('v','name');

SELECT DISTINCT 
  CONCAT(',SUM(IF(salesperson = "',salesperson,'",1,0)) AS `',salesperson,'`') 
  AS countpivotarg 
FROM test.sales 
WHERE salesperson IS NOT NULL | 

SELECT DISTINCT 
  CONCAT(',IF(name = "S % Total",1,0) AS `S % Total`') 
  AS countpivotarg 
FROM v 
WHERE name IS NOT NULL ; 

SELECT 
  hdr_index
  ,IF(name = "S % Total",1,0) AS `S % Total` 
  ,COUNT(*) AS Total
FROM v
GROUP BY name WITH ROLLUP; 

