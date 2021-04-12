/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.service;

import java.sql.SQLException;
import lapr.project.data.ClientDB;
import lapr.project.model.Client;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.User;
import lapr.project.utils.Constants;


/**
 *
 * @author Asus
 */
public class ServiceClient {
    
    private ClientDB clientDB;
    public ServiceClient(){
        clientDB = new ClientDB();
    }
    
    public boolean insertClient(Client client) throws SQLException {
        return clientDB.insertClient(client);
    }
    
//    public Iterable<Client> getClientsFromPharmacy(Pharmacy pharmacy) throws SQLException {
//        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
//                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(ROLE_ADMIN)) {
//            return null;
//        }
//        return clientDB.getClientsByPharmacyId(pharmacy.getId());
//    }
    
    public Client getClientByUserSession(User user) throws SQLException {
        if (PharmaDeliveriesApp.getInstance().getCurrentSession() == null
                || !PharmaDeliveriesApp.getInstance().getCurrentSession().getUserRole().getDescription().equals(Constants.ROLE_CLIENT)) {
            return null;
        }
        return clientDB.getClientByUserSession(user);
    }

    public void setClientDB(ClientDB clientDB) {
        this.clientDB = clientDB;
    }
}
