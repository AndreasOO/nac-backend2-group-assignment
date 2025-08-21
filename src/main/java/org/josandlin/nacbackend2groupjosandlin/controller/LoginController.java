package org.josandlin.nacbackend2groupjosandlin.controller;


import org.josandlin.nacbackend2groupjosandlin.dto.UserDTO;
import org.josandlin.nacbackend2groupjosandlin.service.UserServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class LoginController {

    @GetMapping("/loginView")
    public String login(Model model) {
        model.addAttribute("user", new UserDTO());
        return "loginView";
    }
}
