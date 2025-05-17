/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

import java.util.Date;

/**
 * Mục đích trả về thông tin người dùng trong phản hồi.
 * @author giahu
 */
public class ThesisUserDTO {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private Date registeredAt;

    public ThesisUserDTO(Long id, String firstname, String lastname, String email, Date registeredAt) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.registeredAt = registeredAt;
    }
    
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the firstname
     */
    public String getFirstname() {
        return firstname;
    }

    /**
     * @param firstname the firstname to set
     */
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    /**
     * @return the lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     * @param lastname the lastname to set
     */
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the registeredAt
     */
    public Date getRegisteredAt() {
        return registeredAt;
    }

    /**
     * @param registeredAt the registeredAt to set
     */
    public void setRegisteredAt(Date registeredAt) {
        this.registeredAt = registeredAt;
    }
    
    
}
