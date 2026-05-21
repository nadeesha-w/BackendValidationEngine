package com.qa.engine;

/**
 * Pure transition rules for account status.
 *
 * Deliberately has no database dependency so the rules can be unit tested
 * anywhere, instantly.
 */
public class StatusValidator {

    /**
     * Check whether an account status transition is allowed.
     *
     * @param from the current status
     * @param to   the status being moved to
     * @return true if the transition is allowed, false otherwise
     */
    public static boolean isValidTransition(String from, String to) {
        if (from == null || to == null) {
            return false;
        }

        if ("PENDING".equals(from) && "ACTIVE".equals(to)) {
            return true;
        }

        if ("ACTIVE".equals(from) && "SUSPENDED".equals(to)) {
            return true;
        }

        return false;
    }
}
