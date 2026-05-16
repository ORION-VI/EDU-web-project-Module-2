package org.example.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionFactoryProvider {
    private static final SessionFactory sessionFactory;
    private static final Logger log = LoggerFactory.getLogger(SessionFactoryProvider.class);

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

    public static void sessionFactoryShutdown() {
        if(sessionFactory != null) {
            sessionFactory.close();
            log.info("SUCCESS: Session Factory has been closed");
        }
    }
}
