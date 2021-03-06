CREATE TABLE "JUDGE_PROBLEMS"
(
  "ID"                NUMBER        NOT NULL ENABLE,
  "TITLE"             VARCHAR2(255) NOT NULL ENABLE,
  "CATEGORY"          VARCHAR2(255) NOT NULL ENABLE,
  "SUBCATEGORY"       VARCHAR2(255) NOT NULL ENABLE,
  "PROBLEM_STATEMENT" CLOB          NOT NULL ENABLE,
  "INPUT"             CLOB          NOT NULL ENABLE,
  "CONSTRAINTS"       CLOB          NOT NULL ENABLE,
  "OUTPUT"            CLOB          NOT NULL ENABLE,
  "ADDED_BY"          VARCHAR2(50)  NOT NULL ENABLE,
  "ADDED_ON"          TIMESTAMP(6),
  "MODIFIED_ON"       TIMESTAMP(6),
  "CONTEST_ID"        NUMBER        NOT NULL ENABLE,
  CONSTRAINT "JUDGE_PROBLEMS_PK" PRIMARY KEY ("ID") ENABLE,
  CONSTRAINT "JUDGE_PROBLEMS_UK1" UNIQUE ("TITLE") ENABLE
);
ALTER TABLE "JUDGE_PROBLEMS" ADD CONSTRAINT "JUDGE_PROBLEMS_CON" FOREIGN KEY ("CONTEST_ID")
REFERENCES "JUDGE_CONTESTS" ("CONTEST_ID") ENABLE;
ALTER TABLE "JUDGE_PROBLEMS" ADD CONSTRAINT "JUDGE_PROBLEMS_FK1" FOREIGN KEY ("CATEGORY")
REFERENCES "JUDGE_CATEGORIES" ("CATEGORY") ENABLE;
ALTER TABLE "JUDGE_PROBLEMS" ADD CONSTRAINT "JUDGE_PROBLEMS_FK2" FOREIGN KEY ("SUBCATEGORY")
REFERENCES "JUDGE_SUBCATEGORIES" ("SUBCATEGORY") ENABLE;
ALTER TABLE "JUDGE_PROBLEMS" ADD CONSTRAINT "JUDGE_PROBLEMS_FKEY3" FOREIGN KEY ("ADDED_BY")
REFERENCES "JUDGE_USERS" ("USERNAME") ENABLE;

CREATE OR REPLACE TRIGGER "BI_JUDGE_PROBLEMS"
BEFORE INSERT ON "JUDGE_PROBLEMS"
FOR EACH ROW
  BEGIN
    IF :NEW."ID" IS NULL
    THEN
      SELECT "JUDGE_PROBLEMS_SEQ".nextval
      INTO :NEW."ID"
      FROM dual;
    END IF;
  END;

/
ALTER TRIGGER "BI_JUDGE_PROBLEMS" ENABLE;