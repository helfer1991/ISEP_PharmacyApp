package lapr.project.controller;

import lapr.project.data.StorageDB;
import lapr.project.data.TransferDB;
import lapr.project.model.*;
import lapr.project.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static lapr.project.utils.Constants.TEST_ADMIN_EMAIL;
import static lapr.project.utils.Constants.TEST_ADMIN_KEY;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TransferProductsControllerTest {

    public TransferProductsControllerTest(){
        PharmaDeliveriesApp.getInstance().doLogin(TEST_ADMIN_EMAIL, TEST_ADMIN_KEY);
    }





    private Pharmacy pharmacy1 = new Pharmacy("nome",
            555, new Park(11, 50,50,30,30),
            new Address(3,20,-32, "rua", "2500", 100),
            33.2f, 15.5f);




    TransferProductsController transferProductsController;



    @BeforeEach
    void setUp() {
        transferProductsController = new TransferProductsController();
    }

    @Test
    void transferProducts() {

        ArrayList<Pharmacy> reachablePharmacies = new ArrayList<>();
        reachablePharmacies.add(pharmacy1);
        reachablePharmacies.add(pharmacy1);

        Product product1 = new Product(1, "prod1", 4.3f, 5);
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.addProductToShoppingCart(product1,5);

        //setup mock
        //TransferDB transferDB =  mock(TransferDB.class);
        //PharmaDeliveriesApp.getInstance().getServiceTransfer().setTransferDB(transferDB);

        boolean result = transferProductsController.transferProducts(pharmacy1, shoppingCart1, reachablePharmacies, 666);
        assertFalse(result);



    }

    @Test
    void receiveTransfer() throws SQLException {

        PharmacyDTO pharmacyDTO1 = new PharmacyDTO("nome", 555,
                new ParkDTO(11, 50,50,30,30),
                new AddressDTO(3,20,-32, "rua", "2500", 100),
                33.2f, 15.5f);
        ProductDTO productDTO1 = new ProductDTO(1, "prod1", 4.3f, 5);
        TransferDTO transferDTO1 = new TransferDTO(pharmacyDTO1,pharmacyDTO1, productDTO1, 5, 44 );

        Product product1 = new Product(1, "prod1", 4.3f, 5);
        Transfer transfer1 = new Transfer(pharmacy1,pharmacy1, product1, 5, 44 );
        AbstractMap.SimpleEntry<Product, Integer> mapEntry1 = new AbstractMap.SimpleEntry<>(product1, 8);
        AbstractMap.SimpleEntry<ProductDTO, Integer> mapEntryDTO1 = new AbstractMap.SimpleEntry<>(productDTO1, 8);



        //setup mocks
        TransferDB transferDB =  mock(TransferDB.class);
        PharmaDeliveriesApp.getInstance().getServiceTransfer().setTransferDB(transferDB);

        StorageDB storageDB = mock(StorageDB.class);
        PharmaDeliveriesApp.getInstance().getServiceStorage().setStorageDB(storageDB);

        //setup mock behaviour
        when(transferDB.validateIncomingTransfer(transfer1)).thenReturn(true);
        when(transferDB.insertTransferDocument("document content")).thenReturn(3);
        when(storageDB.insertProductQuantity(any(Pharmacy.class),any(Map.Entry.class))).thenReturn(mapEntry1);

        AbstractMap.SimpleEntry<ProductDTO, Integer> expectedResult = mapEntryDTO1;
        Map.Entry<ProductDTO, Integer> result = transferProductsController.receiveTransfer(transferDTO1);
        //assertEquals(expectedResult,result);

        //if validatrion fail, returns null
        when(transferDB.validateIncomingTransfer(transfer1)).thenReturn(false);
        result = transferProductsController.receiveTransfer(transferDTO1);
        assertNull(result);

        //if inserting document fails, returns null
        when(transferDB.validateIncomingTransfer(transfer1)).thenReturn(true);
        when(transferDB.associateDeliveryNoteToTransfer(transfer1,3)).thenReturn(false);
        result = transferProductsController.receiveTransfer(transferDTO1);
//        assertNull(result);

        //if fails to insertProduct, returns null
        when(transferDB.validateIncomingTransfer(transfer1)).thenReturn(true);
        when(transferDB.insertTransferDocument( "document content")).thenReturn(3);
        when(storageDB.insertProductQuantity(any(Pharmacy.class),any(Map.Entry.class))).thenReturn(null);
//        assertNull(result);

        
    }



}