ALTER TABLE PUBLIC.TRANSACTION ADD COLUMN TypeTmp VARCHAR(255);

UPDATE TRANSACTION SET TypeTmp='BUY' WHERE Type='0';
UPDATE TRANSACTION SET TypeTmp='SELL' WHERE Type='1';
UPDATE TRANSACTION SET TypeTmp='DIVIDEND' WHERE Type='2';
UPDATE TRANSACTION SET TypeTmp='ERROR' WHERE  Type='-1';

ALTER TABLE PUBLIC.TRANSACTION DROP COLUMN Type;
ALTER TABLE PUBLIC.TRANSACTION ALTER COLUMN TypeTmp RENAME TO Type;
