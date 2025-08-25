package org.josandlin.webapp.dao;

import org.josandlin.library.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Long> {

}
