package org.josandlin.nacbackend2groupjosandlin.service;

import jakarta.transaction.Transactional;
import org.josandlin.nacbackend2groupjosandlin.dto.OrderCreateDTO;
import org.josandlin.nacbackend2groupjosandlin.dto.OrderDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Order;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(long id);

    @Transactional
    boolean deleteOrderById(Long id);

    @Transactional
    OrderDTO createOrder(OrderCreateDTO createDTO);
}
