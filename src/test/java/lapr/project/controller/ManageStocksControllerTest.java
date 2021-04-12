package lapr.project.controller;

import java.util.AbstractMap;
import java.util.Map;
import lapr.project.data.StorageDB;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.StorageDTO;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.model.Pharmacy;
import lapr.project.model.Product;
import lapr.project.model.Storage;
import lapr.project.model.User;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.ParkDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static lapr.project.controller.DTOConverter.*;
import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ManageStocksControllerTest {

    private User user;

    public ManageStocksControllerTest() {
        //Login as administrator
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        user = PharmaDeliveriesApp.getInstance().getCurrentSession().getUser();
    }

    
    
    /**
     * Test of getProductsByPharmacy method, of class ManageStocksController.
     */
    @Test
    public void testGetProductsByPharmacy() throws Exception {
        System.out.println("getProductsByPharmacy");
        
        //Null test
        PharmacyDTO pharmacyDTO = null;
        ManageStocksController instance = new ManageStocksController();
        StorageDTO expResult = null;
        StorageDTO result = instance.getProductsByPharmacy(pharmacyDTO);
        assertEquals(expResult, result);

        //Value test
        expResult = new StorageDTO();
        expResult.addProductToStorage(getProductDTOTest("a"), 2);
        expResult.addProductToStorage(getProductDTOTest("b"), 3);
        expResult.addProductToStorage(getProductDTOTest("c"), 4);
        expResult.addProductToStorage(getProductDTOTest("d"), 5);
        expResult.addProductToStorage(getProductDTOTest("e"), 6);
        
        //resultDB
        Storage resultDB = new Storage();
        resultDB.addProductToStorage(convertProductDTO(getProductDTOTest("a")), 2);
        resultDB.addProductToStorage(convertProductDTO(getProductDTOTest("b")), 3);
        resultDB.addProductToStorage(convertProductDTO(getProductDTOTest("c")), 4);
        resultDB.addProductToStorage(convertProductDTO(getProductDTOTest("d")), 5);
        resultDB.addProductToStorage(convertProductDTO(getProductDTOTest("e")), 6);
        StorageDB db = mock(StorageDB.class);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(db);
        pharmacyDTO = getPharmacyDTOTest("teste");
        
        when(db.getProductsByPharmacy(any(Pharmacy.class))).thenReturn(resultDB);
        StorageDTO tmp = instance.getProductsByPharmacy(pharmacyDTO);
        for (ProductDTO p : tmp.getProductMap().keySet()) {
            if(expResult.getProductMap().keySet().contains(p))
                expResult.getProductMap().keySet().remove(p);
        }
        Assertions.assertEquals(expResult.getProductMap().keySet().size(),0);
        
        when(db.getProductsByPharmacy(any(Pharmacy.class))).thenReturn(null);
        Assertions.assertNull(instance.getProductsByPharmacy(pharmacyDTO));
        
        
    }

    /**
     * Test of insertProductQuantity method, of class ManageStocksController.
     */
    @Test
    public void testInsertProductQuantity() throws Exception {
        System.out.println("insertProductQuantity");
        
        //Null test
        PharmacyDTO pharmacyDTO = null;
        Map.Entry<ProductDTO, Integer> entry = null;
        ManageStocksController instance = new ManageStocksController();
        Map.Entry<ProductDTO, Integer> expResult = null;
        Map.Entry<ProductDTO, Integer> result = instance.insertProductQuantity(pharmacyDTO, entry);
        assertEquals(expResult, result);
         
        //Value test 
        expResult = new AbstractMap.SimpleEntry<>(getProductDTOTest("a"),2);
    
        //resultDB
        Map.Entry<Product, Integer> resultDB = new AbstractMap.SimpleEntry<>(convertProductDTO(getProductDTOTest("a")),2);
        StorageDB db = mock(StorageDB.class);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(db);
        pharmacyDTO = getPharmacyDTOTest("teste");
        
        when(db.insertProductQuantity(any(Pharmacy.class), any())).thenReturn(resultDB);
        result = instance.insertProductQuantity(pharmacyDTO, expResult);
        Assertions.assertEquals(result.getKey().getDescription(), expResult.getKey().getDescription());
        
         when(db.insertProductQuantity(any(Pharmacy.class), any())).thenReturn(null);
        Assertions.assertNull(instance.insertProductQuantity(pharmacyDTO, expResult));
    }
//
    /**
     * Test of removeProductQuantity method, of class ManageStocksController.
     */
    @Test
    public void testRemoveProductQuantity() throws Exception {
        System.out.println("removeProductQuantity");
        
        //Null test
        PharmacyDTO pharmacyDTO = null;
        Map.Entry<ProductDTO, Integer> entry = null;
        ManageStocksController instance = new ManageStocksController();
        Map.Entry<ProductDTO, Integer> expResult = null;
        Map.Entry<ProductDTO, Integer> result = instance.removeProductQuantity(pharmacyDTO, entry);
        assertEquals(expResult, result);
         
        //Value test 
        expResult = new AbstractMap.SimpleEntry<>(getProductDTOTest("a"),2);
        Map.Entry<ProductDTO, Integer> input = new AbstractMap.SimpleEntry<>(getProductDTOTest("a"),2);
    
        //resultDB
        Map.Entry<Product, Integer> resultDB = new AbstractMap.SimpleEntry<Product, Integer>(convertProductDTO(getProductDTOTest("a")),2);
        StorageDB db = mock(StorageDB.class);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(db);
        pharmacyDTO = getPharmacyDTOTest("teste");
        
        when(db.insertProductQuantity(any(Pharmacy.class), any())).thenReturn(resultDB);
        result = instance.removeProductQuantity(pharmacyDTO, input);
        Assertions.assertEquals(result.getKey().getDescription(), expResult.getKey().getDescription());
        Assertions.assertEquals(result.getValue(), expResult.getValue());
        
        when(db.insertProductQuantity(any(Pharmacy.class), any())).thenReturn(null);
        Assertions.assertNull(instance.removeProductQuantity(pharmacyDTO, expResult));
    }

//    /**
//     * Test of insertProduct method, of class ManageStocksController.
//     */
//    @Test
//    public void testInsertProduct() throws Exception {
//        System.out.println("insertProduct");
//        
//        //Null test
//        ManageStocksController instance = new ManageStocksController();
//        PharmacyDTO pharmacyDTO = mock(PharmacyDTO.class);
//        ProductDTO product = null;
//        ProductDTO expResult = null;
//        ProductDTO result = instance.insertProduct(product);
//        assertEquals(expResult, result);
//
//        pharmacyDTO = null;
//        product = mock(ProductDTO.class);
//        result = instance.insertProduct(product);
//        assertEquals(expResult, result);
//
//
//        //Value test 
//        //resultDB
//        expResult = getProductDTOTest("a");
//        Product resultDB = new Product(expResult.getId(), expResult.getDescription(), expResult.getPrice(), expResult.getWeight());
//        StorageDB db = mock(StorageDB.class);
//        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(db);
//        pharmacyDTO = getPharmacyDTOTest("teste");
//        
//        when(db.insertProduct(any())).thenReturn(resultDB);
//        Assertions.assertEquals(instance.insertProduct(expResult).getDescription(), expResult.getDescription());
//        
//        when(db.insertProduct(any())).thenReturn(null);
//        Assertions.assertNull(instance.insertProduct(expResult));
//    }

    /**
     * Test of updateProduct method, of class ManageStocksController.
     */
    @Test
    public void testUpdateProduct() throws Exception {
        
        System.out.println("updateProduct");
        //Null test
        PharmacyDTO pharmacyDTO = null;
        ProductDTO product = null;
        ManageStocksController instance = new ManageStocksController();
        ProductDTO expResult = null;
        ProductDTO result = instance.updateProduct(pharmacyDTO, product);
        assertEquals(expResult, result);
        
        //Value test 
        //resultDB
        expResult = getProductDTOTest("a");
        Product resultDB = new Product(expResult.getId(), expResult.getDescription(), expResult.getPrice(), expResult.getWeight());
        StorageDB db = mock(StorageDB.class);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(db);
        pharmacyDTO = getPharmacyDTOTest("teste");
        
        when(db.updateProduct(any(Pharmacy.class), any(Product.class))).thenReturn(resultDB);
        Assertions.assertEquals(instance.updateProduct(pharmacyDTO, expResult).getDescription(), expResult.getDescription());
        
        when(db.updateProduct(any(Pharmacy.class), any(Product.class))).thenReturn(null);
        Assertions.assertNull(instance.updateProduct(pharmacyDTO, expResult));
    }



    public ProductDTO getProductDTOTest(String name) {
        return new ProductDTO(1, name, (float)1, (float)1);
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