package lapr.project.model;


import java.util.Objects;

public class Transfer implements Deliverable{

    private final Pharmacy pharmacyAsking;
    private final Pharmacy pharmacySending;
    private  ShoppingCart shoppingCart;

    private final int orderId;
    private int transferId;
    private int transferStatus;

    public Transfer(Pharmacy pharmacyAsking, Pharmacy pharmacySending, Product product, int quantity, int orderId) {
        this.pharmacyAsking = pharmacyAsking;
        this.pharmacySending = pharmacySending;
        this.shoppingCart = new ShoppingCart();
        shoppingCart.addProductToShoppingCart(product,quantity);
        this.orderId = orderId;

    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public Pharmacy getPharmacyAsking() {
        return pharmacyAsking;
    }

    public Pharmacy getPharmacySending() {
        return pharmacySending;
    }

    public Product getProduct() {
        if(shoppingCart.getProductMap() != null && !shoppingCart.getProductMap().isEmpty()){
            return shoppingCart.getProductMap().entrySet().iterator().next().getKey();
        }
        return null;
    }

    public int getQuantity() {
        if(shoppingCart.getProductMap() != null && !shoppingCart.getProductMap().isEmpty()){
            return shoppingCart.getProductMap().entrySet().iterator().next().getValue();
        }
        return 0;
    }

    public int getOrderId() {
        return orderId;
    }

    public int getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(int transferStatus) {
        this.transferStatus = transferStatus;
    }

    @Override
    public ShoppingCart getShopCart() {
        return shoppingCart;
    }

    @Override
    public void setShopCart(ShoppingCart shoppingCart) {
        if(shoppingCart.getProductMap().size()>1){
            throw new IllegalStateException("Tansfers can only have one product.");
        }
        this.shoppingCart = shoppingCart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transfer)) return false;
        Transfer transfer = (Transfer) o;
        return getQuantity() == transfer.getQuantity()
                && getOrderId() == transfer.getOrderId()
                && Objects.equals(getPharmacyAsking(), transfer.getPharmacyAsking())
                && Objects.equals(getPharmacySending(), transfer.getPharmacySending())
                && Objects.equals(getProduct(), transfer.getProduct());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPharmacyAsking(), getPharmacySending(), getProduct(), getQuantity(), getOrderId());
    }

    @Override
    public int getIdOrder() {
        return 0;
    }

    @Override
    public void setIdOrder(int idOrder) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Address getAddress() {
        return pharmacySending.getAddress();
    }

    @Override
    public void setAddress(Address address) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }


}
