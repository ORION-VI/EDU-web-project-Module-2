package org.example.service;

import org.example.dao.DataAccessInterface;
import org.example.entity.User;
import org.example.validator.InputValidator;
import java.util.List;

public class Service implements ServiceInterface {
    private DataAccessInterface dataAccessObject;
    private static final InputValidator inputValidator = new InputValidator();

    public Service(DataAccessInterface dataAccessObject) {
        this.dataAccessObject = dataAccessObject;
    }

    @Override
    public void saveUser(String name, String email, Integer age) {
        if(inputValidator.validateName(name) && inputValidator.validateEmail(email) && inputValidator.validateAge(age)) {
            User user = User.buildUser(name, email, age);
            dataAccessObject.saveUser(user);
        }
    }

    @Override
    public void updateUser(Long id, String name, String email, Integer age) {
        if(inputValidator.validateId(id) && inputValidator.validateName(name) && inputValidator.validateEmail(email) && inputValidator.validateAge(age)) {
            User user = User.buildUser(name, email, age);
            user.setId(id);
            dataAccessObject.updateUser(user);
        }
    }

    @Override
    public User findUser(Long id) {
        if(inputValidator.validateId(id)) return dataAccessObject.findUser(id);
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return dataAccessObject.findAllUsers();
    }

    @Override
    public void deleteUser(Long id) {
        if(inputValidator.validateId(id)) dataAccessObject.deleteUser(id);
    }
}
