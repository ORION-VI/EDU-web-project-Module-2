package org.example.service;

import org.example.entity.User;

import java.util.List;

public interface ServiceInterface {

    User saveUser(User user);

    User updateUser(Long id, User user);

    User findUser(Long id);

    List<User> findAllUsers();

    void deleteUser(Long id);
}
