package org.example.service;

import org.example.event.UserEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private UserEventProducer userEventProducer;
    private static final InputValidator inputValidator = new InputValidator();
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    public User saveUser(User newUser) {
        if (inputValidator.validateUser(newUser)) {
            userRepository.save(newUser);
            userEventProducer.sendEvent("USER_CREATED", newUser.getName(), newUser.getEmail());
            return newUser;
        } else {
            logger.error("INVALID USER ERROR: Cannot save USER entity to the database");
            throw new IllegalArgumentException();
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
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User deletedUser = userOptional.get();
                userRepository.deleteById(id);
                userEventProducer.sendEvent("USER_DELETED", deletedUser.getName(), deletedUser.getEmail());
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
