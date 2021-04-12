package lapr.project.service;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import lapr.project.data.AddressDB;
import lapr.project.data.CommonDB;
import lapr.project.data.RoadDB;
import lapr.project.model.Address;
import lapr.project.model.Road;

public class ServiceCommon {

    CommonDB commonDB;
    AddressDB addressDB;
    RoadDB roadDB;

    public ServiceCommon() {
        commonDB = new CommonDB();
        addressDB = new AddressDB();
        roadDB = new RoadDB();
    }

    public boolean runScriptOnDatabase(String filename, boolean dropFirst) throws FileNotFoundException, SQLException {
        return commonDB.runScriptOnDatabase(filename, dropFirst);
    }
    
    public Address insertAddress(Address address) throws SQLException{
       if(address!=null){
           return addressDB.insertAddress(address);
       }
       return null;
    }
    
    public Road insertRoadRestriction(Road road) throws SQLException{
       if(road!=null){
           return roadDB.insertRoadRestriction(road);
       }
       return null;
    }
    
    public Road insertAirRestriction(Road road) throws SQLException{
       if(road!=null){
           return roadDB.insertAirRestriction(road);
       }
       return null;
    }

    public void setCommonDB(CommonDB commonDB) {
        this.commonDB = commonDB;
    }

    public void setAddressDB(AddressDB addressDB) {
        this.addressDB = addressDB;
    }

    public void setRoadDB(RoadDB roadDB) {
        this.roadDB = roadDB;
    }

    public CommonDB getCommonDB() {
        return commonDB;
    }
    
    
}
