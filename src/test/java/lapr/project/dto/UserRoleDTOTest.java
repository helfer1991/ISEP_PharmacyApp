/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.dto;

import lapr.project.ui.dto.UserRoleDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author catarinaserrano
 */
public class UserRoleDTOTest {
    
    private final UserRoleDTO instance= new UserRoleDTO("1", "admin");
    
    public UserRoleDTOTest() {
    }
    
    

    /**
     * Test of getM_strRole method, of class UserRoleDTO.
     */
    @Test
    public void testGetM_strRole() {
        String expResult = "1";
        String result = instance.getRole();
        assertEquals(expResult, result);
    }

    /**
     * Test of getM_strDescription method, of class UserRoleDTO.
     */
    @Test
    public void testGetM_strDescription() {
        String expResult = "admin";
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }
    
}
