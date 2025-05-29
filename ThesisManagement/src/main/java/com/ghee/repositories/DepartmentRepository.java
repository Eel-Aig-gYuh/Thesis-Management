/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Departments;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface DepartmentRepository {
    Map<String, Object> getDepartments ();
    
    Departments findById(long id);
}
