package lapr.project.service;

import lapr.project.data.StorageDB;
import lapr.project.data.TransferDB;
import lapr.project.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ServiceTransferTest {



    private Pharmacy pharmacy1 = new Pharmacy("nome1",
            1, new Park(1, 50,50,30,30),
            new Address(3,20,-32, "rua", "2500", 100),
            33.2f, 15.5f);

    private Pharmacy pharmacy2 = new Pharmacy("nome2",
            2, new Park(2, 50,50,30,30),
            new Address(3,20,-32, "rua", "2500", 100),
            33.2f, 15.5f);

    private Pharmacy pharmacy3 = new Pharmacy("nome3",
            3, new Park(3, 50,50,30,30),
            new Address(3,20,-32, "rua", "2500", 100),
            33.2f, 15.5f);

    private Pharmacy pharmacy4 = new Pharmacy("nome4",
            4, new Park(4, 50,50,30,30),
            new Address(3,20,-32, "rua", "2500", 100),
            33.2f, 15.5f);

    private Product product1 = new Product(1,"vacina", 55.55f, 100);
    private Product product2 = new Product(2,"gelo", 5.33f, 400);
    private Product product3 = new Product(2,"vacina2", 10.55f, 200);
    private Product product4 = new Product(3,"vacina3", 25.0f, 150);
    private Product product5 = new Product(5,"vacina5", 125.0f, 315);

    Storage storage1;
    Storage storage2;
    Storage storage3;
    Storage storage4;

    ArrayList<Pharmacy> reachablePharmacies;

    @Mock
    private TransferDB transferDB;

    @Mock
    private StorageDB storageDB;

    @Mock
    private ServiceTransfer serviceTransferMOCK;


    private ServiceTransfer serviceTransfer;

    @BeforeEach
    void setUp(TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());

        serviceTransfer = new ServiceTransfer();

        storage1 = new Storage();
        storage2 = new Storage();
        storage3 = new Storage();
        storage4 = new Storage();

        reachablePharmacies = new ArrayList<>();

    }

    @Test
    void CreateTransferRequests() throws SQLException {

        List<Transfer> listTransfers = new ArrayList<>();
        Transfer transfer1 = new Transfer(pharmacy2, pharmacy3, product1, 10,88);
        listTransfers.add(transfer1);

        List<Pharmacy> lstPharmacies = new ArrayList<>();
        lstPharmacies.add(pharmacy1);
        lstPharmacies.add(pharmacy2);

        ShoppingCart productsNeeded = new ShoppingCart();
        productsNeeded.addProductToShoppingCart(product1,2);
        productsNeeded.addProductToShoppingCart(product2,2);

        //methods called are stubbed so they don't do anything -> products will not be removed from  'productsToTransfer'
        doNothing().when(serviceTransferMOCK).sortPharmaciesByDistance(lstPharmacies, pharmacy1);
        doNothing().when(serviceTransferMOCK).createTransfersByProximity(pharmacy1, lstPharmacies, productsNeeded, listTransfers, 88);
        doNothing().when(serviceTransferMOCK).createPartialTransfers(pharmacy1, lstPharmacies, productsNeeded, listTransfers, 88);

        //set normal behaviour for the method to be tested
        when(serviceTransferMOCK.createTransferRequests(pharmacy1, productsNeeded,lstPharmacies, 88)).thenCallRealMethod();


        //since at the end, 'productsToTransfer' still has products in it, then an IllegalStateException is thrown
        assertThrows(IllegalStateException.class, ()->{

            Iterable<Transfer> expectedResult =
            serviceTransferMOCK.createTransferRequests(pharmacy1, productsNeeded, lstPharmacies,88);

        });





        //if the 'productsToTransfer'  becomes empty during the method the list of transfers is returned
        ShoppingCart productsNeeded2 = new ShoppingCart();


        //methods called are stubbed so they don't do anything -> products will not be removed from  'productsToTransfer'
        doNothing().when(serviceTransferMOCK).sortPharmaciesByDistance(lstPharmacies, pharmacy1);
        doNothing().when(serviceTransferMOCK).createTransfersByProximity(pharmacy1, lstPharmacies, productsNeeded2, listTransfers, 88);
        doNothing().when(serviceTransferMOCK).createPartialTransfers(pharmacy1, lstPharmacies, productsNeeded2, listTransfers, 88);

        //set normal behaviour for the method to be tested
        when(serviceTransferMOCK.createTransferRequests(pharmacy1, productsNeeded2,lstPharmacies, 88)).thenCallRealMethod();


        //since 'productsToTransfer' is empty the method returns the list of transfers
        assertDoesNotThrow(()->{

            Iterable<Transfer> expectedResult =
                    serviceTransferMOCK.createTransferRequests(pharmacy1, productsNeeded2, lstPharmacies,88);
            assertNotNull(expectedResult);

        });

    }



    @Test
    void createTransfersByProximity_1Pharmacy() throws SQLException {
        //inject mock through setter
        serviceTransfer.setStorageDB(storageDB);

        //pharmacy 2 and 3 are reachable
        reachablePharmacies.add(pharmacy2);
        reachablePharmacies.add(pharmacy3);

        storage2.addProductToStorage(product1, 2);
        storage2.addProductToStorage(product2, 2);
        storage2.addProductToStorage(product3, 2);

        storage3.addProductToStorage(product1, 5);
        storage3.addProductToStorage(product2, 5);
        storage3.addProductToStorage(product3, 5);
        storage3.addProductToStorage(product4, 5);


        //the goal is to create transfers for these products
        ShoppingCart productsNeeded = new ShoppingCart();
        productsNeeded.addProductToShoppingCart(product1,2);
        productsNeeded.addProductToShoppingCart(product2,2);


        //Result -> the list should change according to the transfers created and added inside the method
        ArrayList<Transfer> result = new ArrayList<>();
        Transfer transfer1 = new Transfer(pharmacy2, pharmacy3, product1, 10,88);
        result.add(transfer1);



        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy3)).thenReturn(storage3);

        assertFalse(productsNeeded.getProductMap().isEmpty());

        //since the reachablePharmacies list is already ordered the method will try to transfer from the first
        //pharmacy on the list, then the second, etc.
        serviceTransfer.createTransfersByProximity(pharmacy1, reachablePharmacies, productsNeeded, result,5);

        //if all transfers where created there shouldn't be any products left to transfer
        assertTrue(productsNeeded.getProductMap().isEmpty());

        //building expected Result -> pharamacy 2 is nearest and has all the products!
        ArrayList<Transfer> expectedResult = new ArrayList<>();
        expectedResult.add(transfer1);
        expectedResult.add(new Transfer(pharmacy1, pharmacy2, product1, 2,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy2, product2, 2,5));

        assertEquals(expectedResult,result);

    }

    @Test
    void createTransfersByProximity_multiplePharmacies() throws SQLException {
        //inject mock through setter
        serviceTransfer.setStorageDB(storageDB);

        //pharmacy 2, 3 and 4 are reachable
        reachablePharmacies.add(pharmacy2);
        reachablePharmacies.add(pharmacy3);
        reachablePharmacies.add(pharmacy4);

        //storage of pharmacy 2 -> has 0 products needed
        storage2.addProductToStorage(product1, 2);
        storage2.addProductToStorage(product2, 2);

        //storage of pharmacy 3 -> has 1 product needed (more quantity than needed)
        storage3.addProductToStorage(product1, 5);
        storage3.addProductToStorage(product2, 5);
        storage3.addProductToStorage(product3, 5);

        //storage of pharmacy 4 -> has all products needed in sufficient quantity
        storage4.addProductToStorage(product3, 5);
        storage4.addProductToStorage(product4, 5);
        storage4.addProductToStorage(product5, 5);

        //the goal is to create transfers for these products
        ShoppingCart productsNeeded = new ShoppingCart();
        productsNeeded.addProductToShoppingCart(product3,4);
        productsNeeded.addProductToShoppingCart(product4,3);
        productsNeeded.addProductToShoppingCart(product5,3);


        //Result -> the list should change according to the transfers created and added inside the method
        ArrayList<Transfer> result = new ArrayList<>();
        Transfer transfer1 = new Transfer(pharmacy2, pharmacy3, product1, 10,88);
        result.add(transfer1);



        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy3)).thenReturn(storage3);
        when(storageDB.getProductsByPharmacy(pharmacy4)).thenReturn(storage4);

        assertFalse(productsNeeded.getProductMap().isEmpty());

        //since the reachablePharmacies list is already ordered the method will try to transfer from the first
        //pharmacy on the list, then the second, etc.
        serviceTransfer.createTransfersByProximity(pharmacy1, reachablePharmacies, productsNeeded, result,5);

        //if all transfers where created there shouldn't be any products left to transfer
        assertTrue(productsNeeded.getProductMap().isEmpty());

        //building expected Result ->  product3 from pharmacy3 and product4 and 5 from pharmacy4
        ArrayList<Transfer> expectedResult = new ArrayList<>();
        expectedResult.add(transfer1); //same as result list
        expectedResult.add(new Transfer(pharmacy1, pharmacy3, product3, 4,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy4, product4, 3,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy4, product5, 3,5));

        assertEquals(expectedResult,result);

    }

    @Test
    void createTransfersByProximity_noPartialTransfers() throws SQLException {
        //inject mock through setter
        serviceTransfer.setStorageDB(storageDB);

        //pharmacy 2, 3 and 4 are reachable
        reachablePharmacies.add(pharmacy2);
        reachablePharmacies.add(pharmacy3);
        reachablePharmacies.add(pharmacy4);

        //storage of pharmacy 2 -> has 0 products needed
        storage2.addProductToStorage(product1, 2);
        storage2.addProductToStorage(product2, 2);

        //storage of pharmacy 3 -> has 1 product needed (not enough quantity!!)
        storage3.addProductToStorage(product1, 5);
        storage3.addProductToStorage(product2, 5);
        storage3.addProductToStorage(product3, 5);

        //storage of pharmacy 4 -> has all products needed (but not enough quantity of product 3!!)
        storage4.addProductToStorage(product3, 5);
        storage4.addProductToStorage(product4, 5);
        storage4.addProductToStorage(product5, 5);

        //the goal is to create transfers for these products
        ShoppingCart productsNeeded = new ShoppingCart();
        productsNeeded.addProductToShoppingCart(product3,8);
        productsNeeded.addProductToShoppingCart(product4,3);
        productsNeeded.addProductToShoppingCart(product5,3);


        //Result -> the list should change according to the transfers created and added inside the method
        ArrayList<Transfer> result = new ArrayList<>();
        Transfer transfer1 = new Transfer(pharmacy2, pharmacy3, product1, 10,88);
        result.add(transfer1);



        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy3)).thenReturn(storage3);
        when(storageDB.getProductsByPharmacy(pharmacy4)).thenReturn(storage4);

        assertEquals(3,productsNeeded.getProductMap().size());

        //since the reachablePharmacies list is already ordered the method will try to transfer from the first
        //pharmacy on the list, then the second, etc.
        serviceTransfer.createTransfersByProximity(pharmacy1, reachablePharmacies, productsNeeded, result,5);

        //one transfer should NOT have been made so there should remain one product in 'productsNeeded'
        assertEquals(1,productsNeeded.getProductMap().size());

        //building expected Result -> products 4 and 5 from pharmacy 4. (No transfer for product 3!)
        ArrayList<Transfer> expectedResult = new ArrayList<>();
        expectedResult.add(transfer1); //same as result list
        expectedResult.add(new Transfer(pharmacy1, pharmacy4, product4, 3,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy4, product5, 3,5));

        assertEquals(expectedResult,result);
    }




    @Test
    void createPartialTransfers() throws SQLException {

        //inject mock through setter
        serviceTransfer.setStorageDB(storageDB);

        //pharmacy 2 and 3 are reachable
        reachablePharmacies.add(pharmacy2);
        reachablePharmacies.add(pharmacy3);


        //pharmacy 2 has both products needed (1 and 2) -> should be first option for prod2 and second option for prod1
        storage2.addProductToStorage(product1, 6);
        storage2.addProductToStorage(product2, 7);
        storage2.addProductToStorage(product3, 2);

        //pharmacy 3 has both products needed (1 and 2) -> should be first option for prod1 and second option for prod2
        storage3.addProductToStorage(product1, 9);
        storage3.addProductToStorage(product2, 5);
        storage2.addProductToStorage(product3, 2);
        storage2.addProductToStorage(product4, 17);



        //the goal is to create transfers for these products (4 transfers in total)
        ShoppingCart productsNeeded = new ShoppingCart();
        productsNeeded.addProductToShoppingCart(product1,10);
        productsNeeded.addProductToShoppingCart(product2,10);


        //Result -> the list should change according to the transfers created and added inside the method
        ArrayList<Transfer> result = new ArrayList<>();
        Transfer transfer1 = new Transfer(pharmacy2, pharmacy3, product1, 55,88); //dummy transfer
        result.add(transfer1);



        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy3)).thenReturn(storage3);

        assertFalse(productsNeeded.getProductMap().isEmpty());

        //since the reachablePharmacies list is already ordered the method will try to transfer from the first
        //pharmacy on the list, then the second, etc.
        serviceTransfer.createPartialTransfers(pharmacy1, reachablePharmacies, productsNeeded, result,5);

        //if all transfers where created there shouldn't be any products left to transfer
        assertTrue(productsNeeded.getProductMap().isEmpty());

        //building expected Result -> pharmacy 2 is nearest and has all the products!
        ArrayList<Transfer> expectedResult = new ArrayList<>();
        expectedResult.add(transfer1); //dummy transfer
        expectedResult.add(new Transfer(pharmacy1, pharmacy3, product1, 9,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy2, product1, 1,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy2, product2, 7,5));
        expectedResult.add(new Transfer(pharmacy1, pharmacy3, product2, 3,5));


        assertEquals(expectedResult,result);

    }

    @Test
    void sortPharmacyByProductStockDesc() throws SQLException {
        //inject mock through setter
        serviceTransfer.setStorageDB(storageDB);

        //list of pharmacies to order
        reachablePharmacies.add(pharmacy1);
        reachablePharmacies.add(pharmacy2);
        reachablePharmacies.add(pharmacy3);
        reachablePharmacies.add(pharmacy4);

        //pharmacy 1
        storage1.addProductToStorage(product1, 0);
        storage1.addProductToStorage(product2, 98);
        storage1.addProductToStorage(product3, 0);

        //pharmacy 2
        storage2.addProductToStorage(product1, 9);
        storage2.addProductToStorage(product2, 1);
        storage2.addProductToStorage(product3, 0);
        storage2.addProductToStorage(product4, 0);

        //pharmacy 3
        storage3.addProductToStorage(product1, 3);
        storage3.addProductToStorage(product2, 99);
        storage3.addProductToStorage(product4, 0);

        //pharmacy 4
        storage4.addProductToStorage(product1, 17);
        storage4.addProductToStorage(product2, 2);
        storage4.addProductToStorage(product4, 0);

        when(storageDB.getProductsByPharmacy(pharmacy1)).thenReturn(storage1);
        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy3)).thenReturn(storage3);
        when(storageDB.getProductsByPharmacy(pharmacy4)).thenReturn(storage4);



        //for product 1 the correct order is -> 4, 2, 3, 1
        List<Pharmacy> expectedResult = new ArrayList<>();
        expectedResult.add(pharmacy4);
        expectedResult.add(pharmacy2);
        expectedResult.add(pharmacy3);
        expectedResult.add(pharmacy1);

        serviceTransfer.sortPharmaciesByProductStockDesc(reachablePharmacies, product1);

        assertEquals(expectedResult, reachablePharmacies);

        //for product 2 the correct order is -> 3,1,4,2
        expectedResult = new ArrayList<>();
        expectedResult.add(pharmacy4);
        expectedResult.add(pharmacy2);
        expectedResult.add(pharmacy3);
        expectedResult.add(pharmacy1);

        serviceTransfer.sortPharmaciesByProductStockDesc(reachablePharmacies, product1);

        assertEquals(expectedResult, reachablePharmacies);
    }

/*
    @Test
    void sortPharmaciesByDistance() {

    }

    @Test
    void registerTransferToBeDelivered() {
    }

 */
    @Test
    void getNeededChargeForTrip() {
        //assertEquals(70, serviceTransfer.getNeededChargeForTrip(pharmacy1.getAddress(), pharmacy2.getAddress()));
    }
}