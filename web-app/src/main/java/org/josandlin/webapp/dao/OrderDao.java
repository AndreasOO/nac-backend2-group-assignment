package org.josandlin.webapp.dao;

import org.josandlin.library.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {
}
