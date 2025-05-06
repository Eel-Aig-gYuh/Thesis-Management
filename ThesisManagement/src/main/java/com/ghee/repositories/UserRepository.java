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
    public List<Users> getUsers(Map<String, String> params); 
    public Users getUserById(int id);
    public Users addOrUpdateUser(Users u);
    public void deleteUser(int id);
}
