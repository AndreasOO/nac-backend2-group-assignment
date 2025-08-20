package org.josandlin.nacbackend2groupjosandlin.mapper;

import org.josandlin.nacbackend2groupjosandlin.dto.OrderCreateDTO;
import org.josandlin.nacbackend2groupjosandlin.dto.OrderDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Order;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;
import org.josandlin.nacbackend2groupjosandlin.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    private final ProductMapper productMapper;
    private final UserMapper userMapper;

    @Autowired
    public OrderMapper(ProductMapper productMapper, UserMapper userMapper) {
        this.productMapper = productMapper;
        this.userMapper = userMapper;
    }

    public OrderDTO toOrderDto(Order entity) {
        if (entity == null) {
            return null;
        }

        return new OrderDTO(entity.getId(), userMapper.toUserSummaryDto(entity.getUser()),
                entity.getProducts().stream().map(productMapper::toProductSummaryDto).toList());
    }

    public Order createDtoToOrderEntity(OrderCreateDTO dto, User user, List<Product> products) {
        if (dto == null) {
            return null;
        }

        return new Order(dto.getId(), user, products);
    }
}
