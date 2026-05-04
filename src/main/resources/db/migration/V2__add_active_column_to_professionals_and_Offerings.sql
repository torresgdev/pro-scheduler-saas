
ALTER TABLE professionals ADD COLUMN active BOOLEAN DEFAULT TRUE;


ALTER TABLE services ADD COLUMN active BOOLEAN DEFAULT TRUE;

UPDATE professionals SET active = TRUE WHERE active IS NULL;
UPDATE services SET active = TRUE WHERE active IS NULL;