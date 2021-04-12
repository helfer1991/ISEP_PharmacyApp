package lapr.project.controller;

import java.sql.SQLException;
import java.util.List;
import lapr.project.model.autorization.UserSession;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.model.Client;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.ui.dto.AddressDTO;

/**
 *
 * @author Asus
 */
public class ManageClientsController {

    private PharmaDeliveriesApp pharmaDeliveriesApp;
    private CommonController commonController;

    public ManageClientsController() {
        this.pharmaDeliveriesApp = PharmaDeliveriesApp.getInstance();
        commonController = new CommonController();
    }

//    /**
//     * This method retrives a list of working corries for a specific pharmacy
//     *
//     * @return
//     * @throws SQLException
//     */
//    public LinkedList<ClientDTO> getClients(PharmacyDTO pharmacyDTO) throws SQLException {
//        Iterable<Client> temp;
//        LinkedList<ClientDTO> clients = new LinkedList<>();
//        temp = this.pharmaDeliveriesApp.getServiceClient().getClientsFromPharmacy(commonController.convertPharmacyDTO(pharmacyDTO));
//        if(temp == null){
//            return null;
//        }
//        for (Client c : temp) {
//            clients.add(convertClient(c));
//        }
//        if(clients.isEmpty()){
//            return null;
//        }
//        return clients;
//
//    }
    
     public List<AddressDTO> getAllAddress() {
        return commonController.getAllAddress();
    }

    public boolean insertClient(ClientDTO clientDTO) throws SQLException {
        Client client = DTOConverter.convertClientDTO(clientDTO);
        return this.pharmaDeliveriesApp.getServiceClient().insertClient(client);
    }

    public ClientDTO getClientByUserSession() throws SQLException{
        UserSession u = this.pharmaDeliveriesApp.getCurrentSession();
        if(u == null){ return null;}

        Client c = this.pharmaDeliveriesApp.getServiceClient().getClientByUserSession(u.getUser());
        if (c == null){return null; }

        return DTOConverter.convertClient(c);
    }

  
}
