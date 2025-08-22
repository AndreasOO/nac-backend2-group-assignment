package org.josandlin.nacbackend2groupjosandlin.dao;

import org.josandlin.nacbackend2groupjosandlin.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDao extends JpaRepository<Order, Long> {
}
