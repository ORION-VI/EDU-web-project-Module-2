package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.SelectionQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class DataAccessObject implements DataAccessInterface {
    private final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(DataAccessObject.class);

    public DataAccessObject(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean saveUser(User user) {
        Transaction transaction = null;
        Session session = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(user);
            Long id = user.getId();
            transaction.commit();
            System.out.println("SUCCESS: User created with ID: " + id.toString());
            return true;
        }
        catch (Exception e) {
            if(transaction != null) transaction.rollback();
            log.error("ERROR: An error has occurred while saving user to database", e);
            return false;
        }
        finally {
            if(session != null && session.isOpen()) session.close();
        }
    }

    @Override
    public boolean updateUser(User user) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User persistedUser = session.find(User.class, user.getId());
            if(persistedUser != null) {
                if(user.getName() != null && !user.getName().equals(persistedUser.getName())) persistedUser.setName(user.getName());
                if(user.getEmail() != null && !user.getEmail().equals(persistedUser.getEmail())) persistedUser.setEmail(user.getEmail());
                if(user.getAge() != null && !user.getAge().equals(persistedUser.getAge())) persistedUser.setAge(user.getAge());
                session.merge(persistedUser);
                transaction.commit();
                System.out.println("SUCCESS: User ID " + user.getId() + " has been successfully updated!");
                return true;
            }
            if(transaction != null) transaction.rollback();
            System.out.println("UPDATING FAILED: User ID: " + user.getId().toString() + " is not found");
            return false;
        }
        catch (Exception e) {
            if(transaction != null) transaction.rollback();
            log.error("ERROR: Failed to update user ID: {}", user.getId().toString(), e);
            return false;
        }
    }

    @Override
    public User findUser(Long id) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if(user == null) {
                System.out.println("READING FAILED: User ID: " + id.toString() + " is not found");
                return null;
            }
            return user;
        }
        catch (Exception e) {
            log.error("ERROR: An error has occurred while searching for user ID: {}", id.toString(), e);
            return null;
        }
    }

    @Override
    public List<User> findAllUsers() {
        try(Session session = sessionFactory.openSession()) {
            SelectionQuery<User> query = session.createSelectionQuery("FROM User", User.class);
            List<User> userList = query.list();
            if(!userList.isEmpty()) return userList;
            System.out.println("READING FAILED: User table in database is empty");
            return new ArrayList<>();
        }
        catch (Exception e) {
            log.error("ERROR: An error has occurred while searching for users", e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean deleteUser(Long id) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            User user = session.find(User.class, id);
            if(user != null) {
                session.remove(user);
                transaction.commit();
                System.out.println("SUCCESS: Deleted user ID: " + id.toString());
                return true;
            }
            if(transaction != null) transaction.rollback();
            System.out.println("DELETION FAILED: User ID: " + id.toString() + " is not found");
            return false;
        }
        catch (Exception e) {
            if(transaction != null) transaction.rollback();
            log.error("ERROR: An error has occurred while deleting user ID {}", id.toString(), e);
            return false;
        }
    }
}
