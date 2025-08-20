package org.josandlin.nacbackend2groupjosandlin.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderCreateDTO {
    Long id;

    Long userId;

    List<Long> productIds = new ArrayList<>();

    public OrderCreateDTO(){}

    public OrderCreateDTO(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public OrderCreateDTO(Long id, Long userId, List<Long> productIds) {
        this.id = id;
        this.userId = userId;
        this.productIds = productIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getProducts() {
        return productIds;
    }

    public void setProducts(List<Long> products) {
        this.productIds = products;
    }

    public void addProduct(Long productId) {
        productIds.add(productId);
    }

    public void removeProduct(Long productId) {
        productIds.remove(productId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}