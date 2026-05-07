package org.example.dal;

import org.example.entity.User;

//Абстрактный интерфейс для DAL класса.
public interface DataAccessInterface {

    boolean saveUser(User user);

    boolean updateUser(Long id);

    User findUser(Long id);
}
