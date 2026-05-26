# Code review notes — DatabaseManager

From the Week 5 code review. Two things to fix, both in `DatabaseManager`.
Neither breaks the tests, so I am carrying them into the M6 refactor task
instead of stopping the milestone now.

## 1. SQL injection — string concatenation in getUserStatus  — FIXED 26 May

Was:

    String query = "SELECT account_status FROM Users WHERE id = " + userId;

Now `"... WHERE id = ?"` with `stmt.setInt(1, userId)`. The command is compiled
first and the data goes in afterwards, so the data can never be read as command.

## 2. Resources are never closed  — FIXED 26 May

`Connection`, `PreparedStatement` and `ResultSet` are now declared in a single
try-with-resources block and closed automatically.

## Also flagged, lower priority

- Credentials are plain mutable `static` strings, and the password is in the
  source file.  — still open, see the completion checklist
- `e.printStackTrace()` everywhere instead of a message that says what failed.
  — FIXED 26 May
- `getUserStatus` returns `""` when the user does not exist, which hides the
  not-found case from the caller.  — FIXED 26 May, returns null

**Status:** M6 refactor merged 26 May. Credentials still to move out.
