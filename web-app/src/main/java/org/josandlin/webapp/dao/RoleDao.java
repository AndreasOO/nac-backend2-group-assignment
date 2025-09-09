package org.josandlin.webapp.dao;

import org.josandlin.library.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);
}
