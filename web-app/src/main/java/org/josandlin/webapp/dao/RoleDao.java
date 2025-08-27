package org.josandlin.webapp.dao;

import org.josandlin.library.entity.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role, Long> {

    public Optional<Role> findRolesByName(String roleName);
}
