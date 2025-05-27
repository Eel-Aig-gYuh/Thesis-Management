/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.services.impl;

import com.ghee.dto.DepartmentDTO;
import com.ghee.pojo.Departments;
import com.ghee.pojo.Theses;
import com.ghee.repositories.DepartmentRepository;
import com.ghee.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author giahu
 */
@Service
@Transactional
public class DepartmentServiceImpl implements DepartmentService{

    @Autowired
    private DepartmentRepository departRepo;
    
    @Override
    public Map<String, Object> getDepartments() {
        Map<String, Object> result = this.departRepo.getDepartments();
        List<Departments> departments = (List<Departments>) result.get("departments");
        result.put("departments", departments.stream().map(this::mapToResponse).collect(Collectors.toList()));

        return result;
    }
    
    private DepartmentDTO.DepartmentResponse mapToResponse (Departments d) {
        DepartmentDTO.DepartmentResponse response = new DepartmentDTO.DepartmentResponse();
        response.setId(d.getId());
        response.setName(d.getName());
        
        return response;
    }
    
}
