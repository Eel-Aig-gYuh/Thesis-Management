/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.admin;

import com.ghee.pojo.Users;
import com.ghee.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author giahu
 */
@Controller
@RequestMapping("/admin")
@CrossOrigin
public class AdminUserController {
    @Autowired
    private UserService userService;
    
    @GetMapping("/users")
    public String userView(Model model) {
        model.addAttribute("users", new Users());
        return "userPage/userDetail";
    }
    
    @GetMapping("/users/{userId}")
    public String updateUser(Model model, @PathVariable(value="userId") int id) {
        model.addAttribute("users", this.userService.getUserById(id));
        
        return "userPage/userDetail";
    }
    
    @PostMapping("/add")
    public String createUser(@ModelAttribute(value="users") Users u) {
        this.userService.addOrUpdateUser(u);
        
        return "redirect:/admin";
    }
}
