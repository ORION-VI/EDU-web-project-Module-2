package org.example.dal;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

//Утилитный класс-синглтон для использования SessionFactory (создается один раз за все приложение).
public class HibernateUtility {
    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    private HibernateUtility() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
