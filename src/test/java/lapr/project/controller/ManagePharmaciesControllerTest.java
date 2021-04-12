package lapr.project.controller;

import java.sql.SQLException;
import java.util.LinkedList;
import lapr.project.data.PharmacyDB;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import lapr.project.model.User;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.utils.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static lapr.project.controller.DTOConverter.convertPharmacyDTO;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author 1100241
 */
public class ManagePharmaciesControllerTest {

    private CommonController commonController;
    private User user;
    public ManagePharmaciesControllerTest() {
        //Login as administrator
        PharmaDeliveriesApp.getInstance().doLogin(Constants.TEST_ADMIN_EMAIL, Constants.TEST_ADMIN_KEY);
        commonController = new CommonController();
        user = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();
    }

    /**
     * Test of getPharmaciesByAdministrator method, of class
     * ManagePharmaciesController.
     */
    @Test
    public void testGetPharmaciesByAdministrator() throws Exception {
        System.out.println("getPharmaciesByAdministrator");
        
        //Logout test
        ManagePharmaciesController instance = new ManagePharmaciesController();
//        PharmaDeliveriesApp.getInstance().doLogout();
//        LinkedList<PharmacyDTO> exp = null;
//        LinkedList<PharmacyDTO> result = instance.getPharmaciesByAdministrator();
//        Assertions.assertEquals(exp, result);
//        PharmaDeliveriesApp.getInstance().doLogin("adminteste@isep.ipp.pt", "adminteste");
        
        //Value test
        PharmacyDB db = Mockito.mock(PharmacyDB.class);
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(db);
        
        LinkedList<Pharmacy> inputsDB = new LinkedList<>();
        inputsDB.add(convertPharmacyDTO(getPharmacyDTOTest("testeA")));
        inputsDB.add(convertPharmacyDTO(getPharmacyDTOTest("testeB")));
        
        LinkedList<Pharmacy> expResultDB = new LinkedList<>();
        expResultDB.add(convertPharmacyDTO(getPharmacyDTOTest("testeA")));
        expResultDB.add(convertPharmacyDTO(getPharmacyDTOTest("testeB")));
        
        LinkedList<Pharmacy> inputsCobtroller = new LinkedList<>();
        inputsCobtroller.add(convertPharmacyDTO(getPharmacyDTOTest("testeA")));
        inputsCobtroller.add(convertPharmacyDTO(getPharmacyDTOTest("testeB")));
        
        LinkedList<PharmacyDTO> expResult = new LinkedList<>();
        expResult.add(getPharmacyDTOTest("testeA"));
        expResult.add(getPharmacyDTOTest("testeB"));
        
        when(db.getPharmaciesByAdministrator(any(User.class))).thenReturn(expResultDB);
        Assertions.assertEquals(instance.getPharmaciesByAdministrator().size(), expResult.size());
        
        when(db.getPharmaciesByAdministrator(any(User.class))).thenReturn(null);
        Assertions.assertNull(instance.getPharmaciesByAdministrator());
        
    }

    /**
     * Test of insertPharmacy method, of class ManagePharmaciesController.
     */
    @Test
    public void testInsertPharmacy() throws Exception {
        System.out.println("insertPharmacy");
        
        //Null test
        PharmacyDTO pharmacy = null;
        ManagePharmaciesController instance = new ManagePharmaciesController();
        PharmacyDTO expResult = null;
        PharmacyDTO result = instance.updatePharmacy(pharmacy);
        Assertions.assertEquals(expResult, result);
        
        //Value test
        instance = new ManagePharmaciesController();
        PharmacyDB db = Mockito.mock(PharmacyDB.class);
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(db);
        PharmacyDTO inserted = getPharmacyDTOTest("teste1");
        Pharmacy returnedP = convertPharmacyDTO(inserted);
        returnedP.setId(1);
        
        when(db.insertPharmacy(any(Pharmacy.class),any(User.class))).thenReturn(returnedP);
        Assertions.assertEquals(instance.insertPharmacy(inserted).getName(), inserted.getName());
        Assertions.assertNotEquals(instance.insertPharmacy(inserted).getId(), inserted.getId());
        
        when(db.insertPharmacy(any(Pharmacy.class),any(User.class))).thenReturn(null);
        Assertions.assertNull(instance.insertPharmacy(inserted));
    }

    /**
     * Test of updatePharmacy method, of class ManagePharmaciesController.
     */
    @Test
    public void testUpdatePharmacy() throws Exception {
        System.out.println("updatePharmacy");

        //Null test
        PharmacyDTO pharmacy = null;
        ManagePharmaciesController instance = new ManagePharmaciesController();
        PharmacyDTO expResult = null;
        PharmacyDTO result = instance.updatePharmacy(pharmacy);
        Assertions.assertEquals(expResult, result);

        //Value test
        instance = new ManagePharmaciesController();
        PharmacyDB db = Mockito.mock(PharmacyDB.class);
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(db);
        PharmacyDTO update = getPharmacyDTOTest("teste1");
        Pharmacy returnedP = convertPharmacyDTO(update);
        when(db.updatePharmacy(any(Pharmacy.class))).thenReturn(returnedP);
        Assertions.assertEquals(instance.updatePharmacy(update).getName(), update.getName());
        Assertions.assertEquals(instance.updatePharmacy(update).getId(), update.getId());
        
        when(db.updatePharmacy(any(Pharmacy.class))).thenReturn(null);
        Assertions.assertNull(instance.updatePharmacy(update));
    }

    /**
     * Test of removePharmacy method, of class ManagePharmaciesController.
     */
    @Test
    public void testRemovePharmacy() throws SQLException {
        System.out.println("removePharmacy");
        
        //Null test
        PharmacyDTO pharmacy = null;
        ManagePharmaciesController instance = new ManagePharmaciesController();
        boolean expResult = false;
        boolean result = instance.removePharmacy(pharmacy);
        Assertions.assertEquals(expResult, result);
        
        //Value test
        PharmacyDB db = Mockito.mock(PharmacyDB.class);
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(db);
        when(db.removePharmacy(any(Pharmacy.class))).thenReturn(true);
        Assertions.assertEquals(instance.removePharmacy(getPharmacyDTOTest("a")), true);
        when(db.removePharmacy(any(Pharmacy.class))).thenReturn(false);
        Assertions.assertEquals(instance.removePharmacy(getPharmacyDTOTest("b")), false);
    }

    /**
     * Test of getAllPharmacies method, of class ManagePharmaciesController.
     */
    @Test
    public void testGetAllPharmacies() throws Exception {
        System.out.println("getAllPharmacies");

        ManagePharmaciesController instance = new ManagePharmaciesController();
        LinkedList<PharmacyDTO> expResult= new LinkedList<>();
        LinkedList<PharmacyDTO> result = instance.getAllPharmacies();
        expResult.add(getPharmacyDTOTest("a"));
        expResult.add(getPharmacyDTOTest("b"));
        expResult.add(getPharmacyDTOTest("c"));

        LinkedList<Pharmacy> resultDB = new LinkedList<>();
        resultDB.add(convertPharmacyDTO(getPharmacyDTOTest("a")));
        resultDB.add(convertPharmacyDTO(getPharmacyDTOTest("b")));
        resultDB.add(convertPharmacyDTO(getPharmacyDTOTest("c")));
        PharmacyDB db = Mockito.mock(PharmacyDB.class);
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(db);
        when(db.getAllPharmacies()).thenReturn(resultDB);
        result = instance.getAllPharmacies();
        Assertions.assertEquals(result.size(), expResult.size());
        Assertions.assertEquals(result.get(0).getName(), expResult.get(0).getName());
        Assertions.assertEquals(result.get(1).getName(), expResult.get(1).getName());
       Assertions.assertEquals(result.get(2).getName(), expResult.get(2).getName());
        
//        int i = 0;
//        for(PharmacyDTO p : result){
//            Assertions.assertEquals(p.getName(), expResult.get(i).getName());
//            i++;
//        }
        
        when(db.getAllPharmacies()).thenReturn(null);
        Assertions.assertNull(instance.getAllPharmacies());
    }
    
    
     public PharmacyDTO getPharmacyDTOTest(String name) {
        AddressDTO address = new AddressDTO(0, 0, 0, "Rua A", "4444-111", 0);
        Float maxload = (float) 5;
        Float minLoad = (float) 5;
        int scooterParkCapacity = 10;
        float scooterChargerCapacity= (float)10;
        int droneParkCapacity = 10;
        float droneChargerCapacity = (float)10;
        return new PharmacyDTO(name, 0, new ParkDTO(0, scooterParkCapacity, droneParkCapacity, scooterChargerCapacity, droneChargerCapacity), address, minLoad, maxload);
    }


}
