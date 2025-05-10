/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.admin;

import com.ghee.enums.UserMajor;
import com.ghee.enums.UserRole;
import com.ghee.pojo.Users;
import com.ghee.services.UserService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author giahu
 */
@Controller
@ControllerAdvice
@RequestMapping("/admin/users")
@CrossOrigin
public class AdminUserController {
    @Autowired
    private UserService userService;
    
    @ModelAttribute
    public void commonResponse(Model model) {
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("majors", UserMajor.values());
    }
    
    @GetMapping("/")
    public String userView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("users", this.userService.getUsers(params));
        model.addAttribute("activePage", "users");
        return "userPage/userPage";
    }
    
    @GetMapping("/create")
    public String userCreateView(Model model) {
        return "userPage/userDetail";
    }
}
