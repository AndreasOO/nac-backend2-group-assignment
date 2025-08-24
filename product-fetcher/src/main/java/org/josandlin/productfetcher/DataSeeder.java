package org.josandlin.productfetcher;

import org.josandlin.library.dto.ProductDTO;
import org.josandlin.productfetcher.service.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder {

    private final ProductService service;

    public DataSeeder(ProductService service) {
        this.service = service;
    }

    public void updateDatabase() throws Exception {
        List<ProductDTO> fetchedProducts = FakeStoreProductFetcher.fetchProducts();
        service.saveAll(fetchedProducts);
    }
}
