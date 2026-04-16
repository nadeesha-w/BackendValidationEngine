-- The state transition this project validates, written by hand first.
-- The Java engine automates exactly these two statements.

USE qa_test_db;

-- 1. Simulate the state change: user 1 is approved.
UPDATE Users SET account_status = 'ACTIVE' WHERE id = 1;

-- 2. Read the status back. This is the query DatabaseManager runs.
SELECT account_status FROM Users WHERE id = 1;
