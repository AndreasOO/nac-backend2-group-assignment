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
        System.out.println("STARTING COMMAND LINE RUNNER");
        List<ProductDTO> allProducts = FakeStoreProductFetcher.fetchProducts();
        productService.saveAll(allProducts);
        System.out.println("STOPPING COMMAND LINE RUNNER");
    }
}
