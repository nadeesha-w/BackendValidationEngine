# Data-Driven Backend Validation Engine

[![Build and test](https://github.com/nadeesha-w/BackendValidationEngine/actions/workflows/maven.yml/badge.svg)](https://github.com/nadeesha-w/BackendValidationEngine/actions/workflows/maven.yml)

A Java test utility that reads user scenarios from a CSV file, queries a MySQL database, and asserts that account status transitions actually happened.

## About

Before you can test a user interface, you have to know the data underneath it is correct. QA engineers check this by hand: run a SQL query, read the result, compare it to what the ticket said should happen. This project automates that check.

It is my first portfolio project on the QA Automation Engineer path. I built it to turn Java and SQL theory from university into something that runs, and to learn the professional habits around the code: branching, conventional commits, code review, and tests that protect a refactor.

## Built With

- Java 17
- MySQL 8
- JDBC (mysql-connector-j 8.3.0)
- JUnit 5
- Maven 3
- Git and GitHub

## Getting Started

### Prerequisites

- JDK 17 or newer
- Maven 3.6 or newer
- MySQL Server 8.x running locally

### Installation

```bash
# Clone the repository
git clone https://github.com/nadeesha-w/BackendValidationEngine.git
cd BackendValidationEngine

# Create the database, the table, and the seed data
mysql -u root -p < db/schema.sql
mysql -u root -p < db/seed.sql

# Apply the state change the tests validate
mysql -u root -p < db/queries.sql

# Point the engine at your database
cp src/main/resources/config.example.properties src/main/resources/config.properties
# then edit config.properties with your own MySQL username and password
```

### Running the Project

```bash
mvn -q compile exec:java
```

This prints the expected and actual status for every scenario in `data/test-data.csv`.

### Running Tests

```bash
mvn test
```

`StatusValidatorTest` runs anywhere with no setup. `DatabaseValidationTest` needs MySQL running and seeded, and will fail without it. All six tests should pass green once the database is in place.

## Project Structure

```
BackendValidationEngine/
├── data/test-data.csv        # Test scenarios: id, username, expected status
├── db/schema.sql             # Creates qa_test_db and the Users table
├── db/seed.sql               # Three mock users, all PENDING
├── db/queries.sql            # The state change the engine validates
├── docs/review-notes.md      # Code review findings and what I did about them
├── src/main/java/com/qa/engine/
│   ├── DatabaseManager.java  # JDBC connection and the status query
│   ├── DataLoader.java       # Reads test scenarios from the CSV
│   ├── StatusValidator.java  # Pure transition rules, no database
│   ├── User.java             # Test data model
│   └── Main.java             # Manual runner
├── src/test/java/com/qa/engine/
│   ├── DatabaseValidationTest.java
│   └── StatusValidatorTest.java
├── BUILD-LOG.md              # What I decided, broke, fixed and learned
└── README.md
```

## Key Features

- Data-driven validation: test scenarios live in a CSV file, so new cases need no code change and no recompile
- Parameterized SQL queries, so the command and the data stay separate
- try-with-resources on every JDBC connection, statement and result set
- A pure transition validator with four unit tests that run without a database
- Credentials loaded from a local config file that is never committed

## What I Learned

- A parameterized query is not just "safer". The command gets compiled first and the data goes in afterwards, which is the actual reason injection stops working.
- Tests earn their keep during a refactor, not when you write them. Watching `mvn test` stay green while I restructured `getUserStatus` was the first time my own tests protected me.
- I had a database password sitting in `DatabaseManager.java` for two months without noticing. It was only a local test database, but the habit is the problem.
- Keeping test data out of the code is what makes a framework a framework. Adding a scenario should be a line in a CSV, not an edit and a rebuild.
- I could not judge my own README, because I am the worst possible reader of it. I gave it to a friend who does not write Java and asked her what the project does.

## Roadmap / Future Improvements

- [x] Run the test suite automatically on every push with GitHub Actions
- [ ] Add negative-path tests for users that do not exist
- [ ] Validate full transition chains, not just a single status read
- [ ] Replace the hand-rolled CSV parsing with a proper library

## Author

**Nadeesha Wickramasinghe**

- GitHub: https://github.com/nadeesha-w
- LinkedIn: https://www.linkedin.com/in/nadeesha-wickramasinghe

QA Automation Engineer | Java · SQL · JUnit · Selenium

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
