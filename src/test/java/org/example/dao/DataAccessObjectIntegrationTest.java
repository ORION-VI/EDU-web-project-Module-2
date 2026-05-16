package org.example.dao;

import org.example.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.hibernate.cfg.Configuration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class DataAccessObjectIntegrationTest {
    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16").withDatabaseName("test-postgres");

    private static SessionFactory testingSessionFactory;
    private static DataAccessObject testingDataAccessObject;

    @BeforeAll
    public static void initialization() {
        postgreSQLContainer.start();
        try {
            Configuration configuration = new Configuration();
            configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
            configuration.setProperty("dialect", "org.hibernate.dialect.PostgreSQLDialect");
            configuration.setProperty("hibernate.connection.url", postgreSQLContainer.getJdbcUrl());
            configuration.setProperty("hibernate.connection.username", postgreSQLContainer.getUsername());
            configuration.setProperty("hibernate.connection.password", postgreSQLContainer.getPassword());
            configuration.setProperty("hibernate.connection.driver_class", postgreSQLContainer.getDriverClassName());
            configuration.setProperty("hibernate.show_sql", false);
            configuration.setProperty("hibernate.format_sql", false);
            configuration.setProperty("hibernate.use_sql_comments", false);
            configuration.addAnnotatedClass(User.class);
            testingSessionFactory = configuration.buildSessionFactory();
            testingDataAccessObject = new DataAccessObject(testingSessionFactory);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @BeforeEach
    public void setUp() {
        try(Session testSession = testingSessionFactory.openSession()) {
            Transaction transaction = testSession.beginTransaction();
            testSession.createMutationQuery("delete from User").executeUpdate();
            transaction.commit();
        }
    }

    @Test
    public void saveUser_whenValidData_persistUser() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        boolean isPersisted = testingDataAccessObject.saveUser(testUser);
        assertTrue(isPersisted);
        assertNotNull(testingDataAccessObject.findUser(testUser.getId()));
        assertEquals(testUser, testingDataAccessObject.findUser(testUser.getId()));
    }
    @Test
    public void saveUser_whenEmailExists_noPersistence() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User nextTestUser = User.buildUser("Newtest", "test@test.com", 25);
        boolean isPersisted = testingDataAccessObject.saveUser(nextTestUser);
        assertFalse(isPersisted);
        assertNull(testingDataAccessObject.findUser(nextTestUser.getId()));
    }

    @Test
    public void updateUser_whenValidData_updateUser() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User nextTestUser = User.buildUser("Newtest", "test2@test.com", 35);
        nextTestUser.setId(testUser.getId());
        boolean isUpdated = testingDataAccessObject.updateUser(nextTestUser);
        User updatedUser = testingDataAccessObject.findUser(testUser.getId());
        assertTrue(isUpdated);
        assertNotEquals(testUser.getName(), updatedUser.getName());
        assertNotEquals(testUser.getEmail(), updatedUser.getEmail());
        assertNotEquals(testUser.getAge(), updatedUser.getAge());
    }
    @Test
    public void updateUser_whenUserForUpdateNotFound_updateFailed() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testUser.setId(1L);
        boolean isUpdated = testingDataAccessObject.updateUser(testUser);
        User updatedUser = testingDataAccessObject.findUser(testUser.getId());
        assertFalse(isUpdated);
        assertNull(updatedUser);
    }
    @Test
    public void updateUser_whenUserNameIsEqual_updateOnlyEmailAge() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User nextUser = User.buildUser("Test", "test2@test.com", 35);
        nextUser.setId(testUser.getId());
        boolean isUpdated = testingDataAccessObject.updateUser(nextUser);
        User updatedUser = testingDataAccessObject.findUser(testUser.getId());
        assertTrue(isUpdated);
        assertEquals(testUser.getName(), updatedUser.getName());
        assertNotEquals(testUser.getEmail(), updatedUser.getEmail());
        assertNotEquals(testUser.getAge(), updatedUser.getAge());
    }
    @Test
    public void updateUser_whenEmailIsEqual_updateOnlyNameAge() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User nextUser = User.buildUser("Newtest", "test@test.com", 35);
        nextUser.setId(testUser.getId());
        boolean isUpdated = testingDataAccessObject.updateUser(nextUser);
        User updatedUser = testingDataAccessObject.findUser(testUser.getId());
        assertTrue(isUpdated);
        assertNotEquals(testUser.getName(), updatedUser.getName());
        assertEquals(testUser.getEmail(), updatedUser.getEmail());
        assertNotEquals(testUser.getAge(), updatedUser.getAge());
    }
    @Test
    public void updateUser_whenAgeIsEqual_updateOnlyNameEmail() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User nextUser = User.buildUser("Newest", "test2@test.com", 25);
        nextUser.setId(testUser.getId());
        boolean isUpdated = testingDataAccessObject.updateUser(nextUser);
        User updatedUser = testingDataAccessObject.findUser(testUser.getId());
        assertTrue(isUpdated);
        assertNotEquals(testUser.getName(), updatedUser.getName());
        assertNotEquals(testUser.getEmail(), updatedUser.getEmail());
        assertEquals(testUser.getAge(), updatedUser.getAge());
    }

    @Test
    public void deleteUser_whenUserExists_deleteUser() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        boolean isDeleted = testingDataAccessObject.deleteUser(testUser.getId());
        User deletedUser = testingDataAccessObject.findUser(testUser.getId());
        assertTrue(isDeleted);
        assertNull(deletedUser);
    }
    @Test
    public void deleteUser_userNotExists_deletionFails() {
        boolean isDeleted = testingDataAccessObject.deleteUser(1L);
        User deletedUser = testingDataAccessObject.findUser(1L);
        assertFalse(isDeleted);
        assertNull(deletedUser);
    }

    @Test
    public void findUser_userExists_userFound() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        testingDataAccessObject.saveUser(testUser);
        User foundUser = testingDataAccessObject.findUser(testUser.getId());
        assertNotNull(foundUser);
        assertEquals(testUser.getName(), foundUser.getName());
        assertEquals(testUser.getEmail(), foundUser.getEmail());
        assertEquals(testUser.getAge(), foundUser.getAge());
    }
    @Test
    public void findUser_userNotExists_userNotFound() {
        User foundUser = testingDataAccessObject.findUser(1L);
        assertNull(foundUser);
    }

    @Test
    public void findAllUsers_usersExist_usersFound() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        User anotherTestUser = User.buildUser("Newtest", "test2@test.com", 35);
        testingDataAccessObject.saveUser(testUser);
        testingDataAccessObject.saveUser(anotherTestUser);
        List<User> users = testingDataAccessObject.findAllUsers();
        assertFalse(users.isEmpty());
        assertTrue(users.contains(testUser));
        assertTrue(users.contains(anotherTestUser));
    }
    @Test
    public void findAllUsers_usersNotExist_usersNotFound() {
        List<User> users = testingDataAccessObject.findAllUsers();
        assertTrue(users.isEmpty());
    }

    @AfterAll
    public static void shutdown() {
        if(testingSessionFactory != null) testingSessionFactory.close();
        if(postgreSQLContainer.isRunning()) postgreSQLContainer.stop();
    }
}
