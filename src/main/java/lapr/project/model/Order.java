package lapr.project.model;

import java.util.Objects;

/**
 *
 * @author danie
 */
public class Order implements Deliverable{

    private int idOrder;
    private int deliveryFee;
    private int status;
    private String dateEntry;
    private ShoppingCart shopCart;
    private Address address;
    private int pharmacyId;
    private int credits;
    private Invoice invoice;

    public Order(int idOrder, int deliveryFee, int status, String dateEntry) {
        this.idOrder = idOrder;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.dateEntry = dateEntry;
        this.shopCart = new ShoppingCart();
    }

    public Order(int idOrder, int deliveryFee, int status, String dateEntry,ShoppingCart shopCart) {
        this.idOrder = idOrder;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.dateEntry = dateEntry;
        this.shopCart = shopCart;
    }
    
    /*
    Order constructor for orders not yet registed and used to submit to registration
    */
    public Order(int deliveryFee, ShoppingCart shopCart) {
        this.deliveryFee = deliveryFee;
        this.shopCart = shopCart;
    }


    @Override
    public ShoppingCart getShopCart() {
        return shopCart;
    }

    @Override
    public void setShopCart(ShoppingCart shopCart) {
        this.shopCart = shopCart;
    }

    public int getIdOrder() {
        return idOrder;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return dateEntry;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setDate(String date) {
        this.dateEntry = date;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getPharmacyId() {
        return pharmacyId;
    }

    public void setPharmacyId(int pharmacyId) {
        this.pharmacyId = pharmacyId;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return getIdOrder() == order.getIdOrder()
                && getDeliveryFee() == order.getDeliveryFee()
                && getStatus() == order.getStatus()
                && Objects.equals(dateEntry, order.dateEntry)
                && Objects.equals(getShopCart(), order.getShopCart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getIdOrder(), getDeliveryFee(), getStatus(), dateEntry, getShopCart());
    }
}
