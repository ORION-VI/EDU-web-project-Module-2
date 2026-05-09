package org.example.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Утилитный класс-синглтон для использования SessionFactory (создается один раз за все приложение).
public class SessionFactoryProvider {
    private static final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(SessionFactoryProvider.class);

    //static блок для инициализации SessionFactory и обработки ошибки создания.
    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        catch (Exception e) {
            log.error("ERROR: Failed to create a SessionFactory instance", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    private SessionFactoryProvider() {
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //Метод закрытия SessionFactory.
    public static void sessionFactoryShutdown() {
        if(sessionFactory != null) {
            sessionFactory.close();
            log.info("SUCCESS: Session Factory has been closed");
        }
    }
}
