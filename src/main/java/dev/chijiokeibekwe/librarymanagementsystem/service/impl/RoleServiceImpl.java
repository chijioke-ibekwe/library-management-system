package dev.chijiokeibekwe.librarymanagementsystem.service.impl;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Role;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;
import dev.chijiokeibekwe.librarymanagementsystem.repository.RoleRepository;
import dev.chijiokeibekwe.librarymanagementsystem.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role getRoleByUserType(RoleName roleName) {

        return roleRepository.findByName(roleName).orElseThrow(() -> new
                EntityNotFoundException("Role not found"));
    }
}
