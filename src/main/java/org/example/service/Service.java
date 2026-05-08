package org.example.service;

import org.example.dao.DataAccessInterface;
import org.example.entity.User;
import org.example.validator.InputValidator;
import java.util.List;

public class Service implements ServiceInterface {
    private DataAccessInterface dataAccessObject;
    private static final InputValidator inputValidator = new InputValidator();

    //Конструктор сервиса, получает DAO через интерфейс (не привязано к конкретной реализации).
    public Service(DataAccessInterface dataAccessObject) {
        this.dataAccessObject = dataAccessObject;
    }

    /*
    Метод записи пользователя.
    Получает данные для создания User, валидирует их, создает объект и передает его в DAO.
    */
    @Override
    public void saveUser(String name, String email, Integer age) {
        if(inputValidator.validateName(name) && inputValidator.validateEmail(email) && inputValidator.validateAge(age)) {
            User user = User.buildUser(name, email, age);
            dataAccessObject.saveUser(user);
        }
    }

    /*
    Метод обновления пользователя.
    Получает данные для создания User с id, валидирует их, создает объект с присвоением переданного id и отдает его в DAO.
    */
    @Override
    public void updateUser(Long id, String name, String email, Integer age) {
        if(inputValidator.validateId(id) && inputValidator.validateName(name) && inputValidator.validateEmail(email) && inputValidator.validateAge(age)) {
            User user = User.buildUser(name, email, age);
            user.setId(id);
            dataAccessObject.updateUser(user);
        }
    }

    /*
    Метод чтения (поиска) пользователя.
    Получает id, валидирует и передает его в DAO.
    */
    @Override
    public User findUser(Long id) {
        if(inputValidator.validateId(id)) return dataAccessObject.findUser(id);
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return dataAccessObject.findAllUsers();
    }

    /*
    Метод удаления пользователя.
    Получает id, валидирует и передает его в DAO.
    */
    @Override
    public void deleteUser(Long id) {
        if(inputValidator.validateId(id)) dataAccessObject.deleteUser(id);
    }
}
