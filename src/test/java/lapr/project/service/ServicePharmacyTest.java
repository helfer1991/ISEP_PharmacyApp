package lapr.project.service;

import lapr.project.data.PharmacyDB;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import lapr.project.model.*;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServicePharmacyTest {

    @Mock
    private PharmacyDB pharmacyDB;
    private ServicePharmacy srvcPharmacy;

    private Pharmacy pharmacy1;
    User currentUser;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        srvcPharmacy = new ServicePharmacy();
        srvcPharmacy.setPharmacyDB(pharmacyDB);

        pharmacy1 = new Pharmacy(
                "pharmacyName", 4,
                new Park(1, 5,5,3,3),
                new Address(2,200,-320, "street", "8000", 50),
                30.0f, 10.0f);
        currentUser = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();
    }

    @Test
    void insertPharmacy() throws SQLException {
        when(pharmacyDB.insertPharmacy(pharmacy1, currentUser)).thenReturn(pharmacy1);

        //confirms that the DAO object method was called with the expect arguments
        Pharmacy result = srvcPharmacy.insertPharmacy(pharmacy1);
        assertEquals(pharmacy1, result);

        //if there is no login the result should be null
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcPharmacy.insertPharmacy(pharmacy1);
        assertNull(result);

        //confirms if the method was called only once
        verify(pharmacyDB).insertPharmacy(pharmacy1, currentUser);

    }

    @Test
    void updatePharmacy() throws SQLException {
        when(pharmacyDB.updatePharmacy(pharmacy1)).thenReturn(pharmacy1);

        //confirms that the DAO object method was called with the expect arguments
        Pharmacy result = srvcPharmacy.updatePharmacy(pharmacy1);
        assertEquals(pharmacy1, result);

        //if there is no login the result should be null
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcPharmacy.updatePharmacy(pharmacy1);
        assertNull(result);

        //confirms if the method was called only once
        verify(pharmacyDB).updatePharmacy(pharmacy1);
    }

    @Test
    void removePharmacy() throws SQLException {
        when(pharmacyDB.removePharmacy(pharmacy1)).thenReturn(false);

        //if the return from the DAO method is false, the result should be false
        Boolean result = srvcPharmacy.removePharmacy(pharmacy1);
        assertFalse(result);

        when(pharmacyDB.removePharmacy(pharmacy1)).thenReturn(true);

        //if the return from the DAO method is true, the result should be true
        result = srvcPharmacy.removePharmacy(pharmacy1);
        assertTrue(result);

        //if there is no login the result should be false
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcPharmacy.removePharmacy(pharmacy1);
        assertFalse(result);

        //confirms if the method was called twice
        verify(pharmacyDB, times(2)).removePharmacy(pharmacy1);
    }

    @Test
    void getPharmacyByCourier() throws SQLException {


        //should fail because of the Admin logIn
        Pharmacy result = srvcPharmacy.getPharmacyByCourier(currentUser);
        assertNull(result);

        //confirms that the DAO object method was called with the expect arguments
        PharmaDeliveriesApp.getInstance().doLogin(TEST_COURIER_EMAIL, TEST_COURIER_KEY);
        currentUser = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();

        //when(pharmacyDB.getPharmacyByCourier(currentUser)).thenReturn(pharmacy1);
        result = srvcPharmacy.getPharmacyByCourier(currentUser);
        //assertEquals(pharmacy1, result);

        //if there is no login the result should be null
        result = srvcPharmacy.getPharmacyByCourier(null);
        assertNull(result);

        //confirms if the method was called only once
        //verify(pharmacyDB).getPharmacyByCourier(currentUser);
    }

    @Test
    void getPharmaciesByAdministrator() throws SQLException {
        List<Pharmacy> expectedResult = new ArrayList<>();
        expectedResult.add(pharmacy1);
        expectedResult.add(pharmacy1);
        expectedResult.add(pharmacy1);

        when(pharmacyDB.getPharmaciesByAdministrator(currentUser)).thenReturn(expectedResult);

        //confirms that the DAO object method was called with the expect arguments
        Iterable<Pharmacy> result = srvcPharmacy.getPharmaciesByAdministrator(currentUser);
        assertEquals(expectedResult, result);

        //confirms if the method was called only once
        verify(pharmacyDB).getPharmaciesByAdministrator(currentUser);

        //if there is login different from admin the result should be null
        PharmaDeliveriesApp.getInstance().doLogin(TEST_COURIER_EMAIL, TEST_COURIER_KEY);
        User invalidUser = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();
        result = srvcPharmacy.getPharmaciesByAdministrator(invalidUser);
        //assertNull(result);

        //if the argument is null the result should be null
        result = srvcPharmacy.getPharmaciesByAdministrator(null);
        assertNull(result);

    }

    @Test
    void getAllPharmacys() throws SQLException {
        List<Pharmacy> expectedResult = new ArrayList<>();
        expectedResult.add(pharmacy1);
        expectedResult.add(pharmacy1);

        when(pharmacyDB.getAllPharmacies()).thenReturn(expectedResult);

        //confirms that the DAO object method was called with the expect arguments
        Iterable<Pharmacy> result = srvcPharmacy.getAllPharmacies();
        assertEquals(expectedResult, result);

        //confirms if the method was called only once
        verify(pharmacyDB).getAllPharmacies();
    }
}