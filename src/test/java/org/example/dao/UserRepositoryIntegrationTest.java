package org.example.dao;

import org.example.entity.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
public class UserRepositoryIntegrationTest {
    @Container
    private static final PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:16").withDatabaseName("test-postgres");

    @Autowired
    private UserRepository userRepositoryTest;

    @DynamicPropertySource
    static void configureTestProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.driver-class-name", postgreSQLContainer::getDriverClassName);
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        dynamicPropertyRegistry.add("spring.datasource.hikari.idleTimeout", () -> "0");
        dynamicPropertyRegistry.add("spring.datasource.hikari.maxLifetime", () -> "1");
        dynamicPropertyRegistry.add("spring.datasource.hikari.connectionTimeout", () -> "1000");
    }

    @BeforeEach
    public void setUp() {
        userRepositoryTest.deleteAll();
    }

    @Test
    public void save_whenOk_transactionCompletes() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        User savedUser = userRepositoryTest.save(testUser);
        assertNotNull(savedUser);
        assertEquals(testUser, savedUser);
    }

    @Test
    public void save_whenEmailAlreadyExists_transactionFails() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        userRepositoryTest.save(testUser);
        User newTestUser = User.buildUser("Newtest", "test@test.com", 35);
        assertThrowsExactly(DataIntegrityViolationException.class, () -> {
            userRepositoryTest.save(newTestUser);
            userRepositoryTest.flush();
        });
    }

    @Test
    public void deleteById_transactionCompletes() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        User savedUser = userRepositoryTest.save(testUser);
        userRepositoryTest.deleteById(savedUser.getId());
        Optional<User> deletedUser = userRepositoryTest.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void findById_transactionCompletes() {
        User testUser = User.buildUser("Test", "test@test.com", 25);
        User savedUser = userRepositoryTest.save(testUser);
        Optional<User> foundUser = userRepositoryTest.findById(testUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(savedUser, foundUser.get());
    }

    @AfterAll
    static void cleanUp() {
        if (postgreSQLContainer != null && postgreSQLContainer.isRunning()) postgreSQLContainer.stop();
    }
}
