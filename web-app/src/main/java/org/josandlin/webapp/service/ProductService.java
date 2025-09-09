package org.josandlin.webapp.service;

import jakarta.transaction.Transactional;
import org.josandlin.library.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProducts();

    ProductDTO getProductById(Long id);

    @Transactional
    boolean saveAll(List<ProductDTO> products);
}
