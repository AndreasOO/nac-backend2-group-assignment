package org.josandlin.webapp.dao;

import org.josandlin.library.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Long> {
    //@Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(String username);
}
