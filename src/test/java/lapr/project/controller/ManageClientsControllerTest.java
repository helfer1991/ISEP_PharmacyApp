package lapr.project.controller;

import lapr.project.data.ClientDB;
import lapr.project.model.*;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import lapr.project.ui.dto.CreditCardDTO;

import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 *
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ManageClientsControllerTest {
    
    public ManageClientsControllerTest() {
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
    }
    
    private ManageClientsController controllerInstance;
    private Client client1;
    private ClientDTO clientDTO1;

    private final Pharmacy pharmacy1 = new Pharmacy(
            "pharmacyName", 4,
            new Park(1, 5,5,3,3),
            new Address(2,200,-320, "street", "8000", 50),
            30.0f, 10.0f);
    private final PharmacyDTO pharmacyDTO1 = new PharmacyDTO(
            "pharmacyName", 4,
            new ParkDTO(1, 5,5,3,3),
            new AddressDTO(2,200,-320, "street", "8000", 50),
            30.0f, 10.0f);


    @Mock
    private ClientDB clientDB;

    @BeforeEach
    public void setUp() {
        controllerInstance = new ManageClientsController();
        PharmaDeliveriesApp.getInstance().getServiceClient().setClientDB(clientDB);

        client1 = new Client(); 
        client1.setEmail("hmcfb@isep.ipp.pt");
        client1.setPassword("balelos");
        client1.setName("Helder");
        client1.setNIF(12345);
        client1.setAddress(new Address(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
        client1.setCreditCard(new CreditCard("12345", 3456, "2020-06-06"));
        
        clientDTO1 = new ClientDTO(); 
        clientDTO1.setEmail("hmcfb@isep.ipp.pt");
        clientDTO1.setPassword("balelos");
        clientDTO1.setName("Helder");
        clientDTO1.setNIF(12345);
        clientDTO1.setAddress(new AddressDTO(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
        clientDTO1.setCreditCard(new CreditCardDTO("12345", 3456, "2020-06-06"));
    }


//    @Test
//    void getClients() throws SQLException {
//        List<Client> clientDBResult = new LinkedList<>();
//        clientDBResult.add(client1);
//        clientDBResult.add(client1);
//        when(clientDB.getClientsByPharmacyId(pharmacyDTO1.getId())).thenReturn(clientDBResult);
//
//        List<ClientDTO> expectedResult = new LinkedList<>();
//        expectedResult.add(clientDTO1);
//        expectedResult.add(clientDTO1);
//
//        //calls method and verifies that the result is the same as received from the DB
//        List<ClientDTO> result = controllerInstance.getClients(pharmacyDTO1);
//        assertEquals(expectedResult,result);
//
//        //if the list returned from the DB is empty returns null
//        List<Client> emptyList = new LinkedList<>();
//        when(clientDB.getClientsByPharmacyId(pharmacyDTO1.getId())).thenReturn(emptyList);
//        result = controllerInstance.getClients(pharmacyDTO1);
//        assertNull(result);
//
//        //if there is no LogIn or the user dosen't have admin role returns null
//        PharmaDeliveriesApp.getInstance().doLogout();
//        result = controllerInstance.getClients(pharmacyDTO1);
//        assertNull(result);
//
//
//
//        //confirm DAO method was only called twice
//        verify(clientDB, times(2)).getClientsByPharmacyId(pharmacyDTO1.getId());
//
//    }

//    @Test
//    void insertClient() throws SQLException {
//
//        when(clientDB.insertClient(client1,pharmacy1 )).thenReturn(true);
//        boolean result = controllerInstance.insertClient(clientDTO1, pharmacyDTO1);
//        assertTrue(result);
//
//        when(clientDB.insertClient(client1,pharmacy1 )).thenReturn(false);
//        result = controllerInstance.insertClient(clientDTO1, pharmacyDTO1);
//        assertFalse(result);
//
//        verify(clientDB, times(2)).insertClient(client1, pharmacy1);
//    }


    @Test
    void getClientByUserSession() throws SQLException {

        //fail because user has admin role
        ClientDTO result = controllerInstance.getClientByUserSession();
        assertNull(result);

        PharmaDeliveriesApp.getInstance().doLogin(TEST_CLIENT_EMAIL, TEST_CLIENT_KEY);
        User currentUser = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();
        when(clientDB.getClientByUserSession(currentUser)).thenReturn(client1);
        result = controllerInstance.getClientByUserSession();
        //assertEquals(clientDTO1, result);

        when(clientDB.getClientByUserSession(currentUser)).thenReturn(null);
        result = controllerInstance.getClientByUserSession();
        assertNull(null);

        PharmaDeliveriesApp.getInstance().doLogout();
        result = controllerInstance.getClientByUserSession();
        assertNull(null);

        //verify(clientDB, times(2)).getClientByUserSession(currentUser);

    }

    @Test
    void convertClientDTO() {
        Client expectedResult = client1;
        Client result = DTOConverter.convertClientDTO(clientDTO1);
        assertEquals(expectedResult,result);

    }

    @Test
    void convertClient() {
        ClientDTO expectedResult = clientDTO1;
        ClientDTO result = DTOConverter.convertClient(client1);
        assertEquals(expectedResult,result);
    }
}
