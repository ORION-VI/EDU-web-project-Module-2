package org.example.validator;

import org.example.entity.User;

public class InputValidator {

    public InputValidator() {
    }

    public boolean validateId(Long id) {
        if(id == null) {
            System.out.println("ERROR: ID cannot be empty!");
            return false;
        }
        if(id <= 0) {
            System.out.println("ERROR: ID cannot be 0 or less!");
            return false;
        }
        return true;
    }

    public boolean validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            System.out.println("ERROR: Name cannot be empty!");
            return false;
        }
        return true;
    }

    public boolean validateEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            System.out.println("ERROR: Email cannot be empty!");
            return false;
        }
        if(!email.contains("@") || !email.contains(".")) {
            System.out.println("ERROR: Email must contain @ symbol and domain!");
            return false;
        }
        return true;
    }

    public boolean validateAge(Integer age) {
        if(age == null) {
            System.out.println("ERROR: Age cannot be empty!");
            return false;
        }
        if(age < 0) {
            System.out.println("ERROR: Age cannot be negative!");
            return false;
        }
        return true;
    }

    public boolean validateUser(User user) {
        return validateName(user.getName()) && validateEmail(user.getEmail()) && validateAge(user.getAge());
    }
}
