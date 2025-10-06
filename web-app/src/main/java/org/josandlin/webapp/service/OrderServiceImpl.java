package org.josandlin.webapp.service;

import org.josandlin.webapp.dao.OrderDao;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dao.UserDao;
import org.josandlin.library.dto.OrderCreateDTO;
import org.josandlin.library.dto.OrderDTO;
import org.josandlin.library.entity.order.Order;
import org.josandlin.library.entity.product.Product;
import org.josandlin.library.entity.user.User;
import org.josandlin.library.mapper.order.OrderMapper;
import org.josandlin.webapp.utils.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDao orderDao;
    private final UserDao userDao;
    private final ProductDao productDao;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao, UserDao userDao, ProductDao productDao, OrderMapper orderMapper) {
        this.orderDao = orderDao;
        this.userDao = userDao;
        this.productDao = productDao;
        this.orderMapper = orderMapper;
    }

    @Override
    public ResultMessage createOrder(OrderCreateDTO createDTO) {

        User user = userDao.findById(createDTO.getUserId()).orElse(null);
        if (user == null) {
            return new ResultMessage(false, "Purchase failed; no customer found. Please try login in again.");
        }

        List<Product> products = createDTO.getProducts().stream().map(productDao::findById)
                .filter(Optional::isPresent).map(Optional::get).toList();
        if (products.isEmpty()){
            return new ResultMessage(false, "Purchase failed; product not found in database. Sorry.");
        }

        Order newOrder = orderMapper.createDtoToOrderEntity(createDTO, user, products);
        orderDao.save(newOrder);

        return new ResultMessage(true, "Order created successfully.");
    }


    @Override
    public List<OrderDTO> getAllOrders() {
        return orderDao.findAll()
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public OrderDTO getOrderById(long id) {
        return orderDao.findById(id).map(orderMapper::toOrderDto).orElse(null);
    }

    @Override
    public ResultMessage deleteOrderById(Long id) {

        Order order = orderDao.findById(id).orElse(null);
        if (order == null) {
            return new ResultMessage(false, "Could not delete order with id " + id + ": Order not found.");
        }

        orderDao.delete(order);
        return new ResultMessage(true, "Successfully deleted order with id " + id + ".");
    }
}
