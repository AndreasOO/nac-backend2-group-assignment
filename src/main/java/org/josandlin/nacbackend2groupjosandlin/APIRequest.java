package org.josandlin.nacbackend2groupjosandlin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.nacbackend2groupjosandlin.dao.ProductDao;
import org.josandlin.nacbackend2groupjosandlin.entity.Product;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URL;
import java.util.List;

@Component
public class APIRequest implements CommandLineRunner{

    ProductDao productDao;

    public APIRequest(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<Product> allProducts = mapper.readValue(
                new URL("https://fakestoreapi.com/products"),
                new TypeReference<List<Product>>() {}
        );
        productDao.saveAll(allProducts);
    }
}
