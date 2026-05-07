package org.example.dal;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DataAccessLayer implements DataAccessInterface {
    private SessionFactory sessionFactory;

    //Конструктор, передаем ссылку на SessionFactory.
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    //Метод транзакции на создание (сохранение) пользователя.
    @Override
    public boolean saveUser(User user) {
        Transaction transaction = null;
        try(Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            Long id = user.getId();
            transaction.commit();
            System.out.println("SUCCESS: User successfully created with ID: " + id.toString() + "!");
            return true;
        } catch (Exception e) {
            if(transaction != null) transaction.rollback();
            System.err.println("ERROR: An error has occurred while saving user to database!" + '\n' + e);
            return false;
        }
    }

    @Override
    public boolean updateUser(Long id) {
        return true;
    }

    //Метод поиска пользователя по ID.
    @Override
    public User findUser(Long id) {
        try(Session session = sessionFactory.openSession()) {
            User user = session.find(User.class, id);
            if(user == null) {
                System.out.println("ERROR: User with ID: " + id.toString() + " is not found!");
                return null;
            }
            return user;
        } catch (Exception e) {
            System.err.println("ERROR: An error has occurred while searching for user!" + '\n' + e);
            return null;
        }
    }
}
