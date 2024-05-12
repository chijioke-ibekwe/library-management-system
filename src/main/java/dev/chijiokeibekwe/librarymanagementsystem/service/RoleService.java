package dev.chijiokeibekwe.librarymanagementsystem.service;

import dev.chijiokeibekwe.librarymanagementsystem.entity.Role;
import dev.chijiokeibekwe.librarymanagementsystem.enums.RoleName;

public interface RoleService {

    Role getRoleByUserType(RoleName roleName);
}
