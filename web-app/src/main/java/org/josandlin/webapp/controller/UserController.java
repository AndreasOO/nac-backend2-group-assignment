package org.josandlin.webapp.controller;


import org.apache.el.util.Validation;
import org.josandlin.library.dto.UserDTO;
import org.josandlin.webapp.service.UserService;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UserController {

    UserService userService;

    @GetMapping("/loginView")
    public String login(Model model) {
        model.addAttribute("user", new UserDTO());
        return "loginView";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String admin(Model model) {
        model.addAttribute("user", new UserDTO());
        return "admin-orders";
    }

    @PostMapping("/register")
    public String createUser(RedirectAttributes redirectAttributes, UserDTO userDTO, BindingResult result) {
        /// Kanske bra att hämta servicemetoden här????
        System.out.println("inside reg post endpoint");
        if (result.hasErrors()) {
            return "redirect:/registerUser";
        }
            userService.createUser(userDTO);
            redirectAttributes.addFlashAttribute("success", "Customer registered successfully!");
            return "redirect:/loginView";
    }
    @GetMapping("/register")
    public String register(Model model) {
        System.out.println("inside reg get endpoint");
        model.addAttribute("user", new UserDTO());
        return "registerUser";
    }
}
