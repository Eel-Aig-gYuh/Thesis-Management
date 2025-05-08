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
    public Users getUserById(long id);
    public Users createUser(Users u);
    public Users updateUser(Users u);

    public Users getUserByUsername(String username);
    public boolean authenticated(String username, String password);
}
