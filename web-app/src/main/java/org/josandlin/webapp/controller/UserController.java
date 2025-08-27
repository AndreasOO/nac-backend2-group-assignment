package org.josandlin.webapp.controller;


import org.josandlin.library.dto.UserDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
public class UserController {

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
}
