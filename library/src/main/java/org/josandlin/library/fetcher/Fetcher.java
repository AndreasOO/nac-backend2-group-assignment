package org.josandlin.library.fetcher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.library.dto.ProductDTO;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
public class Fetcher {


    public List<ProductDTO> fetchProducts(String targetUrl) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        List<ProductDTO> allProducts = mapper.readValue(
                new URL(targetUrl),
                new TypeReference<>() {
                }
        );
        return allProducts;
    }
}
