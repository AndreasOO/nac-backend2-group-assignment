package org.josandlin.webapp;

import org.josandlin.library.dto.ProductDTO;
import org.josandlin.webapp.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Order(1)
public class APIRequest implements CommandLineRunner {

    private final ProductService productService;
    private final FakeStoreProductFetcher fetcher;

    public APIRequest(ProductService productService, FakeStoreProductFetcher fetcher) {
        this.productService = productService;
        this.fetcher = fetcher;
    }

    @Override
    public void run(String... args) throws Exception {
        List<ProductDTO> allProducts = fetcher.fetchProducts();
        productService.saveAll(allProducts);
    }
}
