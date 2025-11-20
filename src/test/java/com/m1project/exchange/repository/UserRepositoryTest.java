
package com.m1project.exchange.repository;

import com.m1project.exchange.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveAndFindUser() {
        // Arrange
        User user = new User("test@example.com", "Test User", "Paris");
        user.setBio("Une bio de test");

        // Act
        User savedUser = entityManager.persistAndFlush(user);
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    void testFindByEmail() {
        // Arrange
        User user = new User("unique@example.com", "Unique User", "Lyon");
        entityManager.persistAndFlush(user);

        // Act
        Optional<User> foundUser = userRepository.findByEmail("unique@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Unique User", foundUser.get().getName());
    }

    @Test
    void testFindByEmail_NotFound() {
        // Act
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(foundUser.isPresent());
    }

    @Test
    void testExistsByEmail() {
        // Arrange
        User user = new User("exists@example.com", "Existing User", "Marseille");
        entityManager.persistAndFlush(user);

        // Act
        boolean exists = userRepository.existsByEmail("exists@example.com");
        boolean notExists = userRepository.existsByEmail("notexists@example.com");

        // Assert
        assertTrue(exists);
        assertFalse(notExists);
    }

    @Test
    void testDeleteUser() {
        // Arrange
        User user = new User("delete@example.com", "Delete User", "Toulouse");
        User savedUser = entityManager.persistAndFlush(user);
        Long userId = savedUser.getId();

        // Act
        userRepository.deleteById(userId);
        Optional<User> deletedUser = userRepository.findById(userId);

        // Assert
        assertFalse(deletedUser.isPresent());
    }

    @Test
    void testUpdateUser() {
        // Arrange
        User user = new User("update@example.com", "Original Name", "Nice");
        User savedUser = entityManager.persistAndFlush(user);

        // Act
        savedUser.setName("Updated Name");
        savedUser.setLocation("Bordeaux");
        User updatedUser = userRepository.save(savedUser);

        // Assert
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("Bordeaux", updatedUser.getLocation());
        assertEquals("update@example.com", updatedUser.getEmail()); // Email ne change pas
    }

    @Test
    void testFindAll() {
        // Arrange
        entityManager.persistAndFlush(new User("user1@example.com", "User 1", "Paris"));
        entityManager.persistAndFlush(new User("user2@example.com", "User 2", "Lyon"));
        entityManager.persistAndFlush(new User("user3@example.com", "User 3", "Marseille"));

        // Act
        var users = userRepository.findAll();

        // Assert
        assertTrue(users.size() >= 3);
    }
}