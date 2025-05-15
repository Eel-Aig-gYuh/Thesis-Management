/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.repositories;

import com.ghee.pojo.Users;
import java.util.List;
import java.util.Map;

/**
 *
 * @author giahu
 */
public interface UserRepository {
    Users getUserById(long id);
    Users getUserByUsername(String username);
    List<Users> getUsers(Map<String, String> params); 
    List<Users> getUserByUserRole(String userRole);
    
    Users createUser(Users u);
    Users createOrUpdate(Users u);
    Users updateUser(Users u);
    
    void deleteUser(long id);

    boolean authenticated(String username, String password);
}
