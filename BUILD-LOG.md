# Build Log — Data-Driven Backend Validation Engine

**Project:** Project 1 of 4 — Data-Driven Backend Validation Engine
**Career path:** QA Automation Engineer / SDET
**Kept by:** Nadeesha Wickramasinghe

**How I keep this log.** One entry per feature or coding session. For each one I
capture four things, in rough notes, while it is fresh: **Decisions** (what I
chose and why), **Challenges** (what got hard or broke), **Solutions** (how I got
unstuck), and **Learnings** (what I understand now that I did not before). Plain
language, half-sentences, whatever is fastest. I polish it into an article later.
The "because" and the struggle are the parts worth writing down.

---

## Entry: 1 April — Setting up the project and Git

**Decisions**
- Installed JDK 17, IntelliJ, MySQL, and Git, made the `BackendValidationEngine`
  folder, ran `git init`, and added a `.gitignore` before writing any code. I
  wanted version control from the very first commit.

**Challenges**
- `java -version` worked but `javac -version` said command not found. I did not
  understand Windows environment variables (User vs System PATH) and lost about
  an hour to it.

**Solutions**
- Added the JDK `bin` folder to PATH correctly. Asking AI worked once I gave it my
  OS, the exact version, and the exact error, instead of "java isn't working".

**Learnings**
- The JDK includes the compiler (`javac`); the JRE only runs Java. That is why one
  worked and the other did not. And `.gitignore` keeps generated files (`*.class`,
  `target/`) and machine-specific settings (`.idea/`) out of the repo.

---

## Entry: 9 April — Creating the database schema

**Decisions**
- Made `id` the primary key on the `Users` table, with `username`, `email`, and
  `account_status` columns.

**Challenges**
- Got "No database selected" when I ran the CREATE TABLE — I had forgotten
  `USE qa_test_db;` first.

**Solutions**
- Added the `USE` statement at the top of `schema.sql` and ran it in Workbench.

**Learnings**
- A primary key uniquely identifies each row and cannot be null or duplicated.
  That is what lets me later say "user #1" and the database knows exactly which
  row I mean.

---

## Entry: 16 April — Seed data and validation queries

**Decisions**
- Seeded 3 users all as PENDING. In `queries.sql` I update user 1 to ACTIVE, then
  SELECT the status back to check it.

**Challenges**
- Nothing broke this week. SQL felt closer to the theory I already knew.

**Solutions**
- Ran the scripts in Workbench and confirmed the SELECT returns ACTIVE for user 1.

**Learnings**
- DDL (CREATE) defines structure; DML (INSERT, UPDATE, SELECT) works with the
  data. The SELECT that returns ACTIVE is the exact query my Java will run later —
  the whole project is really "run this SELECT from Java and check it matches."

---

## Entry: 23 April — Connecting Java to MySQL (JDBC)

**Decisions**
- Started a branch `feat/jdbc-connection-manager` (never build new work on main).
  Wrote `DatabaseManager.connect()` and a `main` that prints "Connected
  successfully!".

**Challenges**
- Lost a whole evening to a connection error before realising MySQL was not even
  running — I was debugging my code when the problem was the server. I also just
  used `e.printStackTrace()` because that is what the AI example showed.

**Solutions**
- Started the MySQL service and the connection worked. When I finally saw
  "Connected successfully!" I actually celebrated — first time my Java did
  something real.

**Learnings**
- JDBC is the bridge between Java and the database. `DriverManager.getConnection`
  takes a URL, user, and password. `SQLException` is a checked exception, so Java
  forces me to handle it. And `printStackTrace()` works but is not great — flagged
  to come back to it.

---

## Entry: 28 April — A User class to hold test data (POJO)

**Decisions**
- Made a `User` class with private `id`, `username`, and `expectedStatus`, a
  constructor, and getters.

**Challenges**
- I did not really understand why the fields should be private instead of just
  public.

**Solutions**
- Generated the getters and instantiated a `User` in `main` to check it works.

**Learnings**
- Encapsulation: private fields with getters keep other classes from reaching in
  and changing my data. For test data that keeps things predictable, and a proper
  object beats passing around raw strings or arrays.

---

## Entry: 30 April — Reading test data from a CSV file

**Decisions**
- Keep test scenarios in `test-data.csv` instead of hardcoding them. Wrote
  `DataLoader.loadUsers` to read the file with a `BufferedReader` and `split(",")`.

**Challenges**
- Got the column order wrong at first — array index 0 is the first column, not 1.
  Also had to think about what happens if the file is missing (`IOException`).

**Solutions**
- Looped through the list and printed the usernames to confirm the parse worked.

**Learnings**
- File I/O is a stream, read line by line. Keeping test data in a CSV means I can
  add scenarios without rewriting and recompiling my Java. Decoupling data from
  logic is what professional frameworks do.

---

## Entry: 4 May — Querying user status from the database

**Decisions**
- `getUserStatus()` opens its own DB connection by calling `connect()` each time
  it runs. Simplest way to get a working query.

**Challenges**
- Every call opens a brand new connection, and I never close them. Works, but
  wasteful, and a connection you do not close is a leak.

**Solutions**
- The fix is try-with-resources: `try (Connection conn = ...) { ... }`, so Java
  closes it automatically when the block ends. This is my M6 refactor task — the
  branch still has the quick version.

**Learnings**
- Opening a DB connection is expensive and unclosed connections leak. Close once
  you are actually done, not per call. This is the problem connection pooling
  exists to solve.

---

## Entry: 6 May — What the code review caught (SQL injection)

**Decisions**
- Built the lookup query by putting the id straight into the string:
  `"SELECT account_status FROM Users WHERE id = " + userId`. It worked in my tests,
  so I moved on.

**Challenges**
- The code review flagged it as a SQL injection risk. The database cannot tell
  which part of that string is my command and which part is data.

**Solutions**
- The fix is a parameterized query: `"... WHERE id = ?"` with
  `stmt.setInt(1, userId)`. Also part of my M6 refactor — the branch still has the
  concatenated version.

**Learnings**
- Separate the command from the data, and compile the command first. My id is a
  number so this exact line is hard to attack, but the habit protects the next
  query that carries user text. "It works" is not the same as "it is safe" — that
  gap is the whole point of review.
