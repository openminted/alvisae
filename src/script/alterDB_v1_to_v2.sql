--
-- Script used to update existing Database Physical Model

-- (from v1.0 to v2.0)
DROP TABLE databasemodelversion;

CREATE TABLE databasemodelversion (
    v2 character varying(128) NOT NULL
);



INSERT INTO taskdefinition .... 




-- (from v2.0 to v2.1)
-- 2012/08/17 [r346]
ALTER TABLE "authorization" ADD COLUMN scope character varying(128);
UPDATE "authorization" SET scope='AlvisAE';
ALTER TABLE "authorization" ALTER COLUMN scope SET NOT NULL;

ALTER TABLE "user" 
  ADD COLUMN is_active boolean,  
  ADD COLUMN props text;

UPDATE "user" SET is_active=true,  props='{}';

ALTER TABLE "user" 
	ALTER COLUMN is_active SET NOT NULL ,
	ALTER COLUMN props  SET NOT NULL;


DROP TABLE databasemodelversion;
CREATE TABLE databasemodelversion (
    v2_1 character varying(128) NOT NULL
);

-- 2012/09/12 [r370]
DELETE FROM "authorization" WHERE id = 31;
INSERT INTO "authorization" (description, id, campaignrelated, scope) VALUES('Upload document',	31,	true, "AlvisIR");
INSERT INTO "authorization" (description, id, campaignrelated, scope) VALUES('Acces to PDF document',	32,	true, "AlvisIR");
INSERT INTO "authorization" (description, id, campaignrelated, scope) VALUES('Start AlvisAE',	33,	true, "AlvisIR");


-- 2012/09/17 [r377]
ALTER TABLE  annotationset ADD COLUMN unmatched TEXT; 

-- (from v2.1 to v2.2) 
-- 2012/11/06 [r410]
ALTER TABLE document ADD COLUMN html_annset text ;

ALTER TABLE document ADD COLUMN external_id character varying(128);

CREATE UNIQUE INDEX "documentIDX1" ON document USING btree (external_id);

DROP TABLE databasemodelversion;
CREATE TABLE databasemodelversion (
    v2_2 character varying(128) NOT NULL
);


-- 