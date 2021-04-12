/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.controller;

import java.util.LinkedList;
import lapr.project.data.CourierDB;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.CourierDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.model.Courier;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

/**
 *
 * @author catarinaserrano
 */
public class ManageCouriesControllerTest {

    private final ParkDTO park1 = new ParkDTO(1, 10, 10, 5, 5);
    private final AddressDTO adress1 = new AddressDTO(1, 41.15227, -8.60929, "Rua Trindade", "4450-136", 100);
    private final PharmacyDTO pharmacyDTO = new PharmacyDTO("Pharmacyteste", 5, park1, adress1, 9, 3);

    private CourierDTO cDTO1;
    private CourierDTO cDTO2;
    private CourierDTO cDTO3;
    private CourierDTO cDTO4;
    private CourierDTO cDTO5;

    private Courier c1;
    private Courier c3;
    private Courier c4;
    private Courier c5;
    private Courier c6;

    public ManageCouriesControllerTest() {
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);

        cDTO1 = new CourierDTO(55, true);
        cDTO1.setNIF(259723444);
        cDTO1.setName("Catarina");
        cDTO1.setEmail("catarina@isep.ipp.pt");
        cDTO1.setPassword("catarina");
        cDTO2 = new CourierDTO(50, true);
        cDTO2.setNIF(259723444);
        cDTO2.setName("Catarina");
        cDTO2.setEmail("catarina@isep.ipp.pt");
        cDTO2.setPassword("catarina");
        cDTO3 = new CourierDTO(55, false);
        cDTO3.setNIF(259723444);
        cDTO3.setName("Catarina");
        cDTO3.setEmail("catarina@isep.ipp.pt");
        cDTO3.setPassword("catarina");
        cDTO4 = new CourierDTO(55, true);
        cDTO4.setNIF(259723444);
        cDTO4.setName("Catarina");
        cDTO4.setEmail("catarina23@isep.ipp.pt");
        cDTO4.setPassword("catarina");
        cDTO5 = new CourierDTO(55, true);
        cDTO5.setNIF(259723445);
        cDTO5.setName("Catarina");
        cDTO5.setEmail("catarina@isep.ipp.pt");
        cDTO5.setPassword("catarina");
        c1 = new Courier(55, true);
        c1.setNIF(259723444);
        c1.setName("Catarina");
        c1.setEmail("catarina@isep.ipp.pt");
        c1.setPassword("catarina");
        c3 = new Courier(55, true);
        c3.setNIF(259723445);
        c3.setName("Catarina");
        c3.setEmail("catarina@isep.ipp.pt");
        c3.setPassword("catarina");
        c4 = new Courier(50, true);
        c4.setNIF(259723444);
        c4.setName("Catarina");
        c4.setEmail("catarina@isep.ipp.pt");
        c4.setPassword("catarina");
        c5 = new Courier(55, false);
        c5.setNIF(259723444);
        c5.setName("Catarina");
        c5.setEmail("catarina@isep.ipp.pt");
        c5.setPassword("catarina");
        c6 = new Courier(55, true);
        c6.setNIF(259723444);
        c6.setName("Catarina");
        c6.setEmail("catarina23@isep.ipp.pt");
        c6.setPassword("catarina");

    }

    /**
     * Test of getCouries method, of class ManageCouriesController.
     */
    @Test
    public void testGetCouries() throws Exception {

        ManageCouriesController instance = new ManageCouriesController();
        CourierDB db = Mockito.mock(CourierDB.class);
        PharmaDeliveriesApp.getInstance().getServiceCourier().setCourierDB(db);

        //db
        LinkedList<Courier> expResultDB = new LinkedList<>();
        expResultDB.add(DTOConverter.convertCourierDTO(getCourierDTOTest("testeA")));
        expResultDB.add(DTOConverter.convertCourierDTO(getCourierDTOTest("testeB")));
        expResultDB.add(DTOConverter.convertCourierDTO(getCourierDTOTest("testeC")));

        when(db.getWorkingCouriersByPharmacyId(anyInt())).thenReturn(expResultDB);
        Assertions.assertEquals(instance.getCouries(pharmacyDTO).size(), expResultDB.size());

    }

    /**
     * Test of insertCourier method, of class ManageCouriesController.
     */
    @Test
    public void testInsertCourier() throws Exception {

        //Null test
        CourierDTO courier = null;
        ManageCouriesController instance = new ManageCouriesController();
        boolean expResult = false;
        boolean result = instance.insertCourier(courier, pharmacyDTO);

        Assertions.assertEquals(expResult, result);

        //Value test
        instance = new ManageCouriesController();
        CourierDB db = Mockito.mock(CourierDB.class);
        PharmaDeliveriesApp.getInstance().getServiceCourier().setCourierDB(db);
        CourierDTO inserted = getCourierDTOTest("teste1");

        boolean expResultDB = true;

        when(db.insertCourier(any(Courier.class), any(Pharmacy.class))).thenReturn(expResultDB);

        Assertions.assertEquals(instance.insertCourier(inserted, pharmacyDTO), expResultDB);
    }

    /**
     * Test of updateCourier method, of class ManageCouriesController.
     */
    @Test
    public void testUpdateCourier() throws Exception {

        //Null test
        CourierDTO courier = null;
        ManageCouriesController instance = new ManageCouriesController();
        boolean expResult = false;
        boolean result = instance.updateCourier(courier, pharmacyDTO);

        Assertions.assertEquals(expResult, result);

        //Value test
        instance = new ManageCouriesController();
        CourierDB db = Mockito.mock(CourierDB.class);
        PharmaDeliveriesApp.getInstance().getServiceCourier().setCourierDB(db);
        CourierDTO updated = getCourierDTOTest("teste1");

        boolean expResultDB = true;

        when(db.updateCourier(any(Courier.class), any(Pharmacy.class))).thenReturn(expResultDB);

        Assertions.assertEquals(instance.updateCourier(updated, pharmacyDTO), expResultDB);
    }

    /**
     * Test of removeCourier method, of class ManageCouriesController.
     */
    @Test
    public void testRemoveCourier() throws Exception {

//        //Null test
        CourierDTO courier = null;
        ManageCouriesController instance = new ManageCouriesController();
        boolean expResult = false;
        boolean result = instance.removeCourier(courier, pharmacyDTO);

        Assertions.assertEquals(expResult, result);

        //Value test
        instance = new ManageCouriesController();
        CourierDB db = Mockito.mock(CourierDB.class);
        PharmaDeliveriesApp.getInstance().getServiceCourier().setCourierDB(db);
        CourierDTO removed = getCourierDTOTest("teste1");

        boolean expResultDB = true;

        when(db.updateCourier(any(Courier.class), any(Pharmacy.class))).thenReturn(expResultDB);

        Assertions.assertEquals(instance.removeCourier(removed, pharmacyDTO), expResultDB);
    }

//    /**
//     * Test of convertCourierDTO method, of class ManageCouriesController.
//     */
//    @Test
//    public void testConvertCourierDTO() {
//
//        ManageCouriesController instance = new ManageCouriesController();
//        Courier result1 = DTOConverter.convertCourierDTO(cDTO1);
//        assertEquals(c1, result1);
//        assertEquals(result1, result1);
//        assertNotEquals(null, result1);
//        assertNotEquals(cDTO1, result1);
//        assertNotEquals(c4, result1);
//        assertNotEquals(c5, result1);
//        assertNotEquals(c3, result1);
//        assertNotEquals(c6, result1);
//    }

//    /**
//     * Test of convertCourier method, of class ManageCouriesController.
//     */
//    @Test
//    public void testConvertCourier() {
//        ManageCouriesController instance = new ManageCouriesController();
//        CourierDTO result1 = DTOConverter.convertCourier(c1);
//        assertEquals(cDTO1, result1);
//        assertEquals(result1, result1);
//        assertNotEquals(null, result1);
//        assertNotEquals(c1, result1);
//        assertNotEquals(cDTO2, result1);
//        assertNotEquals(cDTO3, result1);
//        assertNotEquals(cDTO4, result1);
//        assertNotEquals(cDTO5, result1);
//    }

    public CourierDTO getCourierDTOTest(String name) {
        CourierDTO expResult = new CourierDTO(55, true);
        expResult.setNIF(259723444);
        expResult.setName(name);
        expResult.setEmail("catarina23@isep.ipp.pt");
        expResult.setPassword("catarina");
        return expResult;
    }

}
