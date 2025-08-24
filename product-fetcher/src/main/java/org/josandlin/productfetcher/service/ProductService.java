package org.josandlin.productfetcher.service;

import jakarta.transaction.Transactional;
import org.josandlin.productfetcher.dto.ProductDTO;

import java.util.List;

public interface ProductService {

    List<ProductDTO> getProducts();

    ProductDTO getProductById(Long id);

    @Transactional
    boolean saveAll(List<ProductDTO> products);
}
