package lapr.project.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import lapr.project.data.*;
import lapr.project.model.*;

public class ServiceDeliveryRun {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DeliveryDB deliveryDB;
    private ClientDB clientDB;
    private CourierDB courierDB;
    private VehicleDB vehicleDB;
    private TransferDB transferDB;
    private OrderDB orderDB;

    public ServiceDeliveryRun() {
        deliveryDB = new DeliveryDB();
        clientDB = new ClientDB();
        courierDB = new CourierDB();
        transferDB = new TransferDB();
        vehicleDB = new VehicleDB();
        orderDB = new OrderDB();
    }

    /**
     * used To Mock DeliveryDB during tests
     *
     * @param deliveryDB
     */
    public void setDeliveryDB(DeliveryDB deliveryDB) {
        this.deliveryDB = deliveryDB;
    }

    public void setClientDB(ClientDB clientDB) {
        this.clientDB = clientDB;
    }

    public void setCourierDB(CourierDB courierDB) {
        this.courierDB = courierDB;
    }

    public void setVehicleDB(VehicleDB vehicleDB) {
        this.vehicleDB = vehicleDB;
    }

    public void setTransferDB(TransferDB transferDB) {
        this.transferDB = transferDB;
    }

    public void setOrderDB(OrderDB orderDB) {
        this.orderDB = orderDB;
    }

    /**
     * Fetches from the database all the deliverables(orders and transfers) that
     * are waiting for delivery
     *
     * @param pharmacy
     * @return
     * @throws SQLException
     */
    public List<Deliverable> fetchDeliveriesReadyForShipping(Pharmacy pharmacy) throws SQLException {

        List<Deliverable> deliverables = deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy);
        deliverables.addAll(deliveryDB.getTransfersReadyByPharmacy(pharmacy));

        return deliverables;
    }

    /**
     * Accepts deliverables(orders and transfers) and groups in a way that can
     * be carried by a single drone/scooter --> aka deliveryRuns Groups together
     * as many deliverables as possible, but keeping the overall weight between
     * min and max payLoad for that pharmacy.
     *
     * @param pharmacy
     * @param deliverables
     * @return
     */
    public List<DeliveryRun> createDeliveryRuns(Pharmacy pharmacy, List<Deliverable> deliverables) {

        //Sorts deliverables by weight (heaviest first)
        deliverables.sort(Comparator.comparingDouble((d) -> (d.getShopCart().getShoppingCartWeight())));
        Collections.reverse(deliverables);
        
        float maxLoad = pharmacy.getMaximumPayloadCourier();
        float minLoad = pharmacy.getMinimumLoadCourier();

        float weight;

        Iterator<Deliverable> deliverableIterator = deliverables.listIterator();
        List<Deliverable> toIgnore = new ArrayList<>();
        DeliveryRun deliveryRun;
        List<DeliveryRun> listDeliveryRuns = new ArrayList<>();
        boolean createdAtLeastOne;
        boolean forcedShipping = false;
        int counter = 0;
        do {
            weight = 0;
            createdAtLeastOne = false;
            deliveryRun = new DeliveryRun();
            deliveryRun.setId(counter+1);
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            deliveryRun.setDate(timestamp.toString());
            while (deliverableIterator.hasNext()) {

                Deliverable d = deliverableIterator.next();

                if (d.getShopCart().getShoppingCartWeight() + weight <= maxLoad //too heavy
                        && !toIgnore.contains(d)) {         //already added
                    createdAtLeastOne = true;
                    deliveryRun.addDeliverable(d);
                    toIgnore.add(d);
                    weight += d.getShopCart().getShoppingCartWeight();
                    if(d instanceof Order && ((Order) d).getStatus() ==3 ) {
                        forcedShipping = true;
                    }else if (d instanceof Transfer && ((Transfer)d).getTransferStatus() ==4)
                        forcedShipping = true;
                }

                if ( (!deliverableIterator.hasNext() && weight > minLoad)
                    || (!deliverableIterator.hasNext() && forcedShipping)) { //if the loop ended and there's a valid deliveryRun, adds it to the list
                    listDeliveryRuns.add(deliveryRun);
                }
            }

        } while (createdAtLeastOne); //of at least one DeliveryRun was created, tries to create another one

        
        return listDeliveryRuns;
    }

    /**
     * Returns the courier that is going to execute the delivery run (if the
     * delivery run is made by scooter) Selects the courier that has been
     * waiting the longest.
     *
     * @param pharmacy
     * @return
     */
    public Courier getWeightAverageOfCourier(Pharmacy pharmacy) throws SQLException {
        return courierDB.getWeightAverageOfCourier(pharmacy);
    }
    
    /**
     * Returns the courier that is going to execute the delivery run (if the
     * delivery run is made by scooter) Selects the courier that has been
     * waiting the longest.
     *
     * @return
     */
    public Courier getCourierByUserSession() throws SQLException {
        if(PharmaDeliveriesApp.getInstance().getCurrentSession()==null){
            return null;
        }
        return courierDB.getCourierByUserEmail(PharmaDeliveriesApp.getInstance().getCurrentSession().getUser().getEmail());
    }

    /**
     * Gets this pharmacy vehicles (drones and scooters) that are available to
     * be used and that have enough battery for this deliveryRun
     *
     * @param pharmacy the pharmacy that owns the vehicles and requires the
     * delivery run
     * @param estimates Estimates object that contains the deliveryRun to be
     * performed and all the energy estimates related to that DeliveryRun
     * @return List with all the vehicles that can perform the delivery run
     */
    public List<Vehicle> getAvailableVehiclesForDelivery(Pharmacy pharmacy, Estimates estimates) throws SQLException {
        
        Iterable<Vehicle> availableVehicles = vehicleDB.getAvailableVehiclesByPharmacy(pharmacy);
        List<Vehicle> listVehicles = new LinkedList<>();
        Iterator<Vehicle> iterator = availableVehicles.iterator();
        Vehicle vehicle;

        while (iterator.hasNext()) {
            vehicle = iterator.next();
            if (vehicle instanceof Scooter) {
                if (vehicle.getActualCharge() >= estimates.getRequiredBatteryToCompletePath()) {
                    listVehicles.add(vehicle);
                }
            } else if (vehicle instanceof Drone) {
                if (vehicle.getActualCharge() >= estimates.getRequiredBatteryToCompletePath()) {
                    listVehicles.add(vehicle);
                }

            } else {
                throw new IllegalStateException("Drones and Scooters are the only vehicles supported for now.");
            }
        }
        return listVehicles;
    }

    /**
     * Changes the order status in the database to 'In-transit'
     *
     * @param deliverables
     * @return
     */
    public boolean markDeliverablesAsInTransit(LinkedList<Deliverable> deliverables) throws SQLException {
        int errorCount = 0;
        for (Deliverable d : deliverables) {
            if (d instanceof Order) {
                if (!orderDB.markOrderAsInTransit(((Order) d))) {
                    errorCount++;
                }
            }
            if (d instanceof Transfer) {
                if (!transferDB.markTransferAsInTransit(((Transfer) d))) {
                    errorCount++;
                }
            }
        }
        return errorCount == 0;
    }

    /**
     * Changes the order status in the database to 'Delivered'
     *
     * @param deliverable
     * @return
     * @throws SQLException
     */
    public boolean setOrderAsDelivered(Deliverable deliverable) throws SQLException {
        return orderDB.markOrderAsDelivered((Order) deliverable);
    }

    /**
     * Retrieve the client that made the order passed as parameter
     *
     * @param order
     * @return
     */
    public Client getClientByOrder(Order order) throws SQLException {
        return clientDB.getClientByOrderId(order.getIdOrder());
    }

    /**
     * Inserts in the database the information related to delivery and the
     * delivery_Stops
     *
     * @param deliveryRun
     * @param costMap
     * @param vehicle
     * @return
     * @throws SQLException
     */
    public boolean insertDeliveryRun(DeliveryRun deliveryRun, LinkedHashMap<Address, Double> costMap, Vehicle vehicle, Courier courier) throws SQLException {
        if (deliveryRun == null || costMap == null || costMap.isEmpty() || vehicle == null || courier == null) {
            return false;
        }
        return deliveryDB.insertDeliveries(deliveryRun, costMap, vehicle, courier);
    }

    /**
     * Starts a timer that runs every hour and creates
     *
     * @return
     */
    public boolean startTimerForHourlyDeliveries(List<Pharmacy> allPharmacies) {
        Timer timer = new Timer();
        TimerTask prTimerProcessOrders = new TimerTask() {

            public void run() {
                try {
                    forceDeliveryRuns(allPharmacies);

                } catch (SQLException ex) {
                    Logger.getLogger(ServiceDeliveryRun.class
                            .getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        timer.schedule(prTimerProcessOrders, 3600000, 3600000);
        return true;
    }

    /**
     * Method that runs at one hour intervals (hourly timer) and forces the selected orders
     * to be incorporated in the next delivery runs.
     * The selected orders are orders/transfers that have been 'ready' for over an hour but have not been shipped yet
     * @return
     * @throws SQLException
     */
    public boolean forceDeliveryRuns(List<Pharmacy> allPharmacies) throws SQLException {
        int errorCount = 0;
        for (Pharmacy p : allPharmacies) {
            if (!markDeliverablesAsForceShipping(p)) {
                errorCount++;
            }
        }
        return errorCount == 0;
    }

    /**
     * Changes the status of orders and transfers that have been ready (and not
     * shipped) for over an hour, to 'forced shipping'. This status forces the
     * deliverables to be shipped even if they don't meet the minimumPayload
     * restrictions of the pharmacy
     *
     * @param pharmacy
     * @return
     * @throws SQLException
     */
    public boolean markDeliverablesAsForceShipping(Pharmacy pharmacy) throws SQLException {
        try {
            List<Deliverable> deliverablesReady = deliveryDB.getOrdersReadyToDeliverByPharmacy(pharmacy);
            double waitingTime;
            //if the deliverable has been ready to be shipped (but has not left the pharmacy) for more than 60min
            //then it is marked as 'force shipping' and when the admin queries for the available delivery runs
            //the deliverables with 'force shipping' status will always be included
            for (Deliverable d : deliverablesReady) {
                if (d instanceof Order) {
                    waitingTime = deliveryDB.getOrderWaitingTime((Order) d);
                    if (waitingTime > 60) {
                        orderDB.markOrderAsForcedShipping((Order) d);
                    }
                } else if (d instanceof Transfer) {
                    waitingTime = transferDB.getTransferWaitingTime((Transfer) d);
                    if (waitingTime > 60) {
                        transferDB.markTransferAsForcedShipping((Transfer) d);
                    }
                } else {
                    throw new UnsupportedOperationException("Only orders and transfers are supported right now.");
                }
            }
            return true;
        } catch (SQLException e) {
            Logger.getGlobal().log(Level.FINE, e.getMessage(), e);
            return false;
        }

    }

    /**
     * Sends an email notification to the client saying that the order has been
     * shipped
     *
     * @param client The client that will receive the email
     * @param orderId the orderId (made by the client) that has been shipped
     * @return true if the email was sent correctly false if the email was not
     * sent
     */
    public boolean sendDeliveryEmailToClient(Client client, int orderId) {
        ServiceEmail emailService = PharmaDeliveriesApp.getInstance().getServiceEmail();
        String emailSubjectLine = "Delivery Start Notification";
        String emailContent;
        emailContent = "Hi " + client.getName() + ", your order (id: " + orderId + ") has left our pharmacy and should arrive at your address shortly.";
        return emailService.sendEmail(client.getEmail(), emailSubjectLine, emailContent);
    }

    public boolean markCourierAsBusy(Courier courier) throws SQLException {
        return courierDB.markCourierAsBusy(courier);
    }
    public boolean markCourierAsFree(String courierEmail) throws SQLException {
        Courier courier = courierDB.getCourierByUserEmail(courierEmail);
        return courierDB.markCourierAsFree(courier);
    }

    public boolean markVehicleAsBusy(Vehicle vehicle) throws SQLException {
        return vehicleDB.markVehicleAsTransiting(vehicle);
    }


}
