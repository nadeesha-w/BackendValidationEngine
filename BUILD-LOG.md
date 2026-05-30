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

---

## Entry: 7 May — Moving the project to Maven

**Decisions**
- Migrated to the Maven layout (`src/main/java`, `src/test/java`) and wrote a
  `pom.xml` with the MySQL connector and JUnit as dependencies.

**Challenges**
- My imports broke the moment the files moved, and I did not understand why until
  I matched each package name to its folder path.

**Solutions**
- Reloaded the Maven project in IntelliJ and the imports resolved.

**Learnings**
- Maven fetches dependencies from a `pom.xml` "shopping list" instead of me
  downloading JAR files by hand. That is what stops the "works on my machine"
  problem when I push to GitHub.

---

## Entry: 12 May — My first JUnit tests

**Decisions**
- Wrote `DatabaseValidationTest`: one `@Test` that just prints to confirm the
  runner works, then `validateUserStatusUpdate` that loads the CSV, queries the
  database, and asserts expected equals actual.

**Challenges**
- IntelliJ did not see the JUnit import at first (a Maven reload issue). Then I
  deliberately changed the DB status to SUSPENDED to watch a test fail.

**Solutions**
- Reloaded the pom to fix the import. The failing run showed me the exact
  mismatch, expected vs actual — seeing it go red on purpose made the whole point
  click.

**Learnings**
- `@Test` is a label the framework reads. `assertEquals(expected, actual)` passes
  silently or reports both values on failure. Same test logic, different data from
  the CSV — that is data-driven testing.

---

## Entry: 21 May — A status-transition validator

**Decisions**
- Added `StatusValidator.isValidTransition(from, to)` — a small pure function
  (PENDING to ACTIVE is valid, the reverse is not). Wrote it so I could unit-test
  real logic without needing the database running.

**Challenges**
- My existing test needs MySQL up and the data seeded. I wanted at least some
  tests that run anywhere, instantly.

**Solutions**
- Wrote four tests — valid, reverse-rejected, unknown-status-rejected, and
  null-safe — and added the JUnit engine plus the Surefire plugin so `mvn test`
  runs them from the terminal, not just in the IDE. Added the exec plugin at the
  same time so `mvn exec:java` runs the manual runner.

**Learnings**
- A pure function is the easiest thing in the world to test: no database, no
  setup, just arrange, act, assert. And the null check is one line — the null test
  proves it holds, and I can watch it go red if I remove the guard.

---

## Entry: 26 May — The M6 refactor, finally

**Decisions**
- Went back to `DatabaseManager` and did both fixes I have been flagging since
  April: try-with-resources for the connection, and the parameterized query.
  One branch, `refactor/m6-connection-and-query`, both fixes together because
  they touch the same method.

**Challenges**
- try-with-resources meant restructuring `getUserStatus` more than I expected,
  because the connection, the statement, and the result set all need to be
  declared in the same try. Got a compile error first because I swapped
  `Statement` for `PreparedStatement` and forgot the type changes too.

**Solutions**
- Declared all three resources in one try block. Changed the query string to
  `WHERE id = ?` with `stmt.setInt(1, userId)`. Ran `mvn test` and everything
  stayed green, which is the first time my tests actually protected me during a
  refactor.

**Learnings**
- try-with-resources works on anything that implements `AutoCloseable`, which is
  why `Connection`, `PreparedStatement` and `ResultSet` all fit in it.
- A parameterized query gets compiled first and the data goes in afterwards. That
  is the actual reason injection stops working, not just "it is safer".
- I read both of these weeks ago in the review notes. Doing them is a completely
  different thing from reading them.

---

## Entry: 28 May — README, with help from Prompt #8

**Decisions**
- Wrote the README last on purpose, because only now do I actually understand
  what the project is well enough to explain it in one sentence.

**Challenges**
- My first attempt was a wall of text about Java and JDBC. Nobody cares about
  that. And I could not judge whether a stranger would understand it, because I
  am not a stranger to it.

**Solutions**
- Ran the README Generator with the project and this build log as the input, then
  rewrote about half of the output in my own words. Cut two features it described
  that I never built. Then I gave it to a friend who does not write Java and asked
  her what the project does. She got it right.

**Learnings**
- The AI gave me a good structure and some wrong facts, both in the same output.
  The structure saved me an hour. The wrong facts would have cost me an interview.
  Reading it properly is the real work.
- A README is written for someone who has never seen the project, and I am the
  worst possible judge of that. Ask an actual stranger.

---

## Entry: 30 May — Running the completion checklist

**Decisions**
- Went through the six completion items honestly instead of just deciding it felt
  finished.

**Challenges**
- My `.gitignore` was right from the first commit, but my database username and
  password were still sitting in `DatabaseManager.java`. They had been there since
  April and I never noticed.
- Two commits near the start say "wip" and "fix". I cannot rewrite those now
  without rewriting history I already pushed.

**Solutions**
- Moved the credentials into a config file, added it to `.gitignore`, and
  committed a `config.example` so someone else knows what to fill in.
- Left the two bad commit messages alone and made sure everything after them is a
  proper `type: description`.

**Learnings**
- I had committed a database password and not noticed for two months. It was only
  a local test database, but the habit is the problem, and the habit is what I
  carry to a real one.
- "Clean from here on" is a real answer. You cannot fix everything retroactively,
  and pretending otherwise is how people waste a day rewriting git history.

---

## Where I landed

Project 1 is done. Not perfect, done. The engine works end to end: CSV in,
database queried, assertion checked, and a pure-logic validator with its own
tests, all running from `mvn test`. The refactor I flagged for six weeks is
merged, the README passes the thirty-second test, the credentials are out of the
repo, and the six completion items pass.

The thing I did not expect is how much this file did for me. It was my finishing
list when I could not remember what was left. It was the input for the README.
And every "Learnings" line in here is a paragraph I lifted straight into my
article. I wrote it because I was told to. I would keep it now without being
told.

Next up is Project 2, and the first thing I am doing differently is writing the
config file on day one instead of day sixty.
