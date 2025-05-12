/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.formatters;

import com.ghee.pojo.Users;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
/**
 *
 * @author giahu
 */
public class UserFormatter implements Formatter<Users>{

    @Override
    public String print(Users u, Locale locale) {
        return String.valueOf(u.getId());
    }

    @Override
    public Users parse(String userId, Locale locale) throws ParseException {
        Users u = new Users();
        u.setId(Long.valueOf(userId));
        return u;
    }
}
