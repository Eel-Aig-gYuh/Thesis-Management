/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Theses;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface ThesisRepository {
    Theses getThesisById(long id);
    List<Theses> getTheses(Map<String, String> params);
    
    Theses createThesis(Theses theses);
    Theses updateThesis(Theses theses);
    void deleteThesis(long id);
    
    boolean existsByTitle(String title);
}
