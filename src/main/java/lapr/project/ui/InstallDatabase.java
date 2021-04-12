/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lapr.project.ui;

import static java.lang.Thread.sleep;
import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lapr.project.controller.BackOrderController;
import lapr.project.controller.CommonController;
import lapr.project.controller.ManageClientsController;
import lapr.project.controller.ManageCouriesController;
import lapr.project.controller.ManageDeliveryRunController;
import lapr.project.controller.ManagePharmaciesController;
import lapr.project.controller.ManageVehiclesController;
import lapr.project.controller.ManageStocksController;
import lapr.project.model.PharmaDeliveriesApp;
import lapr.project.ui.dto.AddressDTO;
import lapr.project.ui.dto.BatteryDTO;
import lapr.project.ui.dto.ClientDTO;
import lapr.project.ui.dto.CourierDTO;
import lapr.project.ui.dto.CreditCardDTO;
import lapr.project.ui.dto.DroneDTO;
import lapr.project.ui.dto.OrderDTO;
import lapr.project.ui.dto.ParkDTO;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ProductDTO;
import lapr.project.ui.dto.RoadDTO;
import lapr.project.ui.dto.ScooterDTO;
import lapr.project.utils.Utils;

/**
 *
 * @author 1100241
 */
public class InstallDatabase {

    private String adminEmail = "admin1@isep.ipp.pt";
    private String adminKey = "admin1";
    private CommonController commonController;
    private ManagePharmaciesController managePharmaciesController;
    private ManageVehiclesController manageVehiclesController;
    private ManageCouriesController manageCourierController;
    private ManageStocksController manageStocksController;
    private ManageClientsController manageClientsController;
    private BackOrderController backOrderController;
    private ManageDeliveryRunController manageDeliveryRunController;

    public void start() throws InterruptedException {

        //Start controller
        commonController = new CommonController();
        managePharmaciesController = new ManagePharmaciesController();
        manageVehiclesController = new ManageVehiclesController();
        manageCourierController = new ManageCouriesController();
        manageStocksController = new ManageStocksController();
        manageClientsController = new ManageClientsController();
        backOrderController = new BackOrderController();
        manageDeliveryRunController = new ManageDeliveryRunController();
//
//        Run script and insert constants
        runScriptOnDatabase();
//        Insert Address
        insertAddressByFile();
//        Insert Air Constraints
        insertAirConstraintsByFile();
//        Insert Road Constraints
        insertRoadConstraintsByFile();
//        Insert Pharmacy
        insertPharmacyByFile();
//        Insert Scooter
        insertScooterByFile();
//        Insert Drone
        insertDroneByFile();
//        Insert Courier
        insertCourierByFile();
//        Insert Product
        insertProductByFile();
//        Insert Product Stock
        insertProductStockByFile();
//        Insert Client
        insertClientByFile();
//        Insert Orders.
        sleep(1000);
        insertOrderByFile();

    }

    public void runScriptOnDatabase() {

        try {
            System.out.println("");
            System.out.println("Create Database ...");
            if (commonController.runScriptOnDatabase("database/LAPR3.SCHEMA.sql", true)) {
                System.out.println("Schema created ...");
                if (commonController.runScriptOnDatabase("database/LARP3.INSERT.CONSTANTS.sql", false)) {
                    System.out.println("Constants inserted ...");
                }
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at running scripts: " + ex.getMessage());
        }

    }

    public void insertAddressByFile() {

        System.out.println("Insert Address ...");
        try {
            List<String> tmp = Utils.readFromFile("database/LAPR3.ADDRESS.csv");
            AddressDTO object = null;
            AddressDTO inserted = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new AddressDTO();
                object.setLatitude(Utils.convertStringToDouble(arrayStr[0].trim()));
                object.setLongitude(Utils.convertStringToDouble(arrayStr[1].trim()));
                object.setAddress(arrayStr[2].trim());
                object.setZipcode(arrayStr[3].trim());
                object.setElevation(Utils.convertStringToFloat(arrayStr[4].trim()));
                inserted = commonController.insertAddress(object);
                System.out.println("Address Id: " + inserted.getId() + "; Rua: " + inserted.getAddress()
                        + "; (x,y,z) " + inserted.getLatitude() + ", " + inserted.getLongitude() + ", " + inserted.getElevation());
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert address: " + ex.getMessage());
        }

    }

    public void insertAirConstraintsByFile() {
        System.out.println("Insert Air Constraints ...");
        try {
            List<String> tmp = Utils.readFromFile("database/LAPR3.AIRCONSTRAINTS.csv");
            RoadDTO object = null;
            RoadDTO inserted = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new RoadDTO(0, new AddressDTO(Utils.convertStringToInt(arrayStr[0].trim()), 0, 0, "", "", 0), new AddressDTO(Utils.convertStringToInt(arrayStr[1].trim()), 0, 0, "", "", 0));
                inserted = commonController.insertAirRestriction(object);
                System.out.println("Restriction Air: " + inserted.getId() + "; Address Id Origin: " + object.getAddressOrig().getId() + "; Address Id End: " + object.getAddressDest().getId());
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert air Constraints: " + ex.getMessage());
        }
    }

    public void insertRoadConstraintsByFile() {

        System.out.println("Insert Road Constraints ...");
        try {
            List<String> tmp = Utils.readFromFile("database/LAPR3.ROADCONSTRAINTS.csv");
            RoadDTO object = null;
            RoadDTO inserted = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new RoadDTO();
                object.setAddressOrig(new AddressDTO(Utils.convertStringToInt(arrayStr[0].trim()), 0, 0, "", "", 0));
                object.setAddressDest(new AddressDTO(Utils.convertStringToInt(arrayStr[1].trim()), 0, 0, "", "", 0));
                inserted = commonController.insertRoadRestriction(object);
                System.out.println("Restriction Road: " + inserted.getId() + "; Address Id Origin: " + object.getAddressOrig().getId() + "; Address Id End: " + object.getAddressDest().getId());
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert road: " + ex.getMessage());
        }
    }

    public void insertPharmacyByFile() {

        System.out.println("Insert Pharmacy ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.PHARMACY.csv");
            PharmacyDTO object = null;
            PharmacyDTO inserted = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new PharmacyDTO();
                object.setName((arrayStr[0].trim()));
                object.setMaximumPayloadCourier(Utils.convertStringToFloat(arrayStr[1].trim()));
                object.setMinimumLoadCourier(Utils.convertStringToFloat(arrayStr[2].trim()));
                object.setAddress(new AddressDTO(Utils.convertStringToInt(arrayStr[3].trim()), 0, 0, "", "", 0));
                object.setPark(new ParkDTO(0, Utils.convertStringToInt(arrayStr[4].trim()), Utils.convertStringToInt(arrayStr[5].trim()),
                        Utils.convertStringToFloat(arrayStr[6].trim()), Utils.convertStringToFloat(arrayStr[7].trim())));
                inserted = managePharmaciesController.insertPharmacy(object);
                System.out.println("Pharmacy Id: " + inserted.getId() + "; Name: " + inserted.getName() + "; Park Id: " + inserted.getPark().getId()
                        + "; Address Id: " + inserted.getAddress().getId() + "; Min. Payload: " + inserted.getMinimumLoadCourier() + " kg");
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert pharmacy: " + ex.getMessage());
        }

    }

    public void insertScooterByFile() {

        System.out.println("Insert Scooter ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.SCOOTER.csv");
            ScooterDTO object = null;
            ScooterDTO inserted = null;
            PharmacyDTO pharmacyDTO = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                pharmacyDTO = new PharmacyDTO("", Utils.convertStringToInt(arrayStr[0].trim()), new ParkDTO(), new AddressDTO(), 0, 0);
                object = new ScooterDTO();
                object.setWeight(Utils.convertStringToInt(arrayStr[1].trim()));
                object.setIsAvailable(arrayStr[2].trim());
                object.setBattery(new BatteryDTO(0, Utils.convertStringToFloat(arrayStr[3].trim())));
                inserted = (ScooterDTO) manageVehiclesController.insertVehicle(object, pharmacyDTO);
                System.out.println("Pharmacy Id: " + pharmacyDTO.getId() + " Scooter Id: " + inserted.getId() + "; Weight: " + inserted.getWeight()
                        + "kg; Battery: " + inserted.getBattery().getCapacity() + " kwh");
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert scooter: " + ex.getMessage());
        }

    }

    public void insertDroneByFile() {

        System.out.println("Insert Drone ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.DRONE.csv");
            DroneDTO object = null;
            DroneDTO inserted = null;
            PharmacyDTO pharmacyDTO = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                pharmacyDTO = new PharmacyDTO("", Utils.convertStringToInt(arrayStr[0].trim()), new ParkDTO(), new AddressDTO(), 0, 0);
                object = new DroneDTO();
                object.setWeight(Utils.convertStringToInt(arrayStr[1].trim()));
                object.setIsAvailable(arrayStr[2].trim());
                object.setBattery(new BatteryDTO(0, Utils.convertStringToFloat(arrayStr[3].trim())));
                inserted = (DroneDTO) manageVehiclesController.insertVehicle(object, pharmacyDTO);
                System.out.println("Pharmacy Id: " + pharmacyDTO.getId() + " Drone Id: " + inserted.getId() + "; Weight: " + inserted.getWeight()
                        + "kg; Battery: " + inserted.getBattery().getCapacity() + " kwh");
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert drone: " + ex.getMessage());
        }

    }

    public void insertCourierByFile() {

        System.out.println("Insert Courier ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.COURIER.csv");
            CourierDTO object = null;
            PharmacyDTO pharmacyDTO = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                pharmacyDTO = new PharmacyDTO("", Utils.convertStringToInt(arrayStr[0].trim()), new ParkDTO(), new AddressDTO(), 0, 0);
                object = new CourierDTO();
                object.setEmail((arrayStr[1].trim()));
                object.setPassword((arrayStr[2].trim()));
                object.setName((arrayStr[3].trim()));
                object.setNIF(Utils.convertStringToInt(arrayStr[4].trim()));
                object.setWeight(Utils.convertStringToDouble(arrayStr[5].trim()));
                object.setIsWorking(true);
                if (manageCourierController.insertCourier(object, pharmacyDTO)) {
                    System.out.println("Pharmacy Id: " + pharmacyDTO.getId() + " Courier NIF: " + object.getNIF() + "; Name: " + object.getName()
                            + "; Email: " + object.getEmail() + "; Password: " + object.getPassword() + "; Weight: " + object.getWeight() + " kg; ");
                }
                ;

            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert courier: " + ex.getMessage());
        }

    }

    public void insertProductByFile() {

        System.out.println("Insert Product ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.PRODUCT.csv");
            ProductDTO object = null;
            ProductDTO inserted = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new ProductDTO();
                object.setDescription((arrayStr[0].trim()));
                object.setPrice(Utils.convertStringToFloat(arrayStr[1].trim()));
                object.setWeight(Utils.convertStringToFloat(arrayStr[2].trim()));
                inserted = manageStocksController.insertProduct(object);
                System.out.println("Product Id: " + inserted.getId() + " Description: " + object.getDescription() + "; Price: " + object.getPrice()
                        + " €; Weight: " + object.getWeight() + " kg");

            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert product: " + ex.getMessage());
        }

    }

    public void insertProductStockByFile() {

        System.out.println("Insert Product Stock ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.PRODUCTSTOCK.csv");
            ProductDTO object = null;
            Map.Entry<ProductDTO, Integer> inserted = null;
            PharmacyDTO pharmacyDTO = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new ProductDTO();
                pharmacyDTO = new PharmacyDTO("", Utils.convertStringToInt(arrayStr[0].trim()), new ParkDTO(), new AddressDTO(), 0, 0);
                object.setId(Utils.convertStringToInt(arrayStr[1].trim()));
                Integer quantity = Utils.convertStringToInt(arrayStr[2].trim());
                inserted = manageStocksController.insertProductQuantity(pharmacyDTO, new AbstractMap.SimpleEntry<ProductDTO, Integer>(object, quantity));
                System.out.println("Pharmacy Id: " + pharmacyDTO.getId() + "; Product Id: " + inserted.getKey().getId() + "; Quantity: " + inserted.getValue());

            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert product stock: " + ex.getMessage());
        }

    }

    public void insertClientByFile() {

        System.out.println("Insert Client ...");
        try {
            if (!doLogin(adminEmail, adminKey)) {
                return;
            }

            List<String> tmp = Utils.readFromFile("database/LAPR3.CLIENT.csv");
            ClientDTO object = null;
            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                object = new ClientDTO();
                object.setNIF(Utils.convertStringToInt(arrayStr[0].trim()));
                object.setName((arrayStr[1].trim()));
                object.setEmail((arrayStr[2].trim()));
                object.setPassword((arrayStr[3].trim()));
                object.setAddress(new AddressDTO(Utils.convertStringToInt(arrayStr[4].trim()), 0, 0, "", "", 0));
                object.setCreditCard(new CreditCardDTO(arrayStr[5].trim(), Utils.convertStringToInt(arrayStr[6].trim()), arrayStr[7].trim()));

                if (manageClientsController.insertClient(object)) {
                    System.out.println("Client NIF: " + object.getNIF() + "; Client email: " + object.getEmail() + "; Address Id: " + object.getAddress().getId() + "; CreditCard: " + object.getCreditCard().getNumber());
                }
                ;

            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert client: " + ex.getMessage());
        }

    }

    public void insertOrderByFile() {

        System.out.println("Insert Order ...");
        try {

            List<String> tmp = Utils.readFromFile("database/LAPR3.ORDER.csv");
            Map<ClientDTO, OrderDTO> map = new LinkedHashMap<>();
            ClientDTO object = null;
            OrderDTO order = null;
            ProductDTO product = null;

            for (String s : tmp) {
                String[] arrayStr = s.split(";");
                //Client
                object = new ClientDTO();
                object.setEmail((arrayStr[0].trim()));
                object.setPassword((arrayStr[1].trim()));
                object.setCreditCard(new CreditCardDTO(arrayStr[3].trim(), Utils.convertStringToInt(arrayStr[4].trim()), arrayStr[5].trim()));
                object.setAddress(new AddressDTO(Utils.convertStringToInt(arrayStr[2].trim()), 0, 0, "", "", 0));

                //Product
                product = new ProductDTO();
                product.setId(Utils.convertStringToInt(arrayStr[6].trim()));
                product.setDescription((arrayStr[7].trim()));
                product.setPrice(Utils.convertStringToFloat(arrayStr[8].trim()));
                product.setWeight(Utils.convertStringToFloat(arrayStr[9].trim()));

                int quantity = Utils.convertStringToInt(arrayStr[10].trim());

                //Verify if client exists on map
                if (!map.containsKey(object)) {
                    //Address
                    map.put(object, new OrderDTO());
                    order = map.get(object);
                    order.setAddress(new AddressDTO(Utils.convertStringToInt(arrayStr[2].trim()), 0, 0, "", "", 0));
                }
                order = map.get(object);
                order.getShopCart().addProductToShoppingCart(product, quantity);
            }

            //Iterar o mapa
            for (Map.Entry<ClientDTO, OrderDTO> entry : map.entrySet()) {
                ClientDTO key = entry.getKey();
                OrderDTO val = entry.getValue();
                doLogin(key.getEmail(), key.getPassword());

                try {
                    ClientDTO c = manageClientsController.getClientByUserSession();
                    c.setCreditCard(key.getCreditCard());

                    PharmacyDTO p = backOrderController.getNearestPharmacy();
                    if (p != null) {
                        if (backOrderController.registerOrderRequest(p, val, c) != null) {
                            System.out.println("Pharmacy Id: " + p.getId() + "; Client email: " + c.getEmail() + "; Shopping cart price: " + val.getShopCart().getTotalCostShoppingCart() + "; Shopping cart weight: " + val.getShopCart().getShoppingCartWeight() + " kg");
                        }
                    }
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }

                doLogout();
            }
            System.out.println("");
        } catch (Exception ex) {
            System.out.println("Error at insert order: " + ex.getMessage());
        }

    }

    public boolean doLogin(String strEmail, String strPwd) {
        return PharmaDeliveriesApp.getInstance().doLogin(strEmail, strPwd);
    }

    public void doLogout() {
        PharmaDeliveriesApp.getInstance().doLogout();
    }

}
