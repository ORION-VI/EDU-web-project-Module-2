package org.example.service;

import ch.qos.logback.classic.Logger;
import jakarta.transaction.Transactional;
import org.example.dao.UserRepository;
import org.example.entity.User;
import org.example.validator.InputValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements ServiceInterface {
    private UserRepository userRepository;
    private static final InputValidator inputValidator = new InputValidator();
    private Logger logger;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User saveUser(User newUser) {
        if (inputValidator.validateUser(newUser)) {
            userRepository.save(newUser);
            return newUser;
        } else {
            logger.error("INVALID USER ERROR: Cannot save USER entity to the database");
            throw new RuntimeException();
        }
    }

    @Override
    public User updateUser(Long id, User newUser) {
        if (inputValidator.validateId(id) && inputValidator.validateUser(newUser)) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                if (!userOptional.get().getName().equals(newUser.getName()))
                    userOptional.get().setName(newUser.getName());
                if (!userOptional.get().getEmail().equals(newUser.getEmail()))
                    userOptional.get().setEmail(newUser.getEmail());
                if (!userOptional.get().getAge().equals(newUser.getAge())) userOptional.get().setAge(newUser.getAge());
                userRepository.save(userOptional.get());
                return userOptional.get();
            } else {
                logger.error("USER NOT FOUND ERROR: User ID {} not found", id);
                throw new RuntimeException();
            }
        } else {
            logger.error("INVALID USER ERROR: Cannot update USER entity in the database");
            throw new IllegalArgumentException();
        }
    }

    @Override
    public User findUser(Long id) {
        if (inputValidator.validateId(id)) {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) return userOptional.get();
            else {
                logger.error("USER NOT FOUND ERROR: User ID {} not found", id);
                throw new RuntimeException();
            }
        } else {
            logger.error("INVALID ID ERROR: User ID {} is not valid", id);
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUser(Long id) {
        if (inputValidator.validateId(id)) {
            if (userRepository.findById(id).isPresent()) {
                userRepository.deleteById(id);
            } else {
                logger.error("USER NOT FOUND ERROR: User ID {} not found", id);
                throw new RuntimeException();
            }
        } else {
            logger.error("INVALID ID ERROR: User ID {} is not valid", id);
            throw new IllegalArgumentException();
        }
    }
}
