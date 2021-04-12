package lapr.project.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lapr.project.data.VehicleDB;
import lapr.project.model.*;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ScooterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static lapr.project.controller.DTOConverter.convertScooter;
import lapr.project.ui.dto.BatteryDTO;
import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 */
@ExtendWith(MockitoExtension.class)
public class ManageVehiclesControllerTest {
    
    public ManageVehiclesControllerTest() {
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
    }
    
    private ScooterDTO scooterDTO1, scooterDTO2;
    private Scooter scooter1, scooter2;
    private ParkDTO park1 = new ParkDTO(3, 10,10,3,3);
    private AddressDTO adress1 = new AddressDTO(3, 41.1492549, -8.6237193,"Rua Miguel Bombarda","4050-383", 100);
    private PharmacyDTO pharmacyDTO = new PharmacyDTO("PharmacyTest1", 3, park1, adress1, 9, 3);

    @Mock
    private VehicleDB vehicleDB;
    private ManageVehiclesController instance;

    @BeforeEach
    void setUp(){
        instance = new ManageVehiclesController();
        PharmaDeliveriesApp.getInstance().getServiceVehicle().setVehicleDB(vehicleDB);

        scooterDTO1 = new ScooterDTO(1, 1, 10, new BatteryDTO(1, 10),99);
        scooterDTO2 = new ScooterDTO(2, 2, 20, new BatteryDTO(2, 20),99);
        scooter1 = new Scooter(1, 1, 10, new Battery(1, 10),99);
        scooter2 = new Scooter(2, 2, 20, new Battery(2, 20),99);
    }

    /**
     * Test of getScooters method, of class ManageScootersController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetScooters() throws Exception {
        List<Scooter> expResultDB = new LinkedList<>();
        expResultDB.add(scooter1);
        expResultDB.add(scooter2);

        doReturn(expResultDB).when(vehicleDB).getScootersByPharmacy(pharmacyDTO.getId());
        //when(vehicleDB.getAvailableVehiclesByPharmacy(anyInt())).thenReturn(expResultDB);
        List<ScooterDTO> result =  instance.getScooters(pharmacyDTO);

        List<ScooterDTO> expectedResult = new ArrayList<>();
        expectedResult.add(scooterDTO1);
        expectedResult.add(scooterDTO2);
        assertEquals(expectedResult,result);
    }

    /**
     * Test of insertScooter method, of class ManageScootersController.
     */
    @Test
    public void testInsertScooter() throws Exception {
        System.out.println("insertScooter");
        
        //Null test
        ScooterDTO result =(ScooterDTO) instance.insertVehicle(null, pharmacyDTO);
        assertNull(result);
        result =(ScooterDTO) instance.insertVehicle(scooterDTO1, null);
        assertNull(result);
        
        //Value test
        ScooterDTO inserted = scooterDTO1;
        Scooter returnedDB = scooter1;

        when(vehicleDB.insertVehicle(any(Scooter.class),any(Pharmacy.class))).thenReturn(returnedDB);
        result = (ScooterDTO) instance.insertVehicle(inserted, pharmacyDTO);
        assertEquals(convertScooter(returnedDB) , result);
    }

    /**
     * Test of updateScooter method, of class ManageScootersController.
     */
    @Test
    public void testUpdateScooter() throws Exception {
        System.out.println("editScooter");
        
        //Null test
        ScooterDTO result =(ScooterDTO) instance.updateVehicle(null, pharmacyDTO);
        assertNull(result);
        result =(ScooterDTO) instance.updateVehicle(scooterDTO1, null);
        assertNull(result);
        
        //Value test
        ScooterDTO inserted = scooterDTO1;
        Scooter returnedDB = scooter1;

        when(vehicleDB.updateVehicle(any(Scooter.class),any(Pharmacy.class))).thenReturn(returnedDB);
        result = (ScooterDTO) instance.updateVehicle(inserted, pharmacyDTO);
        assertEquals(convertScooter(returnedDB) , result);
    }

    /**
     * Test of removeScooter method, of class ManageScootersController.
     */
    @Test
    public void testRemoveScooter() throws SQLException {

        ScooterDTO removed = new ScooterDTO(1,2,25,new BatteryDTO(3,4),99);

        when(vehicleDB.removeVehicle(any(Scooter.class),any(Pharmacy.class))).thenReturn(true);
        assertTrue( instance.removeVehicle(removed, pharmacyDTO) );

        when(vehicleDB.removeVehicle(any(Scooter.class),any(Pharmacy.class))).thenReturn(false);
        assertFalse( instance.removeVehicle(removed, pharmacyDTO) );

        verify(vehicleDB, times(2)).removeVehicle(any(Scooter.class), any(Pharmacy.class));
    }

    
}
