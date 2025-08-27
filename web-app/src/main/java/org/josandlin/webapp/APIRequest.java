package org.josandlin.webapp;

import org.josandlin.library.dto.ProductDTO;
import org.josandlin.webapp.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.josandlin.library.fetcher.Fetcher;


import java.util.List;

@Component
@Order(1)
public class APIRequest implements CommandLineRunner {

    @Value("${product.fetcher.url}")
    private String targetUrl;

    private final ProductService productService;
    private final Fetcher fetcher;

    public APIRequest(ProductService productService, Fetcher fetcher) {
        this.productService = productService;
        this.fetcher = fetcher;
    }

    @Override
    public void run(String... args) throws Exception {
        List<ProductDTO> allProducts = fetcher.fetchProducts(targetUrl);
        productService.saveAll(allProducts);
    }
}
