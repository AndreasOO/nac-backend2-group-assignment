package org.josandlin.nacbackend2groupjosandlin.service;

import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;

import java.util.List;

public interface ProductService {
/*
    @Transactional
    ProductDTO addProductToOrder(Long productId, Long orderId); <--- ?????????

*/
    List<ProductDTO> getProducts();

}
