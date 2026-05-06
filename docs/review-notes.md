# Code review notes — DatabaseManager

From the Week 5 code review. Two things to fix, both in `DatabaseManager`.
Neither breaks the tests, so I am carrying them into the M6 refactor task
instead of stopping the milestone now.

## 1. SQL injection — string concatenation in getUserStatus

Current:

    String query = "SELECT account_status FROM Users WHERE id = " + userId;

The database cannot tell which part of that string is my command and which part
is data. My id is an int so this exact line is hard to attack, but the habit is
the problem.

Fix: `"... WHERE id = ?"` with `stmt.setInt(1, userId)`.

## 2. Resources are never closed

`connect()` is called on every query and the `Connection`,
`PreparedStatement` and `ResultSet` are never closed. That is a leak.

Fix: try-with-resources, all three declared in the same `try`.

## Also flagged, lower priority

- Credentials are plain mutable `static` strings, and the password is in the
  source file.
- `e.printStackTrace()` everywhere instead of a message that says what failed.
- `getUserStatus` returns `""` when the user does not exist, which hides the
  not-found case from the caller.

**Status:** all of the above scheduled for M6.
