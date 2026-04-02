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
