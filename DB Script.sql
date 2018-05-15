/* Formatted on 9/05/2018 8:58:02 PM (QP5 v5.215.12089.38647) */
/*
This table is used to store the company details.
*/

CREATE TABLE ACCELO.TA001_COMPANY
(
   ID           INTEGER NOT NULL,
   TITLE        VARCHAR2 (200 BYTE) NOT NULL,
   WEBSITE      VARCHAR2 (200 BYTE) NOT NULL,
   IS_ENABLED   VARCHAR2 (1) DEFAULT 'Y' NOT NULL
);

/*
Adding a primary key to the table.
*/
ALTER TABLE ACCELO.TA001_COMPANY ADD (
CONSTRAINT ID_pk PRIMARY KEY(ID));

/*
This check constaint is used to enable or disable a company without actually deleting it.
*/
ALTER TABLE ACCELO.TA001_COMPANY ADD CONSTRAINT chk_bool_value CHECK (IS_ENABLED IN ('N', 'Y'));

/*
Creating a sequence for the companies.
*/
CREATE SEQUENCE company_id_seq
   INCREMENT BY 1;

/*
Creating a trigger that automatically gets invoked when a value is inserted into the table.
*/
CREATE OR REPLACE TRIGGER COMPANIES_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_COMPANY
   FOR EACH ROW
BEGIN
   SELECT company_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

/*
Creating a table with the features to store the contact details. The table also provides options to disable a contact.
*/
CREATE TABLE ACCELO.TA001_CONTACT
(
   ID           INTEGER NOT NULL,
   FIRST_NAME   VARCHAR2 (200 BYTE) NOT NULL,
   SURNAME      VARCHAR2 (200 BYTE) NOT NULL,
   IS_ENABLED   VARCHAR2 (1) DEFAULT 'Y' NOT NULL
);

/*
Adding a primary key constraint to a contact in a table.
*/
ALTER TABLE ACCELO.TA001_CONTACT ADD (
CONSTRAINT contact_pk PRIMARY KEY(ID));


/*
Creating a sequence to serve as the ID to the contact table.
*/
CREATE SEQUENCE contact_id_seq
   INCREMENT BY 1;

/*
Assigning an ID to each entry to the table as the contacts are inserted.
*/
CREATE OR REPLACE TRIGGER CONTACTS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_CONTACT
   FOR EACH ROW
BEGIN
   SELECT contact_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

/*
Creating a mapping between contacts,email ID and phone numbers.
This is done because same contacts can have multiple links with different companies.
*/
CREATE TABLE ACCELO.TA002_CONTACT_DETAILS
(
   ID           INTEGER NOT NULL,
   EMAIL        VARCHAR2 (200 BYTE) NOT NULL,
   PHONE        VARCHAR2 (30 BYTE) NOT NULL,
   CONTACT_ID   INTEGER
);

/*
The foreign key mapping is done in order to link both the tables.
*/
ALTER TABLE ACCELO.TA002_CONTACT_DETAILS ADD CONSTRAINT fk_contact_id
FOREIGN KEY(CONTACT_ID) REFERENCES ACCELO.TA001_CONTACT(ID);

/*
Using a sequence and a trigger to maintain the table ID during insertion.
*/
CREATE SEQUENCE contact_details_seq
   INCREMENT BY 1;

CREATE OR REPLACE TRIGGER CONTACT_DETAILS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA002_CONTACT_DETAILS
   FOR EACH ROW
BEGIN
   SELECT contact_details_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

/*
This table is used to create a mapping between the company and the contact.
If the assignee to a project has to be changed, this table can be used as a reference to see the contacts which are aligned to the company.
*/
CREATE TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
(
   COMPANY_ID   INTEGER,
   CONTACT_ID   INTEGER
);

/*
Adding an additional field of ID in order to reference this table rows in project table mapping.
*/
ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
  ADD ID INTEGER;

ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
  ADD (CONSTRAINT pk_contact_map PRIMARY KEY  (ID));
;

/*
Establishing the relationships with company and contact tables.
*/
ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP ADD CONSTRAINT fk_map_company
FOREIGN KEY(COMPANY_ID) REFERENCES ACCELO.TA001_COMPANY(ID);

ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP ADD CONSTRAINT fk_map_contact
FOREIGN KEY(CONTACT_ID) REFERENCES ACCELO.TA001_CONTACT(ID);

CREATE SEQUENCE contact_comp_map_seq
   INCREMENT BY 1;

CREATE OR REPLACE TRIGGER CONTACT_COMP_MAP_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA002_CONTACT_COMPANY_MAPP
   FOR EACH ROW
BEGIN
   SELECT contact_comp_map_seq.NEXTVAL INTO :new.ID FROM DUAL;ACCELO.TA002_CONTACT_COMPANY_MAPP
END;
/

/*
Creating the project table. The company and contact will be references to the pre-existing values.
*/

CREATE TABLE ACCELO.TA001_PROJECT
(
   ID            INTEGER NOT NULL,
   TITLE         VARCHAR2 (200 BYTE) NOT NULL,
   COMPANY       INTEGER,
   CONTACT       INTEGER,
   DESCRIPTION   VARCHAR2 (500),
   IS_ENABLED    VARCHAR2 (1) DEFAULT 'Y' NOT NULL
);

ALTER TABLE ACCELO.TA001_PROJECT ADD (
CONSTRAINT ID_project_pk PRIMARY KEY(ID));

ALTER TABLE ACCELO.TA001_PROJECT ADD CONSTRAINT chk_bool_val CHECK (IS_ENABLED IN ('N', 'Y'));

CREATE SEQUENCE project_id_seq
   INCREMENT BY 1;

CREATE OR REPLACE TRIGGER PROJECTS_ALIGN_ID
   BEFORE INSERT
   ON ACCELO.TA001_PROJECT
   FOR EACH ROW
BEGIN
   SELECT project_id_seq.NEXTVAL INTO :new.ID FROM DUAL;
END;
/

ALTER TABLE ACCELO.TA001_PROJECT ADD CONSTRAINT fk_project_company
FOREIGN KEY(COMPANY) REFERENCES ACCELO.TA001_COMPANY(ID);

ALTER TABLE ACCELO.TA001_PROJECT ADD CONSTRAINT fk_project_contact
FOREIGN KEY(CONTACT) REFERENCES ACCELO.TA002_CONTACT_COMPANY_MAPP(ID);

/*
Inserting few samples in order to see that the mapping is right.
*/

INSERT INTO ACCELO.TA001_COMPANY (TITLE, WEBSITE)
     VALUES ('Google', 'google.com.au');

INSERT INTO ACCELO.TA001_CONTACT (FIRST_NAME, SURNAME)
     VALUES ('Praveen', 'Vinny');

INSERT INTO ACCELO.TA002_CONTACT_DETAILS (EMAIL, PHONE, CONTACT_ID)
     VALUES ('praveen.vinny@gmail.com',
             '+61 415 715 989',
             (SELECT ID
                FROM TA001_CONTACT
               WHERE FIRST_NAME = 'Praveen' AND SURNAME = 'Vinny'));

INSERT INTO ACCELO.TA002_CONTACT_COMPANY_MAPP (COMPANY_ID, CONTACT_ID)
     VALUES ( (SELECT ID
                 FROM TA001_COMPANY
                WHERE TITLE = 'Google'),
             (SELECT ID
                FROM TA001_CONTACT
               WHERE FIRST_NAME = 'Praveen' AND SURNAME = 'Vinny'));


INSERT INTO ACCELO.TA001_PROJECT (TITLE,
                                  COMPANY,
                                  CONTACT,
                                  DESCRIPTION)
     VALUES (
               'Alpha Go',
               (SELECT ID
                  FROM TA001_COMPANY
                 WHERE TITLE = 'Google'),
               (SELECT ID
                  FROM TA001_CONTACT
                 WHERE     FIRST_NAME = 'Praveen'
                       AND SURNAME = 'Vinny'
                       AND ID IN
                              (SELECT DISTINCT CONTACT_ID
                                 FROM TA002_CONTACT_COMPANY_MAPP
                                WHERE COMPANY_ID = (SELECT ID
                                                      FROM TA001_COMPANY
                                                     WHERE TITLE = 'Google'))),
               'To train artificial neural networks to play the game of Go.');

/* Formatted on 10/05/2018 3:10:11 PM (QP5 v5.215.12089.38647) */
ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
  ADD IS_ENABLED VARCHAR2(1 BYTE) DEFAULT 'Y' NOT NULL;
  
  ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP
  ADD MAPPING_ID INTEGER;
  
  ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP add constraint FK_MAPPING_ID
  FOREIGN KEY (MAPPING_ID) 
  REFERENCES ACCELO.TA002_CONTACT_DETAILS (ID)
  

/*
This check constaint is used to enable or disable a contact's link with a company without actually deleting it.
*/
ALTER TABLE ACCELO.TA002_CONTACT_COMPANY_MAPP ADD CONSTRAINT chk_bool_val_map CHECK (IS_ENABLED IN ('N', 'Y'));

/* Formatted on 10/05/2018 3:14:26 PM (QP5 v5.215.12089.38647) */
SELECT A.FIRST_NAME,
       A.SURNAME,
       B.EMAIL,
       B.PHONE
  FROM TA001_CONTACT A, TA002_CONTACT_DETAILS B
 WHERE     A.ID = B.CONTACT_ID
       AND A.ID = (SELECT DISTINCT contact_id
                     FROM TA002_CONTACT_COMPANY_MAPP
                    WHERE company_id = 1 AND IS_ENABLED = 'Y');

/* Formatted on 10/05/2018 4:56:54 PM (QP5 v5.215.12089.38647) */
SELECT X.ID,
       X.TITLE,
       X.WEBSITE,
       A.FIRST_NAME,
       A.SURNAME,
       B.EMAIL,
       B.PHONE
  FROM TA001_COMPANY X,
       TA001_CONTACT A,
       TA002_CONTACT_DETAILS B,
       TA002_CONTACT_COMPANY_MAPP C
 WHERE     A.ID = B.CONTACT_ID
       AND C.CONTACT_ID = A.ID
       AND C.COMPANY_ID = X.ID
       AND X.IS_ENABLED = 'Y'
       AND C.IS_ENABLED = 'Y'
       AND C.COMPANY_ID = 1
       AND C.MAPPING_ID = B.ID;

Select count(1) from ACCELO.TA001_COMPANY where upper(trim(title)) = 'GOOGLE' and TA001_COMPANY.IS_ENABLED = 'N';

Select distinct title from ACCELO.TA001_COMPANY where upper(trim(title)) = 'GOOGLE';

Select count(ID), ID from ACCELO.TA002_CONTACT_DETAILS where upper(trim(EMAIL)) = 'SAM.CHRISTY@GOOGLE.COM' and upper(trim(PHONE)) = '+91 8089822275' and CONTACT_ID = 2 group by ID;

/* Formatted on 12/05/2018 9:31:02 PM (QP5 v5.215.12089.38647) */
SELECT DISTINCT X.*, NVL(Z.TITLE, 'UNASSIGNED') Company
  FROM (SELECT B.ID,
               B.FIRST_NAME,
               B.SURNAME,
               A.EMAIL,
               A.PHONE
          FROM TA002_CONTACT_DETAILS A, TA001_CONTACT B
         WHERE A.CONTACT_ID = B.ID AND B.IS_ENABLED = 'Y') X,
       TA002_CONTACT_COMPANY_MAPP Y,
       TA001_COMPANY Z
 WHERE Z.ID(+) = X.ID
 AND Y.CONTACT_ID(+) = X.ID;
 
SELECT DISTINCT X.*, NVL(Z.TITLE, 'UNASSIGNED') Company FROM (SELECT B.ID, B.FIRST_NAME, B.SURNAME, A.EMAIL, A.PHONE
FROM TA002_CONTACT_DETAILS A, TA001_CONTACT B WHERE A.CONTACT_ID = B.ID AND B.IS_ENABLED = 'Y') X,
TA002_CONTACT_COMPANY_MAPP Y, TA001_COMPANY Z WHERE Z.ID(+) = X.ID AND Y.CONTACT_ID(+) = X.ID;
 

Select ID, count(1) from ACCELO.TA001_CONTACT where upper(trim(FIRST_NAME)) = 'SAM'
             and upper(trim(SURNAME)) = 'CHRISTY' and TA001_CONTACT.IS_ENABLED = 'Y' group by ID;
             
             Select count(ID) from ACCELO.TA002_CONTACT_DETAILS
                           where upper(trim(EMAIL)) = 'ABCD'
               and upper(trim(PHONE)) = '1234'
                and CONTACT_ID = 21;

             
             Select count(DUMMY) from dual where DUMMY <> 'X';
             
             Select * from dual;
             
SELECT DISTINCT X.*, NVL(Z.TITLE, 'UNASSIGNED') Company
  FROM (SELECT B.ID,
               B.FIRST_NAME,
               B.SURNAME,
               A.EMAIL,
               A.PHONE,
               A.ID MAPPING_ID
          FROM TA002_CONTACT_DETAILS A, TA001_CONTACT B
         WHERE A.CONTACT_ID = B.ID AND B.IS_ENABLED = 'Y') X,
       TA002_CONTACT_COMPANY_MAPP Y,
       TA001_COMPANY Z
 WHERE Z.ID(+) = Y.COMPANY_ID
 AND Y.MAPPING_ID (+)= X.MAPPING_ID
  AND Y.CONTACT_ID(+) = X.ID;
 
 Select count(ID) from TA002_CONTACT_COMPANY_MAPP A where A.COMPANY_ID = 1 and A.CONTACT_ID = 1 and A.MAPPING_ID = 1;
 
 Select A.ID CONTACT_ID, A.FIRST_NAME, A.SURNAME, B.EMAIL, B.PHONE, B.ID MAPPING_ID from TA001_CONTACT A, TA002_CONTACT_DETAILS B where A.ID = B.CONTACT_ID and A.IS_ENABLED = 'Y' and B.ID = 22;
 
 Select distinct ID, TITLE from TA001_COMPANY WHERE IS_ENABLED = 'Y';
 
/*
Adding an additional field in order to reference contact mapping right.
*/
ALTER TABLE ACCELO.TA001_PROJECT
  ADD MAPPING_ID INTEGER;


ALTER TABLE ACCELO.TA002_CONTACT_DETAILS ADD (CONSTRAINT tbl_ID_pk PRIMARY KEY(ID));

ALTER TABLE ACCELO.TA001_PROJECT ADD CONSTRAINT fk_contact_map_proj FOREIGN KEY(MAPPING_ID) REFERENCES ACCELO.TA002_CONTACT_DETAILS(ID);

Select distinct contact_id, count(1) from TA002_CONTACT_COMPANY_MAPP A where A.COMPANY_ID = 1 and A.MAPPING_ID = 1 group by contact_id;

Select count(ID), ID from TA001_COMPANY A where upper(trim(A.TITLE)) =  'GOOGLE' and IS_ENABLED = 'Y' group by ID

Select ID, CONTACT_ID from TA002_CONTACT_DETAILS where PHONE = '12345678';

Select A.ID, A.TITLE, A.DESCRIPTION, B.FIRST_NAME ||' '|| B.SURNAME CONTACT, C.TITLE COMPANY, D.EMAIL, D.PHONE FROM TA001_PROJECT A, TA001_CONTACT B, TA001_COMPANY C, TA002_CONTACT_DETAILS D where A.CONTACT = B.ID and A.COMPANY = C.ID and D.ID = A.MAPPING_ID and A.ID = 21;

Select ID from TA002_CONTACT_DETAILS A where A.EMAIL = 'praveen.vinny@gmail.com' and A.PHONE = '+61415715989';



Select COMPANY_ID, CONTACT_ID from TA002_CONTACT_COMPANY_MAPP A where A.CONTACT_ID = (Select CONTACT_ID from TA002_CONTACT_DETAILS where ID = 1);

Select ID from TA001_COMPANY A where upper(trim(A.TITLE)) =  'GOOGLE' and IS_ENABLED = 'Y';

