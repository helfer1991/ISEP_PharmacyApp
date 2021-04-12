package lapr.project.service;

import lapr.project.data.ClientDB;
import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceClientTest {


    @Mock
    ClientDB clientDB;
    ServiceClient srvcClientInstance;
    Client client1;
    
    @BeforeEach
    void setUp() {
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        srvcClientInstance = new ServiceClient();
        srvcClientInstance.setClientDB(clientDB);
        Client client1 = new Client(
            new Address(2, 34,45, "rua", "1200", 100),
            new CreditCard("5464", 455, "2023"));
   
        client1.setNIF(22222222);
        client1.setName("nome");
        client1.setEmail("email");
        client1.setPassword("pwd");
    
    }
    
    

//    Pharmacy pharmacy1 = new Pharmacy(
//            "nome", 555,
//            new Park(11, 50,50,30,30),
//            new Address(3,20,-32, "rua", "2500", 100),
//            33.2f, 15.5f
//    );
    @Test
    void insertClient() throws SQLException {

        when(clientDB.insertClient(client1)).thenReturn(false);
        boolean result = srvcClientInstance.insertClient(client1);
        assertFalse(result);

        when(clientDB.insertClient(client1)).thenReturn(true);
        result = srvcClientInstance.insertClient(client1);
        assertTrue(result);

//        PharmaDeliveriesApp.getInstance().doLogout();
//        result = srvcClientInstance.insertClient(client1);
//        assertFalse(result);
//
//        verify(clientDB, times(2)).insertClient(client1);
    }

//    @Test
//    void getClientsFromPharmacy() throws SQLException {
//        List<Client> expectedResult = new ArrayList<>();
//        expectedResult.add(client1);
//        expectedResult.add(client1);
//
//        when(clientDB.getClientsByPharmacyId(pharmacy1.getId())).thenReturn(expectedResult);
//        Iterable<Client> result = srvcClientInstance.getClientsFromPharmacy(pharmacy1);
//        assertEquals(expectedResult, result);
//
//        PharmaDeliveriesApp.getInstance().doLogout();
//        result = srvcClientInstance.getClientsFromPharmacy(pharmacy1);
//        assertNull(result);
//
//
//        verify(clientDB).getClientsByPharmacyId(pharmacy1.getId());
//    }

    @Test
    void getClientByUserSession() throws SQLException {
        User user1 = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();

        //with admin login
        //when(clientDB.getClientByUserSession(user1)).thenReturn(client1);
        Client result = srvcClientInstance.getClientByUserSession(user1);
        assertNull(result);

        //with client login
        PharmaDeliveriesApp.getInstance().doLogin(TEST_CLIENT_EMAIL, TEST_CLIENT_KEY);
        result = srvcClientInstance.getClientByUserSession(user1);
        assertEquals(result, client1);

        //without login
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcClientInstance.getClientByUserSession(user1);
        assertNull(result);

        //verify(clientDB).getClientByUserSession(user1);
    }
}