package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.webapp.dto.OrderCreateDTO;
import org.josandlin.webapp.dto.OrderDTO;
import org.josandlin.webapp.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(long id);

    @Transactional
    boolean deleteOrderById(Long id);

    @Transactional
    OrderDTO createOrder(OrderCreateDTO createDTO);
}
