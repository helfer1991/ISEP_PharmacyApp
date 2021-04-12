package lapr.project.controller;

import lapr.project.model.*;
import lapr.project.ui.dto.*;

import java.util.*;

public class DTOConverter {

    private DTOConverter() {

    }

    public static PharmacyDTO convertPharmacy(Pharmacy p) {
        if (p == null) {
            return null;
        }

        return new PharmacyDTO(
                p.getName(),
                p.getId(),
                convertPark(p.getPark()),
                convertAddress(p.getAddress()),
                p.getMaximumPayloadCourier(),
                p.getMinimumLoadCourier());
    }

    public static Pharmacy convertPharmacyDTO(PharmacyDTO p) {
        if (p == null) {
            return null;
        }

        return new Pharmacy(
                p.getName(),
                p.getId(),
                convertParkDTO(p.getPark()),
                convertAddressDTO(p.getAddress()),
                p.getMaximumPayloadCourier(),
                p.getMinimumLoadCourier());
    }

    public static ParkDTO convertPark(Park p) {
        if (p == null) {
            return null;
        }
        return new ParkDTO(
                p.getId(),
                p.getScooterChargersNumber(),
                p.getDroneChargersNumber(),
                p.getScooterChargerCapacity(),
                p.getDroneChargerCapacity());
    }

    public static Park convertParkDTO(ParkDTO p) {
        if (p == null) {
            return null;
        }
        return new Park(
                p.getId(),
                p.getScooterChargersNumber(),
                p.getDroneChargersNumber(),
                p.getScooterChargerCapacity(),
                p.getDroneChargerCapacity());
    }

    public static Product convertProductDTO(ProductDTO p) {
        if (p == null) {
            return null;
        }
        return new Product(
                p.getId(),
                p.getDescription(),
                p.getPrice(),
                p.getWeight());

    }

    public static ProductDTO convertProduct(Product p) {
        if (p == null) {
            return null;
        }
        return new ProductDTO(
                p.getId(),
                p.getDescription(),
                p.getPrice(),
                p.getWeight());
    }

    public static ShoppingCart convertShoppingCartDTO(ShoppingCartDTO cartDTO) {
        if (cartDTO == null) {
            return null;
        }
        ShoppingCart cart = new ShoppingCart();
        for (ProductDTO p : cartDTO.getProductMap().keySet()) {

            cart.addProductToShoppingCart(
                    convertProductDTO(p),
                    cartDTO.getProductMap().get(p));
        }
        return cart;
    }

    public static ShoppingCartDTO convertShoppingCart(ShoppingCart cart) {
        if (cart == null) {
            return null;
        }
        ShoppingCartDTO cartDTO = new ShoppingCartDTO();
        for (Product p : cart.getProductMap().keySet()) {

            cartDTO.addProductToShoppingCart(
                    convertProduct(p),
                    cart.getProductMap().get(p));
        }
        return cartDTO;
    }

    public static OrderDTO convertOrder(Order o) {
        if (o == null) {
            return null;
        }

        OrderDTO orderDTO = new OrderDTO(
                o.getIdOrder(),
                o.getDeliveryFee(),
                o.getStatus(),
                o.getDate());

        orderDTO.setCredits(o.getCredits());
        orderDTO.setShopCart(convertShoppingCart(o.getShopCart()));
        orderDTO.setAddress(convertAddress(o.getAddress()));
        return orderDTO;
    }

    public static Order convertOrderDTO(OrderDTO o) {
        if (o == null) {
            return null;
        }

        Order order = new Order(
                o.getId(),
                o.getDeliveryFee(),
                o.getStatus(),
                o.getDate());

        order.setCredits(o.getCredits());
        order.setShopCart(convertShoppingCartDTO(o.getShopCart()));
        order.setAddress(convertAddressDTO(o.getAddress()));
        return order;
    }

    public static AddressDTO convertAddress(Address a) {
        if (a == null) {
            return null;
        }
        return new AddressDTO(
                a.getId(),
                a.getLatitude(),
                a.getLongitude(),
                a.getAddress(),
                a.getZipcode(),
                a.getElevation());
    }

    public static Address convertAddressDTO(AddressDTO a) {
        if (a == null) {
            return null;
        }
        return new Address(
                a.getId(),
                a.getLatitude(),
                a.getLongitude(),
                a.getAddress(),
                a.getZipcode(),
                a.getElevation());
    }

    public static StorageDTO convertStorage(Storage s) {
        if (s == null) {
            return null;
        }
        StorageDTO storageDTO = new StorageDTO();
        for (Product p : s.getProductMap().keySet()) {
            storageDTO.getProductMap().put(convertProduct(p), s.getProductMap().get(p));
        }
        return storageDTO;
    }

    public static Storage convertStorageDTO(StorageDTO s) {
        if (s == null) {
            return null;
        }
        Storage storage = new Storage();
        for (ProductDTO p : s.getProductMap().keySet()) {
            storage.getProductMap().put(convertProductDTO(p), s.getProductMap().get(p));
        }
        return storage;
    }

    public static Client convertClientDTO(ClientDTO c) {
        if (c == null) {
            return null;
        }
        Client client = new Client(convertAddressDTO(c.getAddress()), convertCreditCardDTO(c.getCreditCard()));
        client.setNIF(c.getNIF());
        client.setName(c.getName());
        client.setEmail(c.getEmail());
        client.setPassword(c.getPassword());
        client.setCredits(c.getCredits());
        return client;
    }
    
    public static ClientDTO convertClient(Client c) {
        if (c == null) {
            return null;
        }
        ClientDTO client = new ClientDTO(convertAddress(c.getAddress()), convertCreditCard(c.getCreditCard()));
        client.setNIF(c.getNIF());
        client.setName(c.getName());
        client.setEmail(c.getEmail());
        client.setPassword(c.getPassword());
        client.setCredits(c.getCredits());
        return client;
    }

    public static CreditCardDTO convertCreditCard(CreditCard c) {
        if (c == null) {
            return null;
        }
        return new CreditCardDTO(c.getNumber(), c.getCcv(), c.getValidThru());
    }

    public static CreditCard convertCreditCardDTO(CreditCardDTO c) {
        if (c == null) {
            return null;
        }
        return new CreditCard(c.getNumber(), c.getCcv(), c.getValidThru());
    }

    public static Courier convertCourierDTO(CourierDTO c) {
        if (c == null) {
            return null;
        }
        Courier courier = new Courier(c.getWeight(), c.getIsWorking());
        courier.setNIF(c.getNIF());
        courier.setName(c.getName());
        courier.setEmail(c.getEmail());
        courier.setPassword(c.getPassword());
        return  courier;
    }

    public static CourierDTO convertCourier(Courier c) {
         if (c == null) {
            return null;
        }
        CourierDTO courier = new CourierDTO(c.getWeight(), c.getIsWorking());
        courier.setNIF(c.getNIF());
        courier.setName(c.getName());
        courier.setEmail(c.getEmail());
        courier.setPassword(c.getPassword());
        return  courier;
    }

    public static BatteryDTO convertBattery(Battery o) {
        if (o == null) {
            return null;
        }
        return new BatteryDTO(o.getIdBattery(), o.getCapacity());
    }

    public static Battery convertBatteryDTO(BatteryDTO o) {
        if (o == null) {
            return null;
        }
        return new Battery(o.getIdBattery(), o.getCapacity());
    }

    public static ScooterDTO convertScooter(Scooter s) {
        if (s == null) {
            return null;
        }
        return new ScooterDTO(
                s.getId(),
                s.getQrCode(),
                s.getWeight(),
                convertBattery(s.getBattery()),
                s.getActualCharge());
    }

    public static Scooter convertScooterDTO(ScooterDTO s) {
        if (s == null) {
            return null;
        }
        return new Scooter(
                s.getId(),
                s.getQrCode(),
                s.getWeight(),
                convertBatteryDTO(s.getBattery()),
                s.getActualCharge());

    }

    public static Drone convertDroneDTO(DroneDTO d) {
        return new Drone(
                d.getId(),
                d.getQrCode(),
                d.getWeight(),
                convertBatteryDTO(d.getBattery()),
                d.getActualCharge());
    }

    public static DroneDTO convertDrone(Drone d) {
        return new DroneDTO(
                d.getId(),
                d.getQrCode(),
                d.getWeight(),
                convertBattery(d.getBattery()),
                d.getActualCharge());
    }

    public static Transfer convertTransferDTO(TransferDTO t) {
        if (t == null) {
            return null;
        }
        Transfer transfer = new Transfer(
                convertPharmacyDTO(t.getPharmacyAsking()),
                convertPharmacyDTO(t.getPharmacySending()),
                convertProductDTO(t.getProduct()),
                t.getQuantity(),
                t.getOrderId());
        transfer.setTransferId(t.getTransferId());
        return transfer;
    }

    public static TransferDTO convertTransfer(Transfer t) {
        if (t == null) {
            return null;
        }
        TransferDTO transferDTO =new TransferDTO(
                convertPharmacy(t.getPharmacyAsking()),
                convertPharmacy(t.getPharmacySending()),
                convertProduct(t.getProduct()),
                t.getQuantity(),
                t.getOrderId());
        transferDTO.setTransferId(t.getTransferId());
        return transferDTO;
    }

    public static Map.Entry<ProductDTO, Integer> convertMapEntry(Map.Entry<Product, Integer> entry) {
        if (entry == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(
                convertProduct(entry.getKey()),
                entry.getValue());
    }

    public static Map.Entry<Product, Integer> convertMapEntryDTO(Map.Entry<ProductDTO, Integer> entry) {
        if (entry == null) {
            return null;
        }
        return new AbstractMap.SimpleEntry<>(
                convertProductDTO(entry.getKey()),
                entry.getValue());
    }

    public static DeliveryRun convertDeliveryRunDTO(DeliveryRunDTO deliveryRunDTO) {
        if (deliveryRunDTO == null) {
            return null;
        }
        DeliveryRun deliveryRun = new DeliveryRun(deliveryRunDTO.getId(), deliveryRunDTO.getDate());

        LinkedList<Deliverable> deliverables = new LinkedList<>();
        for (DeliverableDTO d : deliveryRunDTO.getDeliverables()) {
            deliverables.add(convertDeliverableDTO(d));
        }
        deliveryRun.setDeliverables(deliverables);

        return deliveryRun;

    }

    public static DeliveryRunDTO convertDeliveryRun(DeliveryRun deliveryRun) {
        if (deliveryRun == null) {
            return null;
        }
        DeliveryRunDTO deliveryRunDTO = new DeliveryRunDTO(deliveryRun.getId(), deliveryRun.getDate());

        LinkedList<DeliverableDTO> deliverablesDTO = new LinkedList<>();
        for (Deliverable d : deliveryRun.getDeliverables()) {
            deliverablesDTO.add(convertDeliverable(d));
        }
        deliveryRunDTO.setDeliverables(deliverablesDTO);

        return deliveryRunDTO;
    }

    public static DeliverableDTO convertDeliverable(Deliverable deliverable) {
        if (deliverable == null) {
            return null;
        }
        if (deliverable instanceof Order) {
            return convertOrder((Order) deliverable);
        } else if (deliverable instanceof Transfer) {
            return convertTransfer((Transfer) deliverable);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Deliverable convertDeliverableDTO(DeliverableDTO deliverableDTO) {
        if (deliverableDTO == null) {
            return null;
        }
        if (deliverableDTO instanceof OrderDTO) {
            return convertOrderDTO((OrderDTO) deliverableDTO);
        } else if (deliverableDTO instanceof TransferDTO) {
            return convertTransferDTO((TransferDTO) deliverableDTO);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Estimates convertEstimatesDTO(EstimatesDTO estimatesDTO) {
        if (estimatesDTO == null) {
            return null;
        }
        DeliveryRun deliveryRun = convertDeliveryRunDTO(estimatesDTO.getDeliveryRunDTO());
        Estimates estimates = new Estimates(deliveryRun);
        estimates.setRequiredBatteryToCompletePath(estimatesDTO.getRequiredBatteryToCompletePathDTO());
        estimates.setRequiredBatteryToReachNextPharmacy(estimatesDTO.getRequiredBatteryToReachNextPharmacyDTO());
        estimates.setDistanceTotalLengh(estimatesDTO.getDistanceTotalLenghDTO());
        estimates.setTimeDuration(estimatesDTO.getTimeDurationDTO());
        LinkedHashMap<Address, Double> energyCostMap = convertCostMapDTO(estimatesDTO.getEnergyCostMapDTO());
        LinkedHashMap<Address, Double> distanceCostMap = convertCostMapDTO(estimatesDTO.getDistanceCostMapDTO());
        estimates.setEnergyCostMap(energyCostMap);
        estimates.setDistanceCostMap(distanceCostMap);
        return estimates;
    }

    public static EstimatesDTO convertEstimates(Estimates estimates) {
        if (estimates == null) {
            return null;
        }
        DeliveryRunDTO deliveryRunDTO = convertDeliveryRun(estimates.getDeliveryRun());
        EstimatesDTO estimatesDTO = new EstimatesDTO(deliveryRunDTO);

        estimatesDTO.setRequiredBatteryToCompletePathDTO(estimates.getRequiredBatteryToCompletePath());
        estimatesDTO.setRequiredBatteryToReachNextPharmacyDTO(estimates.getRequiredBatteryToReachNextPharmacy());
        estimatesDTO.setDistanceTotalLenghDTO(estimates.getDistanceTotalLengh());
        estimatesDTO.setTimeDurationDTO(estimates.getTimeDuration());
        LinkedHashMap<AddressDTO, Double> energyCostMap = convertCostMap(estimates.getEnergyCostMap());
        LinkedHashMap<AddressDTO, Double> distanceCostMap = convertCostMap(estimates.getDistanceCostMap());
        estimatesDTO.setEnergyCostMapDTO(energyCostMap);
        estimatesDTO.setDistanceCostMapDTO(distanceCostMap);
        return estimatesDTO;
    }

    private static LinkedHashMap<Address, Double> convertCostMapDTO(LinkedHashMap<AddressDTO, Double> costMapDTO) {
        LinkedHashMap<Address, Double> costMap = new LinkedHashMap<>();
        for (Map.Entry<AddressDTO, Double> entryDTO : costMapDTO.entrySet()) {
            costMap.put(convertAddressDTO(entryDTO.getKey()), entryDTO.getValue());
        }
        return costMap;
    }

    private static LinkedHashMap<AddressDTO, Double> convertCostMap(LinkedHashMap<Address, Double> costMap) {
        LinkedHashMap<AddressDTO, Double> costMapDTO = new LinkedHashMap<>();
        for (Map.Entry<Address, Double> entry : costMap.entrySet()) {
            costMapDTO.put(convertAddress(entry.getKey()), entry.getValue());
        }
        return costMapDTO;
    }
    
    private static LinkedList<Address> convertListAddressDTO(LinkedList<AddressDTO> lstDTO) {
        if (lstDTO == null) {
            return null;
        }
        LinkedList<Address> lst = new LinkedList<>();
        for (AddressDTO d : lstDTO) {
            lst.add(convertAddressDTO(d));
        }
        return lst;
    }

    private static LinkedList<AddressDTO> convertListAddress(LinkedList<Address> lst) {
        if (lst == null) {
            return null;
        }
        LinkedList<AddressDTO> lstDTO = new LinkedList<>();
        for (Address d : lst) {
            lstDTO.add(convertAddress(d));
        }
        return lstDTO;
    }

    private static List<Deliverable> convertListDeliverableDTO(List<DeliverableDTO> lstDTO) {
        if (lstDTO == null) {
            return null;
        }
        ArrayList<Deliverable> lst = new ArrayList<>();
        for (DeliverableDTO d : lstDTO) {
            lst.add(convertDeliverableDTO(d));
        }
        return lst;
    }

    private static List<DeliverableDTO> convertListDeliverable(List<Deliverable> lst) {
        if (lst == null) {
            return null;
        }
        ArrayList<DeliverableDTO> lstDTO = new ArrayList<>();
        for (Deliverable d : lst) {
            lstDTO.add(convertDeliverable(d));
        }
        return lstDTO;
    }

    public static VehicleDTO convertVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return null;
        }
        if (vehicle instanceof Scooter) {
            return convertScooter((Scooter) vehicle);
        } else if (vehicle instanceof Drone) {
            return convertDrone((Drone) vehicle);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static Vehicle convertVehicleDTO(VehicleDTO vehicleDTO) {
        if (vehicleDTO == null) {
            return null;
        }
        if (vehicleDTO instanceof ScooterDTO) {
            return convertScooterDTO((ScooterDTO) vehicleDTO);
        } else if (vehicleDTO instanceof DroneDTO) {
            return convertDroneDTO((DroneDTO) vehicleDTO);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public static RoadDTO convertRoad(Road o) {
        if (o == null) {
            return null;
        }
        return new RoadDTO(
                o.getId(),
                convertAddress(o.getAddressOrig()),
                convertAddress(o.getAddressDest()));
    }

    public static Road convertRoadDTO(RoadDTO o) {
        if (o == null) {
            return null;
        }
        return new Road(
                o.getId(),
                convertAddressDTO(o.getAddressOrig()),
                convertAddressDTO(o.getAddressDest()));
    }

}
