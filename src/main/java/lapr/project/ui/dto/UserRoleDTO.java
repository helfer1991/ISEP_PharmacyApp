/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui.dto;

import lapr.project.model.UserRole;

import java.util.Objects;

/**
 * This class represents a DTO to UserRole class in model in order to show the object UserRole in UI.
 * @author catarinaserrano
 */
public class UserRoleDTO {
    
    private String role;
    private String description;

    public UserRoleDTO(String role, String description) {
        this.role = role;
        this.description = description;
    }
    
    public UserRoleDTO(){
    }
    
    public String getRole() {
        return role;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRoleDTO that = (UserRoleDTO) o;
        return getRole().equals(that.getRole());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRole());
    }
}
