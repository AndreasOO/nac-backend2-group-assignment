package org.josandlin.webapp.dao;

import org.josandlin.webapp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
}
