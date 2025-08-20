package org.josandlin.nacbackend2groupjosandlin.dao;

import org.josandlin.nacbackend2groupjosandlin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {
}
