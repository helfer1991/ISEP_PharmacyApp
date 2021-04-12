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
import lapr.project.model.Person;

/**
 *
 * @author catarinaserrano
 */
public class PersonDB extends DataHandler{
    
    public boolean insertPerson(Person person) throws SQLException {
        boolean success = false;
         Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
           
            rs = stmt.executeQuery("INSERT INTO PERSON (nif, name, email) VALUES("
                + person.getNIF() + ", '"
                + person.getName() + "', '"
                + person.getEmail() + "')");
            rs.close();
            success = true;
            return success;
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
