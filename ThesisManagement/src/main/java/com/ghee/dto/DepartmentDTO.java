/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ghee.dto;

/**
 *
 * @author giahu
 */
public class DepartmentDTO {
    public static class DepartmentResponse {
        private Long id;
        private String name;

        public DepartmentResponse(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public DepartmentResponse() {
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
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        
        
    }
}
