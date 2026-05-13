package org.example.dao;

import org.example.entity.User;
import java.util.List;

public interface DataAccessInterface {

    boolean saveUser(User user);

    boolean updateUser(User user);

    User findUser(Long id);

    List<User> findAllUsers();

    boolean deleteUser(Long id);
}
