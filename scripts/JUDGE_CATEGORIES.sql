CREATE TABLE "JUDGE_CATEGORIES"
(
  "ID"          NUMBER        NOT NULL ENABLE,
  "CATEGORY"    VARCHAR2(255) NOT NULL ENABLE,
  "DESCRIPTION" VARCHAR2(255),
  CONSTRAINT "JUDGE_CATEGORIES_PK" PRIMARY KEY ("ID") ENABLE,
  CONSTRAINT "JUDGE_CATEGORIES_UK1" UNIQUE ("CATEGORY") ENABLE
);

CREATE OR REPLACE TRIGGER "BI_JUDGE_CATEGORIES"
BEFORE INSERT ON "JUDGE_CATEGORIES"
FOR EACH ROW
  BEGIN
    IF :NEW."ID" IS NULL
    THEN
      SELECT "JUDGE_CATEGORIES_SEQ".nextval
      INTO :NEW."ID"
      FROM dual;
    END IF;
  END;

/
ALTER TRIGGER "BI_JUDGE_CATEGORIES" ENABLE;