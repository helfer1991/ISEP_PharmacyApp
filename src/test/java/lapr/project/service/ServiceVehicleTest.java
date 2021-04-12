package lapr.project.service;

import lapr.project.data.VehicleDB;
import lapr.project.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;
import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceVehicleTest {

    public ServiceVehicleTest(){
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
    }


    @Mock
    private VehicleDB vehicleDB;

    ServiceVehicle srvInstance;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        srvInstance= new ServiceVehicle();

        srvInstance.setVehicleDB(vehicleDB);
    }

    private Scooter scooter1 = new Scooter(13,55564, 450, new Battery(1,150),99);
    private Pharmacy pharmacy1 = new Pharmacy(
            "nome", 555,
                new Park(11, 50,50,30,30),
                new Address(3,20,-32, "rua", "2500", 100),
             33.2f, 15.5f
    );

    @Test
    void insertScooter() throws SQLException {
        Scooter expResult = new Scooter(1,555, 500, new Battery(43, 157),99);

        when( vehicleDB.insertVehicle(scooter1, pharmacy1 )).thenReturn(expResult);
        Scooter result = (Scooter) srvInstance.insertVehicle(scooter1, pharmacy1);
        assertEquals(expResult, result );

        //if there is not an admin log in the result is null
        PharmaDeliveriesApp.getInstance().doLogout();

        result = (Scooter) srvInstance.insertVehicle(scooter1, pharmacy1);
        assertNull(result);


        //verify the DAO method was called only once
        verify(vehicleDB).insertVehicle(scooter1, pharmacy1);
    }

    @Test
    void getScootersFromPharmacy() throws SQLException {

        List<Scooter> expectedResult = new ArrayList<>();
        expectedResult.add(scooter1);
        PharmaDeliveriesApp.getInstance().doLogout();

        //define behaviour
        when( vehicleDB.getScootersByPharmacy(pharmacy1.getId())).thenReturn(expectedResult);

        //call method without logIn
        srvInstance.getAvailableVehiclesByPharmacy(pharmacy1);
        //verify the method didn't interact with the DAO
        verifyNoInteractions(vehicleDB);


        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);

        //call method with logIn
        Iterable<Scooter> result = srvInstance.getScootersByPharmacy(pharmacy1);
        //assert the result is the iterable returned from the DAO
        assertEquals(result, expectedResult);

        //verify the method from the DAO was called with the coorect parameter
        verify(vehicleDB).getScootersByPharmacy(pharmacy1.getId());
    }

    @Test
    void updateScooter() throws SQLException {
        
        Scooter expResult = new Scooter(13,55564, 450, new Battery(1,250),99);

        when( vehicleDB.updateVehicle(scooter1, pharmacy1 )).thenReturn(expResult);
        Scooter result = (Scooter) srvInstance.updateVehicle(scooter1, pharmacy1);
        assertEquals(expResult, result );

        //if there is not an admin log in the result is null
        PharmaDeliveriesApp.getInstance().doLogout();

        result = (Scooter) srvInstance.updateVehicle(expResult, pharmacy1);
        assertNull(result);


        //verify the DAO method was called only once
        //verify(scooterDB).updateScooter(expResult, pharmacy1);
    }

    @Test
    void removeScooter() throws SQLException {

        //if DAO return false -> result is false
        when( vehicleDB.removeVehicle(scooter1, pharmacy1 )).thenReturn(false);
        boolean result = srvInstance.removeVehicle(scooter1, pharmacy1);
        assertFalse(result);


        //if DAO returns true -> result is true
        when( vehicleDB.removeVehicle(scooter1, pharmacy1 )).thenReturn(true);
        result = srvInstance.removeVehicle(scooter1, pharmacy1);
        assertTrue(result);


        //Nevertheless, if no one is logged in -> result is false
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvInstance.removeVehicle(scooter1, pharmacy1);
        assertFalse(result);


        //verify the DAO method was called as many times as expected
        verify(vehicleDB, times(2)).removeVehicle(scooter1, pharmacy1);

    }

    @Test
    void simulateScooterCharging() {
        //mock ServiceScooter so we can verify setChargingTimer() invocation and invocation parameters
        ServiceVehicle instance2 = mock(ServiceVehicle.class);
        //sensorFile to be passed in the method
        ParkSensorFile sensorFile = new ParkSensorFile(1, 150, 75, 30, "1994_...");

        //call the method to be tested
        when(instance2.simulateVehicleCharging(sensorFile)).thenCallRealMethod();
        instance2.simulateVehicleCharging(sensorFile);

        //confirm setChargingTimer was called with the right arguments
        verify(instance2).setChargingTimer(sensorFile,(60*1000*30/(100-75)));
    }

    @Test
    void setChargingTimer_charging() throws SQLException, InterruptedException {

        //sensorFile to be passed
        ParkSensorFile sensorFile = new ParkSensorFile(1, 150, 75, 30, "1994_...");
        //method to be tested is called with sensorFile
        srvInstance.setChargingTimer(sensorFile, 100000);
        // since battery is not fully charged, the method should be called with the initial battery percentage plus 1% increased
        sleep(500); //enough time for the timer thread to run
        verify(vehicleDB).updateScooterStatus(sensorFile, 75+1);
    }

    @Test
    void setChargingTimer_charged() throws SQLException {
        //when battery is at 100% there's no attempt to call updateScooterStatus()
        srvInstance.setChargingTimer(new ParkSensorFile(1, 150, 100, 30, "1994_..."), 100000);
        verifyNoInteractions(vehicleDB);
    }


}

