package service;

import dao.OrderDao;
import jakarta.transaction.Transactional;
import org.josandlin.nacbackend2groupjosandlin.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class OrderServiceImpl {
    public OrderDao orderDao;

    @Autowired
    public OrderServiceImpl(OrderDao orderDao){
        this.orderDao = orderDao;
    }
/*
    @Override
    public List<OrderDTO> getAllOrders(){
        return orderDao.findAll()
                .stream()
                .map(OrderMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public boolean deleteOrderById(OrderDTO orderDTO) {

    }


 */


}
