package lapr.project.ui.dto;

import java.util.LinkedList;
import java.util.Objects;

/**
 *
 * @author 1100241
 */
public class DeliveryRunDTO {
    
    private int id;
    private String date;
    private LinkedList<DeliverableDTO> ordersToDeliver;
    private float weight;
    
    public DeliveryRunDTO(int id, String date) {
        this.id = id;
        this.date = date;
        this.ordersToDeliver = new LinkedList<>();
    }

    public DeliveryRunDTO(){
        this.ordersToDeliver = new LinkedList<>();
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
    
    public LinkedList<DeliverableDTO> getDeliverables() {
        LinkedList<DeliverableDTO> orders = new LinkedList<>();
        orders.addAll(ordersToDeliver);
        return orders;
    }

    public void setDeliverables(LinkedList<DeliverableDTO> ordersToDeliver) {

        LinkedList<DeliverableDTO> orders = new LinkedList<>();
        orders.addAll(ordersToDeliver);
        this.ordersToDeliver = orders;
    }

    public float getWeight() {
        weight = 0;
        for(DeliverableDTO d : ordersToDeliver){
            weight = weight+d.getShopCart().getShoppingCartWeight();
        }
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DeliveryRunDTO)) return false;
        DeliveryRunDTO that = (DeliveryRunDTO) o;
        return getId() == that.getId() && Objects.equals(getDate(), that.getDate()) && Objects.equals(ordersToDeliver, that.ordersToDeliver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDate(), ordersToDeliver);
    }
}
