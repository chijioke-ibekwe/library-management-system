package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.dto.request.UserRegistrationRequest;
import dev.chijiokeibekwe.librarymanagementsystem.dto.response.UserResponse;
import dev.chijiokeibekwe.librarymanagementsystem.entity.Role;
import dev.chijiokeibekwe.librarymanagementsystem.entity.User;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import dev.chijiokeibekwe.librarymanagementsystem.enums.UserType;
import dev.chijiokeibekwe.librarymanagementsystem.mapper.Mapper;
import dev.chijiokeibekwe.librarymanagementsystem.repository.UserRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.RoleService;
import dev.chijiokeibekwe.librarymanagementsystem.service.UserService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserResponse registerUser(UserRegistrationRequest userRegistrationRequest){

        if(userRepository.existsByUsername(userRegistrationRequest.email()))
            throw new EntityExistsException(String.format("Email %s already exists", userRegistrationRequest.email()));

        Role role = roleService.getRoleByName(RoleName.ROLE_USER);

        User user = User.builder()
                .firstName(userRegistrationRequest.firstName())
                .lastName(userRegistrationRequest.lastName())
                .username(userRegistrationRequest.email())
                .password(passwordEncoder.encode(userRegistrationRequest.password()))
                .phoneNumber(userRegistrationRequest.phoneNumber())
                .role(role)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        return Mapper.toUserResponse(userRepository.save(user));
    }

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable){

        return userRepository.findAll(pageable).map(Mapper::toUserResponse);
    }

    @Override
    public UserResponse getUser(Long id) {
        return userRepository.findById(id).map(Mapper::toUserResponse)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}