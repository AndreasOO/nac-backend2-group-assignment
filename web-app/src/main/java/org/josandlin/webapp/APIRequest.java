package org.josandlin.webapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.webapp.dao.ProductDao;
import org.josandlin.webapp.dto.ProductDTO;
import org.josandlin.webapp.entity.Product;
import org.josandlin.webapp.service.ProductService;
import org.josandlin.webapp.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

@Component
@Order(1)
public class APIRequest implements CommandLineRunner {

    ProductService productService;

    public APIRequest(ProductService productService) {
        this.productService = productService;
    }

//    @Override
//    public void run(String... args) throws Exception {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        List<Product> allProducts = mapper.readValue(
//                new URL("https://fakestoreapi.com/products"),
//                new TypeReference<List<Product>>() {}
//        );
//        productDao.saveAll(allProducts);
//    }

    @Override
    public void run(String... args) throws Exception {
        List<ProductDTO> allProducts = FakeStoreProductFetcher.fetchProducts();
        productService.saveAll(allProducts);
    }
}
