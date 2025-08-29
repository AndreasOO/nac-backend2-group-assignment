package org.josandlin.webapp.controller;


import org.apache.el.util.Validation;
import org.josandlin.library.dto.UserCreateDTO;
import org.josandlin.library.dto.UserDTO;
import org.josandlin.webapp.service.UserService;
import org.josandlin.webapp.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.validation.ValidationErrors;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

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

    @PostMapping("/registerUser")
    public String createNewUser(RedirectAttributes redirectAttributes, UserCreateDTO userDTO, BindingResult result) {
        System.out.println("USERNAME:" + userDTO.getUsername());
        System.out.println("PASSWORD:"+ userDTO.getPassword());
        System.out.println("ROLE:" + userDTO.getRole());
        System.out.println("inside post endpoint");
        if (result.hasErrors()) {
            return "redirect:/registerUser";
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.setPassword(encoder.encode(userDTO.getPassword()));
        userService.createUser(userDTO);
        redirectAttributes.addFlashAttribute("success", "Customer registered successfully!");
        return "redirect:/loginView";
    }

    @GetMapping("/register")
    public String register(Model model) {
        System.out.println("inside getMapping endpoint");
        model.addAttribute("user", new UserCreateDTO());
        return "registerUser";
    }
}
