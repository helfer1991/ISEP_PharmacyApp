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
import lapr.project.model.Road;

/**
 *
 * @author catarinaserrano
 */
public class RoadDB extends DataHandler {

    public Iterable<Road> getTerrestrialRestrictions() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt =conn.createStatement();
            rs = stmt.executeQuery("select road_restriction.id_road_restriction, a.id_address as idAddressOrig, a.latitude as latitudeOrig, a.longitude as longitudeOrig, a.address as addressOrig, a.zip_code as zipOrig, a.elevation as elevationOrig, "
                    + "b.id_address as idAddressDest, b.latitude as latitudeDest, b.longitude as longitudeDest, b.address as addressDest, b.zip_code as zipDest, b.elevation as elevationDest "
                    + "from road_restriction "
                    + "inner join address a "
                    + "on road_restriction.FK_ADDRESS_ID_START=a.id_address "
                    + "inner join address b "
                    + "on road_restriction.FK_ADDRESS_ID_END=b.id_address");

            Road road = null;

            List<Road> tmp = new LinkedList<Road>();
            while (rs.next()) {
                road = new Road(rs.getInt("id_road_restriction"), new Address(rs.getInt("idAddressOrig"), rs.getDouble("latitudeOrig"), rs.getDouble("longitudeOrig"), rs.getString("addressOrig"), rs.getString("zipOrig"), rs.getFloat("elevationOrig")),
                        new Address(rs.getInt("idAddressDest"), rs.getDouble("latitudeDest"), rs.getDouble("longitudeDest"), rs.getString("addressDest"), rs.getString("zipDest"), rs.getFloat("elevationDest")));
                tmp.add(road);
            }
            return tmp;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
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
    
    public Iterable<Road> getAirRestricitons() throws SQLException {

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt =conn.createStatement();
            rs = stmt.executeQuery("select aerial_restriction.id_aerial_restriction, a.id_address as idAddressOrig, a.latitude as latitudeOrig, a.longitude as longitudeOrig, a.address as addressOrig, a.zip_code as zipOrig, a.elevation as elevationOrig, "
                    + "b.id_address as idAddressDest, b.latitude as latitudeDest, b.longitude as longitudeDest, b.address as addressDest, b.zip_code as zipDest, b.elevation as elevationDest "
                    + "from aerial_restriction "
                    + "inner join address a "
                    + "on aerial_restriction.FK_ADDRESS_ORIGIN=a.id_address "
                    + "inner join address b "
                    + "on aerial_restriction.FK_ADDRESS_END=b.id_address");

            Road road = null;

            List<Road> tmp = new LinkedList<Road>();
            while (rs.next()) {
                road = new Road(rs.getInt("id_aerial_restriction"), new Address(rs.getInt("idAddressOrig"), rs.getDouble("latitudeOrig"), rs.getDouble("longitudeOrig"), rs.getString("addressOrig"), rs.getString("zipOrig"), rs.getFloat("elevationOrig")),
                        new Address(rs.getInt("idAddressDest"), rs.getDouble("latitudeDest"), rs.getDouble("longitudeDest"), rs.getString("addressDest"), rs.getString("zipDest"), rs.getFloat("elevationDest")));
                tmp.add(road);
            }
            return tmp;
            //callStmt = getConnection().prepareCall("select userrole.description, user.email, user.password from user join userrole on userrole.id_userrole=user.fk_userrole_id");
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
    
    public Road insertRoadRestriction(Road road) throws SQLException{
        
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select max(Id_Road_Restriction) id from Road_Restriction");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            road.setId(id + 1);
            rs = stmt.executeQuery(CommonDB.insertRoadRestrictionQuery(road));
            return road;
        } catch (SQLException ex) {
            throw new SQLException("Error while insert Road restriction: " + ex.getMessage());
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
    
    public Road insertAirRestriction(Road road) throws SQLException{
        
       Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select max(id_Aerial_Restriction) id from aerial_restriction");
            int id = 1;
            if (rs.next()) {
                id = rs.getInt("id");
            }
            road.setId(id + 1);
            rs = stmt.executeQuery(CommonDB.insertAirRestrictionQuery(road));
            return road;
        } catch (SQLException ex) {
            throw new SQLException("Error while insert Aerial restriction: " + ex.getMessage());
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
