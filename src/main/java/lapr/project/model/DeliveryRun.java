package lapr.project.model;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 */
public class DeliveryRun {

    private int id;
    private String date;
    private LinkedList<Deliverable> ordersToBeDelivered;
    private double energyCost;

    /**
     * Constructor to DeliveryRun
     * @param id
     * @param date 
     */
    public DeliveryRun(int id, String date) {
        this.id = id;
        this.date = date;
        ordersToBeDelivered = new LinkedList<>();
    }

    /**
     * Constructor to DeliveryRun
     */
    public DeliveryRun(){
        ordersToBeDelivered = new LinkedList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the list of deliverables (orders e/and transfers) from delivery run
     * @return LinkedList
     */
    public LinkedList<Deliverable> getDeliverables() {
        LinkedList<Deliverable> orders = new LinkedList<>();
        orders.addAll(ordersToBeDelivered);
        return orders;
    }
    
    /**
     * Add a order into a list of orders to delivery 
     * @param deliverable
     * @return 
     */
    public boolean addDeliverable(Deliverable deliverable){
        return ordersToBeDelivered.add(deliverable);
    }

    /**
     * Set the list of deliverables in delivery run
     * @param ordersToDelivery 
     */
    public void setDeliverables(LinkedList<Deliverable> ordersToDelivery) {
        if (ordersToDelivery != null){
            LinkedList<Deliverable> orders = new LinkedList<>();
            orders.addAll(ordersToDelivery);
            this.ordersToBeDelivered = orders;
        }
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(double energyCost) {
        this.energyCost = energyCost;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryRun)) return false;
        DeliveryRun that = (DeliveryRun) o;
        return getId() == that.getId() && Objects.equals(getDate(), that.getDate()) && Objects.equals(ordersToBeDelivered, that.ordersToBeDelivered);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), ordersToBeDelivered);
    }
}
