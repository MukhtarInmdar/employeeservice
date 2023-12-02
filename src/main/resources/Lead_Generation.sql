CREATE SEQUENCE LEAD_CREATION_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 START WITH 1 INCREMENT BY 1 CACHE 20; 

drop table project_preferred_location;

CREATE TABLE project_preferred_location (
    location_id NUMBER(10) PRIMARY KEY,
    location_name varchar2(100),
    location_status char(1),
    created_date date,
    modified_date date
    
);

drop table lead_source;

CREATE TABLE lead_source (
    source_id NUMBER(10) PRIMARY KEY,
    source_name VARCHAR2(100) NOT NULL,
    source_desc VARCHAR2(100),
    source_type VARCHAR2(50) ,
    source_status char(1) ,
    created_date DATE ,
    modified_date DATE
);

drop table time_frame_to_purchase;
CREATE TABLE time_frame_to_purchase (
    time_frame_to_purchase_id NUMBER(10) PRIMARY KEY,
    time_frame VARCHAR2(255) NOT NULL,
    time_frame_status char(1),
    created_date DATE ,
    modified_date DATE
);

drop table housing_requirement;
CREATE TABLE housing_requirement (
    housing_id NUMBER(10) PRIMARY KEY,
    housing_type VARCHAR2(255) NOT NULL,
    housing_status char(1),
    created_date DATE ,
    modified_date DATE
  );

drop table lead_sub_status;  
  create table lead_sub_status(
    lead_sub_status_id NUMBER(10) PRIMARY KEY,
    lead_sub_status_type VARCHAR2(255) NOT NULL,
    lead_sub_status char(1),
    created_date DATE ,
    modified_date DATE
  );
  
  drop table marketing_type;
  CREATE TABLE marketing_type (
    marketing_type_id NUMBER(10) PRIMARY KEY,
    marketing_type_name VARCHAR2(255) ,
    channel_partner_lead varchar2(10),
    channel_partner_name varchar2(100),
    channel_partner_address varchar2(255),
    channel_partner_number number(15),
    marketing_type_status char(1),
    created_date DATE ,
    modified_date DATE
  );
  

  CREATE TABLE "LEAD_COMMENTS" 
   (	"LEAD_COMMENT_ID" NUMBER(10,0), 
	"LEAD_COMMENT" VARCHAR2(255 BYTE), 
	"LEAD_COMMENT_OWNER" VARCHAR2(100 BYTE), 
	"LEAD_COMMENT_DATE" DATE, 
	 CONSTRAINT "CONSTR_LEAD_COMMENT_OWNER" UNIQUE ("LEAD_COMMENT_ID", "LEAD_COMMENT", "LEAD_COMMENT_OWNER")
  ) ;
  


drop table lead_creation;

  CREATE TABLE "CUSTOMERAPP_CUG"."LEAD_CREATION" 
   (	"LEAD_ID" NUMBER(10,0), 
	"CUSTOMER_NAME" VARCHAR2(50 BYTE) NOT NULL ENABLE, 
	"COMPANY" VARCHAR2(255 BYTE), 
	"DESIGNATION" VARCHAR2(255 BYTE), 
	"REGISTERED_CREATED" TIMESTAMP (6), 
	"LEAD_CREATED_TO_SITE_SCHEDULED" VARCHAR2(100 BYTE), 
	"EMAIL" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL1" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL2" VARCHAR2(50 BYTE), 
	"MOBILE" NUMBER(12,0) NOT NULL ENABLE, 
	"ADDITIONAL_MOBILE1" VARCHAR2(12 BYTE), 
	"ADDITIONAL_MOBILE2" VARCHAR2(12 BYTE), 
	"PHONE" NUMBER(15,0), 
	"EXTENSION" NUMBER(10,0), 
	"PROJECT_ID" NUMBER(10,0), 
	"PREFERED_PROJECT_LOCATION" NUMBER(10,0), 
	"FIRST_SOURCE_ID" NUMBER(10,0), 
	"LAST_SOURCE_ID" NUMBER(10,0), 
	"LEAD_OWNER_ID" NUMBER(10,0), 
	"MIN_BUDGET" NUMBER(12,0), 
	"MAX_BUDGET" NUMBER(12,0), 
	"BUDGET_RANGE" VARCHAR2(50 BYTE), 
	"REQUIREMENT_TYPE" VARCHAR2(100 BYTE), 
	"MIN_FLAT_AREA" NUMBER(12,0), 
	"MAX_FLAT_AREA" NUMBER(12,0), 
	"TIME_FRAME_TO_PURCHASE" NUMBER(10,0), 
	"HOUSING_REQUIREMENT" NUMBER(10,0), 
	"CUSTOMER_COMMENTS" VARCHAR2(255 BYTE), 
	"CUSTOMER_ADDRESS_AREA" VARCHAR2(255 BYTE), 
	"CUSTOMER_LOCALITY" VARCHAR2(50 BYTE), 
	"CUSTOMER_ALTERNATE_ADDRESS" VARCHAR2(255 BYTE), 
	"CUSTOMER_CITY" NUMBER(10,0), 
	"CUSTOMER_STATE" NUMBER(10,0), 
	"MARKETING_ID" NUMBER(10,0), 
	"LEAD_CREATION_STATUS" CHAR(1 BYTE), 
	"LEAD_SUB_STATUS_ID" NUMBER(2,0), 
	"CREATED_DATE" DATE, 
	"MODIFIED_DATE" DATE, 
	"LEAD_TASK_COMMENTS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_LEAD_ID" VARCHAR2(20 BYTE), 
	"CHANNEL_PARTNER_NAME" VARCHAR2(100 BYTE), 
	"CHANNEL_PARTNER_ADDRESS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_NUMBER" NUMBER(15,0), 
	"LEAD_INACTIVE_COMMENTS" VARCHAR2(255 BYTE), 
	 PRIMARY KEY ("LEAD_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE, 
	 CONSTRAINT "CONSTRAINT_CUSTOMERNAME_MOBILE" UNIQUE ("CUSTOMER_NAME", "MOBILE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE, 
	 CONSTRAINT "CONSTRAINT_MOBILE" UNIQUE ("MOBILE")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE, 
	 CONSTRAINT "CONSTRAINT_EMAIL" UNIQUE ("EMAIL")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "SYSTEM"  ENABLE
   ) ;

  
--drop trigger LEAD_AFTER_INSERT;
/
ALTER TRIGGER "CUSTOMERAPP_CUG"."LEAD_AFTER_INSERT" ENABLE;

create table lead_site_visit(
lead_site_visit_id number(10),
lead_id number(10), 
lead_project_id number(10), 
lead_project_name varchar2(255), 
lead_date_visit date,
lead_site_visit_status number(1),
lead_site_visit_status_name varchar2(50),
lead_sales_rep varchar2(100),
lead_sales_met_comments varchar2(100),
CREATED_DATE date,
MODIFIED_DATe date);

CREATE SEQUENCE LEAD_SITE_VISIT_SEQ MINVALUE 1 MAXVALUE 999999999999999999999999999 START WITH 1 INCREMENT BY 1 CACHE 20; 



  CREATE TABLE "CUSTOMERAPP_CUG"."LEAD_INACTIVE_REMARKS_MASTER" 
   (	"INACTIVE_REMARK_ID" NUMBER(3,0), 
	"INACTIVE_REMARK_NAME" VARCHAR2(255 BYTE), 
	"INACTIVE_REMARK_STATUS" CHAR(1 BYTE), 
	"CREATED_DATE" DATE, 
	"MODIFIED_DATE" DATE
   ) ;

alter table lead_creation add(MARK_AS_SITE_VISIT char(1),MARK_AS_BOOKING char(1));
alter table lead_creation add(PROJECT_NAME varchar2(255), SALES_REPLY varchar2(400), MEET_COMMENTS varchar2(400) );

CREATE TABLE "LEAD_CREATION_HIST" 
   (	"LEAD_ID" NUMBER(10,0), 
	"CUSTOMER_NAME" VARCHAR2(50 BYTE), 
	"COMPANY" VARCHAR2(255 BYTE), 
	"DESIGNATION" VARCHAR2(255 BYTE), 
	"REGISTERED_CREATED" TIMESTAMP (6), 
	"LEAD_CREATED_TO_SITE_SCHEDULED" VARCHAR2(100 BYTE), 
	"EMAIL" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL1" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL2" VARCHAR2(50 BYTE), 
	"MOBILE" NUMBER(12,0) NOT NULL ENABLE, 
	"ADDITIONAL_MOBILE1" VARCHAR2(12 BYTE), 
	"ADDITIONAL_MOBILE2" VARCHAR2(12 BYTE), 
	"PHONE" NUMBER(15,0), 
	"EXTENSION" NUMBER(10,0), 
	"PROJECT_ID" NUMBER(10,0), 
	"PREFERED_PROJECT_LOCATION" NUMBER(10,0), 
	"FIRST_SOURCE_ID" NUMBER(10,0), 
	"LAST_SOURCE_ID" NUMBER(10,0), 
	"LEAD_OWNER_ID" NUMBER(10,0), 
	"MIN_BUDGET" NUMBER(12,0), 
	"MAX_BUDGET" NUMBER(12,0), 
	"BUDGET_RANGE" VARCHAR2(50 BYTE), 
	"REQUIREMENT_TYPE" VARCHAR2(100 BYTE), 
	"MIN_FLAT_AREA" NUMBER(12,0), 
	"MAX_FLAT_AREA" NUMBER(12,0), 
	"TIME_FRAME_TO_PURCHASE" NUMBER(10,0), 
	"HOUSING_REQUIREMENT" NUMBER(10,0), 
	"CUSTOMER_COMMENTS" VARCHAR2(255 BYTE), 
	"CUSTOMER_ADDRESS_AREA" VARCHAR2(255 BYTE), 
	"CUSTOMER_LOCALITY" VARCHAR2(50 BYTE), 
	"CUSTOMER_ALTERNATE_ADDRESS" VARCHAR2(255 BYTE), 
	"CUSTOMER_CITY" NUMBER(10,0), 
	"CUSTOMER_STATE" NUMBER(10,0), 
	"MARKETING_ID" NUMBER(10,0), 
	"LEAD_CREATION_STATUS" CHAR(1 BYTE), 
	"LEAD_SUB_STATUS_ID" NUMBER(2,0), 
	"CREATED_DATE" DATE, 
	"MODIFIED_DATE" DATE, 
	"LEAD_TASK_COMMENTS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_LEAD_ID" VARCHAR2(20 BYTE), 
	"CHANNEL_PARTNER_NAME" VARCHAR2(100 BYTE), 
	"CHANNEL_PARTNER_ADDRESS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_NUMBER" NUMBER(15,0), 
	"LEAD_INACTIVE_COMMENTS"     VARCHAR2(255 BYTE),
	MARK_AS_SITE_VISIT char(1),
	MARK_AS_BOOKING char(1),
	PROJECT_NAME varchar2(255), 
	SALES_REPLY varchar2(400), 
	MEET_COMMENTS varchar2(400)
   ) ;
   
   CREATE TABLE "LEAD_CREATION_MIS" 
   (	"LEAD_ID" NUMBER(10,0), 
	"CUSTOMER_NAME" VARCHAR2(50 BYTE), 
	"COMPANY" VARCHAR2(255 BYTE), 
	"DESIGNATION" VARCHAR2(255 BYTE), 
	"REGISTERED_CREATED" TIMESTAMP (6), 
	"LEAD_CREATED_TO_SITE_SCHEDULED" VARCHAR2(100 BYTE), 
	"EMAIL" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL1" VARCHAR2(50 BYTE), 
	"ALTERNATE_EMAIL2" VARCHAR2(50 BYTE), 
	"MOBILE" NUMBER(12,0) NOT NULL ENABLE, 
	"ADDITIONAL_MOBILE1" VARCHAR2(12 BYTE), 
	"ADDITIONAL_MOBILE2" VARCHAR2(12 BYTE), 
	"PHONE" NUMBER(15,0), 
	"EXTENSION" NUMBER(10,0), 
	"PROJECT_ID" NUMBER(10,0), 
	"PREFERED_PROJECT_LOCATION" NUMBER(10,0), 
	"FIRST_SOURCE_ID" NUMBER(10,0), 
	"LAST_SOURCE_ID" NUMBER(10,0), 
	"LEAD_OWNER_ID" NUMBER(10,0), 
	"MIN_BUDGET" NUMBER(12,0), 
	"MAX_BUDGET" NUMBER(12,0), 
	"BUDGET_RANGE" VARCHAR2(50 BYTE), 
	"REQUIREMENT_TYPE" VARCHAR2(100 BYTE), 
	"MIN_FLAT_AREA" NUMBER(12,0), 
	"MAX_FLAT_AREA" NUMBER(12,0), 
	"TIME_FRAME_TO_PURCHASE" NUMBER(10,0), 
	"HOUSING_REQUIREMENT" NUMBER(10,0), 
	"CUSTOMER_COMMENTS" VARCHAR2(255 BYTE), 
	"CUSTOMER_ADDRESS_AREA" VARCHAR2(255 BYTE), 
	"CUSTOMER_LOCALITY" VARCHAR2(50 BYTE), 
	"CUSTOMER_ALTERNATE_ADDRESS" VARCHAR2(255 BYTE), 
	"CUSTOMER_CITY" NUMBER(10,0), 
	"CUSTOMER_STATE" NUMBER(10,0), 
	"MARKETING_ID" NUMBER(10,0), 
	"LEAD_CREATION_STATUS" CHAR(1 BYTE), 
	"LEAD_SUB_STATUS_ID" NUMBER(2,0), 
	"CREATED_DATE" DATE, 
	"MODIFIED_DATE" DATE, 
	"LEAD_TASK_COMMENTS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_LEAD_ID" VARCHAR2(20 BYTE), 
	"CHANNEL_PARTNER_NAME" VARCHAR2(100 BYTE), 
	"CHANNEL_PARTNER_ADDRESS" VARCHAR2(255 BYTE), 
	"CHANNEL_PARTNER_NUMBER" NUMBER(15,0), 
	"LEAD_INACTIVE_COMMENTS"     VARCHAR2(255 BYTE),
	MARK_AS_SITE_VISIT char(1),
	MARK_AS_BOOKING char(1),
    PROJECT_NAME varchar2(255),
    SALES_REPLY varchar2(400),
    MEET_COMMENTS varchar2(400), 
	 CONSTRAINT "CONSTR_LEAD_CREATION_MIS" UNIQUE ("LEAD_ID", "CUSTOMER_NAME", "LEAD_CREATED_TO_SITE_SCHEDULED","EMAIL",
     "MOBILE", "PROJECT_ID","FIRST_SOURCE_ID","LAST_SOURCE_ID","LEAD_OWNER_ID","LEAD_SUB_STATUS_ID")
   ) ;


  CREATE TABLE "CUSTOMERAPP_CUG"."LEAD_NEW_SITE_VISIT" 
   (	"LEAD_ID" NUMBER(10,0), 
	"LEAD_CREATED_TO_SITE_SCHEDULED" VARCHAR2(100 BYTE), 
	"PROJECT_ID" NUMBER(10,0), 
	"LEAD_CREATION_STATUS" CHAR(1 BYTE), 
	"LEAD_SUB_STATUS_ID" NUMBER(2,0), 
	"CREATED_DATE" DATE, 
	"MODIFIED_DATE" DATE, 
	"PROJECT_NAME" VARCHAR2(255 BYTE), 
	"SALES_REPLY" VARCHAR2(400 BYTE), 
	"MEET_COMMENTS" VARCHAR2(400 BYTE), 
	 CONSTRAINT "LEAD_NEW_SITE_VISIT_UK1" UNIQUE ("LEAD_ID", "LEAD_CREATED_TO_SITE_SCHEDULED", "PROJECT_ID", "LEAD_SUB_STATUS_ID", "PROJECT_NAME"));



CREATE OR REPLACE EDITIONABLE TRIGGER "LEAD_AFTER_INSERT" 
AFTER INSERT or UPDATE
   ON LEAD_CREATION
REFERENCING NEW AS NEW OLD AS OLD
   FOR EACH ROW
DECLARE
    duplicate_key EXCEPTION;
    PRAGMA EXCEPTION_INIT(duplicate_key, -1); -- Adjust the error code based on your requirement
BEGIN
	Insert into LEAD_CREATION_HIST (LEAD_ID,CUSTOMER_NAME,COMPANY,DESIGNATION,REGISTERED_CREATED,LEAD_CREATED_TO_SITE_SCHEDULED,EMAIL,ALTERNATE_EMAIL1,ALTERNATE_EMAIL2,MOBILE,ADDITIONAL_MOBILE1,ADDITIONAL_MOBILE2,PHONE,EXTENSION,PROJECT_ID,PREFERED_PROJECT_LOCATION,FIRST_SOURCE_ID,LAST_SOURCE_ID,LEAD_OWNER_ID,MIN_BUDGET,MAX_BUDGET,BUDGET_RANGE,REQUIREMENT_TYPE,MIN_FLAT_AREA,MAX_FLAT_AREA,TIME_FRAME_TO_PURCHASE,HOUSING_REQUIREMENT,CUSTOMER_COMMENTS,CUSTOMER_ADDRESS_AREA,CUSTOMER_LOCALITY,CUSTOMER_ALTERNATE_ADDRESS,CUSTOMER_CITY,CUSTOMER_STATE,MARKETING_ID,LEAD_CREATION_STATUS,LEAD_SUB_STATUS_ID,CREATED_DATE,MODIFIED_DATE,LEAD_TASK_COMMENTS,CHANNEL_PARTNER_LEAD_ID,CHANNEL_PARTNER_NAME,CHANNEL_PARTNER_ADDRESS,CHANNEL_PARTNER_NUMBER,LEAD_INACTIVE_COMMENTS,MARK_AS_SITE_VISIT,MARK_AS_BOOKING,PROJECT_NAME , SALES_REPLY,  MEET_COMMENTS )
	values (:new.LEAD_ID,:new.CUSTOMER_NAME,:new.COMPANY,:new.DESIGNATION,:new.REGISTERED_CREATED,:new.LEAD_CREATED_TO_SITE_SCHEDULED,:new.EMAIL,:new.ALTERNATE_EMAIL1,:new.ALTERNATE_EMAIL2,:new.MOBILE,:new.ADDITIONAL_MOBILE1,:new.ADDITIONAL_MOBILE2,:new.PHONE,:new.EXTENSION,:new.PROJECT_ID,:new.PREFERED_PROJECT_LOCATION,:new.FIRST_SOURCE_ID,:new.LAST_SOURCE_ID,:new.LEAD_OWNER_ID,:new.MIN_BUDGET,:new.MAX_BUDGET,:new.BUDGET_RANGE,:new.REQUIREMENT_TYPE,:new.MIN_FLAT_AREA,:new.MAX_FLAT_AREA,:new.TIME_FRAME_TO_PURCHASE,:new.HOUSING_REQUIREMENT,:new.CUSTOMER_COMMENTS,:new.CUSTOMER_ADDRESS_AREA,:new.CUSTOMER_LOCALITY,:new.CUSTOMER_ALTERNATE_ADDRESS,:new.CUSTOMER_CITY,:new.CUSTOMER_STATE,:new.MARKETING_ID,:new.LEAD_CREATION_STATUS,:new.LEAD_SUB_STATUS_ID,:new.CREATED_DATE,:new.MODIFIED_DATE,:new.LEAD_TASK_COMMENTS,:new.CHANNEL_PARTNER_LEAD_ID,:new.CHANNEL_PARTNER_NAME,:new.CHANNEL_PARTNER_ADDRESS,:new.CHANNEL_PARTNER_NUMBER,:new.LEAD_INACTIVE_COMMENTS,:new.MARK_AS_SITE_VISIT,:new.MARK_AS_BOOKING,:new.PROJECT_NAME , :new.SALES_REPLY,  :new.MEET_COMMENTS );
	BEGIN
	Insert into LEAD_CREATION_MIS (LEAD_ID,CUSTOMER_NAME,COMPANY,DESIGNATION,REGISTERED_CREATED,LEAD_CREATED_TO_SITE_SCHEDULED,EMAIL,ALTERNATE_EMAIL1,ALTERNATE_EMAIL2,MOBILE,ADDITIONAL_MOBILE1,ADDITIONAL_MOBILE2,PHONE,EXTENSION,PROJECT_ID,PREFERED_PROJECT_LOCATION,FIRST_SOURCE_ID,LAST_SOURCE_ID,LEAD_OWNER_ID,MIN_BUDGET,MAX_BUDGET,BUDGET_RANGE,REQUIREMENT_TYPE,MIN_FLAT_AREA,MAX_FLAT_AREA,TIME_FRAME_TO_PURCHASE,HOUSING_REQUIREMENT,CUSTOMER_COMMENTS,CUSTOMER_ADDRESS_AREA,CUSTOMER_LOCALITY,CUSTOMER_ALTERNATE_ADDRESS,CUSTOMER_CITY,CUSTOMER_STATE,MARKETING_ID,LEAD_CREATION_STATUS,LEAD_SUB_STATUS_ID,CREATED_DATE,MODIFIED_DATE,LEAD_TASK_COMMENTS,CHANNEL_PARTNER_LEAD_ID,CHANNEL_PARTNER_NAME,CHANNEL_PARTNER_ADDRESS,CHANNEL_PARTNER_NUMBER,LEAD_INACTIVE_COMMENTS,MARK_AS_SITE_VISIT,MARK_AS_BOOKING,PROJECT_NAME , SALES_REPLY,  MEET_COMMENTS )
	values (:new.LEAD_ID,:new.CUSTOMER_NAME,:new.COMPANY,:new.DESIGNATION,:new.REGISTERED_CREATED,:new.LEAD_CREATED_TO_SITE_SCHEDULED,:new.EMAIL,:new.ALTERNATE_EMAIL1,:new.ALTERNATE_EMAIL2,:new.MOBILE,:new.ADDITIONAL_MOBILE1,:new.ADDITIONAL_MOBILE2,:new.PHONE,:new.EXTENSION,:new.PROJECT_ID,:new.PREFERED_PROJECT_LOCATION,:new.FIRST_SOURCE_ID,:new.LAST_SOURCE_ID,:new.LEAD_OWNER_ID,:new.MIN_BUDGET,:new.MAX_BUDGET,:new.BUDGET_RANGE,:new.REQUIREMENT_TYPE,:new.MIN_FLAT_AREA,:new.MAX_FLAT_AREA,:new.TIME_FRAME_TO_PURCHASE,:new.HOUSING_REQUIREMENT,:new.CUSTOMER_COMMENTS,:new.CUSTOMER_ADDRESS_AREA,:new.CUSTOMER_LOCALITY,:new.CUSTOMER_ALTERNATE_ADDRESS,:new.CUSTOMER_CITY,:new.CUSTOMER_STATE,:new.MARKETING_ID,:new.LEAD_CREATION_STATUS,:new.LEAD_SUB_STATUS_ID,:new.CREATED_DATE,:new.MODIFIED_DATE,:new.LEAD_TASK_COMMENTS,:new.CHANNEL_PARTNER_LEAD_ID,:new.CHANNEL_PARTNER_NAME,:new.CHANNEL_PARTNER_ADDRESS,:new.CHANNEL_PARTNER_NUMBER,:new.LEAD_INACTIVE_COMMENTS,:new.MARK_AS_SITE_VISIT,:new.MARK_AS_BOOKING,:new.PROJECT_NAME , :new.SALES_REPLY,  :new.MEET_COMMENTS );
	EXCEPTION
    WHEN duplicate_key THEN
        -- Bypass the duplicate key exception
       update LEAD_CREATION_MIS
   set
     CUSTOMER_NAME=:new.CUSTOMER_NAME,COMPANY=:new.COMPANY,DESIGNATION=:new.DESIGNATION,REGISTERED_CREATED=:new.REGISTERED_CREATED,LEAD_CREATED_TO_SITE_SCHEDULED=:new.LEAD_CREATED_TO_SITE_SCHEDULED,EMAIL=:new.EMAIL,ALTERNATE_EMAIL1=:new.ALTERNATE_EMAIL1,ALTERNATE_EMAIL2=:new.ALTERNATE_EMAIL2,MOBILE=:new.MOBILE,ADDITIONAL_MOBILE1=:new.ADDITIONAL_MOBILE1,ADDITIONAL_MOBILE2=:new.ADDITIONAL_MOBILE2,PHONE=:new.PHONE,EXTENSION=:new.EXTENSION,PROJECT_ID=:new.PROJECT_ID,PREFERED_PROJECT_LOCATION=:new.PREFERED_PROJECT_LOCATION,FIRST_SOURCE_ID=:new.FIRST_SOURCE_ID,LAST_SOURCE_ID=:new.LAST_SOURCE_ID,LEAD_OWNER_ID=:new.LEAD_OWNER_ID,MIN_BUDGET=:new.MIN_BUDGET,MAX_BUDGET=:new.MAX_BUDGET,BUDGET_RANGE=:new.BUDGET_RANGE,REQUIREMENT_TYPE=:new.REQUIREMENT_TYPE,MIN_FLAT_AREA=:new.MIN_FLAT_AREA,MAX_FLAT_AREA=:new.MAX_FLAT_AREA,TIME_FRAME_TO_PURCHASE=:new.TIME_FRAME_TO_PURCHASE,HOUSING_REQUIREMENT=:new.HOUSING_REQUIREMENT,CUSTOMER_COMMENTS=:new.CUSTOMER_COMMENTS,CUSTOMER_ADDRESS_AREA=:new.CUSTOMER_ADDRESS_AREA,CUSTOMER_LOCALITY=:new.CUSTOMER_LOCALITY,CUSTOMER_ALTERNATE_ADDRESS=:new.CUSTOMER_ALTERNATE_ADDRESS,CUSTOMER_CITY=:new.CUSTOMER_CITY,CUSTOMER_STATE=:new.CUSTOMER_STATE,MARKETING_ID=:new.MARKETING_ID,LEAD_CREATION_STATUS=:new.LEAD_CREATION_STATUS,LEAD_SUB_STATUS_ID=:new.LEAD_SUB_STATUS_ID,MODIFIED_DATE=:new.MODIFIED_DATE,LEAD_TASK_COMMENTS=:new.LEAD_TASK_COMMENTS,CHANNEL_PARTNER_LEAD_ID=:new.CHANNEL_PARTNER_LEAD_ID,CHANNEL_PARTNER_NAME=:new.CHANNEL_PARTNER_NAME,CHANNEL_PARTNER_ADDRESS=:new.CHANNEL_PARTNER_ADDRESS,CHANNEL_PARTNER_NUMBER=:new.CHANNEL_PARTNER_NUMBER,LEAD_INACTIVE_COMMENTS=:new.LEAD_INACTIVE_COMMENTS,MARK_AS_SITE_VISIT=:new.MARK_AS_SITE_VISIT,MARK_AS_BOOKING=:new.MARK_AS_BOOKING,PROJECT_NAME=:new.PROJECT_NAME , SALES_REPLY=:new.SALES_REPLY,  MEET_COMMENTS =:new.MEET_COMMENTS
     where  LEAD_ID = :new.LEAD_ID;
    END;
	BEGIN
   -- Insert record into audit table
   INSERT INTO lead_comments
   ( lead_comment_id,
     lead_comment,
     LEAD_COMMENT_OWNER,
     lead_comment_date)
   VALUES
   ( :new.LEAD_ID,
     :new.LEAD_TASK_COMMENTS,
     :new.LEAD_OWNER_ID,
     :new.MODIFIED_DATE);
     EXCEPTION
    WHEN duplicate_key THEN
        -- Bypass the duplicate key exception
        NULL;
    END;
    IF :new.LEAD_SUB_STATUS_ID = '3' or :new.LEAD_SUB_STATUS_ID = '4' or :new.LEAD_SUB_STATUS_ID = '5' THEN
    BEGIN
    INSERT INTO LEAD_NEW_SITE_VISIT
   ( LEAD_ID,
     LEAD_CREATED_TO_SITE_SCHEDULED,
     PROJECT_ID,
     LEAD_CREATION_STATUS,
     LEAD_SUB_STATUS_ID,
     PROJECT_NAME,
     SALES_REPLY,
     MEET_COMMENTS,
     CREATED_DATE,
     MODIFIED_DATE)
   VALUES
   ( :new.LEAD_ID,
     :new.LEAD_CREATED_TO_SITE_SCHEDULED,
     :new.PROJECT_ID,
     :new.LEAD_CREATION_STATUS,
     :new.LEAD_SUB_STATUS_ID,
     :new.PROJECT_NAME,
     :new.SALES_REPLY,
     :new.MEET_COMMENTS,
     :new.CREATED_DATE,
     :new.MODIFIED_DATE);
     EXCEPTION
    WHEN duplicate_key THEN
        -- Bypass the duplicate key exception
         update LEAD_NEW_SITE_VISIT
   set
     LEAD_CREATED_TO_SITE_SCHEDULED =:new.LEAD_CREATED_TO_SITE_SCHEDULED,
     PROJECT_ID = :new.PROJECT_ID,
     LEAD_CREATION_STATUS =:new.LEAD_CREATION_STATUS,
     LEAD_SUB_STATUS_ID = :new.LEAD_SUB_STATUS_ID,
     PROJECT_NAME = :new.PROJECT_NAME,
     SALES_REPLY = :new.SALES_REPLY,
     MEET_COMMENTS =:new.MEET_COMMENTS,
     MODIFIED_DATE =:new.MODIFIED_DATE
     where  LEAD_ID = :new.LEAD_ID;
         END;
    END IF;
   
END;