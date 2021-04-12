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
import java.util.LinkedList;
import java.util.List;
import lapr.project.model.Address;

/**
 *
 * @author catarinaserrano
 */
public class AddressDB extends DataHandler{
    
    public Iterable<Address> getAllAddresses() throws SQLException {
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from address ");
            
            Address address =null;

            List<Address> tmp = new LinkedList<Address>();
            while (rs.next()) {
                address= new Address(rs.getInt("id_address"), rs.getDouble("latitude"), rs.getDouble("longitude"), rs.getString("address"), rs.getString("zip_code"), rs.getFloat("elevation"));
                tmp.add(address);
            }
            return tmp;
        } catch (SQLException e) {
            throw  new SQLException(e.getMessage());
        } finally {
            if(stmt!=null){
                stmt.close();
            }
            if(rs!=null){
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }
    
    public Address insertAddress(Address address) throws SQLException{
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select max(id_address) id from address");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            address.setId(id + 1);
            rs = stmt.executeQuery(CommonDB.insertAddressQuery(address));
            return address;
        } catch (SQLException ex) {
            throw new SQLException("Error while insert Address: " + ex.getMessage());
        } finally {
            if(stmt!=null){
                stmt.close();
            }
            if(rs!=null){
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }
    
    public Address getAddressById(int addressID) throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from address where ID_ADDRESS = " + addressID);
            rs.next();

            Address address = new Address(
                    rs.getInt("id_address"),
                    rs.getDouble("latitude"),
                    rs.getDouble("longitude"),
                    rs.getString("address"),
                    rs.getString("zip_code"),
                    rs.getFloat("elevation")
            );
            
            return address;
        } catch (SQLException ex) {
            throw new SQLException("Error while fetching Address: " + ex.getMessage());
       } finally {
            if(stmt!=null){
                stmt.close();
            }
            if(rs!=null){
                rs.close();
            }
            try {
                //
            } catch (Exception e) {
            }
        }
    }
    
}
