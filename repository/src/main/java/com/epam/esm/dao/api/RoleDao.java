package com.epam.esm.dao.api;

import com.epam.esm.entity.Role;

public interface RoleDao {

    Role findByRoleName(String roleName);
}
