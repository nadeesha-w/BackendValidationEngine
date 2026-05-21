package com.qa.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the transition rules. No database required.
 */
public class StatusValidatorTest {

    @Test
    public void pendingToActiveIsValid() {
        assertTrue(StatusValidator.isValidTransition("PENDING", "ACTIVE"));
    }

    @Test
    public void activeToPendingIsRejected() {
        assertFalse(StatusValidator.isValidTransition("ACTIVE", "PENDING"));
    }

    @Test
    public void unknownStatusIsRejected() {
        assertFalse(StatusValidator.isValidTransition("PENDING", "BANANA"));
    }

    @Test
    public void nullStatusIsRejected() {
        assertFalse(StatusValidator.isValidTransition(null, "ACTIVE"));
        assertFalse(StatusValidator.isValidTransition("PENDING", null));
    }
}
