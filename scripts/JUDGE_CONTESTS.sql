CREATE TABLE "JUDGE_CONTESTS"
(
  "CONTEST_ID"          NUMBER        NOT NULL ENABLE,
  "CONTEST_NAME"        VARCHAR2(255) NOT NULL ENABLE,
  "CONTEST_DESCRIPTION" CLOB          NOT NULL ENABLE,
  "CONTEST_PRIZE"       CLOB          NOT NULL ENABLE,
  "CONTEST_ORGANIZER"   VARCHAR2(255) NOT NULL ENABLE,
  "CONTEST_DURATION"    NUMBER(3, 0)  NOT NULL ENABLE,
  "IN_Z"                TIMESTAMP(6),
  "OUT_Z"               TIMESTAMP(6),
  CONSTRAINT "JUDGE_CONTESTS_PK" PRIMARY KEY ("CONTEST_ID") ENABLE
);

CREATE OR REPLACE TRIGGER "BI_JUDGE_CONTESTS"
BEFORE INSERT ON "JUDGE_CONTESTS"
FOR EACH ROW
  BEGIN
    IF :NEW."CONTEST_ID" IS NULL
    THEN
      SELECT "JUDGE_CONTESTS_SEQ".nextval
      INTO :NEW."CONTEST_ID"
      FROM dual;
    END IF;
  END;

/
ALTER TRIGGER "BI_JUDGE_CONTESTS" ENABLE;