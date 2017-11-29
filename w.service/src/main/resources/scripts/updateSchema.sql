--
-- Script used to update existing Database to latest Database Physical Model
-- (from un-numbered version to v1.0)

CREATE TABLE databasemodelversion (
    v1_0 character varying(128) NOT NULL
);



CREATE TABLE "authorization" (
    description character varying(128) NOT NULL,
    id bigint NOT NULL,
    campaignrelated boolean NOT NULL
);

ALTER TABLE "authorization" OWNER TO annotation_admin;



CREATE TABLE userauthorization (
    auth_id bigint NOT NULL,
    user_id bigint NOT NULL
);

ALTER TABLE userauthorization OWNER TO annotation_admin;



CREATE TABLE usercampaignauthorization (
    auth_id bigint NOT NULL,
    user_id bigint NOT NULL,
    campaign_id bigint NOT NULL
);

ALTER TABLE usercampaignauthorization OWNER TO annotation_admin;



INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Connect',	1,	false);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Create campaign',	2,	false);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Close campaign',	3,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Add document',	4,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Remove document',	5,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('View documents',	6,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Create annotations',	7,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('View other user''s annotations',	8,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Export annotations',	9,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('View Ontology',	10,	true);
INSERT INTO "authorization" (description, id, campaignrelated) VALUES('Edit Ontology',	11,	true);

ALTER TABLE ONLY "authorization"
    ADD CONSTRAINT authorization_pkey PRIMARY KEY (id);



ALTER TABLE ONLY userauthorization
    ADD CONSTRAINT "userauthorizationCPK" UNIQUE (user_id, auth_id);



ALTER TABLE ONLY usercampaignauthorization
    ADD CONSTRAINT "usercampaignauthorizationCPK" UNIQUE (user_id, campaign_id, auth_id);



CREATE UNIQUE INDEX idx16480406 ON "user" USING btree (login);



ALTER TABLE ONLY userauthorization
    ADD CONSTRAINT "userauthorizationFK1" FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE ONLY userauthorization
    ADD CONSTRAINT "userauthorizationFK2" FOREIGN KEY (auth_id) REFERENCES "authorization"(id);



ALTER TABLE ONLY usercampaignauthorization
    ADD CONSTRAINT "usercampaignauthorizationFK3" FOREIGN KEY (user_id) REFERENCES "user"(id);

ALTER TABLE ONLY usercampaignauthorization
    ADD CONSTRAINT "usercampaignauthorizationFK4" FOREIGN KEY (campaign_id) REFERENCES campaign(id);

ALTER TABLE ONLY usercampaignauthorization
    ADD CONSTRAINT "usercampaignauthorizationFK5" FOREIGN KEY (auth_id) REFERENCES "authorization"(id);


