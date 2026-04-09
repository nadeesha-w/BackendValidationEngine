-- Schema for the QA test database used by the Backend Validation Engine.

CREATE DATABASE IF NOT EXISTS qa_test_db;

CREATE TABLE Users (
    id             INT PRIMARY KEY AUTO_INCREMENT,
    username       VARCHAR(50)  NOT NULL,
    email          VARCHAR(100) NOT NULL,
    account_status VARCHAR(20)  NOT NULL
);
