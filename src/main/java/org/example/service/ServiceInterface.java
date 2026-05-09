package org.example.service;

import org.example.entity.User;
import java.util.List;

public interface ServiceInterface {

    void saveUser(String name, String email, Integer age);

    void updateUser(Long id, String name, String email, Integer age);

    User findUser(Long id);

    List<User> findAllUsers();

    void deleteUser(Long id);
}
