package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.library.dto.OrderCreateDTO;
import org.josandlin.library.dto.OrderDTO;
import org.josandlin.webapp.utils.ResultMessage;

import java.util.List;

public interface OrderService {

    List<OrderDTO> getAllOrders();

    OrderDTO getOrderById(long id);

    @Transactional
    ResultMessage deleteOrderById(Long id);

    @Transactional
    ResultMessage createOrder(OrderCreateDTO createDTO);
}
