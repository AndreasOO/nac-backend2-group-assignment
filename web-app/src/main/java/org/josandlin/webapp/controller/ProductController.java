package org.josandlin.nacbackend2groupjosandlin.controller;

import org.josandlin.nacbackend2groupjosandlin.dto.ProductDTO;
import org.josandlin.nacbackend2groupjosandlin.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
@Controller
public class ProductController {

    ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<ProductDTO> products = productService.getProducts();
        model.addAttribute("products", products);
        return "showAllProducts";
    }

    @GetMapping("/products/{id}")
    public String getProduct(Model model, @PathVariable Long id){
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @PostMapping("/products/{id}/buy")
    public String buyProduct(Model model, @PathVariable Long id){
        return "redirect:/products";
    }

}
