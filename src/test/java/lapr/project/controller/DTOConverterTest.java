package lapr.project.controller;

import lapr.project.model.*;
import lapr.project.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DTOConverterTest {

    private Client client1;
    private ClientDTO clientDTO1;

    private Courier courier1;
    private CourierDTO courierDTO1;

    private final Product product1 = new Product(1, "prod1", 4.3f, 5);
    private final ProductDTO productDTO1 = new ProductDTO(1, "prod1", 4.3f, 5);

    private final ShoppingCart shoppingCart1 = new ShoppingCart();
    private final ShoppingCartDTO shoppingCartDTO1 = new ShoppingCartDTO();

    private final Address address1 = new Address(3, 20, -32, "rua", "2500", 100);
    private final Address address2 = new Address(4, 20, -32, "rua", "2500", 100);
    private final Address address3 = new Address(5, 20, -32, "rua", "2500", 100);

    private final AddressDTO addressDTO1 = new AddressDTO(3, 20, -32, "rua", "2500", 100);
    private final AddressDTO addressDTO2 = new AddressDTO(4, 20, -32, "rua", "2500", 100);
    private final AddressDTO addressDTO3 = new AddressDTO(5, 20, -32, "rua", "2500", 100);

    private final Storage storage1 = new Storage();
    private final StorageDTO storageDTO1 = new StorageDTO();

    private final Scooter scooter1 = new Scooter(13, 55564, 450, new Battery(1, 150), 75);
    private final ScooterDTO scooterDTO1 = new ScooterDTO(13, 55564, 450, new BatteryDTO(1, 150), 75);

    private final Drone drone1 = new Drone(1, 2, 3, new Battery(4, 5), 80);
    private final DroneDTO droneDTO1 = new DroneDTO(1, 2, 3, new BatteryDTO(4, 5), 80);

    private final Pharmacy pharmacy1 = new Pharmacy("nome", 555,
            new Park(11, 50, 50, 30, 30),
            new Address(3, 20, -32, "rua", "2500", 100),
            33.2f, 15.5f);

    private final PharmacyDTO pharmacyDTO1 = new PharmacyDTO("nome", 555,
            new ParkDTO(11, 50, 50, 30, 30),
            new AddressDTO(3, 20, -32, "rua", "2500", 100),
            33.2f, 15.5f);
    private final DeliveryRun deliveryRun1 = new DeliveryRun(13, "2004");
    private final DeliveryRunDTO deliveryRunDTO1 = new DeliveryRunDTO(13, "2004");

    private final Deliverable deliverable1 = new Order(1, 3, 2, "1998_12_31");
    private final Deliverable deliverable2 = new Order(2, 3, 2, "1998_12_31");
    private final Deliverable deliverable3 = new Transfer(pharmacy1, pharmacy1, product1, 5, 44);

    private final DeliverableDTO deliverableDTO1 = new OrderDTO(1, 3, 2, "1998_12_31");
    private final DeliverableDTO deliverableDTO2 = new OrderDTO(2, 3, 2, "1998_12_31");
    private final DeliverableDTO deliverableDTO3 = new TransferDTO(pharmacyDTO1, pharmacyDTO1, productDTO1, 5, 44);

    @BeforeEach
    void setUp() {
        client1 = new Client();
        client1.setEmail("hmcfb@isep.ipp.pt");
        client1.setPassword("balelos");
        client1.setName("Helder");
        client1.setNIF(12345);
        client1.setAddress(new Address(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
        client1.setCreditCard(new CreditCard("12345", 3456, "2020-06-06"));

        clientDTO1 = new ClientDTO();
        clientDTO1.setEmail("hmcfb@isep.ipp.pt");
        clientDTO1.setPassword("balelos");
        clientDTO1.setName("Helder");
        clientDTO1.setNIF(12345);
        clientDTO1.setAddress(new AddressDTO(1, 123.4, 124.5, "Rua sesamo", "1234", 100));
        clientDTO1.setCreditCard(new CreditCardDTO("12345", 3456, "2020-06-06"));

        courier1 = new Courier(55, true);
        courier1.setNIF(259723444);
        courier1.setName("Catarina");
        courier1.setEmail("catarina@isep.ipp.pt");
        courier1.setPassword("catarina");

        courierDTO1 = new CourierDTO(55, true);
        courierDTO1.setNIF(259723444);
        courierDTO1.setName("Catarina");
        courierDTO1.setEmail("catarina@isep.ipp.pt");
        courierDTO1.setPassword("catarina");

    }

    @Test
    void convertPharmacy() {
        PharmacyDTO result = DTOConverter.convertPharmacy(pharmacy1);
        PharmacyDTO expectedResult = pharmacyDTO1;
        assertEquals(expectedResult, result);
        result = DTOConverter.convertPharmacy(null);
        assertNull(result);
    }

    @Test
    void convertPharmacyDTO() {
        Pharmacy expectedResult = pharmacy1;
        Pharmacy result = DTOConverter.convertPharmacyDTO(pharmacyDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertPharmacyDTO(null);
        assertNull(result);
    }

    @Test
    void converPark() {
        ParkDTO parkDTO1 = new ParkDTO(1, 10, 15, 5, 6);
        Park expectedResult = new Park(1, 10, 15, 5, 6);
        Park result = DTOConverter.convertParkDTO(parkDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertParkDTO(null);
        assertNull(result);
    }

    @Test
    void converParkDTO() {
        ParkDTO expectedResult = new ParkDTO(1, 10, 15, 5, 6);
        Park park1 = new Park(1, 10, 15, 5, 6);
        ParkDTO result = DTOConverter.convertPark(park1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertPark(null);
        assertNull(result);
    }

    @Test
    void convertProductDTO() {
        Product expectedResult = product1;
        Product result = DTOConverter.convertProductDTO(productDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertProductDTO(null);
        assertNull(result);
    }

    @Test
    void convertProduct() {
        ProductDTO expectedResult = productDTO1;
        ProductDTO result = DTOConverter.convertProduct(product1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertProduct(null);
        assertNull(result);
    }

    @Test
    void convertShoppingCartDTO() {
        shoppingCart1.addProductToShoppingCart(product1, 5);
        shoppingCartDTO1.addProductToShoppingCart(productDTO1, 5);

        ShoppingCart expectedResult = shoppingCart1;
        ShoppingCart result = DTOConverter.convertShoppingCartDTO(shoppingCartDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertShoppingCartDTO(null);
        assertNull(result);
    }

    @Test
    void convertShoppingCart() {
        shoppingCart1.addProductToShoppingCart(product1, 5);
        shoppingCartDTO1.addProductToShoppingCart(productDTO1, 5);

        ShoppingCartDTO expectedResult = shoppingCartDTO1;
        ShoppingCartDTO result = DTOConverter.convertShoppingCart(shoppingCart1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertShoppingCart(null);

        assertNull(result);
    }

    @Test
    void convertOrder() {
        Order order1 = new Order(2, 3, 2, "1998_12_31");
        OrderDTO expectedResult = new OrderDTO(2, 3, 2, "1998_12_31");
        OrderDTO result = DTOConverter.convertOrder(order1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertOrder(null);
        assertNull(result);
    }

    @Test
    void convertOrderDTO() {
        OrderDTO orderDTO1 = new OrderDTO(2, 3, 2, "1998_12_31");
        Order expectedResult = new Order(2, 3, 2, "1998_12_31");
        Order result = DTOConverter.convertOrderDTO(orderDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertOrderDTO(null);
        assertNull(result);
    }

    @Test
    void convertAddress() {
        AddressDTO expectedResult = addressDTO1;
        AddressDTO result = DTOConverter.convertAddress(address1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertAddress(null);
        assertNull(result);
    }

    @Test
    void convertAddressDTO() {
        Address expectedResult = address1;
        Address result = DTOConverter.convertAddressDTO(addressDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertAddressDTO(null);
        assertNull(result);
    }

    @Test
    void convertStorage() {
        storage1.addProductToStorage(product1, 7);
        storageDTO1.addProductToStorage(productDTO1, 7);

        StorageDTO expectedResult = storageDTO1;
        StorageDTO result = DTOConverter.convertStorage(storage1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertStorage(null);
        assertNull(result);

    }

    @Test
    void convertStorageDTO() {
        storage1.addProductToStorage(product1, 7);
        storageDTO1.addProductToStorage(productDTO1, 7);

        Storage expectedResult = storage1;
        Storage result = DTOConverter.convertStorageDTO(storageDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertStorageDTO(null);
        assertNull(result);
    }

    @Test
    void convertClientDTO() {
        Client expectedResult = client1;
        Client result = DTOConverter.convertClientDTO(clientDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertClientDTO(null);
        assertNull(result);
    }

    @Test
    void convertClient() {
        ClientDTO expectedResult = clientDTO1;
        ClientDTO result = DTOConverter.convertClient(client1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertClient(null);
        assertNull(result);
    }

    @Test
    void convertCourierDTO() {
        Courier expectedResult = courier1;
        Courier result = DTOConverter.convertCourierDTO(courierDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertCourierDTO(null);
        assertNull(result);
    }

    @Test
    void convertCourier() {
        CourierDTO expectedResult = courierDTO1;
        CourierDTO result = DTOConverter.convertCourier(courier1);
        assertEquals(expectedResult.getEmail(), result.getEmail());
        result = DTOConverter.convertCourier(null);
        assertNull(result);
    }

    @Test
    void convertBattery() {
        Battery battery1 = new Battery(1, 400);
        BatteryDTO expectedResult = new BatteryDTO(1, 400);
        BatteryDTO result = DTOConverter.convertBattery(battery1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertBattery(null);
        assertNull(result);
    }

    @Test
    void convertBatteryDTO() {
        BatteryDTO batteryDTO1 = new BatteryDTO(2, 200);
        Battery expectedResult = new Battery(2, 200);
        Battery result = DTOConverter.convertBatteryDTO(batteryDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertBatteryDTO(null);
        assertNull(result);
    }

    @Test
    void convertScooter() {
        ScooterDTO expectedResult = scooterDTO1;
        ScooterDTO result = DTOConverter.convertScooter(scooter1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertScooter(null);
        assertNull(result);
    }

    @Test
    void convertScooterDTO() {
        Scooter expectedResult = scooter1;
        Scooter result = DTOConverter.convertScooterDTO(scooterDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertScooterDTO(null);
        assertNull(result);
    }

    @Test
    void convertTransferDTO() {
        Transfer expectedResult = new Transfer(pharmacy1, pharmacy1, product1, 5, 44);
        TransferDTO transferDTO1 = new TransferDTO(pharmacyDTO1, pharmacyDTO1, productDTO1, 5, 44);
        Transfer result = DTOConverter.convertTransferDTO(transferDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertTransferDTO(null);
        assertNull(result);
    }

    @Test
    void convertTransfer() {
        Transfer transfer1 = new Transfer(pharmacy1, pharmacy1, product1, 5, 44);
        TransferDTO expectedResult = new TransferDTO(pharmacyDTO1, pharmacyDTO1, productDTO1, 5, 44);
        TransferDTO result = DTOConverter.convertTransfer(transfer1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertTransfer(null);
        assertNull(result);
    }

    @Test
    void convertMapEntry() {
        AbstractMap.SimpleEntry<Product, Integer> mapEntry1 = new AbstractMap.SimpleEntry<>(product1, 8);
        AbstractMap.SimpleEntry<ProductDTO, Integer> expectedResult = new AbstractMap.SimpleEntry<>(productDTO1, 8);
        Map.Entry<ProductDTO, Integer> result = DTOConverter.convertMapEntry(mapEntry1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertMapEntry(null);
        assertNull(result);

    }

    @Test
    void convertMapEntryDTO() {
        AbstractMap.SimpleEntry<Product, Integer> expectedResult = new AbstractMap.SimpleEntry<>(product1, 8);
        AbstractMap.SimpleEntry<ProductDTO, Integer> mapEntryDTO1 = new AbstractMap.SimpleEntry<>(productDTO1, 8);
        Map.Entry<Product, Integer> result = DTOConverter.convertMapEntryDTO(mapEntryDTO1);
        assertEquals(expectedResult, result);
        result = DTOConverter.convertMapEntryDTO(null);
        assertNull(result);
    }

    @Test
    void convertDeliveryRunDTO() {
        DeliveryRun expectedResult = deliveryRun1;
        LinkedList<Deliverable> deliverables = new LinkedList<>();
        deliverables.add(new Order(1, 3, 2, "1998_12_31"));
        deliverables.add(new Order(2, 3, 2, "1998_12_31"));
        deliverables.add(new Transfer(pharmacy1, pharmacy1, product1, 5, 44));
        expectedResult.setDeliverables(deliverables);

        DeliveryRunDTO deliveryRunDTO = deliveryRunDTO1;
        LinkedList<DeliverableDTO> deliverablesDTO = new LinkedList<>();
        deliverablesDTO.add(new OrderDTO(1, 3, 2, "1998_12_31"));
        deliverablesDTO.add(new OrderDTO(2, 3, 2, "1998_12_31"));
        deliverablesDTO.add(new TransferDTO(pharmacyDTO1, pharmacyDTO1, productDTO1, 5, 44));
        deliveryRunDTO.setDeliverables(deliverablesDTO);

        DeliveryRun result = DTOConverter.convertDeliveryRunDTO(deliveryRunDTO);
        assertEquals(expectedResult, result);
        assertNull(DTOConverter.convertDeliveryRunDTO(null));
    }

    @Test
    void convertDeliveryRun() {

        DeliveryRun deliveryRun = deliveryRun1;
        LinkedList<Deliverable> deliverables = new LinkedList<>();
        deliverables.add(deliverable1);
        deliverables.add(deliverable2);
        deliverables.add(deliverable3);
        deliveryRun.setDeliverables(deliverables);

        DeliveryRunDTO expectedResult = deliveryRunDTO1;
        LinkedList<DeliverableDTO> deliverablesDTO = new LinkedList<>();
        deliverablesDTO.add(deliverableDTO1);
        deliverablesDTO.add(deliverableDTO2);
        deliverablesDTO.add(deliverableDTO3);
        expectedResult.setDeliverables(deliverablesDTO);

        DeliveryRunDTO result = DTOConverter.convertDeliveryRun(deliveryRun);
        assertEquals(expectedResult, result);
        assertNull(DTOConverter.convertDeliveryRun(null));
    }

    @Test
    void convertDeliverable() {
        assertEquals(deliverableDTO1, DTOConverter.convertDeliverable(deliverable1));
        assertNull(DTOConverter.convertDeliverable(null));
    }

    @Test
    void convertDeliverableDTO() {
        assertEquals(deliverable1, DTOConverter.convertDeliverableDTO(deliverableDTO1));
        assertNull(DTOConverter.convertDeliverableDTO(null));

    }

    @Test
    void convertEstimatesDTO() {
        Estimates expectedResult = new Estimates(deliveryRun1);
        LinkedHashMap<Address, Double> costMap = new LinkedHashMap<>();
        costMap.put(address1, 330.5);
        costMap.put(address2, 700.15);
        expectedResult.setEnergyCostMap(costMap);
        expectedResult.setRequiredBatteryToCompletePath(55);
        expectedResult.setRequiredBatteryToReachNextPharmacy(30);
        LinkedHashMap<Address, Double> distanceCostMap = new LinkedHashMap<>();
        distanceCostMap.put(address1, 111.88);
        expectedResult.setDistanceCostMap(distanceCostMap);

       
        EstimatesDTO estimateDTO1 = new EstimatesDTO(deliveryRunDTO1);
        LinkedHashMap<AddressDTO, Double> costMapDTO = new LinkedHashMap<>();
        costMapDTO.put(addressDTO1, 330.5);
        costMapDTO.put(addressDTO2, 700.15);
        estimateDTO1.setEnergyCostMapDTO(costMapDTO);
        estimateDTO1.setRequiredBatteryToCompletePathDTO(55);
        estimateDTO1.setRequiredBatteryToReachNextPharmacyDTO(30);
        LinkedHashMap<AddressDTO, Double> distanceCostMapDTO = new LinkedHashMap<>();
        distanceCostMapDTO.put(addressDTO1, 111.88);
        estimateDTO1.setDistanceCostMapDTO(distanceCostMapDTO);

        assertEquals(expectedResult, DTOConverter.convertEstimatesDTO(estimateDTO1));
        assertNull(DTOConverter.convertEstimatesDTO(null));

    }

    @Test
    void convertEstimates() {

        Estimates estimates1 = new Estimates(deliveryRun1);
        LinkedHashMap<Address, Double> costMap = new LinkedHashMap<>();
        costMap.put(address1, 330.5);
        costMap.put(address2, 700.15);
        estimates1.setEnergyCostMap(costMap);
        estimates1.setRequiredBatteryToCompletePath(55);
        estimates1.setRequiredBatteryToReachNextPharmacy(30);
        estimates1.setDistanceTotalLengh(23.33);
        LinkedHashMap<Address, Double> distanceCostMap = new LinkedHashMap<>();
        distanceCostMap.put(address1, 111.88);
        estimates1.setDistanceCostMap(distanceCostMap);

        EstimatesDTO expectedResult = new EstimatesDTO(deliveryRunDTO1);
        LinkedHashMap<AddressDTO, Double> costMapDTO = new LinkedHashMap<>();
        costMapDTO.put(addressDTO1, 330.5);
        costMapDTO.put(addressDTO2, 700.15);
        expectedResult.setEnergyCostMapDTO(costMapDTO);
        expectedResult.setRequiredBatteryToCompletePathDTO(55);
        expectedResult.setRequiredBatteryToReachNextPharmacyDTO(30);
        expectedResult.setDistanceTotalLenghDTO(23.33);
        LinkedHashMap<AddressDTO, Double> distanceCostMapDTO = new LinkedHashMap<>();
        distanceCostMapDTO.put(addressDTO1, 111.88);
        expectedResult.setDistanceCostMapDTO(distanceCostMapDTO);
        
        assertEquals(expectedResult, DTOConverter.convertEstimates(estimates1));
        assertNull(DTOConverter.convertEstimates(null));
    }

    @Test
    void convertVehicle() {
        assertEquals(scooterDTO1, DTOConverter.convertVehicle(scooter1));
        assertEquals(droneDTO1, DTOConverter.convertVehicle(drone1));
        assertNull(DTOConverter.convertVehicle(null));
    }

    @Test
    void convertVehicleDTO() {
        assertEquals(scooter1, DTOConverter.convertVehicleDTO(scooterDTO1));
        assertEquals(drone1, DTOConverter.convertVehicleDTO(droneDTO1));
        assertNull(DTOConverter.convertVehicleDTO(null));

    }

    @Test
    void convertRoad() {
        Road road1 = new Road(5, address1, address2);
        RoadDTO roadDTO1 = new RoadDTO(5, addressDTO1, addressDTO2);
        RoadDTO result = DTOConverter.convertRoad(road1);
        assertEquals(roadDTO1, result);
    }

    @Test
    void convertRoadDTO() {
        Road road1 = new Road(5, address1, address2);
        RoadDTO roadDTO1 = new RoadDTO(5, addressDTO1, addressDTO2);
        Road result = DTOConverter.convertRoadDTO(roadDTO1);
        assertEquals(road1, result);
    }
}
