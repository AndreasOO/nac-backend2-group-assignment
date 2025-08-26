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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean createOrder(OrderCreateDTO createDTO) {
        User user = userDao.findById(createDTO.getUserId()).orElse(null);
        if (user == null) return false;

        List<Product> products = createDTO.getProducts().stream().map(productDao::findById)
                .filter(Optional::isPresent).map(Optional::get).toList();
        if (products.isEmpty()) return false;

        Order newOrder = orderMapper.createDtoToOrderEntity(createDTO, user, products);

        orderDao.save(newOrder);

        return true;
    }


    @Override
    public List<OrderDTO> getAllOrders() {
        return orderDao.findAll()
                .stream()
                .map(orderMapper::toOrderDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(long id) {
        return null;
    }

    @Override
    public boolean deleteOrderById(Long id) {
        return false;
    }
}
