package org.josandlin.library.dto;

import java.util.ArrayList;
import java.util.List;

public class OrderDTO {

    Long id;

    UserSummaryDTO user;

    List<ProductSummaryDTO> products = new ArrayList<>();

    public OrderDTO() {
    }

    public OrderDTO(Long id, UserSummaryDTO user, List<ProductSummaryDTO> products) {
        this.id = id;
        this.user = user;
        this.products = products;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProductSummaryDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductSummaryDTO> products) {
        this.products = products;
    }

    public UserSummaryDTO getUser() {
        return user;
    }

    public void setUser(UserSummaryDTO user) {
        this.user = user;
    }
}
