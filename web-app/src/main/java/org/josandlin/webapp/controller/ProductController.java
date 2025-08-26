package org.josandlin.webapp.controller;

import org.springframework.security.core.Authentication;
import org.josandlin.webapp.security.ConcreteUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
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

        //Anledningen till att det här inte funkade förut var att vi hade fel import
        //Knäppt att typa ett Con.Us.Det.-objekt till Con.Us.Det. men det måste tydligen vara så ...
        ConcreteUserDetails user = (ConcreteUserDetails) authentication.getPrincipal();
        Long userId = user.getId();

        //Vi har ju skrivit objekten för att kunna ha en lista med produkter, så därför gjorde jag ju
        //logiken i DTO så att den enda produkten ska läggas till i en lista ...
        //Det finns ju ingen lista från början, så därför får konstruktorn ta in null som lista.
        //Men vi kan ju skriva en ny konstruktor som inte behöver ta in någon lista också antar jag.
        //NU tar jag lite paus.
        // men det här funkar väl som du gjort nu? Jaa det tror jag men har inte kört ... :D
        OrderCreateDTO dto = new OrderCreateDTO(null, userId);
        dto.addProduct(productId);

        orderService.createOrder(dto);

        return "redirect:/products";
    }

}
