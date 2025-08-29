package org.josandlin.webapp.controller;

import org.josandlin.library.dto.OrderDTO;
import org.josandlin.webapp.service.OrderService;
import org.josandlin.webapp.utils.ResultMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;

import java.util.List;


@Controller
public class OrderController {
    OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String getOrders(Model model) {
        List<OrderDTO> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin-orders";
    }

    @PostMapping("/orders/{id}/delete")
    public String deleteOrder(@PathVariable long id, Model model, RedirectAttributes redirectAttributes, Authentication authentication) {

        ResultMessage result = orderService.deleteOrderById(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("deleteConfirmation", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("deleteError", result.getMessage());
        }

        return "redirect:/orders";
    }
}
