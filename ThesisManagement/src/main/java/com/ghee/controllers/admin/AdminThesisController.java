/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.controllers.admin;

import com.ghee.enums.ThesisStatus;
import com.ghee.services.ThesisService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author giahu
 */
@Controller
@ControllerAdvice
@RequestMapping("/admin/thesis")
@CrossOrigin
public class AdminThesisController {
    @Autowired
    private ThesisService thesisService;
    
    @ModelAttribute
    public void commonResponse(Model model) {
        model.addAttribute("status", ThesisStatus.values());
    }
    
    @GetMapping("/")
    public String thesisView(Model model, @RequestParam Map<String, String> params) {
        model.addAttribute("theses", this.thesisService.getThese(params));
        model.addAttribute("activePage", "thesis");
        return "thesisPage/thesisPage";
    }
    
    
}
