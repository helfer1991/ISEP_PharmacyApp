package lapr.project.ui.dto;

import lapr.project.model.ShoppingCart;
import lapr.project.ui.dto.PharmacyDTO;
import lapr.project.ui.dto.ProductDTO;

import java.util.Objects;

public class TransferDTO implements DeliverableDTO{

    private PharmacyDTO pharmacyAsking;
    private PharmacyDTO pharmacySending;
    private ProductDTO product;
    private int transferId;
    private ShoppingCartDTO shoppingCart;
    private int quantity;
    private int orderId;

    public TransferDTO(PharmacyDTO pharmacyAsking, PharmacyDTO pharmacySending, ProductDTO product, int quantity, int orderId) {
        this.pharmacyAsking = pharmacyAsking;
        this.pharmacySending = pharmacySending;
        shoppingCart = new ShoppingCartDTO();
        shoppingCart.addProductToShoppingCart(product,quantity);
        this.product = product;
        this.quantity = quantity;
        this.orderId = orderId;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public PharmacyDTO getPharmacyAsking() {
        return pharmacyAsking;
    }

    public void setPharmacyAsking(PharmacyDTO pharmacyAsking) {
        this.pharmacyAsking = pharmacyAsking;
    }

    public PharmacyDTO getPharmacySending() {
        return pharmacySending;
    }

    public void setPharmacySending(PharmacyDTO pharmacySending) {
        this.pharmacySending = pharmacySending;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public ShoppingCartDTO getShopCart() {
        return this.shoppingCart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TransferDTO)) return false;
        TransferDTO that = (TransferDTO) o;
        return getQuantity() == that.getQuantity() && getOrderId() == that.getOrderId() && Objects.equals(getPharmacyAsking(), that.getPharmacyAsking()) && Objects.equals(getPharmacySending(), that.getPharmacySending()) && Objects.equals(getProduct(), that.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPharmacyAsking(), getPharmacySending(), getProduct(), getQuantity(), getOrderId());
    }

    @Override
    public String toString() {
        return "TransferDTO{" +
                "pharmacyAsking=" + pharmacyAsking +
                ", pharmacySending=" + pharmacySending +
                ", product=" + product +
                ", quantity=" + quantity +
                ", orderId=" + orderId +
                '}';
    }

    @Override
    public void setShopCart(ShoppingCartDTO shopCart) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AddressDTO getAddress() {
        return pharmacySending.getAddress();
    }

    @Override
    public void setAddress(AddressDTO address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getId() {
        return 0; 
    }

    @Override
    public void setId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
