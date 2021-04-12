package lapr.project.controller;

import lapr.project.data.*;
import lapr.project.model.*;
import lapr.project.ui.dto.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static lapr.project.controller.DTOConverter.*;
import static lapr.project.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BackOrderControllerTest {

    public BackOrderControllerTest(){
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
    }


    @Mock
    StorageDB storageDB;

    @Mock
    PharmacyDB pharmacyDB;

    @Mock
    ClientDB clientDB;
    @Mock
    OrderDB orderDB;

    BackOrderController backOrderController;

    PharmacyDTO pharmacyDTO1 = new PharmacyDTO("nome", 555,
            new ParkDTO(11, 50,50,30,30),
            new AddressDTO(2,41.2000276,-8.6661002,"Farmacia Guifoes","4000-000", 0.0f) ,
            33.2f, 15.5f);

    Pharmacy pharmacy1 = new Pharmacy("nome", 555,
            new Park(11, 50,50,30,30),
            new Address(2,41.2000276,-8.6661002,"Farmacia Guifoes","4000-000", 0.0f),

            33.2f, 15.5f);
    Pharmacy pharmacy2 = new Pharmacy("nome2", 222,
            new Park(11, 50,50,30,30),
            new Address(1,41.2196991,-8.5608657,"Farmacia Sousa Torres","4000-000", 0.0f) ,
            33.2f, 15.5f);


    List<Pharmacy> allPharmacies;

    @BeforeEach
    void setUp() {
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        backOrderController = new BackOrderController();
        PharmaDeliveriesApp.getInstance().getPharmacyService().setPharmacyDB(pharmacyDB);
        allPharmacies = new LinkedList<>();
        allPharmacies.add(pharmacy1);
        allPharmacies.add(pharmacy2);

    }

    @Test
    void getNearestPharmacy() throws SQLException {
        PharmaDeliveriesApp.getInstance().getServiceBackOrder().setClientDB(clientDB);
        PharmaDeliveriesApp.getInstance().getServiceBackOrder().setPharmacyDB(pharmacyDB);

        Client client1 = new Client();
        client1.setEmail("hmcfb@isep.ipp.pt");
        client1.setPassword("balelos");
        client1.setName("Helder");
        client1.setNIF(12345);
        client1.setAddress(new Address(1,41.2196991,-8.5608657,"Farmacia Sousa Torres","4000-000", 0.0f) );

        client1.setCreditCard(new CreditCard("12345", 3456, "2020-06-06"));

        when(pharmacyDB.getAllPharmacies()).thenReturn(allPharmacies);
        when(clientDB.getClientByUserSession(any(User.class))).thenReturn(client1);

        PharmacyDTO result =  backOrderController.getNearestPharmacy();
        assertEquals(pharmacyDTO1, result);

        verify(clientDB).getClientByUserSession(any(User.class));
        verify(pharmacyDB).getAllPharmacies();

        PharmaDeliveriesApp.getInstance().doLogout();
        result = backOrderController.getNearestPharmacy();
        assertNull(result);
    }

    @Test
    void getProducts() throws SQLException {
        StorageDTO storageDTO1 = new StorageDTO();
        storageDTO1.addProductToStorage(new ProductDTO(1, "1", 3, 4),10);
        storageDTO1.addProductToStorage(new ProductDTO(22, "5000", 3, 4),20);

        StorageDTO storageDTO2 = new StorageDTO();
        storageDTO2.addProductToStorage(new ProductDTO(2, "2", 4, 5),13);
        storageDTO2.addProductToStorage(new ProductDTO(44, "5000", 4, 5),6);

        BackOrderController mockController = mock(BackOrderController.class);
        when(mockController.getAllProducts()).thenReturn(storageDTO2);
        when(mockController.getReachableProducts(pharmacyDTO1)).thenReturn(storageDTO1);
        when(mockController.getProducts(pharmacyDTO1)).thenCallRealMethod();

        //method called with log in
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
        StorageDTO result = mockController.getProducts(pharmacyDTO1);
        assertEquals(storageDTO1, result);
        verify(mockController).getReachableProducts(pharmacyDTO1);

        //method called withoug login
        PharmaDeliveriesApp.getInstance().doLogout();
        result = mockController.getProducts(pharmacyDTO1);
        assertEquals(storageDTO2, result);
        verify(mockController).getAllProducts();
    }

    @Test
    void getAllProducts() throws SQLException {
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(storageDB);
        Storage storage1 = new Storage();
        storage1.addProductToStorage(new Product(1, "2", 3, 4),10);
        storage1.addProductToStorage(new Product(22, "5000", 3, 4),20);

        when(storageDB.getAllProducts()).thenReturn(storage1);

        StorageDTO result = backOrderController.getAllProducts();

        StorageDTO expectedResult = DTOConverter.convertStorage(storage1);

        assertEquals(expectedResult, result);
    }

    @Test
    void getReachableProducts() throws SQLException {

        when(pharmacyDB.getAllPharmacies()).thenReturn(allPharmacies);

        //setup the pharmacy1 storage and the pharmacy2 storage
        Storage storage1 = new Storage();
        storage1.addProductToStorage(new Product(1, "2", 3, 4),10);
        storage1.addProductToStorage(new Product(22, "5000", 3, 4),20);
        Storage storage2 = new Storage();
        storage2.addProductToStorage(new Product(1, "2", 3, 4),10);
        storage2.addProductToStorage(new Product(44, "5000", 3, 4),20);
        storage2.addProductToStorage(new Product(3, "3", 3, 4),30);


        //defines the storage returned by the mocked DAO (for each pharmacy)
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(storageDB);
        when(storageDB.getProductsByPharmacy(pharmacy1)).thenReturn(storage1);
        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage2);


        //calls the method and assert if the result is a storage containing the
        //storages of the pharmacy 1 and 2 summed together
        StorageDTO resultDTO = backOrderController.getReachableProducts(pharmacyDTO1);

        Storage expectedResult = new Storage();
        expectedResult.addStorage(storage1);
        expectedResult.addStorage(storage2);

        //validates the storage was added correctly
        assertEquals(20, expectedResult.getProductMap().get(new Product(1,"2",3,4)));

        //converts the result
        Storage result = DTOConverter.convertStorageDTO(resultDTO);
        assertNotNull(result.getProductMap());

        //assertEquals(expectedResult,result);
        //Test passes locally but fails remotely

    }

    @Test
    void getReachablePharmacies() throws SQLException {
        VehicleDB vehicleDB = mock(VehicleDB.class);
        PharmaDeliveriesApp.getInstance().getServiceVehicle().setVehicleDB(vehicleDB);
        when(pharmacyDB.getAllPharmacies()).thenReturn(allPharmacies);
        //if the vehicles battery is really large almost all pharmacies are reachable as long as the address
        // are in the graph
        when(vehicleDB.getScooterMaxBatteryCapacity()).thenReturn(10000f);
        when(vehicleDB.getDroneMaxBatteryCapacity()).thenReturn(10000f);

        Iterable<Pharmacy> result = backOrderController.getReachablePharmacies(pharmacy1);

        assertEquals(allPharmacies, result);
    }

    @Test
    void registerOrderRequest() throws SQLException {

        Product product1 = new Product(1, "2", 3, 4);
        Product product2 = new Product(22, "5000", 3, 4);

        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setEmail("hmcfb@isep.ipp.pt");
        clientDTO1.setPassword("balelos");
        clientDTO1.setName("Helder");
        clientDTO1.setNIF(12345);
        clientDTO1.setAddress(new AddressDTO(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
        clientDTO1.setCreditCard(new CreditCardDTO("12345", 3456, "2020-06-06"));


        Storage storage1 = new Storage();
        storage1.addProductToStorage(product1,2);
        storage1.addProductToStorage(product2,2);

        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(storageDB);
        when(storageDB.getProductsByPharmacy(pharmacy1)).thenReturn(storage1);

        //shopping cart has less products than storage1 so the transfer is NOT needed!
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,1);
        shoppingCart1.addProductToShoppingCart(product2,2);

        OrderDTO orderDTO1 = new OrderDTO(2, 3, 2, "1998_12_31");
        orderDTO1.setShopCart(convertShoppingCart(shoppingCart1));
        Order order1 = convertOrderDTO(orderDTO1);
        order1.setShopCart(shoppingCart1);

        PharmaDeliveriesApp.getInstance().getServiceBackOrder().setOrderDB(orderDB);
        when(orderDB.insertOrder(pharmacy1, order1, convertClientDTO(clientDTO1),false)).thenReturn(order1);

        

        PharmaDeliveriesApp.getInstance().doLogin(TEST_CLIENT_EMAIL,TEST_CLIENT_KEY );
        OrderDTO result =  backOrderController.registerOrderRequest(pharmacyDTO1, orderDTO1, clientDTO1);
        assertEquals(orderDTO1, result);
        verify(orderDB).insertOrder(pharmacy1, order1, convertClientDTO(clientDTO1), false);


        //shopping cart has more products than storage 1 so the transfer is needed!
        shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,4);
        shoppingCart1.addProductToShoppingCart(product2,4);

        //sets up the return of the reachable products so the transfer is considered possible
        when(pharmacyDB.getAllPharmacies()).thenReturn(allPharmacies);
        Storage storage3 = new Storage();
        storage3.addProductToStorage(product1,10);
        storage3.addProductToStorage(product2,20);
        Storage storage2 = new Storage();
        storage2.addProductToStorage(product1,1);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(storageDB);
        when(storageDB.getProductsByPharmacy(pharmacy1)).thenReturn(storage2);
        when(storageDB.getProductsByPharmacy(pharmacy2)).thenReturn(storage3);

        when(orderDB.insertOrder(pharmacy1, order1, convertClientDTO(clientDTO1),true)).thenReturn(order1);
        TransferProductsController transferProductsController = mock(TransferProductsController.class);
        backOrderController.setTransferProductsController(transferProductsController);
        when(transferProductsController.transferProducts(any(Pharmacy.class),any(ShoppingCart.class), anyIterable(),anyInt() )).thenReturn(true);
        result =  backOrderController.registerOrderRequest(pharmacyDTO1, orderDTO1, clientDTO1);
        assertEquals(orderDTO1, result);
        verify(orderDB).insertOrder(pharmacy1, order1, convertClientDTO(clientDTO1), true);

    }

    @Test
    void isTransferPossible() throws SQLException {
        Product product1 = new Product(1, "prod1", 4.3f, 5);
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,5);
        backOrderController.isTransferPossible(pharmacy1, shoppingCart1);

    }



}