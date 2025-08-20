package org.josandlin.nacbackend2groupjosandlin.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    Long id;

    UserDTO user;

    List<ProductDTO> products = new ArrayList<>();

    public OrderDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
