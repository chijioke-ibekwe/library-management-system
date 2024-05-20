package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.dto.response.UserResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.User;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import dev.chijiokeibekwe.librarymanagementsystem.repository.UserRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.impl.UserServiceImpl;
import dev.chijiokeibekwe.librarymanagementsystem.util.TestUtil;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {UserServiceImpl.class})
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleService roleService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final TestUtil testUtil = new TestUtil();

    @Test
    public void testRegisterUser() {
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);

        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(roleService.getRoleByName(any())).thenReturn(testUtil.getUserRole());
        when(passwordEncoder.encode(any())).thenReturn("abcde");
        when(userRepository.save(any(User.class))).thenReturn(testUtil.getUser());

        UserResponse response = userService.registerUser(testUtil.getUserRegistrationRequest());

        verify(userRepository, times(1)).save(userArgumentCaptor.capture());

        assertThat(userArgumentCaptor.getValue().getFirstName()).isEqualTo("John");
        assertThat(userArgumentCaptor.getValue().getLastName()).isEqualTo("Doe");
        assertThat(userArgumentCaptor.getValue().getUsername()).isEqualTo("john.doe@starter.com");
        assertThat(userArgumentCaptor.getValue().getPassword()).isEqualTo("abcde");

        assertThat(response.getRole().getId()).isEqualTo(1L);
        assertThat(response.getRole().getName()).isEqualTo(RoleName.ROLE_USER);
        assertThat(response.getRole().getPermissions()).contains("users:read");
    }

    @Test
    public void testRegisterUser_whenUsernameAlreadyExists() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(testUtil.getUserRegistrationRequest()))
                .isInstanceOf(EntityExistsException.class)
                .hasMessage("Email john.doe@starter.com already exists");
    }

    @Test
    public void testGetAllUsers() {
        when(userRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(List.of(testUtil.getUser())));

        Page<UserResponse> response = userService.getAllUsers(Pageable.ofSize(10));

        assertThat(response.getContent().get(0).getId()).isEqualTo(1L);
        assertThat(response.getContent().get(0).getFirstName()).isEqualTo("John");
        assertThat(response.getContent().get(0).getLastName()).isEqualTo("Doe");
        assertThat(response.getContent().get(0).getUsername()).isEqualTo("john.doe@starter.com");
        assertThat(response.getContent().get(0).getRole().getName()).isEqualTo(RoleName.ROLE_USER);
        assertThat(response.getContent().get(0).getRole().getPermissions()).contains("users:read");
    }

    @Test
    public void testGetUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.ofNullable(testUtil.getUser()));

        UserResponse response = userService.getUser(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getUsername()).isEqualTo("john.doe@starter.com");
        assertThat(response.getRole().getName()).isEqualTo(RoleName.ROLE_USER);
        assertThat(response.getRole().getPermissions()).contains("users:read");
    }

    @Test
    public void testGetUser_whenUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUser(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("User not found");
    }
}