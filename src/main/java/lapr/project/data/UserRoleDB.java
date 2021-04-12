/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import lapr.project.model.UserRole;

/**
 *
 * @author catarinaserrano
 */
public class UserRoleDB extends DataHandler{
    
    public UserRole getUserRoleByDescription(String description) throws SQLException{
        
         Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
             stmt = conn.createStatement();
             rs = stmt.executeQuery("select * from USER_ROLE "
                    + "where DESCRIPTION = '" + description +"'");
            
            UserRole userRole = null;
            while(rs.next()){
                userRole= new UserRole(rs.getString("ID_USER_ROLE"), rs.getString("DESCRIPTION"));
            }
            return userRole;
            
        } catch (SQLException e) {
            throw new SQLException(e.getMessage());
         } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }
}
