package org.josandlin.webapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.webapp.dto.ProductDTO;

import java.net.URL;
import java.util.List;


public class FakeStoreProductFetcher {
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
