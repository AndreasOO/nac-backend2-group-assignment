package dao;

import org.josandlin.nacbackend2groupjosandlin.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleDao extends JpaRepository<Role, Long> {
}
