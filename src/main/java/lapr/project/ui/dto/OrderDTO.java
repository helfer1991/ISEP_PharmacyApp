package lapr.project.ui.dto;

import java.util.Objects;

/**
 *
 * @author danie
 */
public class OrderDTO implements DeliverableDTO {

    private int id;
    private int deliveryFee;
    private int status;
    private String dateEntry;
    private ShoppingCartDTO shoppingCart;
    private AddressDTO address;
    private int credits;
    private float energyCost;
    private int idAddress;
    private String addressName;

    public OrderDTO(int idOrder, int deliveryFee, int status, String date) {
        this.id = idOrder;
        this.deliveryFee = deliveryFee;
        this.status = status;
        this.dateEntry = date;
        this.shoppingCart = new ShoppingCartDTO();
        this.address = new AddressDTO();
        setAddressName(address.getAddress());
        setIdAddress(address.getId());
    }

    public OrderDTO() {
        this.shoppingCart = new ShoppingCartDTO();
    }

    public String getDateEntry() {
        return dateEntry;
    }

    public void setDateEntry(String dateEntry) {
        this.dateEntry = dateEntry;
    }

    @Override
    public ShoppingCartDTO getShopCart() {
        return shoppingCart;
    }

    public void setShopCart(ShoppingCartDTO shopCart) {
        this.shoppingCart = shopCart;
    }

    public int getId() {
        return id;
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

    public void setId(int id) {
        this.id = id;
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

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }


    public float getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(float energyCost) {
        this.energyCost = energyCost;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setIdAddress(int idAddress) {
        this.idAddress = idAddress;
    }

    public void setAddressName(String addressName) {
        address.setAddress(addressName);
        this.addressName = address.getAddress();
    }

    public int getIdAddress() {
        idAddress = address.getId();
        return idAddress;
    }

    public String getAddressName() {
        addressName = address.getAddress();
        return addressName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderDTO)) {
            return false;
        }
        OrderDTO orderDTO = (OrderDTO) o;
        return getId() == orderDTO.getId() && getDeliveryFee() == orderDTO.getDeliveryFee() && getStatus() == orderDTO.getStatus() && Objects.equals(getDateEntry(), orderDTO.getDateEntry()) && Objects.equals(getShopCart(), orderDTO.getShopCart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getDeliveryFee(), getStatus(), getDateEntry(), getShopCart());
    }
}
