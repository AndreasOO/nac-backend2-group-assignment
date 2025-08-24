package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.library.dto.OrderCreateDTO;
import org.josandlin.library.dto.OrderDTO;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(long id);

    @Transactional
    boolean deleteOrderById(Long id);

    @Transactional
    OrderDTO createOrder(OrderCreateDTO createDTO);
}
