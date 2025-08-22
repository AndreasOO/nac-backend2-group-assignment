package org.josandlin.nacbackend2groupjosandlin.service;

import jakarta.transaction.Transactional;
import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;

import java.util.List;

public interface ProductService {
/*
    @Transactional
    ProductDTO addProductToOrder(Long productId, Long orderId); <--- ?????????

*/
    List<ProductDTO> getProducts();

    ProductDTO getProductById(Long id);

    @Transactional
    boolean saveAll(List<ProductDTO> products);
}
