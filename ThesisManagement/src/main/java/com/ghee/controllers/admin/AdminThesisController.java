///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.ghee.controllers.admin;
//
//import com.ghee.dto.ThesisRequest;
//import com.ghee.enums.ThesisStatus;
//import com.ghee.enums.UserRole;
//import com.ghee.pojo.Theses;
//import com.ghee.services.ThesisService;
//import com.ghee.services.UserService;
//import java.security.Principal;
//import java.util.Map;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseStatus;
//
///**
// *
// * @author giahu
// */
//@Controller
//@ControllerAdvice
//@RequestMapping("/admin/thesis")
//@CrossOrigin
//public class AdminThesisController {
//    @Autowired
//    private ThesisService thesisService;
//    
//    @Autowired
//    private UserService userService;
//    
//    @ModelAttribute
//    public void commonResponse(Model model, @RequestParam Map<String, String> params) {
//        model.addAttribute("theses", this.thesisService.getThese(params));
//        model.addAttribute("statuses", ThesisStatus.values());
//        model.addAttribute("giangViens", this.userService.getUserByUserRole(String.valueOf(UserRole.ROLE_GIANGVIEN)));
//        model.addAttribute("sinhViens", this.userService.getUserByUserRole(String.valueOf(UserRole.ROLE_SINHVIEN)));
//    }
//    
//    @GetMapping("/")
//    public String thesisView(
//            Model model, 
//            @RequestParam Map<String, String> params,
//            @RequestParam(value = "page", defaultValue = "1") int page) {
//        
//        model.addAttribute("activePage", "thesis");
//        model.addAttribute("currentPage", page);
//        return "thesisPage/thesisPage";
//    }
//    
//    @GetMapping("/{thesisId}")
//    public String thesisCreateView(Model model, @PathVariable(value = "thesisId") long id) {
//        Theses editingThesis = this.thesisService.getThesisById(id);
//        
//        model.addAttribute("thesisForm", editingThesis);
//        model.addAttribute("editId", editingThesis.getId());
//        
//        return "thesisPage/thesisDetail";
//    }
//    
//    @GetMapping("/create")
//    public String userCreateView(Model model) {
//        model.addAttribute("thesisForm", new ThesisRequest());
//        return "thesisPage/thesisDetail";
//    }
//    
//    @PostMapping("/add")
//    public String create(
//            @ModelAttribute(value = "thesisForm") ThesisRequest dto, 
//            Principal principal) {
//        String username = principal.getName();
//        this.thesisService.createThesis(dto, username);
//        
//        return "redirect:/admin/thesis/";
//    }
//    
//    @PostMapping("/edit/{thesisId}")
//    public String update(
//            @PathVariable(value = "thesisId") long id, 
//            @ModelAttribute(value = "thesisForm") ThesisRequest dto,
//            Principal principal) {
//        String username = principal.getName();
//        this.thesisService.updateThesis(id, dto, username);
//        
//        return "redirect:/admin/thesis/";
//    }
//    
//    @DeleteMapping("/{thesisId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void userDelete(Model model, 
//            @PathVariable(value="thesisId") long id, 
//            Principal principal) {
//        String username = principal.getName();
//        this.thesisService.deleteThesis(id, username);
//    }
//    
//}
