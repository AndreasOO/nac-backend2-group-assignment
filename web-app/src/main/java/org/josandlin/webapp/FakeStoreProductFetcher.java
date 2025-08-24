package org.josandlin.webapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.josandlin.library.dto.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;


@Component
public class FakeStoreProductFetcher {

    @Value("${pf.hostname}")
    private String hostname;

    @Value("${pf.port}")
    private int port;

    public List<ProductDTO> fetchProducts() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(
                new URL("http://" + hostname + ":" + port + "/api/products"),
                new TypeReference<List<ProductDTO>>() {
                }
        );
    }


}
