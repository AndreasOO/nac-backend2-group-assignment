package org.josandlin.nacbackend2groupjosandlin.controller;


import org.josandlin.nacbackend2groupjosandlin.dto.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class UserController {

    @GetMapping("/loginView")
    public String login(Model model) {
        model.addAttribute("user", new UserDTO());
        return "loginView";
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("user", new UserDTO());
        return "admin-orders";
    }
}
