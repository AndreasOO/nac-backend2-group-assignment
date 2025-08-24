package org.josandlin.productfetcher.controller;

import org.josandlin.productfetcher.DataSeeder;
import org.josandlin.library.dto.ProductDTO;
import org.josandlin.productfetcher.service.ProductService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    DataSeeder seeder;
    ProductService productService;

    public ProductController(DataSeeder seeder, ProductService productService) {
        this.seeder = seeder;
        this.productService = productService;
    }

    @CrossOrigin
    @GetMapping("/products")
    public List<ProductDTO> getProducts() throws Exception {
        seeder.updateDatabase();
        return productService.getProducts();
    }
}
