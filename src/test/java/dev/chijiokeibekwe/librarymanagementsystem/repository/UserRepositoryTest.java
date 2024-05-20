package dev.chijiokeibekwe.librarymanagementsystem.repository;

import dev.chijiokeibekwe.librarymanagementsystem.entity.*;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final TestUtil testUtil = new TestUtil();

    @Test
    void testFindByUsername() {
        User user = testUtil.getUser();
        user.setId(null);
        user.setRole(null);
        user.setRole(testEntityManager.find(Role.class, 1L));

        testEntityManager.persistAndFlush(user);

        Optional<User> result = userRepository.findByUsername("john.doe@library.com");

        assertThat(result.get().getUsername()).isEqualTo("john.doe@library.com");
    }

    @Test
    void testExistsByUsername() {
        User user = testUtil.getUser();
        user.setId(null);
        user.setRole(null);
        user.setRole(testEntityManager.find(Role.class, 1L));

        testEntityManager.persistAndFlush(user);

        boolean result = userRepository.existsByUsername("john.doe@library.com");

        assertThat(result).isTrue();
    }
}
