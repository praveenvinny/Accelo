/*
    Schema Name: ACCELO
    Password: accelo
*/

/* Formatted on 29/05/2018 4:30:51 PM */
/* 
    * The following are the queries for table creation.
    * Please remember to add the constraints such as primary key, foreign key
      and check constraints.
    * Please make sure that the sequences are created before calling the
      triggers to align sequence values to the table column values.
*/
CREATE TABLE ACCELO.TA001_COMPANY
(
   ID           INTEGER NOT NULL,
   TITLE        VARCHAR2 (200 BYTE) NOT NULL,
   WEBSITE      VARCHAR2 (200 BYTE) NOT NULL,
   IS_ENABLED   VARCHAR2 (1 BYTE) DEFAULT 'Y' NOT NULL
);

ALTER TABLE ACCELO.TA001_COMPANY ADD (
  CONSTRAINT CHK_BOOL_VALUE
  CHECK (IS_ENABLED IN ('N', 'Y')),
  CONSTRAINT ID_PK
  PRIMARY KEY
  (ID));

CREATE TABLE ACCELO.TA001_CONTACT
(
   ID           INTEGER NOT NULL,
   FIRST_NAME   VARCHAR2 (200 BYTE) NOT NULL,
   SURNAME      VARCHAR2 (200 BYTE) NOT NULL,
   IS_ENABLED   VARCHAR2 (1 BYTE) DEFAULT 'Y' NOT NULL
);

ALTER TABLE ACCELO.TA001_CONTACT ADD (
  CONSTRAINT CONTACT_PK
  PRIMARY KEY
  (ID));

CREATE TABLE ACCELO.TA002_CONTACT_DETAILS
(
   ID           INTEGER NOT NULL,
   EMAIL        VARCHAR2 (200 BYTE) NOT NULL,
   PHONE        VARCHAR2 (30 BYTE) NOT NULL,
   CONTACT_ID   INTEGER
);

ALTER TABLE ACCELO.TA002_CONTACT_DETAILS ADD (
  CONSTRAINT TBL_ID_PK
  PRIMARY KEY (ID));

ALTER TABLE ACCELO.TA002_CONTACT_DETAILS ADD (
  CONSTRAINT FK_CONTACT_ID
  FOREIGN KEY (CONTACT_ID)
  REFERENCES ACCELO.TA001_CONTACT (ID));

CREATE TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
(
   ID           INTEGER,
   MAPPING_ID   INTEGER,
   COMPANY_ID   INTEGER,
   CONTACT_ID   INTEGER,
   IS_ENABLED   VARCHAR2 (1 BYTE) DEFAULT 'Y' NOT NULL
);

ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP ADD (
  CONSTRAINT CHK_BOOL_VAL_MAP
  CHECK (IS_ENABLED IN ('N', 'Y')),
  CONSTRAINT PK_CONTACT_MAP
  PRIMARY KEY
  (ID));

ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP ADD (
  CONSTRAINT FK_MAP_COMPANY
  FOREIGN KEY (COMPANY_ID)
  REFERENCES ACCELO.TA001_COMPANY (ID),
  CONSTRAINT FK_MAP_CONTACT
  FOREIGN KEY (CONTACT_ID)
  REFERENCES ACCELO.TA001_CONTACT (ID));

CREATE TABLE ACCELO.TA001_PROJECT
(
   ID            INTEGER NOT NULL,
   TITLE         VARCHAR2 (200 BYTE) NOT NULL,
   COMPANY       INTEGER,
   CONTACT       INTEGER,
   DESCRIPTION   VARCHAR2 (500 BYTE),
   IS_ENABLED    VARCHAR2 (1 BYTE) DEFAULT 'Y' NOT NULL,
   MAPPING_ID    INTEGER
);

ALTER TABLE ACCELO.TA001_PROJECT ADD (
  CONSTRAINT CHK_BOOL_VAL
  CHECK (IS_ENABLED IN ('N', 'Y')),
  CONSTRAINT ID_PROJECT_PK
  PRIMARY KEY
  (ID);

ALTER TABLE ACCELO.TA001_PROJECT ADD (
  CONSTRAINT FK_CONTACT_MAP_PROJ
  FOREIGN KEY (MAPPING_ID)
  REFERENCES ACCELO.TA002_CONTACT_DETAILS (ID),
  CONSTRAINT FK_PROJECT_COMPANY
  FOREIGN KEY (COMPANY)
  REFERENCES ACCELO.TA001_COMPANY (ID),
  CONSTRAINT FK_PROJECT_CONTACT
  FOREIGN KEY (CONTACT)
  REFERENCES ACCELO.TA002_CONTACT_COMPANY_MAPP (ID));

ALTER TABLE TA001_PROJECT DROP CONSTRAINT FK_CONTACT_MAP_PROJ;
ALTER TABLE TA001_PROJECT DROP CONSTRAINT FK_PROJECT_CONTACT;

CREATE SEQUENCE ACCELO.COMPANY_ID_SEQ;

CREATE SEQUENCE ACCELO.CONTACT_COMP_MAP_SEQ;

CREATE SEQUENCE ACCELO.CONTACT_DETAILS_SEQ;

CREATE SEQUENCE ACCELO.CONTACT_ID_SEQ;

CREATE SEQUENCE ACCELO.PROJECT_ID_SEQ;

CREATE OR REPLACE TRIGGER ACCELO.COMPANIES_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_COMPANY
   FOR EACH ROW
BEGIN
   SELECT company_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER ACCELO.CONTACTS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_CONTACT
   FOR EACH ROW
BEGIN
   SELECT contact_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER ACCELO.CONTACT_DETAILS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA002_CONTACT_DETAILS
   FOR EACH ROW
BEGIN
   SELECT contact_details_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER ACCELO.CONTACT_COMP_MAP_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA002_CONTACT_COMPANY_MAPP
   FOR EACH ROW
BEGIN
   SELECT contact_comp_map_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

CREATE OR REPLACE TRIGGER ACCELO.PROJECTS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_PROJECT
   FOR EACH ROW
BEGIN
   SELECT project_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/