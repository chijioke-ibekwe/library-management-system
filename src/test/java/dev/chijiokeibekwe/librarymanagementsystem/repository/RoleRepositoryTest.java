package dev.chijiokeibekwe.librarymanagementsystem.repository;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Role;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void testFindByName() {

        Optional<Role> result = roleRepository.findByName(RoleName.ROLE_USER);

        assertThat(result.get().getName()).isEqualTo(RoleName.ROLE_USER);
    }
}
