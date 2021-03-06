CREATE TABLE "JUDGE_USER_SOLVED_PROBLEMS"
(
  "ID"         NUMBER        NOT NULL ENABLE,
  "USERNAME"   VARCHAR2(255) NOT NULL ENABLE,
  "PROBLEM_ID" NUMBER        NOT NULL ENABLE,
  CONSTRAINT "JUDGE_USER_SOLVED_PROBLEMS_PK" PRIMARY KEY ("ID") ENABLE,
  CONSTRAINT "JUDGE_USER_SOLVED_PROBLEMS_UK1" UNIQUE ("USERNAME", "PROBLEM_ID") ENABLE
);
ALTER TABLE "JUDGE_USER_SOLVED_PROBLEMS" ADD CONSTRAINT "JUDGE_USER_PROBLEMS_FKEY" FOREIGN KEY ("PROBLEM_ID")
REFERENCES "JUDGE_PROBLEMS" ("ID") ENABLE;
ALTER TABLE "JUDGE_USER_SOLVED_PROBLEMS" ADD CONSTRAINT "JUDGE_USER_SOLVED_PROBLEMS_CON" FOREIGN KEY ("USERNAME")
REFERENCES "JUDGE_USERS" ("USERNAME") ENABLE;

CREATE OR REPLACE TRIGGER "BI_JUDGE_USER_SOLVED_PROBLEMS"
BEFORE INSERT ON "JUDGE_USER_SOLVED_PROBLEMS"
FOR EACH ROW
  BEGIN
    IF :NEW."ID" IS NULL
    THEN
      SELECT "JUDGE_USER_SOLVED_PROBLEM_SEQ".nextval
      INTO :NEW."ID"
      FROM dual;
    END IF;
  END;

/
ALTER TRIGGER "BI_JUDGE_USER_SOLVED_PROBLEMS" ENABLE;