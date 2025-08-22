package org.josandlin.webapp.dao;

import org.josandlin.library.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
}
