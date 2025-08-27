package org.josandlin.productfetcher;

import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.fetcher.Fetcher;
import org.josandlin.productfetcher.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder {

    @Value("${product.fetcher.url}")
    private String targetUrl;

    private final ProductService service;
    private final Fetcher productFetcher;

    public DataSeeder(ProductService service, Fetcher productFetcher) {
        this.service = service;
        this.productFetcher = productFetcher;

    }

    public void updateDatabase() throws Exception {
        List<ProductDTO> fetchedProducts = productFetcher.fetchProducts(targetUrl);
        service.saveAll(fetchedProducts);
    }
}
