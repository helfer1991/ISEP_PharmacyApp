/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui.dto;

import lapr.project.model.User;

/**
 * This class represents a DTO to User class in model in order to show the
 * object User in UI.
 *
 * @author catarinaserrano
 */
public class UserDTO {

    private String email;
    private String password;

    private UserRoleDTO userRole;

    public UserDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserDTO() {
        userRole=new UserRoleDTO();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserRoleDTO getUserRole() {
        return userRole;
    }

    public void setEmail(String strEmail) {
        this.email = strEmail;
    }

    public void setPassword(String strPassword) {
        this.password = strPassword;
    }

    public void setUserRole(UserRoleDTO userRole) {
        this.userRole = userRole;
    }

}
