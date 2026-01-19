package com.example.mindStreamApplication.Repository;

import com.example.mindStreamApplication.Domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("UserRepository Tests")
class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@email.com", "password123", "Test User");
        entityManager.persist(testUser);
        entityManager.flush();
    }

    @Nested
    @DisplayName("FindByUsername Tests")
    class FindByUsernameTests {

        @Test
        @DisplayName("Should find user by username when exists")
        void findByUsername_WhenUserExists_ShouldReturnUser() {
            User found = userRepository.findByUsername("testuser");
            
            assertNotNull(found);
            assertEquals("testuser", found.getUsername());
            assertEquals("test@email.com", found.getEmail());
        }

        @Test
        @DisplayName("Should return null when username not exists")
        void findByUsername_WhenUserNotExists_ShouldReturnNull() {
            User found = userRepository.findByUsername("nonexistent");
            
            assertNull(found);
        }

        @Test
        @DisplayName("Should be case sensitive for username")
        void findByUsername_ShouldBeCaseSensitive() {
            User found = userRepository.findByUsername("TESTUSER");
            
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("FindByEmail Tests")
    class FindByEmailTests {

        @Test
        @DisplayName("Should find user by email when exists")
        void findByEmail_WhenUserExists_ShouldReturnUser() {
            User found = userRepository.findByEmail("test@email.com");
            
            assertNotNull(found);
            assertEquals("testuser", found.getUsername());
            assertEquals("test@email.com", found.getEmail());
        }

        @Test
        @DisplayName("Should return null when email not exists")
        void findByEmail_WhenEmailNotExists_ShouldReturnNull() {
            User found = userRepository.findByEmail("nonexistent@email.com");
            
            assertNull(found);
        }
    }

    @Nested
    @DisplayName("ExistsByUsername Tests")
    class ExistsByUsernameTests {

        @Test
        @DisplayName("Should return true when username exists")
        void existsByUsername_WhenUserExists_ShouldReturnTrue() {
            boolean exists = userRepository.existsByUsername("testuser");
            
            assertTrue(exists);
        }

        @Test
        @DisplayName("Should return false when username not exists")
        void existsByUsername_WhenUserNotExists_ShouldReturnFalse() {
            boolean exists = userRepository.existsByUsername("nonexistent");
            
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("ExistsByEmail Tests")
    class ExistsByEmailTests {

        @Test
        @DisplayName("Should return true when email exists")
        void existsByEmail_WhenEmailExists_ShouldReturnTrue() {
            boolean exists = userRepository.existsByEmail("test@email.com");
            
            assertTrue(exists);
        }

        @Test
        @DisplayName("Should return false when email not exists")
        void existsByEmail_WhenEmailNotExists_ShouldReturnFalse() {
            boolean exists = userRepository.existsByEmail("nonexistent@email.com");
            
            assertFalse(exists);
        }
    }

    @Nested
    @DisplayName("CRUD Operations Tests")
    class CrudOperationsTests {

        @Test
        @DisplayName("Should save and retrieve user")
        void save_ShouldPersistUser() {
            User newUser = new User("newuser", "new@email.com", "password", "New User");
            
            User saved = userRepository.save(newUser);
            
            assertNotNull(saved.getId());
            assertEquals("newuser", saved.getUsername());
        }

        @Test
        @DisplayName("Should find user by id")
        void findById_ShouldReturnUser() {
            Optional<User> found = userRepository.findById(testUser.getId());
            
            assertTrue(found.isPresent());
            assertEquals("testuser", found.get().getUsername());
        }

        @Test
        @DisplayName("Should delete user")
        void delete_ShouldRemoveUser() {
            Long userId = testUser.getId();
            
            userRepository.delete(testUser);
            entityManager.flush();
            
            Optional<User> found = userRepository.findById(userId);
            assertFalse(found.isPresent());
        }

        @Test
        @DisplayName("Should update user")
        void update_ShouldModifyUser() {
            testUser.setFullName("Updated Name");
            
            userRepository.save(testUser);
            entityManager.flush();
            entityManager.clear();
            
            User updated = userRepository.findByUsername("testuser");
            assertEquals("Updated Name", updated.getFullName());
        }

        @Test
        @DisplayName("Should count users")
        void count_ShouldReturnCorrectCount() {
            long count = userRepository.count();
            
            assertTrue(count >= 1);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty username search")
        void findByUsername_WithEmptyString_ShouldReturnNull() {
            User found = userRepository.findByUsername("");
            
            assertNull(found);
        }

        @Test
        @DisplayName("Should handle special characters in username")
        void shouldHandleSpecialCharactersInUsername() {
            User specialUser = new User("user_special-123", "special@email.com", "pass", "Special");
            entityManager.persist(specialUser);
            entityManager.flush();
            
            User found = userRepository.findByUsername("user_special-123");
            assertNotNull(found);
        }
    }
}
