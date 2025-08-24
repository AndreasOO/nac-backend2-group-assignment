package org.josandlin.productfetcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.productfetcher.dto.ProductDTO;
import org.josandlin.productfetcher.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
public class FakeStoreProductFetcher {

    ProductService productService;

    @Autowired
    public FakeStoreProductFetcher(ProductService productService) {
        this.productService = productService;
    }

    public static List<ProductDTO> fetchProducts() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ProductDTO> allProducts = mapper.readValue(
                new URL("https://fakestoreapi.com/products"),
                new TypeReference<List<ProductDTO>>() {
                }
        );
        return allProducts;
    }
}
