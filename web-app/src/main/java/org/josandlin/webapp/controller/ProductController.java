package org.josandlin.webapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.josandlin.library.dto.OrderCreateDTO;
import org.josandlin.library.dto.OrderDTO;
import org.josandlin.library.dto.ProductDTO;
import org.josandlin.library.entity.product.Product;
import org.josandlin.webapp.security.UserDetailsServiceImpl;
import org.josandlin.webapp.service.OrderService;
import org.josandlin.webapp.service.ProductService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import org.josandlin.webapp.security.ConcreteUserDetails;

import java.security.Principal;
import java.util.List;

@Controller
public class ProductController {

    ProductService productService;
    UserDetailsService userDetailsService;
    OrderService orderService;

    public ProductController(ProductService productService, UserDetailsService userDetailsService, OrderService orderService) {
        this.productService = productService;
        this.userDetailsService = userDetailsService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String getRootUrl() {
        return "redirect:/products";
    }

    @GetMapping("/products")
    public String getProducts(Model model) {
        List<ProductDTO> products = productService.getProducts();
        model.addAttribute("products", products);
        return "showAllProducts";
    }

    @GetMapping("/products/{id}")
    public String getProduct(Model model, @PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/products/{productId}/buy")
    public String buyProduct(Model model, @PathVariable Long productId, Authentication authentication) {
//        ConcreteUserDetails user = (ConcreteUserDetails) userDetailsService.loadUserByUsername(authentication.name());
//        Long userId = user.getId();
//
//        OrderCreateDTO dto = new OrderCreateDTO(productId, userId);
//
//        orderService.createOrder(productId, userId);
        return "redirect:/products";
    }

}
