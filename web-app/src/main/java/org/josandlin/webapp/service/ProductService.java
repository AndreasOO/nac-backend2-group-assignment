package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.entity.Product;

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
