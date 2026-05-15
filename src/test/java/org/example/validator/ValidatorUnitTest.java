package org.example.validator;

import org.example.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorUnitTest {
    private static final InputValidator testingInputValidator = new InputValidator();

    @Test
    public void validateId_whenValid_validationPassed() {
        assertTrue(testingInputValidator.validateId(1L));
    }
    @Test
    public void validateId_whenIdIsNull_notValid() {
        assertFalse(testingInputValidator.validateId(null));
    }
    @Test
    public void validateId_whenIdIsNegative_notValid() {
        assertFalse(testingInputValidator.validateId(-1L));
    }

    @Test
    public void validateName_whenValid_validationPassed() {
        assertTrue(testingInputValidator.validateName("Test"));
    }
    @Test
    public void validateName_whenNameIsNull_notValid() {
        assertFalse(testingInputValidator.validateName(null));
    }
    @Test
    public void validateName_whenNameIsEmpty_notValid() {
        assertFalse(testingInputValidator.validateName(""));
    }

    @Test
    public void validateEmail_whenValid_validationPassed() {
        assertTrue(testingInputValidator.validateEmail("test@test.com"));
    }
    @Test
    public void validateEmail_whenEmailIsNull_notValid() {
        assertFalse(testingInputValidator.validateEmail(null));
    }
    @Test
    public void validateEmail_whenEmailIsEmpty_notValid() {
        assertFalse(testingInputValidator.validateEmail(""));
    }
    @Test
    public void validateEmail_whenNoAtSymbol_notValid() {
        assertFalse(testingInputValidator.validateEmail("testtest.com"));
    }
    @Test
    public void validateEmail_whenNoDomain_notValid() {
        assertFalse(testingInputValidator.validateEmail("test@test"));
    }

    @Test
    public void validateAge_whenValid_validationPassed() {
        assertTrue(testingInputValidator.validateAge(25));
    }
    @Test
    public void validateAge_whenAgeIsNull_notValid() {
        assertFalse(testingInputValidator.validateAge(null));
    }
    @Test
    public void validateAge_whenAgeBelowZero_notValid() {
        assertFalse(testingInputValidator.validateAge(-5));
    }

    @Test
    public void validateUser_whenValidUser_validationPassed() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        assertTrue(testingInputValidator.validateUser(testUser));
    }
    @Test
    public void validateUser_whenUserNotValid_notValid() {
        User testUser = User.buildUser(null, "testtest.com", -5);
        assertFalse(testingInputValidator.validateUser(testUser));
    }
}
