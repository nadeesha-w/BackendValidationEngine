-- Mock user data for the validation scenarios.
-- Every user starts as PENDING so the state transition is visible.

USE qa_test_db;

INSERT INTO Users (username, email, account_status) VALUES
    ('nimali_p',  'nimali.p@example.com',  'PENDING'),
    ('sahan_k',   'sahan.k@example.com',   'PENDING'),
    ('dilini_r',  'dilini.r@example.com',  'PENDING');
