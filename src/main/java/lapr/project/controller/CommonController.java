package lapr.project.controller;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import lapr.project.model.Address;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Road;
import lapr.project.service.ServiceCommon;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.RoadDTO;


/**
 *
 */

public class CommonController {

    private ServiceCommon serviceCommon;

    public CommonController() {
        serviceCommon = PharmaDeliveriesApp.getInstance().getServiceCommon();
    }


    public boolean runScriptOnDatabase(String fileString, boolean dropFirst) throws FileNotFoundException, SQLException {
        return serviceCommon.runScriptOnDatabase(fileString, dropFirst);
    }
    
    public List<AddressDTO> getAllAddress() {

        Iterable<Address> addresses = PharmaDeliveriesApp.getInstance().getTerrestrialGraph().getGraph().vertices();
        List<AddressDTO> tmp = new LinkedList<>();
        if (addresses != null) {
            for (Address a : addresses) {
                tmp.add(DTOConverter.convertAddress(a));
            }
        }
        if (tmp.size() > 0) {
            return tmp;
        } else {
            return null;
        }
    }
    
    public AddressDTO insertAddress(AddressDTO address) throws SQLException{
       if(address!=null){
           
           return DTOConverter.convertAddress(serviceCommon.insertAddress(DTOConverter.convertAddressDTO(address)));
       }
       return null;
    }
    
     public RoadDTO insertAirRestriction(RoadDTO road) throws SQLException{
       if(road!=null){
           return DTOConverter.convertRoad(serviceCommon.insertAirRestriction(DTOConverter.convertRoadDTO(road)));
       }
       return null;
    }

    public RoadDTO insertRoadRestriction(RoadDTO roadDTO) throws SQLException{
       if(roadDTO!=null){
           Road road = DTOConverter.convertRoadDTO(roadDTO);
           return DTOConverter.convertRoad(serviceCommon.insertRoadRestriction(road));
       }
       return null;
    }
    
    

}
