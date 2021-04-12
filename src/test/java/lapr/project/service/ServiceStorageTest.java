package lapr.project.service;


import lapr.project.data.StorageDB;
import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;


import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceStorageTest {

    @Mock
    private StorageDB storageDB;

    private ServiceStorage srvcStorageInstance;

    private Map<Product, Integer> map1;
    private Product product1, product2, product3;
    private Storage storage1;
    private Pharmacy pharmacy1;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        srvcStorageInstance = new ServiceStorage();
        srvcStorageInstance.setStorageDB(storageDB);

        map1 =  new HashMap<>();
        product1 = new Product(1,"vacina", 55.55f, 100);
        product2 = new Product(2,"vacina2", 10.55f, 200);
        product3 = new Product(3,"vacina3", 25.0f, 150);
        map1.put(product1, 2);
        map1.put(product2, 1);
        map1.put(product3, 5);
        storage1 = new Storage(map1);

        pharmacy1 = new Pharmacy(
                "nome", 555,
                new Park(11, 50, 50, 30, 30),
                new Address(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);

    }

    @Test
    void insertProductQuantity() throws SQLException {

        Map.Entry<Product, Integer> entry1 = new AbstractMap.SimpleEntry<>(product1, 6);

        when(storageDB.insertProductQuantity(pharmacy1, entry1)).thenReturn(entry1);

        //calling the method
        Map.Entry<Product, Integer> result = srvcStorageInstance.insertProductQuantity(pharmacy1, entry1);
        assertNotNull(result);
        assertEquals(entry1, result);

        //without a valid login the return should be  null
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcStorageInstance.insertProductQuantity(pharmacy1, entry1);
        assertNull(result);

        verify(storageDB).insertProductQuantity(pharmacy1,entry1);

    }

    @Test
    void removeProductQuantity() throws SQLException {
        Map.Entry<Product, Integer> entry1 = new AbstractMap.SimpleEntry<>(product1, 6);
        //method "changes" remove quantity 6 to insert quantity -6
        Map.Entry<Product, Integer> entryForDB = new AbstractMap.SimpleEntry<>(product1, -6);


        when(storageDB.insertProductQuantity(pharmacy1, entryForDB)).thenReturn(entry1);

        //calling the method
        Map.Entry<Product, Integer> result = srvcStorageInstance.removeProductQuantity(pharmacy1, entry1);
        assertNotNull(result);
        assertEquals(entry1, result);

        //without a valid login the return should be  null
        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcStorageInstance.removeProductQuantity(pharmacy1, entry1);
        assertNull(result);

        verify(storageDB, times(1)).insertProductQuantity(pharmacy1, entryForDB);
    }

    @Test
    void updateProduct() throws SQLException {
        when(storageDB.updateProduct(pharmacy1, product1)).thenReturn(product1);
        Product result = srvcStorageInstance.updateProduct(pharmacy1, product1);
        assertEquals(product1, result);

        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcStorageInstance.updateProduct(pharmacy1, product1);
        assertNull(result);

        verify(storageDB).updateProduct(pharmacy1, product1);
    }

    @Test
    void insertProduct() throws SQLException {
        when(storageDB.insertProduct(product1)).thenReturn(product1);
        Product result = srvcStorageInstance.insertProduct(product1);
        assertEquals(product1, result);

        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcStorageInstance.insertProduct(product1);
        assertNull(result);

        verify(storageDB).insertProduct(product1);
        
        PharmaDeliveriesApp.getInstance().doLogout();
        assertNull(srvcStorageInstance.insertProduct(product1));
    }
    

    @Test
    void getProductsByPharmacy() throws SQLException {
        when(storageDB.getProductsByPharmacy(pharmacy1)).thenReturn(storage1);
        Storage result = srvcStorageInstance.getProductsByPharmacy(pharmacy1);
        assertEquals(storage1, result);

        PharmaDeliveriesApp.getInstance().doLogout();
        result = srvcStorageInstance.getProductsByPharmacy(pharmacy1);
        assertNull(result);

        verify(storageDB).getProductsByPharmacy(pharmacy1);
    }

    @Test
    void getAllProducts() throws SQLException {
        PharmaDeliveriesApp.getInstance().doLogout();
        when(storageDB.getAllProducts()).thenReturn(storage1);
        Storage result = srvcStorageInstance.getAllProducts();
        assertEquals(storage1, result);
    }
}