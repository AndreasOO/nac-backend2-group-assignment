package org.josandlin.nacbackend2groupjosandlin.service;

import org.josandlin.nacbackend2groupjosandlin.dao.OrderDao;
import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
import org.josandlin.nacbackend2groupjosandlin.dao.UserDao;
import org.josandlin.nacbackend2groupjosandlin.dto.OrderCreateDTO;
import org.josandlin.nacbackend2groupjosandlin.dto.OrderDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Order;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;
import org.josandlin.nacbackend2groupjosandlin.entity.User;
import org.josandlin.nacbackend2groupjosandlin.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
    public OrderDTO createOrder(OrderCreateDTO createDTO) {
        User user = userDao.findById(createDTO.getUserId()).orElse(null);

        List<Product> products = createDTO.getProducts().stream().map(productDao::findById)
                .filter(Optional::isPresent).map(Optional::get).toList();

        Order newOrder = orderMapper.createDtoToOrderEntity(createDTO, user, products);

        orderDao.save(newOrder);

        return orderMapper.toOrderDto(newOrder);
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
